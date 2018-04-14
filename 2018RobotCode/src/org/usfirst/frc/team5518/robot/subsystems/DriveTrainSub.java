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
	
	public static final double kDistancePerRevolution = 8 * Math.PI; // Distance traveled in one wheel rotation (circumference)
	final static double kPulsesPerRevolution = 360; // Encoder pulses in one shaft revolution
	public static final double kDistancePerPulse = kDistancePerRevolution / kPulsesPerRevolution; // Distance in inches per pulse
	
	public Ultrasonic ultra;
	
	private PIDController pidLeft;
	private PIDController pidRight;
	public PIDController pidGyro;

	private Encoder leftEncoder; 
	private Encoder rightEncoder;
	
	private double wheelOutputRate;

	public ADXRS450_Gyro gyro;
	public double angle;

	private float rotAdjustment = 0;
	private double rotateToAngleRate;
	
	public static final float kAngleTolerance = 0.5f;
	public static final float kSpeedTolerance = 0.01f;
	
	private float expiraton = 0.3f; // Motor Safety expiration period

	// Combine all the motor controllers into a drive base
	private MecanumDrive driveBase = new MecanumDrive(frontLeftTalon, backLeftTalon, frontRightTalon, backRightTalon);

	public DriveTrainSub() {
		
		setupTalons(frontLeftTalon);
		setupTalons(backLeftTalon);
		setupTalons(frontRightTalon);
		setupTalons(backRightTalon);
		
		// Construct sensors
		ultra = new Ultrasonic(8, 9);
		ultra.setAutomaticMode(true);
		
		leftEncoder = new Encoder(0, 1, true, EncodingType.k4X);
		rightEncoder = new Encoder(2, 3, false, EncodingType.k4X);
		gyro = new ADXRS450_Gyro();
		// Construct PID controllers
		pidGyro = new PIDController(0.05, 0, 0.05, gyro, this); // tune kP, kI, kD values here
		pidLeft = new PIDController(0, 0, 0, leftEncoder, this);
		pidRight = new PIDController(0, 0, 0, rightEncoder, this);

		// Configure sensors
		leftEncoder.setDistancePerPulse(kDistancePerPulse);
		rightEncoder.setDistancePerPulse(kDistancePerPulse);

		// Configure PID controllers
		pidGyro.setInputRange(-180.0f, 180.0f); // Angle Input
		pidGyro.setOutputRange(-0.4, 0.4); // Left movement and right move 
		pidGyro.setAbsoluteTolerance(kAngleTolerance); // Error range 
		pidGyro.setContinuous(true); 
		angle = 0;

		pidLeft.setInputRange(-1, 1);
		pidLeft.setOutputRange(-1, 1);
		pidLeft.setAbsoluteTolerance(kSpeedTolerance);
		pidLeft.setContinuous(false);

		pidRight.setInputRange(-1, 1);
		pidRight.setOutputRange(-1, 1);
		pidRight.setAbsoluteTolerance(kSpeedTolerance);
		pidRight.setContinuous(false);

		LiveWindow.add(pidGyro); // Adds to Smart dashboard
		LiveWindow.add(pidLeft);
		LiveWindow.add(pidRight);

		leftEncoder.setMaxPeriod(0.1);
		rightEncoder.setMaxPeriod(0.1);

		resetEncoders();

	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(Robot.driveInputCom);
	}

	/**
	 * Used for tele-operated control of the drive train
	 * @author Taha Bokhari
	 * @param drive the y speed (forward/backward) (-0.3, 0, 0) <-- Backwards
	 * @param strafe the x speed (side to side) (0, 0.3, 0) <-- Right
	 * @param rotate the z rotation (rotation) (0, 0, 0.3) <-- Rotate right
	 */
	public void drive(double drive, double strafe, double rotate) {

		// System.out.println("INPUTS drive  " + drive + "  strafe  " + strafe + "  rotate  " + rotate);
		// System.out.println("TALONS FL: " + frontLeftTalon.get() + " BL: " + backLeftTalon.get() + " FR: " + frontRightTalon.get() + " BR: " + backRightTalon.get());
		
		// Use the driveCartesian WPI method, passing in vertical motion, strafing, and tank rotation.
		// driveBase.driveCartesian(drive, strafe, rotate, angle);
		driveBase.driveCartesian(drive, strafe, rotate);
//		Robot.logger.debug("Right enc: " + rightEncoder.getDistance() + " Left enc " + leftEncoder.getDistance() + " Avg enc " + avgEncoderPos());
	}
	
	public void autoDrive(float vertDist, float vertSpeed) {

		// evenDrive();
//		Robot.logger.debug("Right enc: " + rightEncoder.getDistance() + " Left enc " + leftEncoder.getDistance() + " Avg enc " + avgEncoderPos());
//		Robot.logger.verbose("Avg enc " + avgEncoderPos());
//		Robot.logger.debug("Right enc: " + rightEncoder.get() + " Left enc " + leftEncoder.get());
		drive(-vertSpeed, 0, 0.01);
		

	}

	public void autoDriveToPoint(float point, float speed) {
		
		if (ultra.getRangeInches() < point-1) {
			drive(speed, 0, rotAdjustment);
		}
		else if (ultra.getRangeInches() > point+1) {
			drive(-speed, 0, rotAdjustment);
		}
		else {
			drive(0, 0, 0);
		}
		
	}
	
	public void autoAngledDrive(float dist, float speed, float angle) {
		
		driveBase.drivePolar(speed, angle, 0);
		
	}
	
	public void autoStrafe(float strafeDist, float strafeSpeed) {

//		Robot.logger.debug("distance: " + avgAbsEncoderPos());
		Robot.logger.verbose("Avg enc " + avgEncoderPos());
		drive(0, strafeSpeed, rotAdjustment);
		
	}

	public void rotate(float rotatePoint, float rotateSpeed) {
		
		angle = gyro.getAngle();
		
		Robot.logger.info("Gyro value: " + angle);
		
		if (angle < rotatePoint - kAngleTolerance) {
			drive(0.0, 0.0, rotateSpeed);
		}
		else if (angle > rotatePoint + kAngleTolerance) {
			drive(0.0, 0.0, -rotateSpeed);
		}

	}
	
	public void alignScale() {
		double dist = ultra.getRangeInches();
		if (dist > (RobotMap.SCALE_ALIGNMENT_DIST - RobotMap.ALLOWANCE) && dist < (RobotMap.SCALE_ALIGNMENT_DIST+RobotMap.ALLOWANCE)) {
			OI.driveController.setRumble(RumbleType.kLeftRumble, 1);
		}
		else {
			OI.driveController.setRumble(RumbleType.kLeftRumble, 0);
		}
	}
	
	public void pidDrive(float dist) {
		if (pidLeft.onTarget()) {
			pidLeft.disable();
		}
		else {
			pidLeft.setSetpoint(dist);
			pidLeft.enable();
			
		}
	}
	
	public void stop() {
		// Stop driving. Failsafe if connection is interrupted or robot code ends.
		driveBase.driveCartesian(0, 0, 0);
	}
	
	/**
	 * Start the PID controller for the gyro to
	 * turn the robot
	 * 
	 * @param degrees The setpoint of the robot
	 */
	public void enableGyroPID(float degrees) {
		// Robot.logger.debug("Running PID TURN");
		pidGyro.setSetpoint(degrees);
		pidGyro.enable();
	}
	
	/**
	 * Disable the gyro's PID controller. Call
	 * this method in the end() method of a command
	 * after enabling and turning the robot.
	 */
	public void disableGyroPID() {
		pidGyro.disable();
	}
	
	/**
	 * Drive to the setpoint set in the gyro's PID controller.
	 * Call this method in the execute() method of a command after
	 * calling enableGyroPID(float) in the init() method of a command.
	 */
	public void rotatePID() {
		drive(0, 0, rotateToAngleRate); 
}
	
	/**
	 * Return whether the robot is on target with its
	 * setpoint set in the gyro's PID controller
	 * 
	 * @return True if the robot is on target and
	 * false otherwise
	 */
	public boolean isGyroPIDOnTarget() {
		return pidGyro.onTarget();
	}
	
	/**
	 * Get the current gyro angle
	 * 
	 * @return The gyro angle in degrees
	 */
	public double getGyroAngle() {
		return gyro.getAngle();
	}
	
	public double quadCurve(double val) {

//		if (val >= 0) { // Apply a quadratic curve to the inputs of the controller (preserving positive/negative values)
//			val *= val;
//		} else {
//			val *= val;
//			val = -val;
//		}
		
		val = Math.pow(val, 3);
		
		if (Math.abs(val) < 0.03) { // Apply custom deadband directly to inputs
			val = 0;
		}

		return val;
	}
	
	public double applySpeedCap(double speed, double cap) {
		if (speed > cap) {
			speed = cap;
		}
		return speed;
	}
	
	private void evenDrive() {

		if (leftEncoder.getDistance() > rightEncoder.getDistance() + 0.6f) {
			rotAdjustment = -0.35f;
		}
		else if (leftEncoder.getDistance() > rightEncoder.getDistance() + 0.6f) {
			rotAdjustment = 0.35f;
		}

	}

	public double avgEncoderPos() {
		// return (leftEncoder.getDistance() + rightEncoder.getDistance()) / 2;
		return (leftEncoder.getDistance());
	}

	public double avgAbsEncoderPos() {
		// return (Math.abs(leftEncoder.getDistance()) + Math.abs(rightEncoder.getDistance())) / 2;
		return (Math.abs(leftEncoder.getDistance()));
	}
	
	@Override
	public void pidWrite(double output) {
		rotateToAngleRate = output; //Outputs Turning rate from PID
		Robot.logger.debug("AngleRate (PID OUTPUT)" + rotateToAngleRate);
		drive(0, 0, rotateToAngleRate);
		SmartDashboard.putNumber("PID Value", rotateToAngleRate);
		
//		wheelOutputRate = output;
//		backLeftMotor.set(output);
		
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
	
	public void setupTalons(WPI_TalonSRX talon) {
		talon.setSafetyEnabled(true);
		talon.setExpiration(expiraton);
		talon.setInverted(false);
		talon.setNeutralMode(com.ctre.phoenix.motorcontrol.NeutralMode.Brake);
	}
	
}



