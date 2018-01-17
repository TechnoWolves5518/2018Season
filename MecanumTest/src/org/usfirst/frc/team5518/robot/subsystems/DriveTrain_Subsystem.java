package org.usfirst.frc.team5518.robot.subsystems;

import org.usfirst.frc.team5518.robot.RobotMap;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

/**
 *
 */
public class DriveTrain_Subsystem extends Subsystem {
	
	// Construct and define motor controllers
	public VictorSP frontLeftMotor = new VictorSP(RobotMap.FRONT_LEFT);
	public VictorSP backLeftMotor = new VictorSP(RobotMap.BACK_LEFT);
	public VictorSP frontRightMotor = new VictorSP(RobotMap.FRONT_RIGHT);
	public VictorSP backRightMotor = new VictorSP(RobotMap.BACK_RIGHT);
	
	// Combine all the motor controllers into a drive base
	private MecanumDrive driveBase = new MecanumDrive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void Drive(double ySpeed, double xSpeed, double zRot) {
    		driveBase.driveCartesian(ySpeed, xSpeed, zRot);
    		// Use the driveCartesian WPI method, passing in vertical motion, strafing, and tank rotation.
    }
    
    public void Stop() {
    		driveBase.driveCartesian(0, 0, 0);
    		// Stop driving. Failsafe if connection is interrupted or robot code ends.
    }
}

