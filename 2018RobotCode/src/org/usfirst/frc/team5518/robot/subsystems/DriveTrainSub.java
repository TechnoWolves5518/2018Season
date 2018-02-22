package org.usfirst.frc.team5518.robot.subsystems;

import org.usfirst.frc.team5518.robot.Logger;
import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.RobotMap;
import org.usfirst.frc.team5518.robot.commands.MecanumDriveCom;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSourceType;

//import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Drivetrain subsystem is used to drive the robot by powering the
 * motors for each of the wheels.
 */
public class DriveTrainSub extends Subsystem implements PIDOutput {

	/** Calculations for encoders */
	private static final double kDistancePerRevolution = 8 * Math.PI; // Distance traveled in one wheel rotation (circumference)
	private static final double kPulsesPerRevolution = 360; // Encoder pulses in one shaft revolution
	private static final double kDistancePerPulse = kDistancePerRevolution / kPulsesPerRevolution; // Distance in inches per pulse
	
	/** Constants for driving and tolerances */
	public static final float kExpiration = 0.5f; // Motor Safety expiration period
	public static final float kAngleTolerance = 0.5f;
	public static final float kSpeedTolerance = 0.01f;

	//	// Construct and define motor controllers
	//	private WPI_TalonSRX frontLeftTalon = new WPI_TalonSRX(RobotMap.FRONT_LEFT);
	//	private WPI_TalonSRX backLeftTalon = new WPI_TalonSRX(RobotMap.BACK_LEFT);
	//	private WPI_TalonSRX frontRightTalon = new WPI_TalonSRX(RobotMap.FRONT_RIGHT);
	//	private WPI_TalonSRX backRightTalon = new WPI_TalonSRX(RobotMap.BACK_RIGHT);

	/** VictorSP controllers on test bot */
	private VictorSP frontLeftMotor;
	private VictorSP backLeftMotor;
	private VictorSP frontRightMotor;
	private VictorSP backRightMotor;
	
	/** Combine motor controllers into a drive base */
	private MecanumDrive driveBase;
	
	/** Sensors used for driving */
	private Encoder leftEncoder; 
	private Encoder rightEncoder;
	private ADXRS450_Gyro gyro;

	/** PID controllers for encoders and gyro */
	private PIDController pidLeft;
	private PIDController pidRight;
	private PIDController pidGyro;

	/** Rotation adjustment for driving even */
	private float rotAdjustment = 0;
	
	/** Rotation speed to allow rotation to a setpoint */
	private double rotateToAngleRate;
	
	/**
	 * Construct a new subsystem for the drivetrain
	 */
	public DriveTrainSub() {
		// init motor speed controllers
		frontLeftMotor = new VictorSP(RobotMap.FRONT_LEFT);
		backLeftMotor = new VictorSP(RobotMap.BACK_LEFT);
		frontRightMotor = new VictorSP(RobotMap.FRONT_RIGHT);
		backRightMotor = new VictorSP(RobotMap.BACK_RIGHT);
		
		// set up victors
		setupVictors(frontLeftMotor);
		setupVictors(backLeftMotor);
		setupVictors(frontRightMotor);
		setupVictors(backRightMotor);
		
		// Construct drivetrain
		// driveBase = new MecanumDrive(frontLeftTalon, backLeftTalon, frontRightTalon, backRightTalon);
		driveBase = new MecanumDrive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);
		
		// set up drivetrain
		driveBase.setSafetyEnabled(true);
		driveBase.setExpiration(kExpiration);
		
		// Construct sensors
		leftEncoder = new Encoder(0, 1, true, EncodingType.k4X);
		rightEncoder = new Encoder(2, 3, false, EncodingType.k4X);
		gyro = new ADXRS450_Gyro();
		
		// Construct PID controllers
		pidGyro = new PIDController(0.06, 0, 0, gyro, this); // tune kP, kI, kD values here
		pidLeft = new PIDController(0, 0, 0, leftEncoder, frontLeftMotor);
		pidRight = new PIDController(0, 0, 0, rightEncoder, frontRightMotor);
		
		// Configure left encoder
		leftEncoder.setDistancePerPulse(kDistancePerPulse);
		leftEncoder.setMaxPeriod(0.1);
		leftEncoder.setPIDSourceType(PIDSourceType.kDisplacement);
		
		// Configure right encoder
		rightEncoder.setDistancePerPulse(kDistancePerPulse);
		rightEncoder.setMaxPeriod(0.1);
		rightEncoder.setPIDSourceType(PIDSourceType.kDisplacement);

		// Configure Gyro PID controller
		pidGyro.setInputRange(-180.0f, 180.0f); // Angle Input
		pidGyro.setOutputRange(-0.2, 0.2); // Left movement and right move 
		pidGyro.setAbsoluteTolerance(kAngleTolerance); // Error range 
		pidGyro.setContinuous(true); 
		
		// Configure Left Encoder PID controller
		pidLeft.setInputRange(-1, 1);
		pidLeft.setOutputRange(-1, 1);
		pidLeft.setAbsoluteTolerance(kSpeedTolerance);
		pidLeft.setContinuous(false);
		
		// Configure Right PID controller
		pidRight.setInputRange(-1, 1);
		pidRight.setOutputRange(-1, 1);
		pidRight.setAbsoluteTolerance(kSpeedTolerance);
		pidRight.setContinuous(false);
		
		// Add PID controllers for test mode
		LiveWindow.add(pidGyro);
		LiveWindow.add(pidLeft);
		LiveWindow.add(pidRight);
		
		// reset encoders to 0
		resetEncoders();
	}

	/**
	 * Set the default command to be scheduled when
	 * a subsystem is constructed
	 */
	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new MecanumDriveCom());
	}

	/**
	 * Used for teleoperated control of the drive train
	 * @author Taha Bokhari
	 * @param drive the y speed (forward/backward) (-0.3, 0, 0) <-- Backwards
	 * @param strafe the x speed (side to side) (0, 0.3, 0) <-- Right
	 * @param rotate the z rotation (rotation) (0, 0, 0.3) <-- Rotate right
	 */
	public void drive(double drive, double strafe, double rotate) {
		// log values to console
		Logger.getInstance().debug("INPUTS drive  " + drive + "  strafe  " + strafe + "  rotate  " + rotate);
		// System.out.println("TALONS FL: " + frontLeftTalon.get() + " BL: " + backLeftTalon.get() + " FR: " + frontRightTalon.get() + " BR: " + backRightTalon.get());

		// Use the driveCartesian WPI method, passing in vertical motion, strafing, and tank rotation.
		// driveBase.driveCartesian(strafe, drive, rotate, gyro.getAngle());
		driveBase.driveCartesian(strafe, -drive, rotate);
	}
	
	/**
	 * Drive the robot for a certain distance and
	 * specified speed
	 * 
	 * @param distance The distance in inches for the robot to
	 * travel.
	 * @param speed The speed to drive at. If positive, drive forwards.
	 * If negative, drive backwards.
	 */
	public void drive(float distance, float speed) {
		// log encoder values
		// evenDrive();
		Logger.getInstance().debug("Right enc: " + rightEncoder.getDistance() + " Left enc " + leftEncoder.getDistance() + " Avg enc " + avgEncoderPos());
		Logger.getInstance().debug("Right enc: " + rightEncoder.get() + " Left enc " + leftEncoder.get());
		
		// drive
		drive(0, speed, 0);

	}

	/**
	 * Drive the robot for a certain distance and
	 * specified speed
	 * 
	 * @param distance The distance in inches for the robot to
	 * travel.
	 * @param speed The speed to drive at. If positive, drive forwards.
	 * If negative, drive backwards.
	 */
	public void strafe(float distance, float speed) {
		Logger.getInstance().debug("distance: " + avgAbsEncoderPos());
		drive(speed, 0, rotAdjustment);
	}

	/**
	 * 
	 * @param degrees
	 * @param speed
	 */
	public void rotate(float degrees, float speed) {
		double angle = gyro.getAngle();
		Logger.getInstance().debug("Gyro value: " + angle);
		
		if (angle < degrees - kAngleTolerance) {
			drive(0.0, 0.0, speed);
		}
		else if (angle > degrees + kAngleTolerance) {
			drive(0.0, 0.0, -speed);
		}
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
	 * 
	 * @param dist
	 */
	public void pidDrive(float dist) {
		if (pidLeft.onTarget()) {
			pidLeft.disable();
		}
		else {
			pidLeft.setSetpoint(dist);
			pidLeft.enable();
			
		}
	}

	/**
	 * Stop driving. Failsafe if connection is interrupted or robot code ends.
	 */
	public void stop() {
		driveBase.driveCartesian(0, 0, 0);
	}
	
	/**
	 * Get the output from the gyro's PID controller
	 * to facilitate in rotating to a setpoint
	 */
	@Override
	public void pidWrite(double output) {
		rotateToAngleRate = output; //Outputs Turning rate from PID
		Logger.getInstance().debug("AngleRate (PID OUTPUT)" + rotateToAngleRate);
		drive(0, 0, rotateToAngleRate); // need to comment this out when not in test mode
		SmartDashboard.putNumber("PID Value", rotateToAngleRate);
		
//		wheelOutputRate = output;
//		backLeftMotor.set(output);
	}
	
	/**
	 * Apply a quadratic filter to the values set.
	 * Will be used to filter values from the joystick
	 * when setting it in the motor controllers.
	 * 
	 * @param val The speed value to apply the filter to
	 * @return The filtered speed value
	 */
	public double quadCurve(double val) {
		// Apply a quadratic curve to the inputs of the controller (preserving positive/negative values)
		if (val >= 0) { 
			val *= val;
		} 
		else {
			val *= val;
			val = -val;
		}

		// Apply custom deadband directly to inputs
		if (Math.abs(val) < 0.1) {
			val = 0;
		}

		return val;
	}
	
	/**
	 * Apply a filter for a threshold for the maximum speed value that
	 * can be set in the motor controllers
	 * 
	 * @param speed The speed
	 * @param cap The maximum speed threshold
	 * @return The filtered speed
	 */
	public double applySpeedCap(double speed, double cap) {
		if (speed > cap) {
			speed = cap;
		}
		
		return speed;
	}
	
	/**
	 * Helper method to drive straight based on the distance
	 * reported by the encoders.
	 */
	private void evenDrive() {
		if (leftEncoder.getDistance() > rightEncoder.getDistance() + 0.6f) {
			rotAdjustment = -0.35f;
		}
		else if (leftEncoder.getDistance() > rightEncoder.getDistance() + 0.6f) {
			rotAdjustment = 0.35f;
		}
	}

	/**
	 * Reset the encoder ticks back to 0.
	 */
	public void resetEncoders() {
		leftEncoder.reset();
		rightEncoder.reset();
	}
	
	/**
	 * Get the current gyro angle
	 * 
	 * @return The gyro angle in degrees
	 */
	public double getGyroAngle() {
		return gyro.getAngle();
	}

	/**
	 * Reset the gyro to a heading of 0 degrees
	 * using settings by calibrating the gyro at
	 * the beginning of turning the bot on.
	 */
	public void resetGyro() {
		gyro.reset();
	}
	
	/**
	 * Calibrate the gyro at the beginning of
	 * turning the bot on. This blocks the thread,
	 * so please use wisely!
	 */
	public void calibrateGyro() {
		gyro.calibrate();
	}
	
	/**
	 * Get the average distance traveled from both encoders. 
	 * Will be used for driving forwards/backwards.
	 * 
	 * @return The average distance traveled
	 */
	public double avgEncoderPos() {
		return (leftEncoder.getDistance() + rightEncoder.getDistance()) / 2;
	}

	/**
	 * Get the absolute average distance traveled
	 * from both encoders. Will be used for strafing.
	 * 
	 * @return The average distance travelled
	 */
	public double avgAbsEncoderPos() {
		return (Math.abs(leftEncoder.getDistance()) + Math.abs(rightEncoder.getDistance())) / 2;
	}
	
	/**
	 * Helper method to set up victor motor controllers
	 * 
	 * @param victor The victor to set up
	 */
	private void setupVictors(VictorSP victor) {
		System.out.println("SETTING UP VICTORS");

		victor.enableDeadbandElimination(true);
		victor.setSafetyEnabled(false);
		victor.setExpiration(kExpiration);
		victor.setInverted(false);
	}
	
}

