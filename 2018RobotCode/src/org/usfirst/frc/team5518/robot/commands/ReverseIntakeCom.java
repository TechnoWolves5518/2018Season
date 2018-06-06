package org.usfirst.frc.team5518.robot.commands;

import org.usfirst.frc.team5518.robot.OI;
import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ReverseIntakeCom extends Command {

	private double intakeAdjustment;

	public ReverseIntakeCom() {
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {

		intakeAdjustment = OI.sfController.getRawAxis(RobotMap.XBOX_RSTICKX) * 0.1;

		Robot.sfSub.intake(-RobotMap.INTAKE_SPEED, -RobotMap.SECONDARY_INTAKE_SPEED, -RobotMap.EXTENDED_INTAKE_SPEED, intakeAdjustment);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return !OI.sfController.getRawButton(RobotMap.XBOX_YBTN);
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.sfSub.intake(0.0, 0.0, 0.0, 0.0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		Robot.sfSub.intake(0.0, 0.0, 0.0, 0.0);
	}
}
