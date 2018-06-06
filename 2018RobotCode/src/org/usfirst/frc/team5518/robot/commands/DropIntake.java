package org.usfirst.frc.team5518.robot.commands;

import org.usfirst.frc.team5518.robot.OI;
import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DropIntake extends Command {

	private boolean isLPressed, wasLPressed;
	private boolean extended;

	public DropIntake() {
		requires(Robot.sfSub);
		extended = false;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		isLPressed = OI.sfController.getRawButton(RobotMap.XBOX_LBUMPER);
		wasLPressed = false;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {

		isLPressed = OI.sfController.getRawButton(RobotMap.XBOX_LBUMPER);
		if (isLPressed != wasLPressed && isLPressed == true) {
			extended = !extended;

			if (extended) {
				Robot.sfSub.extendIntake();
			}
			else {
				Robot.sfSub.retractIntake();
			}

		}
		wasLPressed = isLPressed;

	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return !OI.sfController.getRawButton(RobotMap.XBOX_RBUMPER);
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
