package org.usfirst.frc.team5518.robot.subsystems;

import org.usfirst.frc.team5518.robot.OI;
import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveTrainSub extends Subsystem implements PIDOutput {

	// Construct and define motor controllers
	private WPI_TalonSRX frontLeftTalon = new WPI_TalonSRX(RobotMap.FRONT_LEFT);
	private WPI_TalonSRX backLeftTalon = new WPI_TalonSRX(RobotMap.BACK_LEFT);
	private WPI_TalonSRX frontRightTalon = new WPI_TalonSRX(RobotMap.FRONT_RIGHT);
	private WPI_TalonSRX backRightTalon = new WPI_TalonSRX(RobotMap.BACK_RIGHT);
	
	// Combine all the motor controllers into a drive base
	private MecanumDrive driveBase = new MecanumDrive(frontLeftTalon, backLeftTalon, frontRightTalon, backRightTalon);
	
	// Set up encoder values
	public static final double kDistancePerRevolution = 8 * Math.PI; // Distance traveled in one wheel rotation (circumference)
	final static double kPulsesPerRevolution = 360; // Encoder pulses in one shaft revolution
	public static final double kDistancePerPulse = kDistancePerRevolution / kPulsesPerRevolution; // Distance in inches per pulse
	
	public Ultrasonic ultra;
	
	public ADXRS450_Gyro gyro;
	public double angle;
	
	public PIDController pidGyro;
	private float rotAdjustment = 0;
	private double rotateToAngleRate;
	public static final float kAngleTolerance = 0.5f;
	public static final float kSpeedTolerance = 0.01f;
	
	private Encoder leftEncoder; 
	private Encoder rightEncoder;

	private float expiraton = 0.3f; // Motor Safety expiration period

	public DriveTrainSub() {
		
		setupTalons(frontLeftTalon);
		setupTalons(backLeftTalon);
		setupTalons(frontRightTalon);
		setupTalons(backRightTalon);
		
		// Construct sensors
		ultra = new Ultrasonic(RobotMap.ULTRA_PING, RobotMap.ULTRA_ECHO);
		ultra.setAutomaticMode(true);
		
		leftEncoder = new Encoder(RobotMap.LEFT_ENC_A, RobotMap.LEFT_ENC_B, true, EncodingType.k4X);
		rightEncoder = new Encoder(RobotMap.RIGHT_ENC_A, RobotMap.RIGHT_ENC_B, false, EncodingType.k4X);
		
		gyro = new ADXRS450_Gyro();
		pidGyro = new PIDController(RobotMap.GYRO_PID_KP, RobotMap.GYRO_PID_KI, RobotMap.GYRO_PID_KD, gyro, this); // tune kP, kI, kD values here

		// Configure sensors
		leftEncoder.setDistancePerPulse(kDistancePerPulse);
		rightEncoder.setDistancePerPulse(kDistancePerPulse);

		// Configure PID controllers
		pidGyro.setInputRange(-180.0f, 180.0f); // Angle Input
		pidGyro.setOutputRange(-RobotMap.GYRO_PID_SPEED, RobotMap.GYRO_PID_SPEED); // Left movement and right move 
		pidGyro.setAbsoluteTolerance(kAngleTolerance); // Error range 
		pidGyro.setContinuous(true); 
		angle = 0;

		LiveWindow.add(pidGyro); // Adds gyro PID to Smart Dashboard Test Mode

		leftEncoder.setMaxPeriod(0.1);
		rightEncoder.setMaxPeriod(0.1);

		resetEncoders();

	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(Robot.driveInputCom);
	}

	// ------------------------- BEGIN TELEOP AND AUTO MOVEMENT FUNCTIONS ------------------------- //
	// these functions are used to apply outputs to the robot, either during autonomous or tele-op
	
	/**
	 * Used for tele-operated control of the drive train
	 * @param drive the y speed (forward/backward) (positive drives forward)
	 * @param strafe the x speed (side to side) (positive strafes right)
	 * @param rotate the z rotation (rotation) (positive rotates right)
	 */
	public void drive(double drive, double strafe, double rotate) {
		// driveBase.driveCartesian(drive, strafe, rotate, angle);
		driveBase.driveCartesian(drive, strafe, rotate);
		debugEncoderValues();
	}
	
	public void autoDrive(float vertDist, float vertSpeed) {
		// evenDrive();
		drive(-vertSpeed, 0, 0);
		debugEncoderValues();
	}

	public void autoDriveToPoint(float point, float speed) {
		
		if (ultra.getRangeInches() < point-1) {
			drive(speed, 0, rotAdjustment);
		} else if (ultra.getRangeInches() > point+1) {
			drive(-speed, 0, rotAdjustment);
		} else {
			drive(0, 0, 0);
		}
		
	}
	
	public void autoAngledDrive(float dist, float speed, float angle) {
		driveBase.drivePolar(speed, angle, 0);
	}
	
	public void autoStrafe(float strafeDist, float strafeSpeed) {
		drive(0, strafeSpeed, rotAdjustment);
		debugEncoderValues();
	}

	/**
	 * Drive to the setpoint set in the gyro's PID controller.
	 * Call this method in the execute() method of a command after calling enableGyroPID(float) in the init() method of a command.
	 */
	public void rotatePID() {
		drive(0, 0, rotateToAngleRate); 
	}
	
	public void rotate(float rotatePoint, float rotateSpeed) { // open-loop substitute for PID gyro control
		
		angle = gyro.getAngle();
		Robot.logger.debug("Gyro value: " + angle);
		
		if (angle < rotatePoint - kAngleTolerance) {
			drive(0.0, 0.0, rotateSpeed);
		} else if (angle > rotatePoint + kAngleTolerance) {
			drive(0.0, 0.0, -rotateSpeed);
		}

	}
	
	public void stop() {
		// Stop driving. Failsafe if connection is interrupted or robot code ends.
		driveBase.driveCartesian(0, 0, 0);
	}
	
	// ------------------------- BEGIN INFORMATIVE AND CONTROL FUNCTIONS ------------------------- //
	// these functions are used to manipulate values that are passed in or to gather information for the robot to use
	
	public void alignScale() {
		double dist = ultra.getRangeInches();
		if (dist > (RobotMap.SCALE_ALIGNMENT_DIST - RobotMap.ALLOWANCE) && dist < (RobotMap.SCALE_ALIGNMENT_DIST+RobotMap.ALLOWANCE)) {
			OI.driveController.setRumble(RumbleType.kLeftRumble, 1);
		}
		else {
			OI.driveController.setRumble(RumbleType.kLeftRumble, 0);
		}
	}
	
	public double readSpeedValues(double speed, double cap) {
		
		speed = Math.pow(speed, 3); // apply a cubic curve to speed to decrease sensitivity
		speed *= cap; // limit speed
		
		if (Math.abs(speed) < RobotMap.CONTROLLER_CUSTOM_DEADBAND) { // Apply custom deadband directly to inputs
			speed = 0;
		}
		return speed;
	}
	
	public void setupTalons(WPI_TalonSRX talon) {
		talon.setSafetyEnabled(true);
		talon.setExpiration(expiraton);
		talon.setInverted(false);
		talon.setNeutralMode(com.ctre.phoenix.motorcontrol.NeutralMode.Brake);
	}
	
	/**
	 * Start the PID controller for the gyro to turn the robot
	 * @param degrees The setpoint of the robot
	 */
	public void enableGyroPID(float degrees) {
		pidGyro.setSetpoint(degrees);
		pidGyro.enable();
	}
	
	@Override
	public void pidWrite(double output) {
		rotateToAngleRate = output; //Outputs Turning rate from PID
		Robot.logger.debug("AngleRate (PID OUTPUT)" + rotateToAngleRate);
		drive(0, 0, rotateToAngleRate);
		SmartDashboard.putNumber("PID Value", rotateToAngleRate);
	}
	
	/**
	 * Return whether the robot is on target with its setpoint set in the gyro's PID controller
	 * @return True if the robot is on target and false otherwise
	 */
	public boolean isGyroPIDOnTarget() {
		return pidGyro.onTarget();
	}
	
	/**
	 * Disable the gyro's PID controller. Call this method in the end() method of a command after enabling and turning the robot.
	 */
	public void disableGyroPID() {
		pidGyro.disable();
	}
	
	public double avgEncoderPos() {
		// return (leftEncoder.getDistance() + rightEncoder.getDistance()) / 2;
		return (leftEncoder.getDistance());
	}

	public double avgAbsEncoderPos() {
		// return (Math.abs(leftEncoder.getDistance()) + Math.abs(rightEncoder.getDistance())) / 2;
		return (Math.abs(leftEncoder.getDistance()));
	}
	
	private void evenDrive() {
		if (leftEncoder.getDistance() > (rightEncoder.getDistance() + 0.6f)) {
			rotAdjustment = -0.35f;
		}
		if (rightEncoder.getDistance() > (leftEncoder.getDistance() + 0.6f)) {
			rotAdjustment = 0.35f;
		}
	}
	
	public void debugEncoderValues() {
//		Robot.logger.debug("TALONS FL: " + frontLeftTalon.get() + " BL: " + backLeftTalon.get() + " FR: " + frontRightTalon.get() + " BR: " + backRightTalon.get());
		Robot.logger.debug("Right enc: " + rightEncoder.getDistance() + " Left enc " + leftEncoder.getDistance() + " Avg enc " + avgEncoderPos());
		Robot.logger.debug("Right enc: " + rightEncoder.get() + " Left enc " + leftEncoder.get());
	}
	
	public void resetEncoders() {
		leftEncoder.reset();
		rightEncoder.reset();
	}

	public void resetGyro() {
		gyro.reset();
	}
	
	public void calibrateGyro() {
		gyro.calibrate();
	}
	
<<<<<<< HEAD
	public void setupTalons(WPI_TalonSRX talon) {
		talon.setSafetyEnabled(true);
		talon.setExpiration(expiraton);
		talon.setInverted(false);
		talon.setNeutralMode(com.ctre.phoenix.motorcontrol.NeutralMode.Brake);
//		talon.configContinuousCurrentLimit(5, 1000);
		talon.enableCurrentLimit(true);
	}
	
=======
>>>>>>> branch 'WolfByte' of https://github.com/TechnoWolves5518/2018Season.git
}



