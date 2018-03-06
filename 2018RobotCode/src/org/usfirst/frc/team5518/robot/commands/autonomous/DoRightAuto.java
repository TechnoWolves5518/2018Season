package org.usfirst.frc.team5518.robot.commands.autonomous;

import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.Robot.FieldTarget;
import org.usfirst.frc.team5518.robot.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DoRightAuto extends CommandGroup {
	
	private char switchPos;
	private char scalePos;
	
	public DoRightAuto(Robot.FieldTarget function, String gameData) {

		// Check if DS is in autonomous mode, print destination and gamedata to console
		if ( Robot.ds.isAutonomous() ) {
			Robot.logger.debug("RightAuto " + function.toString() + " : using gamedata " + gameData);
		}
		
		switchPos = gameData.charAt(0);
		scalePos = gameData.charAt(1);
		
		if (function == FieldTarget.kDoNothing) { // If DO NOTHING is chosen in dashboard
			Robot.logger.debug("Do nothing.");
			autoNothing();
		}

		if (function == FieldTarget.kLine) { // If LINE is chosen in dashboard
			Robot.logger.debug("Drive forward to line from right pos");
		}

		if (function == FieldTarget.kSwitch){ // If SWITCH is chosen in dashboard

			if(switchPos == 'R') { // If the switch is on the right (our side)
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
			if (scalePos == 'R') { // if the scale is on the right (our side)
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
			// only pick 'choose' path if you are sure that the middle bot can take the switch
			// this is because the 'choose' path will prioritize scale over switch
			if (scalePos == 'R') {
				rightToRightScale();
			}
			else if (switchPos == 'R') {
				rightToRightSwitch();
			}
			else if (scalePos == 'L') {
				rightToLeftScale();
			}
			else if (switchPos == 'L') {
				rightToLeftSwitch();
			}
			else {
				// this should never happen
				autoNothing();
				Robot.logger.debug("Exception thrown in RightAuto CHOOSE path ... something broke");
			}
		}

	}

	private void autoNothing() {
		//hitaha	
	}


	private void rightToLine() { // done
		addSequential(new DriveDistance(100, 0.3f));
	}

	private void rightToRightSwitch() { // done
		addSequential(new DriveDistance(152, 0.3f));
		addSequential(new RotateDistance(-90, 0.2f)); //clockwise		
		addSequential(new DriveDistance(30, 0.2f)); // drive up to fence
		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY)); // pass in delay for respective target
	}

	private void rightToLeftSwitch() {
		addSequential(new DriveDistance(220, 0.4f)); // First leg gets us past the left side of switch
		addSequential(new RotateDistance(-90, 0.3f));
		addSequential(new DriveDistance(224, 0.4f)); // Second leg drives across field to right side
		addSequential(new RotateDistance(-90, 0.3f));
		addSequential(new DriveDistance(68, 0.4f)); // Third leg gets us even with switch
		addSequential(new RotateDistance(-90, 0.3f));
		addSequential(new DriveDistance(30, 0.4f)); // Fourth leg drives up to the fence of the switch
		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY)); // pass in delay for respective target
	}

	/* 
	 * The boxes lined up behind the switch would impede its path, which is why we changed the path.
	 */
	private void rightToLeftSwitchBehind() {
		/*
	    	addSequential(new DriveDistance(8, 0.1f));
	    	addSequential(new RotateDistance(-90, 0.2f)); //counterclockwise		
	    	addSequential(new DriveDistance(8, 0.2f));
	    	addSequential(new RotateDistance(-180, 0.2f)); //	COMPLETE 180	
	    	addSequential(new StrafeDistance(8, 0.3f)); //Strafes into position rather than turning.
	    	addSequential(new DriveDistance(8, 0.4f)); //For lining up
		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY)); // pass in delay for respective target
		 */
	}

	private void rightToRightScale() {
		addSequential(new DriveDistance(288, 0.4f));
		addSequential(new RotateDistance(-65, 0.3f));
		addSequential(new AutoLauncherCom(RobotMap.SCALE_DELAY)); // pass in delay for respective target
	}

	private void rightToLeftScale() {
		addSequential(new DriveDistance(220, 0.4f)); // First leg gets us past the left side of switch
		addSequential(new RotateDistance(-90, 0.3f));
		addSequential(new DriveDistance(224, 0.4f)); // Second leg drives across field to right side
		addSequential(new RotateDistance(90, 0.3f));
		addSequential(new DriveDistance(68, 0.4f)); // Third leg gets us even with scale
		addSequential(new RotateDistance(65, 0.3f));
		addSequential(new AutoLauncherCom(RobotMap.SCALE_DELAY)); // pass in delay for respective target
	}

}
