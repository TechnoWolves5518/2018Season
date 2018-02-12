package org.usfirst.frc.team5518.robot.commands;

import org.usfirst.frc.team5518.robot.OI;
import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class MecanumDriveCom extends Command {
	
	public double strafeSpeed, driveSpeed, zRotation;
	public double ltValue, rtValue;
	public boolean nos;
	
    public MecanumDriveCom() {

    		requires(Robot.driveTrainSub); // Declare subsystem dependencies
    		
		strafeSpeed = 0; driveSpeed = 0; zRotation = 0; // Give default values to declared variables
		nos = false;
		
	}
    
    // Called just before this Command runs the first time
    protected void initialize() {
    		System.out.println("START DOING"); // For logging purposes
    }
    
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
		
		// ------------------------- XBOX CONTROLS ------------------------- //
		// Forwards/Backwards movement
		driveSpeed = OI.driveController.getRawAxis(RobotMap.XBOX_LSTICKY); // Set vertical movement to left stick Y axis
		driveSpeed = Robot.driveTrainSub.quadCurve(driveSpeed); // Apply squared inputs
		driveSpeed *= RobotMap.KY; // Apply speed caps
		
		zRotation = OI.driveController.getRawAxis(RobotMap.XBOX_RSTICKX); // Set pivot rotations to right stick
		zRotation = Robot.driveTrainSub.quadCurve(zRotation); // Apply squared inputs
		zRotation *= RobotMap.KZ; // Apply speed caps
		
		strafeSpeed = OI.driveController.getRawAxis(RobotMap.XBOX_LSTICKX); // Set strafe movement to left stick X axis
		strafeSpeed = Robot.driveTrainSub.quadCurve(strafeSpeed); // Apply squared inputs
		strafeSpeed *= RobotMap.KX; // Apply speed caps
		
		nos = OI.driveController.getRawButton(RobotMap.XBOX_RBUMPER); // Set speedy-mode toggle ot right bumper
		
		if (!nos) {
			driveSpeed *= 0.66f; // If the speed button isn't pressed, move at 2/3 speed
		}
		
    		// System.out.println("forward move:   " + xSpeed + "   strafe:   " + ySpeed + "   zRotation:   " + zRotation);
		
		// Call the drive() function from the driveTrainSubsystem, pass in collected speed values
		Robot.driveTrainSub.drive(driveSpeed, strafeSpeed, zRotation); 
		
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
		System.out.println("ROBOT INTERRUPTED");
    		Robot.driveTrainSub.stop(); // Call the failsafe Stop() function
    }
}









