package org.usfirst.frc.team5518.robot.commands.autonomous;

import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.Robot.FieldTarget;
import org.usfirst.frc.team5518.robot.RobotMap;
import org.usfirst.frc.team5518.robot.commands.PneuLauncherCom;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DoMiddleAuto extends CommandGroup {

	public DoMiddleAuto(Robot.FieldTarget function, String gameData) {

		// Check if DS is in autonomous mode, print destination and gamedata to console
		if ( Robot.ds.isAutonomous() ) {
			Robot.logger.debug("MiddleAuto " + function.toString() + " : using gamedata " + Robot.gameData);
		}

		if (function == FieldTarget.kDoNothing) { // If DO NOTHING is chosen in dashboard
			Robot.logger.debug("Do nothing.");
			autoNothing();
		}

		if (function == FieldTarget.kLine) { // If LINE is chosen in dashboard
			Robot.logger.debug("Drive forward to line from middle pos");
			middleToLine();
		}

		if (function == FieldTarget.kSwitch){ // If SWITCH is chosen in dashboard

			if(gameData.charAt(0) == 'R') { // If the switch is on the right
				Robot.logger.debug("Drive from middle pos to right switch");
				middleToRightSwitch();
				// leftToRightSwitchBehind();
			} else { // If the switch is on the left
				//Drive forward, then pivot right to switch.
				Robot.logger.debug("Drive from middle pos to left switch");
				middleToLeftSwitch();
			}			
		}

		if (function == FieldTarget.kScale) { // If SCALE is chosen in dashboard
			if(gameData.charAt(1) == 'R') { // If the scale is on the right
				Robot.logger.debug("Drive from middle pos to right scale");
				middleToRightScale();
			} else { // If the scale is on the left
				Robot.logger.debug("Drive from middle pos to left scale");
				middleToLeftScale();
			}			
		}

		if (function == FieldTarget.kChoose) { // If CHOOSE BEST is chosen in dashboard
			// add logic to choose the best place to go from the left side
			// once the best path is determined, call that method
		}

	}

	private void autoNothing() {

	}

	private void middleToLine() {
		addSequential(new DriveDistance(9, 0.3f));
	}

	private void middleToRightSwitch() {
		// Add to the command group
		// Drive (inches, speed)
		addSequential(new DriveDistance(12, 0.2f));
		addSequential(new DriveDistance(12, -0.2f));
		addSequential(new RotateDistance(360, 0.2f));
		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY)); // pass in delay for respective target

	}

	private void middleToLeftSwitch() {
		addSequential(new DriveDistance(10, 0.3f));
		addSequential(new StrafeDistance(7, 0.2f));	
		addSequential(new DriveDistance(10, 0.1f));
		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY)); // pass in delay for respective target
	}
	
    private void middleToRightScale() {
    	addSequential(new DriveDistance(10, 0.3f));
    	addSequential(new StrafeDistance(3, 0.2f));
    	addSequential(new DriveDistance(15, 0.1f));
    	//addSequential(new RotateDistance(-90, 0.2f)); //counterclockwise
		addSequential(new AutoLauncherCom(RobotMap.SCALE_DELAY)); // pass in delay for respective target

    }
    
    private void middleToLeftScale() {
    	addSequential(new DriveDistance(10, 0.3f));
    	addSequential(new StrafeDistance(-10, 0.2f)); //hope we go left bois
    	addSequential(new DriveDistance(15, 0.3f));
    	//addSequential(new RotateDistance(90, 0.2f)); //clockwise
    }
}
