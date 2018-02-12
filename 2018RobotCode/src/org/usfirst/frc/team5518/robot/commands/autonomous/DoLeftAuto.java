package org.usfirst.frc.team5518.robot.commands.autonomous;

import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.Robot.FieldTarget;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DoLeftAuto extends CommandGroup {

    public DoLeftAuto(Robot.FieldTarget function, String gameData) {
		
    		// Check if DS is in autonomous mode, print destination and gamedata to console
		if ( Robot.ds.isAutonomous() ) {
			Robot.logger.debug("LeftAuto " + function.toString() + " : using gamedata " + Robot.gameData);
		}
		
		if (function == FieldTarget.kDoNothing) { // If DO NOTHING is chosen in dashboard
			Robot.logger.debug("Do nothing.");
			autoNothing();
		}
		
		if (function == FieldTarget.kLine) { // If LINE is chosen in dashboard
			Robot.logger.debug("Drive forward to line from left pos");
		}
		
		if (function == FieldTarget.kSwitch){ // If SWITCH is chosen in dashboard
			
			if(gameData.charAt(0) == 'R') { // If the switch is on the right (opposite from us)
				Robot.logger.debug("Drive from left pos to right switch");
				leftToRightSwitch();
				// leftToRightSwitchBehind();
			} else { // If the switch is on the left (our side)
				//Drive forward, then pivot right to switch.
				Robot.logger.debug("Drive from left pos to left switch");
				leftToLeftSwitch();
			}			
		}
		
		if (function == FieldTarget.kScale) { // If SCALE is chosen in dashboard
			if (gameData.charAt(1) == 'R') { // if the scale is on the right (opposite from us)
				//Drive to right scale
				Robot.logger.debug("Drive from left pos to right scale");
				leftToRightScale();
			} else { // if the scale is on the left side (our side)
				//Drive to left scale
				Robot.logger.debug("Drive from left pos to left scale");
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
    }
    
    private void leftToRightScale() {
    		
    }
    
    private void leftToLeftScale() {
    		
    }
    
}
