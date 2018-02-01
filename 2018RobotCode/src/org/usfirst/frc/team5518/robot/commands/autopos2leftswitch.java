package org.usfirst.frc.team5518.robot.commands;

import org.usfirst.frc.team5518.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class autopos2leftswitch extends Command {
	
    public autopos2leftswitch() {
    	super(1);
    	
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
		requires(Robot.driveTrainSub);
		
		// End drive toLineAndStop.
		setTimeout(3.0);
		
    }
    
    @Override
    protected void initialize() {
    	// Using default speed, etc.
    	//Robot.driveTrainSub.drive(0.3, 0.0, 0.0);
    }
    
    //1. Move forward.
    //2. Strafe Left.
    //3. Switch launch.
    @Override
    protected void execute() {
    	// Using default speed, etc.
    	Robot.driveTrainSub.drive(0.0, 0.3, 0.0);
    	//We are going to LEFT STRAFE
    	System.out.println("Left Strafe, then shoot.");
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
