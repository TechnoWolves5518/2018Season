package org.usfirst.frc.team5518.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.subsystems.DriveTrainSub;

/**
 * Command to rotate the robot to a certain angle
 * either using PID control or bang-bang control
 */
public class RotateDistance extends Command {

	/** The setpoint to rotate to */
	private float degrees;
	
	/** The speed to turn at */
	private float speed;

	/**
	 * Construct a new command to rotate the robot using bang-bang control
	 * @param degrees The setpoint to rotate to
	 * @param speed The speed to turn the robot at. If set equal to or below 0, then rotate
	 */
	public RotateDistance(float degrees, float speed) {
		requires(Robot.driveTrainSub);

		this.degrees = degrees;
		this.speed = speed;
	}
	
	/**
	 * Construct a new command to rotate the robot using PID control
	 * @param degrees
	 */
	public RotateDistance(float degrees) {
		this(degrees, 0);
	}

	/**
	 * Called just before this Command runs the first time
	 */
	protected void initialize() {
		if (speed <= 0) {
			Robot.driveTrainSub.resetGyro();
			Robot.driveTrainSub.enableGyroPID(degrees);
		}
	}

	/**
	 * Called repeatedly when this Command is scheduled to run(non-Javadoc)
	 * @see edu.wpi.first.wpilibj.command.Command#execute()
	 */
	protected void execute() {
		Robot.logger.debug("running rotate");
		
		if (speed <= 0)
			Robot.driveTrainSub.rotatePID();
		else
			Robot.driveTrainSub.rotate(degrees, speed);
	}

	/**
	 * Make this return true when this Command no longer needs to run execute()
	 */
	protected boolean isFinished() {
		if (speed > 0) {
			// check if angle within threshold
			double angle = Robot.driveTrainSub.gyro.getAngle();
			return (angle >= degrees - DriveTrainSub.kAngleTolerance)
				&& (angle <= degrees + DriveTrainSub.kAngleTolerance);
		}
		else if (Robot.driveTrainSub.isGyroPIDOnTarget()) {
			Robot.driveTrainSub.disableGyroPID();
			return true;
		}
		
		return false;
	}

	/**
	 * Called once after isFinished returns true
	 */
	protected void end() {
		Robot.logger.debug("Rotate Distance Ended!");
		Robot.driveTrainSub.disableGyroPID();
		Robot.driveTrainSub.resetGyro();
	}

	/**
	 * Called when another command which requires one or more of the same subsystems is scheduled to run
	 */
	protected void interrupted() {
		end();
	}
}
