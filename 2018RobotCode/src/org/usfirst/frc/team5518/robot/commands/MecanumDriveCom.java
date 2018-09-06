package org.usfirst.frc.team5518.robot.commands;

import org.usfirst.frc.team5518.robot.OI;
import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.RobotMap;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class MecanumDriveCom extends Command {
	
	public double strafeSpeed, driveSpeed, zRotation;
	public double ltValue, rtValue;
	public boolean notnos; // slow button
	
	public boolean lbPressed;
	
    public MecanumDriveCom() {

    		requires(Robot.driveTrainSub); // Declare subsystem dependencies
    		
		strafeSpeed = 0; driveSpeed = 0; zRotation = 0; // Give default values to declared variables
		notnos = false;
		
	}
    
    // Called just before this Command runs the first time
    protected void initialize() {
    		Robot.logger.info("Mecanum Drive Command Initializing");
    }
    
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
		
		// ------------------------- XBOX CONTROLS ------------------------- //
		// Forwards/Backwards movement
		driveSpeed = OI.driveController.getRawAxis(RobotMap.XBOX_LSTICKY); // Set vertical movement to left stick Y axis
		driveSpeed = Robot.driveTrainSub.readSpeedValues(driveSpeed, RobotMap.KY); // Change sens and cap
		
		zRotation = OI.driveController.getRawAxis(RobotMap.XBOX_RSTICKX); // Set pivot rotations to right stick
		zRotation = Robot.driveTrainSub.readSpeedValues(zRotation, RobotMap.KZ); // Change sens and cap
		
		strafeSpeed = OI.driveController.getRawAxis(RobotMap.XBOX_LSTICKX); // Set strafe movement to left stick X axis
		strafeSpeed = Robot.driveTrainSub.readSpeedValues(strafeSpeed, RobotMap.KX); // Change sens and cap
		
		notnos = OI.driveController.getRawButton(RobotMap.XBOX_RBUMPER); // Set slow-mode to right bumper
		lbPressed = OI.driveController.getRawButton(RobotMap.XBOX_LBUMPER);
		
		if (notnos) {
			driveSpeed *= 0.5f; // If the slow button is pressed, move at half speed
		}
		
		if (lbPressed) {
			Robot.driveTrainSub.alignScale();  // Left bumper used to line up for scale shot
		}
		
		// Call the drive() function from the driveTrainSubsystem, pass in collected speed values
		Robot.driveTrainSub.drive(driveSpeed, strafeSpeed, zRotation); 
		Robot.logger.debug("forward move:   " + driveSpeed + "   strafe:   " + strafeSpeed + "   zRotation:   " + zRotation);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
		Robot.driveTrainSub.stop(); // Call the failsafe Stop() function
    }

    // Called when another command which requires one or more of the same subsystems is scheduled to run
    protected void interrupted() {
		Robot.logger.info("ROBOT INTERRUPTED");
		Robot.driveTrainSub.stop(); // Call the failsafe Stop() function
    }
}



