package org.usfirst.frc.team5518.robot.subsystems;

import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.Logger;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * 
 */
public class AutoDriveSub extends Subsystem implements PIDOutput {

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	private boolean isDone;

	public static final double kDistancePerRevolution = 8 * Math.PI; // Distance traveled in one wheel rotation (circumference)
	final static double kPulsesPerRevolution = 360; // Encoder pulses in one shaft revolution
	public static final double kDistancePerPulse = kDistancePerRevolution / kPulsesPerRevolution; // Distance in inches per pulse

	private PIDController pidLeft;
	private PIDController pidRight;
	private PIDController pidGyro;

	private Encoder leftEncoder; 
	private Encoder rightEncoder;

	private ADXRS450_Gyro gyro;
	public double angle;

	private float rotAdjustment = 0;
	private double rotateToAngleRate;
	
	private static final int kAngleTolerance = 3;

	public AutoDriveSub() {
		// Construct sensors
		leftEncoder = new Encoder(0, 1, true, EncodingType.k4X);
		rightEncoder = new Encoder(2, 3, false, EncodingType.k4X);
		gyro = new ADXRS450_Gyro();
		// Construct PID controllers
		pidGyro = new PIDController(0, 0, 0, gyro, this); //come back to this
		
		// Configure sensors
		leftEncoder.setDistancePerPulse(kDistancePerPulse);
		rightEncoder.setDistancePerPulse(kDistancePerPulse);
		
		// Configure PID controllers
		pidGyro.setInputRange(-180.0f, 180.0f); // Angle Input
		pidGyro.setOutputRange(-1.0, 1.0); // Left movement and right move 
		pidGyro.setAbsoluteTolerance(2.0f); // Error range 
		pidGyro.setContinuous(true); 
		LiveWindow.add(pidGyro); // Adds to Smart dashboard 
		angle = 0;

		leftEncoder.setMaxPeriod(0.1);
		// leftEncoder.setMinRate(10);
		
		resetEncoders();

	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		//setDefaultCommand(new MySpecialCommand());
	}

	public void turn(float dgs) {
		if (pidGyro.onTarget()) {
			pidGyro.disable();
		}
		else {
			pidGyro.setSetpoint(dgs);
			pidGyro.enable();
			Robot.driveTrainSub.drive(0, 0, rotateToAngleRate);
		}
	}

	public void autoDrive(float vertDist, float vertSpeed) {

		// evenDrive();
		Robot.logger.debug("Right enc: " + rightEncoder.getDistance() + " Left enc " + leftEncoder.getDistance() + " Avg enc " + avgEncoderPos());
		Robot.logger.debug("Right enc: " + rightEncoder.get() + " Left enc " + leftEncoder.get());
		Robot.driveTrainSub.drive(0, vertSpeed, 0);

	}

	public void autoStrafe(float strafeDist, float strafeSpeed) {

		Robot.logger.debug("distance: " + avgAbsEncoderPos());
		Robot.driveTrainSub.drive(strafeSpeed, 0, rotAdjustment);
		
	}

	public void autoRotate(float rotatePoint, float rotateSpeed) {
		
		angle = gyro.getAngle();
		
		Robot.logger.debug("Gyro value: " + angle);
		
		if (angle < rotatePoint - kAngleTolerance) {
			Robot.driveTrainSub.drive(0.0, 0.0, rotateSpeed);
		}
		else if (angle > rotatePoint + kAngleTolerance) {
			Robot.driveTrainSub.drive(0.0, 0.0, -rotateSpeed);
		}

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

	public boolean doneDriving() {
		return isDone;
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

	@Override
	public void pidWrite(double output) {
		rotateToAngleRate = output; //Outputs Turning rate from PID
	}

}








