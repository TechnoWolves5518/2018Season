package org.usfirst.frc.team5518.robot.commands.autonomous;

import org.usfirst.frc.team5518.robot.Logger;
import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.Robot.FieldTarget;
import org.usfirst.frc.team5518.robot.RobotMap;
import org.usfirst.frc.team5518.robot.commands.PneuLauncherCom;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DoMiddleAuto extends CommandGroup {
	
	private Logger logger;

	public DoMiddleAuto(FieldTarget function, String gameData) {
		// get logger instance
    	logger = Logger.getInstance();

		// Check if DS is in autonomous mode, print destination and gamedata to console
		if ( Robot.ds.isAutonomous() ) {
			logger.debug("MiddleAuto " + function.toString() + " : using gamedata " + gameData);
		}

		if (function == FieldTarget.kDoNothing) { // If DO NOTHING is chosen in dashboard
			logger.debug("Do nothing.");
			autoNothing();
		}

		if (function == FieldTarget.kLine) { // If LINE is chosen in dashboard
			logger.debug("Drive forward to line from middle pos");
			middleToLine();
		}

		if (function == FieldTarget.kSwitch){ // If SWITCH is chosen in dashboard

			if(gameData.charAt(0) == 'R') { // If the switch is on the right
				logger.debug("Drive from middle pos to right switch");
				middleToRightSwitch();
				// leftToRightSwitchBehind();
			} else { // If the switch is on the left
				//Drive forward, then pivot right to switch.
				logger.debug("Drive from middle pos to left switch");
				middleToLeftSwitch();
			}			
		}

		if (function == FieldTarget.kScale) { // If SCALE is chosen in dashboard
			logger.debug("Scale chosen from middle, not doing anything");
			autoNothing();
		}

		if (function == FieldTarget.kChoose) { // If CHOOSE BEST is chosen in dashboard
			// add logic to choose the best place to go from the left side
			// once the best path is determined, call that method
		}

	}

	private void autoNothing() {

	}

	private void middleToLine() {

	}

	private void middleToRightSwitch() {
		// Add to the command group
		// Drive (inches, speed)
		addSequential(new Rotate(90, 0.2f));
		addSequential(new DriveDistance(10, 0.2f));
//		addSequential(new StrafeDistance(6, -0.16f));
//		addSequential(new DriveDistance(6, -0.16f));
//		addSequential(new StrafeDistance(6, 0.16f));
		
		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY)); // pass in delay for respective target

	}

	private void middleToLeftSwitch() {
		
	}

}
