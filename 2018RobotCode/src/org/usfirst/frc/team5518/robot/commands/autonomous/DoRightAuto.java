package org.usfirst.frc.team5518.robot.commands.autonomous;

import org.usfirst.frc.team5518.robot.Logger;
import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.Robot.FieldTarget;
import org.usfirst.frc.team5518.robot.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DoRightAuto extends CommandGroup {
	
	private Logger logger;

    public DoRightAuto(FieldTarget function, String gameData) {
    	// get logger instance
    	logger = Logger.getInstance();
        
    		// Check if DS is in autonomous mode, print destination and gamedata to console
		if ( Robot.ds.isAutonomous() ) {
			logger.debug("RightAuto " + function.toString() + " : using gamedata " + gameData);
		}
		
		if (function == FieldTarget.kDoNothing) { // If DO NOTHING is chosen in dashboard
			logger.debug("Do nothing.");
			autoNothing();
		}
		
		if (function == FieldTarget.kLine) { // If LINE is chosen in dashboard
			logger.debug("Drive forward to line from right pos");
		}
		
		if (function == FieldTarget.kSwitch){ // If SWITCH is chosen in dashboard
			
			if(gameData.charAt(0) == 'R') { // If the switch is on the right (our side)
				logger.debug("Drive from right pos to right switch");
				rightToRightSwitch();
				// leftToRightSwitchBehind();
			} else { // If the switch is on the left (opposite from us)
				//Drive forward, then pivot right to switch.
				logger.debug("Drive from right pos to left switch");
				rightToLeftSwitch();
				// rightToLeftSwitchBehind();
			}			
		}
		
		if (function == FieldTarget.kScale) { // If SCALE is chosen in dashboard
			if (gameData.charAt(1) == 'R') { // if the scale is on the right (our side)
				//Drive to right scale
				logger.debug("Drive from right pos to right scale");
				rightToRightScale();
			} else { // if the scale is on the left side (opposite from us)
				//Drive to left scale
				logger.debug("Drive from right pos to left scale");
				rightToLeftScale();
			}
		}
		
		if (function == FieldTarget.kChoose) { // If CHOOSE BEST is chosen in dashboard
			// add logic to choose the best place to go from the left side
			// once the best path is determined, call that method
		}
    		
    }
    
    private void autoNothing() {
    		
    }
    
    private void rightToRightSwitch() {
    	addSequential(new Rotate(90, 0.2f));
    }
    
    private void rightToLeftSwitch() {
    	addSequential(new DriveDistance(36, 0.2f));
    	Robot.driveTrainSub.resetGyro();
    	addSequential(new Rotate(-90, 0.2f)); //counterclockwise		
    	addSequential(new DriveDistance(36, 0.2f));
    	Robot.driveTrainSub.resetGyro();
    	addSequential(new Rotate(90, 0.2f)); //clockwise
    	addSequential(new DriveDistance(36, 0.2f));
		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY)); // pass in delay for respective target
    }
    
    private void rightToLeftSwitchBehind() {
    		
    }
    
    private void rightToRightScale() {
    		
    }
    
    private void rightToLeftScale() {
    		
    }
    
}
