package org.usfirst.frc.team5518.robot.commands;

import org.usfirst.frc.team5518.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team5518.robot.Logger;

public class ToLineAndStopCom extends Command {
	
    public ToLineAndStopCom() {
    	//super(1);
    	
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
		requires(Robot.driveTrainSub);
		
		// End drive toLineAndStop.
	    // speed = 0.3; //Time we want to pass before stopping.
	}
    
    @Override
    protected void initialize() {
    	Robot.logger.info("toLineAndStopCom init");
    	// Using default speed, etc.
    	//Robot.driveTrainSub.drive(0.3, 0.0, 0.0);
    }
	double forwardTime = 7.0; //Time we want to pass before stopping.

    @Override
    protected void execute() {
    	Robot.logger.debug("toLineAndStopCom execute");
    	double t = this.timeSinceInitialized();
    	Robot.logger.debug("Time since initialized = " + t);
    	if (t < forwardTime){
    		Robot.logger.debug("We are driving forward");
    		Robot.driveTrainSub.drive(0.0, 0.3, 0.0);
    	}
    	else{
    		Robot.logger.debug("We are no longer driving");
    		Robot.driveTrainSub.drive(0.0, 0.0, 0.0); 
    	}
    }
    
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
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
