package org.usfirst.frc.team5518.robot.commands.autonomous;

import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.RobotMap;
import org.usfirst.frc.team5518.robot.commands.TimedIntakeCom;
import org.usfirst.frc.team5518.robot.commands.WaitCom;
import org.usfirst.frc.team5518.robot.Robot.FieldTarget;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DoLeftAuto extends CommandGroup {

	private char switchPos;
	private char scalePos;
	
	public DoLeftAuto(Robot.FieldTarget function, String gameData, String cubes) {

		// Check if DS is in autonomous mode, print destination and gamedata to console
		if ( Robot.ds.isAutonomous() ) {
			Robot.logger.debug("LeftAuto " + function.toString() + " : using gamedata " + Robot.gameData);
		}
		
		switchPos = gameData.charAt(0);
		scalePos = gameData.charAt(1);
		
		if (function == FieldTarget.kDoNothing) { // If DO NOTHING is chosen in dashboard
			Robot.logger.debug("Do nothing.");
			autoNothing();
		}

		if (function == FieldTarget.kLine) { // If LINE is chosen in dashboard
			Robot.logger.debug("Drive forward to line from left pos");
			leftToLine();
		}

		if (function == FieldTarget.kSwitch){ // If SWITCH is chosen in dashboard

			if(switchPos == 'R') { // If the switch is on the right (opposite from us)
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
			if (scalePos == 'R') { // if the scale is on the right (opposite from us)
				//Drive to right scale
				Robot.logger.debug("Drive from left pos to right scale");
				leftToRightScale();
			} else { // if the scale is on the left side (our side)
				//Drive to left scale
				Robot.logger.debug("Drive from left pos to left scale");
				leftToLeftScale(cubes);
			}
		}

		if (function == FieldTarget.kChoose) { // If CHOOSE BEST is chosen in dashboard
			// only pick 'choose' path if you are sure that the middle bot can take the switch
			// this is because the 'choose' path will prioritize scale over switch
			if (scalePos == 'L') {
				leftToLeftScale(cubes);
			}
			else if (switchPos == 'L') {
				leftToLeftSwitch();
			}
			else if (scalePos == 'R') {
				leftToRightScale();
			}
			else if (switchPos == 'R') {
				leftToRightSwitch();
			}
			else {
				// this should never happen
				autoNothing();
				Robot.logger.debug("Exception thrown in LeftAuto CHOOSE path ... something broke");
			}
		}

	}

	private void autoNothing() {
		
	}

	private void leftToLine() { // done
//		addSequential(new DriveDistance(RobotMap.LINE_DISTANCE + RobotMap.SIDE_EXTEND, RobotMap.AUTO_DRIVE_SPEED));
		addSequential(new WaitCom(4, true));
	}

	private void leftToRightSwitch() { // done
//		addSequential(new DriveDistance(220, RobotMap.AUTO_DRIVE_SPEED)); // First leg gets us past the left side of switch
//		addSequential(new RotateDistance(90, RobotMap.AUTO_ROTATE_SPEED));
//		addSequential(new DriveDistance(224, RobotMap.AUTO_DRIVE_SPEED)); // Second leg drives across field to right side
//		addSequential(new RotateDistance(90, RobotMap.AUTO_ROTATE_SPEED));
//		addSequential(new DriveDistance(68, RobotMap.AUTO_DRIVE_SPEED)); // Third leg gets us even with switch
//		addSequential(new RotateDistance(90, RobotMap.AUTO_ROTATE_SPEED));
//		addSequential(new DriveDistance(30, RobotMap.AUTO_DRIVE_SPEED)); // Fourth leg drives up to the fence of the switch
//		addSequential(new WaitCom(RobotMap.WAIT_TIME, false)); // wait and don't move
//		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY)); // pass in delay for respective target
		
		leftToLine();
	}

	private void leftToLeftSwitch() { // done

		// addSequential(new WaitCom(3.2, true));
		addSequential(new DriveDistance(173-RobotMap.ROBOT_LENGTH, RobotMap.AUTO_DRIVE_SPEED));
		addSequential(new RotateDistance(90, RobotMap.AUTO_ROTATE_SPEED)); //clockwise
		addSequential(new WaitCom(1, true)); // wait with move
		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY)); // pass in delay for respective target

	}

	/* 
	 * The boxes lined up behind the switch would impede its path, which is why we changed the path.
	 */
	private void leftToRightSwitchBehind() {
		/*
	    	addSequential(new DriveDistance(8, 0.1f));
	    	addSequential(new RotateDistance(-90, 0.2f)); //counterclockwise		
	    	addSequential(new DriveDistance(8, 0.2f));
	    	addSequential(new RotateDistance(-180, 0.2f)); //	COMPLETE 180	
	    	addSequential(new StrafeDistance(8, 0.3f)); //Strafes into position rather than turning.
	    	addSequential(new DriveDistance(8, RobotMap.DRIVE_SPEED)); //For lining up
		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY)); // pass in delay for respective target
		 */
	}

	private void leftToRightScale() {
		addSequential(new DriveDistance(220, RobotMap.AUTO_DRIVE_SPEED)); // First leg gets us past the left side of switch
		addSequential(new RotateDistance(90, RobotMap.AUTO_ROTATE_SPEED));
		addSequential(new DriveDistance(224, RobotMap.AUTO_DRIVE_SPEED)); // Second leg drives across field to right side
		addSequential(new RotateDistance(-90, RobotMap.AUTO_ROTATE_SPEED));
		addSequential(new DriveDistance(68, RobotMap.AUTO_DRIVE_SPEED)); // Third leg gets us even with scale
		addSequential(new RotateDistance(-65, RobotMap.AUTO_ROTATE_SPEED));
		addSequential(new WaitCom(RobotMap.WAIT_TIME, false));
		addSequential(new AutoLauncherCom(RobotMap.SCALE_DELAY)); // pass in delay for respective target
	}

	private void leftToLeftScale(String numCubes) {
		addSequential(new DriveDistance(300, RobotMap.AUTO_DRIVE_SPEED));
		addSequential(new RotateDistance(90, RobotMap.AUTO_ROTATE_SPEED));
		addSequential(new DriveToPointUltrasonicCom(30, RobotMap.AUTO_DRIVE_SPEED));
		addSequential(new WaitCom(RobotMap.WAIT_TIME, false));
		addSequential(new AutoLauncherCom(RobotMap.SCALE_DELAY)); // pass in delay for respective target
		// Initiate two cube auto after this point
		if (numCubes.equals("two")) {
			addSequential(new RotateDistance(115, RobotMap.AUTO_ROTATE_SPEED));
			addSequential(new DriveDistance(50, RobotMap.AUTO_DRIVE_SPEED));
			addSequential(new StrafeDistance(41, -RobotMap.AUTO_STRAFE_SPEED));
			addSequential(new TimedIntakeCom(10));
			addSequential(new DriveDistance(42, RobotMap.AUTO_DRIVE_SPEED));
			addSequential(new DriveDistance(42, -RobotMap.AUTO_DRIVE_SPEED));
			addSequential(new StrafeDistance(41, RobotMap.AUTO_STRAFE_SPEED));
			addSequential(new DriveDistance(50, -RobotMap.AUTO_DRIVE_SPEED));
			addSequential(new RotateDistance(-115, RobotMap.AUTO_ROTATE_SPEED));
//			addSequential(new AutoLauncherCom(RobotMap.SCALE_DELAY));
		}
	}

}
