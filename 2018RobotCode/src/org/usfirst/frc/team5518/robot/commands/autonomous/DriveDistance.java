package org.usfirst.frc.team5518.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.Logger;

/**
 *
 */
public class DriveDistance extends Command {

	private float distance;
	private float speed;

	public DriveDistance(float m_distance, float m_speed) {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.autoDriveSub);

		distance = m_distance;
		speed = m_speed;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.autoDriveSub.resetEncoders();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.logger.debug("running driveDistance");
		Robot.autoDriveSub.autoDrive(distance, speed);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return !(Robot.autoDriveSub.avgAbsEncoderPos() < distance);
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.autoDriveSub.resetEncoders();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
