package org.usfirst.frc.team5518.robot.subsystems;

import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.Logger;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class AutoDriveSub extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	private boolean isDone;
	
	public static final double kDistancePerRevolution = 8 * Math.PI; // Distance traveled in one wheel rotation (circumfrence)
    public static final double kPulsesPerRevolution = 1440; // Encoder pulses in one shaft revolution
    public static final double kDistancePerPulse = kDistancePerRevolution / kPulsesPerRevolution; // Distance in inches per pulse
	
    private Encoder leftEncoder = new Encoder(0, 1, true, EncodingType.k4X);
    private Encoder rightEncoder = new Encoder(2, 3, false, EncodingType.k4X);
    
    private float rotAdjustment = 0;
    
	public AutoDriveSub() {
		leftEncoder.setDistancePerPulse(kDistancePerPulse);
        rightEncoder.setDistancePerPulse(kDistancePerPulse);
        
        leftEncoder.setMaxPeriod(0.1);
//        leftEncoder.setMinRate(10);
        
//        leftEncoder.setReverseDirection(true);
//        rightEncoder.setReverseDirection(true);
        
        resetEncoders();
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void autoDrive(float vertDist, float vertSpeed) {
    		
    		if (avgEncoderPos() < vertDist) {
    			evenDrive();
    			// System.out.println("distance: " + avgEncoderPos());
    			Robot.logger.debug("Right enc: " + rightEncoder.getDistance() + " Left enc " + leftEncoder.getDistance() + " Avg enc " + avgEncoderPos());
    			Robot.driveTrainSub.drive(vertSpeed, 0, rotAdjustment);
    			// Robot.logger.debug("Outputting drive values");
    			// Robot.driveTrainSub.drive(0.2, 0.0, 0);
    			isDone = false;
    		}
    		else {
    			isDone = true;
    		}
    }
    
    public void autoStrafe(float strafeDist, float strafeSpeed) {

		if (avgEncoderPos() < strafeDist) {
			evenDrive();
			System.out.println("distance: " + avgEncoderPos());
			// Robot.driveTrainSub.drive(0, strafeSpeed, rotAdjustment);
			Robot.driveTrainSub.drive(0.2, 0.0, 0);
		}

    }
    
    public boolean doneDriving() {
    	return isDone;
    }
    
    public void autoRotate(float rotateTime, float rotateSpeed) {
    	
    	if (rotateTime < 50) {
    		rotateTime++;
    		Robot.driveTrainSub.drive(0.0, 0.0, 0.3);
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
    
    public void resetEncoders() {
    	leftEncoder.reset();
        rightEncoder.reset();
    }
    
}








