package org.usfirst.frc.team5518.robot.commands;

import org.usfirst.frc.team5518.robot.OI;
import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.Joystick;

/**
 *
 */
public class MecanumDrive_Command extends Command {
	
	public double ySpeed, xSpeed, zRotation;
	public double ky, kx, kz;
	public double ltValue, rtValue;
	private Joystick driveControl;
	private Joystick flightControl;
	
    public MecanumDrive_Command() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    		requires(Robot.driveTrainSub);
    		ySpeed = 0; xSpeed = 0; zRotation = 0;
    		ky = 1; kx = 1; kz = 1; // Alter the max speeds to tune the movement of the robot
    		driveControl = OI.xbox; // Set locally defined controller to the control from the OI so that calling it is simpler
    		flightControl = OI.flight; // Set locally defined controller to the control from the OI so that calling it is simpler
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    		
    		// ------------------------- XBOX CONTROLS -------------------------
    		
    		ySpeed = ky * driveControl.getRawAxis(RobotMap.XBOX_LSTICKY); // Set vertical movement to left stick
    		zRotation = kz * driveControl.getRawAxis(RobotMap.XBOX_RSTICKX); // Set tank rotation to right stick
    		
    		xSpeed = kx * driveControl.getRawAxis(RobotMap.XBOX_LSTICKX); // Method 1 of strafing
    		
    		ltValue = driveControl.getRawAxis(RobotMap.XBOX_LTRIGGER);
    		rtValue = driveControl.getRawAxis(RobotMap.XBOX_RTRIGGER);
    		
    		// Determine which trigger value is larger - that value will be dominant
    		/*
    		// Method 2 of strafing
    		if (ltValue > rtValue) {
    			xSpeed = -driveControl.getRawAxis(RobotMap.XBOX_LTRIGGER); // LT makes strafe value negative
    		}
    		else if (rtValue > ltValue) {
    			xSpeed = driveControl.getRawAxis(RobotMap.XBOX_RTRIGGER); // RT makes strafe value positive
    		}
    		else {
    			xSpeed = 0;
    		}
    		*/
    		
    		// ------------------------- FLIGHT CONTROLS -------------------------
    		/*
    		ySpeed = driveControl.getRawAxis(RobotMap.JOYSTICK_YAXIS); // Set vertical movement to forward/backward (y axis)
		zRotation = driveControl.getRawAxis(RobotMap.JOYSTICK_ZAXIS); // Set rotation movement to turning stick (z axis)
		xSpeed = driveControl.getRawAxis(RobotMap.JOYSTICK_XAXIS); // Set strafe movment to moving stic l and r (x axis)
    		*/
    		
    		Robot.driveTrainSub.Drive(ySpeed, xSpeed, zRotation);
    		// Call the Drive() function from the DriveTrain_Subsystem, pass in collected 
    		
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    		Robot.driveTrainSub.Stop(); // Call the failsafe Stop() function
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    		Robot.driveTrainSub.Stop(); // Call the failsafe Stop() function
    }
}









