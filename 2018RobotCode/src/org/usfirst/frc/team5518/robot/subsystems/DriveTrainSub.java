package org.usfirst.frc.team5518.robot.subsystems;

import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.RobotMap;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

/**
 *
 */
public class DriveTrainSub extends Subsystem {
	
	// Construct and define motor controllers
	private VictorSP frontLeftMotor = new VictorSP(RobotMap.FRONT_LEFT);
	private VictorSP backLeftMotor = new VictorSP(RobotMap.BACK_LEFT);
	private VictorSP frontRightMotor = new VictorSP(RobotMap.FRONT_RIGHT);
	private VictorSP backRightMotor = new VictorSP(RobotMap.BACK_RIGHT);
	
	// Combine all the motor controllers into a drive base
	private MecanumDrive driveBase = new MecanumDrive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);
	
	public DriveTrainSub() {
		
		// enable deadband elimination
		frontLeftMotor.enableDeadbandElimination(true);
		backLeftMotor.enableDeadbandElimination(true);
		frontRightMotor.enableDeadbandElimination(true);
		backRightMotor.enableDeadbandElimination(true);
		
		// enable the safety
		frontLeftMotor.setSafetyEnabled(true);
		backLeftMotor.setSafetyEnabled(true);
		frontRightMotor.setSafetyEnabled(true);
		backRightMotor.setSafetyEnabled(true);
		
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(Robot.driveInputCom);
    }
    
    /**
     * A cool Taha method.
     * @author Taha Bokhari
     * @param ySpeed the y speed
     * @param xSpeed the x speed
     * @param zRot the z rotation
     */
    public void drive(double ySpeed, double xSpeed, double zRot) {
		driveBase.driveCartesian(ySpeed, xSpeed, zRot);
		// Use the driveCartesian WPI method, passing in vertical motion, strafing, and tank rotation.
    }
    
    public void stop() {
		driveBase.driveCartesian(0, 0, 0);
		// Stop driving. Failsafe if connection is interrupted or robot code ends.
    }
}

