package org.usfirst.frc.team5518.robot.commands.autonomous;

import org.usfirst.frc.team5518.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */

public class AutoLauncherCom extends Command {
	
	private double seconds;
	
    public AutoLauncherCom(double secs) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.sfSub);
        seconds = secs;
    	this.setTimeout(secs);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
		Robot.logger.debug("AutoLaunch, activate! " + seconds);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	// Put the solenoid on forwards
    	Robot.sfSub.pForward();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (this.isTimedOut()) {
    		Robot.logger.debug("AutoLauncher timed out");
    		Robot.sfSub.pReverse();
    	}
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
