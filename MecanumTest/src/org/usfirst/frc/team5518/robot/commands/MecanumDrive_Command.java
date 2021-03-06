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
	public double ltValue, rtValue;
	private Joystick driveControl;
	private Joystick flightControl;
	
    public MecanumDrive_Command() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
		requires(Robot.driveTrainSub);
		ySpeed = 0; xSpeed = 0; zRotation = 0;
		driveControl = OI.xbox; // Set locally defined controller to the control from the OI so that calling it is simpler
		flightControl = OI.flight; // Set locally defined controller to the control from the OI so that calling it is simpler
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println("START TRYING");
    }
    
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
		
		// ------------------------- XBOX CONTROLS -------------------------
		
		xSpeed = RobotMap.KX * driveControl.getRawAxis(RobotMap.XBOX_LSTICKY); // Set vertical movement to left stick
		xSpeed = xSpeed * xSpeed;
		zRotation = RobotMap.KZ * driveControl.getRawAxis(RobotMap.XBOX_RSTICKX); // Set tank rotation to right stick
		zRotation = zRotation * zRotation;
		
		// ySpeed = RobotMap.KY * driveControl.getRawAxis(RobotMap.XBOX_LSTICKX); // Method 1 of strafing
		
		ltValue = driveControl.getRawAxis(RobotMap.XBOX_LTRIGGER);
		rtValue = driveControl.getRawAxis(RobotMap.XBOX_RTRIGGER);
		
		System.out.println("forward move:   " + xSpeed + "   strafe:   " + ySpeed + "   zRotation:   " + zRotation);
		
		// Determine which trigger value is larger - that value will be dominant
		
		// Method 2 of strafing
		
		if (ltValue > rtValue) {
			ySpeed = RobotMap.KY * -driveControl.getRawAxis(RobotMap.XBOX_LTRIGGER); // LT makes strafe value negative
			ySpeed = -1 * (ySpeed * ySpeed);
		}
		else if (rtValue > ltValue) {
			ySpeed = RobotMap.KY * driveControl.getRawAxis(RobotMap.XBOX_RTRIGGER); // RT makes strafe value positive
			ySpeed = ySpeed * ySpeed;
		}
		else {
			ySpeed = 0;
		}
	
	
		// ------------------------- FLIGHT CONTROLS -------------------------
		/*
		ySpeed = flightControl.getRawAxis(RobotMap.JOYSTICK_YAXIS); // Set vertical movement to forward/backward (y axis)
		zRotation = flightControl.getRawAxis(RobotMap.JOYSTICK_ZAXIS); // Set rotation movement to turning stick (z axis)
		xSpeed = flightControl.getRawAxis(RobotMap.JOYSTICK_XAXIS); // Set strafe movment to moving stic l and r (x axis)
		 */
	
		Robot.driveTrainSub.Drive(ySpeed, xSpeed, zRotation);
		// System.out.println("------------OUTPUT-------------");
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









