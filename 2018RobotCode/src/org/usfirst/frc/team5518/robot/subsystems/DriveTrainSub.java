package org.usfirst.frc.team5518.robot.subsystems;

import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.RobotMap;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;

//import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveTrainSub extends Subsystem implements PIDOutput {

	private boolean isTestBot = true;

	//	// Construct and define motor controllers
	//	private WPI_TalonSRX frontLeftTalon = new WPI_TalonSRX(RobotMap.FRONT_LEFT);
	//	private WPI_TalonSRX backLeftTalon = new WPI_TalonSRX(RobotMap.BACK_LEFT);
	//	private WPI_TalonSRX frontRightTalon = new WPI_TalonSRX(RobotMap.FRONT_RIGHT);
	//	private WPI_TalonSRX backRightTalon = new WPI_TalonSRX(RobotMap.BACK_RIGHT);

	private VictorSP frontLeftMotor = new VictorSP(RobotMap.FRONT_LEFT);
	private VictorSP backLeftMotor = new VictorSP(RobotMap.BACK_LEFT);
	private VictorSP frontRightMotor = new VictorSP(RobotMap.FRONT_RIGHT);
	private VictorSP backRightMotor = new VictorSP(RobotMap.BACK_RIGHT);

	public static final double kDistancePerRevolution = 8 * Math.PI; // Distance traveled in one wheel rotation (circumference)
	final static double kPulsesPerRevolution = 360; // Encoder pulses in one shaft revolution
	public static final double kDistancePerPulse = kDistancePerRevolution / kPulsesPerRevolution; // Distance in inches per pulse

	private PIDController pidLeft;
	private PIDController pidRight;
	public PIDController pidGyro;

	private Encoder leftEncoder; 
	private Encoder rightEncoder;
	
	private double wheelOutputRate;

	private ADXRS450_Gyro gyro;
	public double angle;

	private float rotAdjustment = 0;
	private double rotateToAngleRate;
	
	private static final float kAngleTolerance = 0.5f;
	private static final float kSpeedTolerance = 0.01f;
	
	private float expiration = 0.5f; // Motor Safety expiration period

	// Combine all the motor controllers into a drive base
	//	private MecanumDrive driveBase = new MecanumDrive(frontLeftTalon, backLeftTalon, frontRightTalon, backRightTalon);
	private MecanumDrive driveBase = new MecanumDrive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);

	public DriveTrainSub() {
		
		setupVictors(frontLeftMotor);
		setupVictors(backLeftMotor);
		setupVictors(frontRightMotor);
		setupVictors(backRightMotor);
		
		driveBase.setExpiration(expiration);
		
		// Construct sensors
		leftEncoder = new Encoder(0, 1, true, EncodingType.k4X);
		rightEncoder = new Encoder(2, 3, false, EncodingType.k4X);
		gyro = new ADXRS450_Gyro();
		// Construct PID controllers
		pidGyro = new PIDController(0.06, 0, 0, gyro, this); // tune kP, kI, kD values here
		pidLeft = new PIDController(0, 0, 0, leftEncoder, this);
		pidRight = new PIDController(0, 0, 0, rightEncoder, this);
		
		// Configure sensors
		leftEncoder.setDistancePerPulse(kDistancePerPulse);
		rightEncoder.setDistancePerPulse(kDistancePerPulse);

		// Configure PID controllers
		pidGyro.setInputRange(-180.0f, 180.0f); // Angle Input
		pidGyro.setOutputRange(-0.2, 0.2); // Left movement and right move 
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
	 * Used for teleoperated control of the drive train
	 * @author Taha Bokhari
	 * @param drive the y speed (forward/backward) (-0.3, 0, 0) <-- Backwards
	 * @param strafe the x speed (side to side) (0, 0.3, 0) <-- Right
	 * @param rotate the z rotation (rotation) (0, 0, 0.3) <-- Rotate right
	 */
	public void drive(double drive, double strafe, double rotate) {

		System.out.println("INPUTS drive  " + drive + "  strafe  " + strafe + "  rotate  " + rotate);
		//		System.out.println("TALONS FL: " + frontLeftTalon.get() + " BL: " + backLeftTalon.get() + " FR: " + frontRightTalon.get() + " BR: " + backRightTalon.get());
		angle = gyro.getAngle();
		// Use the driveCartesian WPI method, passing in vertical motion, strafing, and tank rotation.
		// driveBase.driveCartesian(strafe, drive, rotate, gyro.getAngle());
		driveBase.driveCartesian(strafe, drive, rotate);
	}
	
	public void autoDrive(float vertDist, float vertSpeed) {

		// evenDrive();
		Robot.logger.debug("Right enc: " + rightEncoder.getDistance() + " Left enc " + leftEncoder.getDistance() + " Avg enc " + avgEncoderPos());
		Robot.logger.debug("Right enc: " + rightEncoder.get() + " Left enc " + leftEncoder.get());
		drive(0, vertSpeed, 0);

	}

	public void autoStrafe(float strafeDist, float strafeSpeed) {

		Robot.logger.debug("distance: " + avgAbsEncoderPos());
		drive(strafeSpeed, 0, rotAdjustment);
		
	}

	public void autoRotate(float rotatePoint, float rotateSpeed) {
		
		angle = gyro.getAngle();
		
		Robot.logger.debug("Gyro value: " + angle);
		
		if (angle < rotatePoint - kAngleTolerance) {
			drive(0.0, 0.0, rotateSpeed);
		}
		else if (angle > rotatePoint + kAngleTolerance) {
			drive(0.0, 0.0, -rotateSpeed);
		}

	}
	
	public void pidTurn(float dgs) {
		// Robot.logger.debug("Running PID TURN");
		pidGyro.enable();
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
	
	public double quadCurve(double val) {

		if (val >= 0) { // Apply a quadratic curve to the inputs of the controller (preserving positive/negative values)
			val *= val;
		} else {
			val *= val;
			val = -val;
		}

		if (Math.abs(val) < 0.1) { // Apply custom deadband directly to inputs
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
		return (leftEncoder.getDistance() + rightEncoder.getDistance()) / 2;
	}

	public double avgAbsEncoderPos() {
		return (Math.abs(leftEncoder.getDistance()) + Math.abs(rightEncoder.getDistance())) / 2;
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
	
	public void setupVictors(VictorSP victor) {
		System.out.println("SETTING UP VICTORS");

		victor.enableDeadbandElimination(true);
		victor.setSafetyEnabled(false);
		victor.setExpiration(expiration);
		victor.setInverted(false);
	}



}

