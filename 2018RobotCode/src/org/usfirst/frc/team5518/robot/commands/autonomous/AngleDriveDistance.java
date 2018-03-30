package org.usfirst.frc.team5518.robot.commands.autonomous;

import org.usfirst.frc.team5518.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AngleDriveDistance extends Command {
	
	private float distance;
	private float speed;
	private float angle;
	
    public AngleDriveDistance(float m_distance, float m_speed, float m_angle) {
        // Use requires() here to declare subsystem dependencies
    		requires(Robot.driveTrainSub);
    		
    		distance = m_distance;
    		speed = m_speed;
    		angle = m_angle;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    		Robot.logger.debug("running AngleDriveDistance");
		Robot.driveTrainSub.autoAngledDrive(distance, speed, angle);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    		
    		if (!(Robot.driveTrainSub.ultra.getRangeInches() < distance)) {
			return true;
		}
		else {
			return false;
		}
    		
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
