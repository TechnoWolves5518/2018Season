package org.usfirst.frc.team5518.robot.commands;

import org.usfirst.frc.team5518.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class toLineAndStopCom extends Command {
	
    public toLineAndStopCom() {
    	super(1);
    	
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
		requires(Robot.driveTrainSub);
		
		// End drive toLineAndStop.
		setTimeout(.9);
    }
    
    @Override
    protected void initialize() {
    	// Using default speed, etc.
    	Robot.driveTrainSub.drive(1.0, 0.0, 0.0);
    }
    
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return isTimedOut();
	}
	
    // Called once after isFinished returns true
    protected void end() {
		Robot.driveTrainSub.stop(); // Call the failsafe Stop() function
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
		end(); // Call the failsafe Stop() function
    }

}
