package org.usfirst.frc.team5518.robot.subsystems;

import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.Logger;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class AutoDriveSub extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	private boolean isDone;
	
	public static final double kDistancePerRevolution = 8 * Math.PI; // Distance traveled in one wheel rotation (circumfrence)
    public static final double kPulsesPerRevolution = 360; // Encoder pulses in one shaft revolution
    public static final double kDistancePerPulse = kDistancePerRevolution / kPulsesPerRevolution; // Distance in inches per pulse
	
    private PIDController pidLeft;
    private PIDController pidRight;
    
    private Encoder leftEncoder = new Encoder(0, 1, true, EncodingType.k4X);
    private Encoder rightEncoder = new Encoder(2, 3, false, EncodingType.k4X);
    
    private ADXRS450_Gyro gyro;
    private double angle;
    
    private float rotAdjustment = 0;
    
	public AutoDriveSub() {
		leftEncoder.setDistancePerPulse(kDistancePerPulse);
        rightEncoder.setDistancePerPulse(kDistancePerPulse);
        
        gyro = new ADXRS450_Gyro();
        angle = 0;
        
        leftEncoder.setMaxPeriod(0.1);
        // leftEncoder.setMinRate(10);
        
        resetEncoders();
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void autoDrive(float vertDist, float vertSpeed) {
    		
    		if (avgEncoderPos() < vertDist) {
    			// evenDrive();
    			Robot.logger.debug("Right enc: " + rightEncoder.getDistance() + " Left enc " + leftEncoder.getDistance() + " Avg enc " + avgEncoderPos());
    			Robot.logger.debug("Right enc: " + rightEncoder.get() + " Left enc " + leftEncoder.get());
    			Robot.driveTrainSub.drive(0, vertSpeed, 0);
    			isDone = false;
    		}
    		else {
    			isDone = true;
    		}
    }
    
    public void autoStrafe(float strafeDist, float strafeSpeed) {

		if (avgAbsEncoderPos() < strafeDist) {
//			evenDrive();
			System.out.println("distance: " + avgAbsEncoderPos());
			Robot.driveTrainSub.drive(strafeSpeed, 0, rotAdjustment);
		}
		
    }
    
    public boolean doneDriving() {
    	return isDone;
    }
    
    public void autoRotate(float rotatePoint, float rotateSpeed) {
    	
    	angle = gyro.getAngle();
//    	angle -= 0.002;
    	System.out.println("Gyro value: " + angle);
    	if (angle < rotatePoint) {
//    		Robot.driveTrainSub.drive(0.0, 0.0, rotateSpeed);
    	}
    	
    }
    
    private void evenDrive() {
    		
    		if (leftEncoder.getDistance() > rightEncoder.getDistance() + 0.3f) {
			rotAdjustment = -0.2f;
		}
    		else if (leftEncoder.getDistance() > rightEncoder.getDistance() + 0.3f) {
    			rotAdjustment = 0.2f;
    		}
    		
    }
    
    private double avgEncoderPos() {
    	return (leftEncoder.getDistance() + rightEncoder.getDistance()) / 2;
    }
    
    private double avgAbsEncoderPos() {
    	return (Math.abs(leftEncoder.getDistance()) + Math.abs(rightEncoder.getDistance())) / 2;
    }
    
    public void resetEncoders() {
    	leftEncoder.reset();
        rightEncoder.reset();
    }
    
    public void resetGyro() {
    	gyro.reset();
    	gyro.calibrate();
    }
    
}








