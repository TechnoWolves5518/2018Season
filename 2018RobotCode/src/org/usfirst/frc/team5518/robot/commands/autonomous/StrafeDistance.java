package org.usfirst.frc.team5518.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team5518.robot.Logger;
import org.usfirst.frc.team5518.robot.Robot;

/**
 *
 */
public class StrafeDistance extends Command {

	/** The distance to strafe */
	private float distance;
	
	/** The speed to strafe at */
	private float speed;
	
	/**
	 * Construct a new command to strafe
	 * 
	 * @param distance The distance to strafe at
	 * @param speed The speed to strafe at
	 */
	public StrafeDistance(float distance, float speed) {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.driveTrainSub);

		this.distance = distance;
		this.speed = speed;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.driveTrainSub.resetEncoders();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Logger.getInstance().debug("running strafeDistance");
		Robot.driveTrainSub.strafe(distance, speed);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return !(Robot.driveTrainSub.avgAbsEncoderPos() < distance);
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.driveTrainSub.resetEncoders();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
