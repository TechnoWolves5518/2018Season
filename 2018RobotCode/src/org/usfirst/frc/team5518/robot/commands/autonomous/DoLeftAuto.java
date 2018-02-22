package org.usfirst.frc.team5518.robot.commands.autonomous;

import org.usfirst.frc.team5518.robot.Logger;
import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.Robot.FieldTarget;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DoLeftAuto extends CommandGroup {
	
	private Logger logger;

    public DoLeftAuto(FieldTarget function, String gameData) {
    	// get logger instance
    	logger = Logger.getInstance();
		
    		// Check if DS is in autonomous mode, print destination and gamedata to console
		if ( Robot.ds.isAutonomous() ) {
			logger.debug("LeftAuto " + function.toString() + " : using gamedata " + gameData);
		}
		
		if (function == FieldTarget.kDoNothing) { // If DO NOTHING is chosen in dashboard
			logger.debug("Do nothing.");
			autoNothing();
		}
		
		if (function == FieldTarget.kLine) { // If LINE is chosen in dashboard
			logger.debug("Drive forward to line from left pos");
		}
		
		if (function == FieldTarget.kSwitch){ // If SWITCH is chosen in dashboard
			
			if(gameData.charAt(0) == 'R') { // If the switch is on the right (opposite from us)
				logger.debug("Drive from left pos to right switch");
				leftToRightSwitch();
				// leftToRightSwitchBehind();
			} else { // If the switch is on the left (our side)
				//Drive forward, then pivot right to switch.
				logger.debug("Drive from left pos to left switch");
				leftToLeftSwitch();
			}			
		}
		
		if (function == FieldTarget.kScale) { // If SCALE is chosen in dashboard
			if (gameData.charAt(1) == 'R') { // if the scale is on the right (opposite from us)
				//Drive to right scale
				logger.debug("Drive from left pos to right scale");
				leftToRightScale();
			} else { // if the scale is on the left side (our side)
				//Drive to left scale
				logger.debug("Drive from left pos to left scale");
				leftToLeftScale();
			}
		}
		
		if (function == FieldTarget.kChoose) { // If CHOOSE BEST is chosen in dashboard
			// add logic to choose the best place to go from the left side
			// once the best path is determined, call that method
		}
    		
    }
    
    private void autoNothing() {
    		
    }
    
    private void leftToRightSwitch() {
    		
    }
    
    private void leftToRightSwitchBehind() {
    		
    }
    
    private void leftToLeftSwitch() {
		// Drive forward 7 inches at 0.4 speed
		addSequential(new DriveDistance(7, 0.4f));
		addSequential(new StrafeDistance(7, 0.3f));
    }
    
    private void leftToRightScale() {
    		
    }
    
    private void leftToLeftScale() {
    		
    }
    
}
