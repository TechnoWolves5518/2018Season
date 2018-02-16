package org.usfirst.frc.team5518.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team5518.robot.Robot;

/**
 *
 */
public class StrafeDistance extends Command {

	private float distance;
	private float speed;

	public StrafeDistance(float m_distance, float m_speed) {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.autoDriveSub);

		distance = m_distance;
		speed = m_speed;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.autoDriveSub.autoStrafe(distance, speed);
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
