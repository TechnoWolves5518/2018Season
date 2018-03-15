package org.usfirst.frc.team5518.robot.commands;

import org.usfirst.frc.team5518.robot.OI;
import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ExtendedIntakeCom extends Command {

	private double leftSpeed, rightSpeed;
	private double leftInput, rightInput;

	private double speed;
	
	public ExtendedIntakeCom() {    		
		// eg. requires(chassis);
	}

	// Called just before this Command runs the first time
	protected void initialize() {

	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		leftInput = OI.sfController.getRawAxis(RobotMap.XBOX_LTRIGGER);
		rightInput = OI.sfController.getRawAxis(RobotMap.XBOX_RTRIGGER);
		
		leftSpeed = speedControl(leftSpeed);
		rightSpeed = speedControl(rightSpeed);
		
		Robot.sfSub.extendedIntake(leftSpeed, rightSpeed);
	}

	private double speedControl(double input) {
		
		speed = input / 3;
		
		if (input >= 0.1) {
			speed += 0.33;
			return speed;
		}
		else {
			return 0;
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
