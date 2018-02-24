package org.usfirst.frc.team5518.robot.commands;

import org.usfirst.frc.team5518.robot.OI;
import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class MecanumDriveCom extends Command {
	
	public boolean nos;
	
    public MecanumDriveCom() {
		requires(Robot.driveTrainSub); // Declare subsystem dependency
    		
		// Give default values to declared variables
		nos = false;
	}
    
    /**
     * Called just before this Command runs the first time
     */
    protected void initialize() {
		System.out.println("START DOING"); // For logging purposes
    }
    
    /**
     * Called repeatedly when this Command is scheduled to run
     */
    protected void execute() {
		// ------------------------- XBOX CONTROLS ------------------------- //
		// Get controller inputs
		double driveSpeed = OI.driveController.getRawAxis(RobotMap.XBOX_LSTICKY); // Vertical movement
		double zRotation = OI.driveController.getRawAxis(RobotMap.XBOX_RSTICKX); // Pivotal rotation
		double strafeSpeed = OI.driveController.getRawAxis(RobotMap.XBOX_LSTICKX); // Strafing movement
		nos = OI.driveController.getRawButton(RobotMap.XBOX_RBUMPER); // Set speedy-mode toggle ot right bumper
		
		// Apply squared inputs
		driveSpeed = Robot.driveTrainSub.quadCurve(driveSpeed);
		zRotation = Robot.driveTrainSub.quadCurve(zRotation);
		strafeSpeed = Robot.driveTrainSub.quadCurve(strafeSpeed);
		
		// Limit speed if it is too high
		Robot.driveTrainSub.applySpeedCap(driveSpeed, RobotMap.KY);
		Robot.driveTrainSub.applySpeedCap(zRotation, RobotMap.KZ);
		Robot.driveTrainSub.applySpeedCap(strafeSpeed, RobotMap.KX);
		
		if (!nos) {
			driveSpeed *= 0.66f; // If the speed button isn't pressed, move at 2/3 speed
		}
		
		System.out.println("forward move:   " + driveSpeed + "   strafe:   " + strafeSpeed + "   zRotation:   " + zRotation);
		
		// Call the drive() function from the driveTrainSubsystem, pass in collected speed values
		Robot.driveTrainSub.drive(driveSpeed, strafeSpeed, zRotation); 
    }

    /**
     * Make this return true when this Command no longer needs to run execute()
     */
    protected boolean isFinished() {
        return false;
    }

    /**
     * Called once after isFinished returns true
     */
    protected void end() {
		Robot.driveTrainSub.stop(); // Call the failsafe Stop() function
    }

    /**
     * Called when another command which requires one or more of the same subsystems is scheduled to run
     */
    protected void interrupted() {
		System.out.println("ROBOT INTERRUPTED");
		Robot.driveTrainSub.stop(); // Call the failsafe Stop() function
    }
}









