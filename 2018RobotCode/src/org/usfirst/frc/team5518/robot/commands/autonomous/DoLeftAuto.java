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
			Robot.logger.verbose("LeftAuto " + function.toString() + " : using gamedata " + Robot.gameData);
		}
		
		switchPos = gameData.charAt(0);
		scalePos = gameData.charAt(1);
		
		if (function == FieldTarget.kDoNothing) { // If DO NOTHING is chosen in dashboard
			Robot.logger.verbose("Do nothing.");
			autoNothing();
		}

		if (function == FieldTarget.kLine) { // If LINE is chosen in dashboard
			Robot.logger.verbose("Drive forward to line from left pos");
			leftToLine();
		}

		if (function == FieldTarget.kSwitch){ // If SWITCH is chosen in dashboard

			if(switchPos == 'R') { // If the switch is on the right (opposite from us)
				Robot.logger.verbose("Drive from left pos to right switch");
				leftToRightSwitch();
			} else { // If the switch is on the left (our side)
				Robot.logger.verbose("Drive from left pos to left switch");
				leftToLeftSwitch();
			}			
		}

		if (function == FieldTarget.kScale) { // If SCALE is chosen in dashboard
			if (scalePos == 'R') { // if the scale is on the right (opposite from us)
				Robot.logger.verbose("Drive from left pos to right scale");
				leftToRightScale();
			} else { // if the scale is on the left side (our side)
				Robot.logger.verbose("Drive from left pos to left scale");
				leftToLeftScale();
			}
		}

		if (function == FieldTarget.kChoose) { // If CHOOSE BEST is chosen in dashboard
			// only pick 'choose' path if you are sure that the middle bot can take the switch
			// this is because the 'choose' path will prioritize scale over switch
			if (scalePos == 'L') {
				leftToLeftScale();
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
				Robot.logger.verbose("Exception thrown in LeftAuto CHOOSE path ... something broke");
			}
		}

	}

	private void autoNothing() {
		
	}

	private void leftToLine() {
//		addSequential(new DriveDistance(RobotMap.LINE_DISTANCE + RobotMap.SIDE_EXTEND, RobotMap.AUTO_DRIVE_SPEED));
		addSequential(new WaitCom(4, true));
	}

	private void leftToRightSwitch() {		
		leftToLine();
	}

	private void leftToLeftSwitch() {
		addSequential(new DriveDistance(173-RobotMap.ROBOT_LENGTH, RobotMap.AUTO_DRIVE_SPEED));
		addSequential(new RotateDistance(90, RobotMap.AUTO_ROTATE_SPEED)); //clockwise
		addSequential(new WaitCom(1, true)); // wait with move
		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY)); // pass in delay for respective target
	}

	private void leftToRightSwitchBehind() {
		
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

	private void leftToLeftScale() {
		addSequential(new DriveDistance(300, RobotMap.AUTO_DRIVE_SPEED));
		addSequential(new RotateDistance(90, RobotMap.AUTO_ROTATE_SPEED));
		addSequential(new DriveToPointUltrasonicCom(30, RobotMap.AUTO_DRIVE_SPEED));
		addSequential(new WaitCom(RobotMap.WAIT_TIME, false));
		addSequential(new AutoLauncherCom(RobotMap.SCALE_DELAY)); // pass in delay for respective target
	}

}
