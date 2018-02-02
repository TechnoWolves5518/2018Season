package org.usfirst.frc.team5518.robot.commands;

import org.usfirst.frc.team5518.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class autopos2leftswitch extends Command {
	
    public autopos2leftswitch() {
    	//super(1);
    	
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
		requires(Robot.driveTrainSub);
		
		// End drive toLineAndStop.
		//setTimeout(4.0);
		
    }
    
    @Override
    protected void initialize() {
    	System.out.println("autopos2 init");
    	// Using default speed, etc.
    	//Robot.driveTrainSub.drive(0.3, 0.0, 0.0);
    }
    
    //1. Move forward.
    //2. Strafe Left.
    //3. Switch launch.
    
    double leftTime = 2.0; //Time we want to pass before going left.
    double stopDrive = 4.0; //Time we want to pass before going shoot.
    // speed = 0.3; //Time we want to pass before going left.
    
    protected void execute() {
    	System.out.println("autopos2 execute");
    	double t = this.timeSinceInitialized();
    	if (t < leftTime)
    	{
    		System.out.println("We are driving forward");
    		Robot.driveTrainSub.drive(0.0, 0.3, 0.0);
    	} else if (t <= stopDrive && t >= leftTime) { 
    		System.out.println("We are driving left");
    		Robot.driveTrainSub.drive(-0.3, 0.0, 0.0);  		
    	}else if (t >= stopDrive) {
    		System.out.println("We are no longer driving");
    		Robot.driveTrainSub.drive(0.0, 0.0, 0.0); 
    	}
    	
    	Robot.log("Finished driving -- shoot at middle left switch");
    	// Using default speed, etc.
    	
    	//We are going to LEFT STRAFE
    	System.out.println("Left Strafe, then shoot.");
    	
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
