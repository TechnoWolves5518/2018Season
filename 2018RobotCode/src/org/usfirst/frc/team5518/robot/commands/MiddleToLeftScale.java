package org.usfirst.frc.team5518.robot.commands;

import org.usfirst.frc.team5518.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team5518.robot.Logger;

/**
 *
 */
public class MiddleToLeftScale extends Command {

    public MiddleToLeftScale() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
		requires(Robot.driveTrainSub);
    }

    @Override
    protected void initialize() {
    	Robot.logger.info("MiddleToLeftScale init");
    	// Using default speed, etc.
    	//Robot.driveTrainSub.drive(0.3, 0.0, 0.0);
    }
    
    //1. Move forward.
    //2. Strafe Left.
    //3. Switch launch.
    
    double forwardTime = 2.0; //Time we want to pass before going left.
    double leftTime = 3.0;
    double stopDrive = 7.0; //Time we want to pass before going shoot.
    // speed = 0.3; //Time we want to pass before going left.
    
    protected void execute() {
    	Robot.logger.debug("MiddleToLeftScale execute");
    	double t = this.timeSinceInitialized();
    	Robot.logger.debug("Time since initialized = " + t);
    	if (t < forwardTime)
    	{
    		Robot.logger.debug("We are driving forward");
    		Robot.driveTrainSub.drive(0.0, 0.3, 0.0);
    	} 
    	else if (forwardTime <= t && t < leftTime) { 
    		Robot.logger.info("We are driving left");
    		Robot.driveTrainSub.drive(-0.3, 0.0, 0.0);  		
    	} 
    	else if (leftTime <= t && t < stopDrive) {
    		Robot.logger.debug("We are driving forward again");
    		Robot.driveTrainSub.drive(0.0, 0.3, 0.0);
    	} 
    	else {
    		Robot.logger.debug("We are no longer driving");
    		Robot.driveTrainSub.drive(0.0, 0.0, 0.0); 
    	}
    	
    	Robot.logger.debug("Finished driving -- shoot at middle left scale");
    	// Using default speed, etc.
    	
    	//We are going to LEFT STRAFE
    	Robot.logger.debug("Left Strafe, then shoot.");
    	
    }
    
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false; //isTimedOut();
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
