package org.usfirst.frc.team5518.robot.commands.autonomous;

import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.RobotMap;
import org.usfirst.frc.team5518.robot.Robot.FieldTarget;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DoRightAuto extends CommandGroup {

    public DoRightAuto(Robot.FieldTarget function, String gameData) {
        
    		// Check if DS is in autonomous mode, print destination and gamedata to console
		if ( Robot.ds.isAutonomous() ) {
			Robot.logger.debug("RightAuto " + function.toString() + " : using gamedata " + gameData);
		}
		
		if (function == FieldTarget.kDoNothing) { // If DO NOTHING is chosen in dashboard
			Robot.logger.debug("Do nothing.");
			autoNothing();
		}
		
		if (function == FieldTarget.kLine) { // If LINE is chosen in dashboard
			Robot.logger.debug("Drive forward to line from right pos");
			rightToLine();
		}
		
		if (function == FieldTarget.kSwitch){ // If SWITCH is chosen in dashboard
			
			if(gameData.charAt(0) == 'R') { // If the switch is on the right (our side)
				Robot.logger.debug("Drive from right pos to right switch");
				rightToRightSwitch();
				// leftToRightSwitchBehind();
			} else { // If the switch is on the left (opposite from us)
				//Drive forward, then pivot right to switch.
				Robot.logger.debug("Drive from right pos to left switch");
				rightToLeftSwitch();
				// rightToLeftSwitchBehind();
			}			
		}

		
		if (function == FieldTarget.kScale) { // If SCALE is chosen in dashboard
			if (gameData.charAt(1) == 'R') { // if the scale is on the right (our side)
				//Drive to right scale
				Robot.logger.debug("Drive from right pos to right scale");
				rightToRightScale();
			} else { // if the scale is on the left side (opposite from us)
				//Drive to left scale
				Robot.logger.debug("Drive from right pos to left scale");
				rightToLeftScale();
			}
		}
		
		if (function == FieldTarget.kChoose) { // If CHOOSE BEST is chosen in dashboard
			// add logic to choose the best place to go from the left side
			// once the best path is determined, call that method
		}
    		
    }
    
    private void autoNothing() {
    	//hitaha	
    }
    
    
    private void rightToLine() {
		addSequential(new DriveDistance(9, 0.3f));
	}
    
    private void rightToRightSwitch() {
    	addSequential(new DriveDistance(8, 0.1f));
    	//addSequential(new RotateDistance(-90, 0.2f)); //counterclockwise	
    	addSequential(new DriveDistance(8, 0.2f));
		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY)); // pass in delay for respective target


    }
    
    private void rightToLeftSwitch() {
    	addSequential(new DriveDistance(36, 0.2f));
    	addSequential(new RotateDistance(-90, 0.2f)); //counterclockwise		
    	addSequential(new DriveDistance(36, 0.2f));
    	addSequential(new RotateDistance(90, 0.2f)); //clockwise
    	addSequential(new DriveDistance(36, 0.2f));
		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY)); // pass in delay for respective target

    }
    
   /* 
    * The boxes lined up behind the switch would impede its path, which is why we changed the path.
    */
    private void rightToLeftSwitchBehind() {
    	addSequential(new DriveDistance(8, 0.1f));
    	//addSequential(new RotateDistance(-90, 0.2f)); //counterclockwise		
    	addSequential(new DriveDistance(8, 0.2f));
    	//addSequential(new RotateDistance(-180, 0.2f)); //	COMPLETE 180	
    	addSequential(new StrafeDistance(8, 0.3f)); //Strafes into position rather than turning.
    	addSequential(new DriveDistance(8, 0.4f)); //For lining up
		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY)); // pass in delay for respective target
		
		    }
    
    private void rightToRightScale() {
    	addSequential(new DriveDistance(8, 0.3f));
    	//addSequential(new RotateDistance(-90, 0.2f)); //counterclockwise	
    	addSequential(new DriveDistance(8, 0.3f)); //For lining up
		addSequential(new AutoLauncherCom(RobotMap.SCALE_DELAY)); // pass in delay for respective target
		
    }
    
    private void rightToLeftScale() {
    	addSequential(new DriveDistance(8, 0.1f));
    	//addSequential(new RotateDistance(-90, 0.2f)); //counterclockwise
    	addSequential(new DriveDistance(7, 0.2f));
    	//addSequential(new RotateDistance(90, 0.2f)); //clockwise
    	addSequential(new DriveDistance(7, 0.3f));
    	//addSequential(new RotateDistance(90, 0.2f)); //clockwise
    	addSequential(new DriveDistance(8, 0.4f)); //For lining up
		addSequential(new AutoLauncherCom(RobotMap.SCALE_DELAY)); // pass in delay for respective target
    }
    
}
