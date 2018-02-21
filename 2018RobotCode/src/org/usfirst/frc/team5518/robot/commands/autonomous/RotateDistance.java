package org.usfirst.frc.team5518.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.subsystems.DriveTrainSub;

/**
 *
 */
public class RotateDistance extends Command {

	private float distance;
	private float speed;

	public RotateDistance(float m_distance, float m_speed) {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.driveTrainSub);

		distance = m_distance;
		speed = m_speed;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.driveTrainSub.resetGyro();
//		Robot.driveTrainSub.pidGyro.enable();
		Robot.driveTrainSub.pidGyro.setSetpoint(distance);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.logger.debug("running rotateDistance");
		// Robot.driveTrainSub.autoRotate(distance, speed);
		Robot.driveTrainSub.pidTurn(distance);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		// return Robot.driveTrainSub.angle == distance;
		if (Robot.driveTrainSub.pidGyro.onTarget()) {
			Robot.driveTrainSub.pidGyro.disable();
			return true;
		}
		else {
			return false;
		}
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.logger.debug("Rotate Distance Ended!");
		Robot.driveTrainSub.resetGyro();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
