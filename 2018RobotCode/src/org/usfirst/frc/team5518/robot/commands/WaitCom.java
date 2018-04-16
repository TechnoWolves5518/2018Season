package org.usfirst.frc.team5518.robot.commands;

import org.usfirst.frc.team5518.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class WaitCom extends Command {

	boolean move;
	
	public WaitCom(double time, boolean m_move) {
		this.setTimeout(time);
		requires(Robot.sfSub);
		move = m_move;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (move) {
			Robot.driveTrainSub.autoDrive(100f, 0.4f);
		}
		else {
			Robot.driveTrainSub.autoDrive(0.1f, 0.1f);
		}
		// Robot.debug.info("Waiting...");
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return this.isTimedOut();
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
