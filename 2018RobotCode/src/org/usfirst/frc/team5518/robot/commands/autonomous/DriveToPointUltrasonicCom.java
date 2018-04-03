package org.usfirst.frc.team5518.robot.commands.autonomous;

import org.usfirst.frc.team5518.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveToPointUltrasonicCom extends Command {
	
	float point, speed;
	
    public DriveToPointUltrasonicCom(float m_point, float m_speed) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.driveTrainSub);
    		point = m_point;
    		speed = m_speed;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    		Robot.driveTrainSub.autoDriveToPoint(point, speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    		
    		if (Robot.driveTrainSub.ultra.getRangeInches() == point) {
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
