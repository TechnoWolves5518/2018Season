package org.usfirst.frc.team5518.robot.commands.autonomous;

import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.Robot.FieldTarget;
import org.usfirst.frc.team5518.robot.commands.TimedIntakeCom;
import org.usfirst.frc.team5518.robot.commands.WaitCom;
import org.usfirst.frc.team5518.robot.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DoRightAuto extends CommandGroup {
	
	private char switchPos;
	private char scalePos;
	
	public DoRightAuto(Robot.FieldTarget function, String gameData, String cubes) {

		// Check if DS is in autonomous mode, print destination and gamedata to console
		if ( Robot.ds.isAutonomous() ) {
			Robot.logger.verbose("RightAuto " + function.toString() + " : using gamedata " + gameData);
		}
		
		switchPos = gameData.charAt(0);
		scalePos = gameData.charAt(1);
		
		if (function == FieldTarget.kDoNothing) { // If DO NOTHING is chosen in dashboard
			Robot.logger.verbose("Do nothing.");
			autoNothing();
		}

		if (function == FieldTarget.kLine) { // If LINE is chosen in dashboard
			Robot.logger.verbose("Drive forward to line from right pos");
			rightToLine();
		}

		if (function == FieldTarget.kSwitch){ // If SWITCH is chosen in dashboard

			if(switchPos == 'R') { // If the switch is on the right (our side)
				Robot.logger.verbose("Drive from right pos to right switch");
				rightToRightSwitch();
			} else { // If the switch is on the left (opposite from us)
				Robot.logger.verbose("Drive from right pos to left switch");
				rightToLeftSwitch();
			}			
		}

		if (function == FieldTarget.kScale) { // If SCALE is chosen in dashboard
			if (scalePos == 'R') { // if the scale is on the right (our side)
				Robot.logger.verbose("Drive from right pos to right scale");
				rightToRightScale();
			} else { // if the scale is on the left side (opposite from us)
				Robot.logger.verbose("Drive from right pos to left scale");
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
				Robot.logger.verbose("Exception thrown in RightAuto CHOOSE path ... something broke");
			}
		}

	}

	private void autoNothing() {
		//hitaha	
	}


	private void rightToLine() { // done
//		addSequential(new DriveDistance(RobotMap.LINE_DISTANCE + RobotMap.SIDE_EXTEND, RobotMap.AUTO_DRIVE_SPEED));
		addSequential(new WaitCom(4, true));
	}

	private void rightToRightSwitch() { // done
		// addSequential(new WaitCom(3.2, true));
		addSequential(new DriveDistance(170-RobotMap.ROBOT_LENGTH, RobotMap.AUTO_DRIVE_SPEED));
		addSequential(new RotateDistance(-90, RobotMap.AUTO_ROTATE_SPEED)); // anticlockwise
		addSequential(new WaitCom(1, true));
		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY)); // pass in delay for respective target
	}

	private void rightToLeftSwitch() {
		rightToLine();
	}

	private void rightToRightScale() {
		addSequential(new WaitCom(6.5, true)); // 6.5 initial scale wait time (guess)
		addSequential(new RotateDistance(-90, RobotMap.AUTO_ROTATE_SPEED));
		addSequential(new DriveDistanceUltrasonic(30, RobotMap.AUTO_DRIVE_SPEED));
		addSequential(new WaitCom(RobotMap.WAIT_TIME, false));
		addSequential(new AutoLauncherCom(RobotMap.SCALE_DELAY)); // pass in delay for respective target
		
		rightToLine();
	}

	private void rightToLeftScale() {
//		addSequential(new DriveDistance(220, RobotMap.AUTO_DRIVE_SPEED)); // First leg gets us past the left side of switch
//		addSequential(new RotateDistance(-90, RobotMap.AUTO_ROTATE_SPEED));
//		addSequential(new DriveDistance(224, RobotMap.AUTO_DRIVE_SPEED)); // Second leg drives across field to right side
//		addSequential(new RotateDistance(90, RobotMap.AUTO_ROTATE_SPEED));
//		addSequential(new DriveDistance(68, RobotMap.AUTO_DRIVE_SPEED)); // Third leg gets us even with scale
//		addSequential(new RotateDistance(65, RobotMap.AUTO_ROTATE_SPEED));
//		addSequential(new WaitCom(RobotMap.WAIT_TIME, false));
//		addSequential(new AutoLauncherCom(RobotMap.SCALE_DELAY)); // pass in delay for respective target
		
		rightToLine();
	}

}
