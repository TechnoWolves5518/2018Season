package org.usfirst.frc.team5518.robot.commands;

import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TimedIntakeCom extends Command {

    public TimedIntakeCom(double secs) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    		this.setTimeout(secs);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    		Robot.sfSub.intake(RobotMap.INTAKE_SPEED, RobotMap.SECONDARY_INTAKE_SPEED, 0);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return this.isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    		Robot.sfSub.intake(0, 0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}