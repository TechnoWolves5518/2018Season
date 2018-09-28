package org.usfirst.frc.team5518.robot.commands.autonomous;

import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.Robot.FieldTarget;
import org.usfirst.frc.team5518.robot.RobotMap;
import org.usfirst.frc.team5518.robot.commands.TimedIntakeCom;
import org.usfirst.frc.team5518.robot.commands.WaitCom;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DoMiddleAuto extends CommandGroup {
	
	public String gameDataPub;
	private char switchPos;
	private char scalePos;
	
	public DoMiddleAuto(Robot.FieldTarget function, String gameData, String cubes) {

		gameDataPub = gameData;
		
		// Check if DS is in autonomous mode, print destination and gamedata to console
		if ( Robot.ds.isAutonomous() ) {
			Robot.logger.verbose("MiddleAuto " + function.toString() + " : using gamedata " + Robot.gameData);
		}

		switchPos = gameData.charAt(0);
		scalePos = gameData.charAt(1);
		
		if (function == FieldTarget.kDoNothing) { // If DO NOTHING is chosen in dashboard
			Robot.logger.verbose("Do nothing.");
			autoNothing(cubes);
		}

		if (function == FieldTarget.kLine) { // If LINE is chosen in dashboard
			Robot.logger.verbose("Drive forward to line from middle pos");
			middleToLine();
		}

		if (function == FieldTarget.kSwitch){ // If SWITCH is chosen in dashboard

			if(switchPos == 'R') { // If the switch is on the right
				Robot.logger.verbose("Drive from middle pos to right switch");
				middleToRightSwitch();
			} else { // If the switch is on the left
				Robot.logger.verbose("Drive from middle pos to left switch");
				middleToLeftSwitch();
			}
		}

		if (function == FieldTarget.kScale) { // If SCALE is chosen in dashboard
			if(scalePos == 'R') { // If the scale is on the right
				Robot.logger.debug("Drive from middle pos to right scale");
				middleToRightScale();
			} else { // If the scale is on the left
				Robot.logger.debug("Drive from middle pos to left scale");
				middleToLeftScale();
			}			
		}

		if (function == FieldTarget.kChoose) { // If CHOOSE BEST is chosen in dashboard
			// Middle path will always go for the switch, so this choose logic will act as if switch was chosen.
			if(switchPos == 'R') { // If the switch is on the right
				Robot.logger.debug("Drive from middle pos to right switch");
				middleToRightSwitch();
			} else { // If the switch is on the left
				Robot.logger.debug("Drive from middle pos to left switch");
				middleToLeftSwitch();
			}
		}

	}

	private void autoNothing(String numCubes) {
		
	}

	private void middleToLine() {
//		addSequential(new DriveDistance(RobotMap.LINE_DISTANCE, RobotMap.AUTO_DRIVE_SPEED));
		// Time based movement
		addSequential(new WaitCom(4, true));
	}

	private void middleToRightSwitch() {
		// district auto
//		addSequential(new DriveDistance(70, RobotMap.AUTO_DRIVE_SPEED));
//		addSequential(new StrafeDistance(60, RobotMap.AUTO_STRAFE_SPEED));
//		addSequential(new DriveDistance(75, RobotMap.AUTO_DRIVE_SPEED + 0.1f));
//		addSequential(new WaitCom(RobotMap.WAIT_TIME));
//		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY));
		
		// states auto
//		addSequential(new DriveDistance(100, RobotMap.AUTO_DRIVE_SPEED)); // drives forward 120 based on ultrasonic
//		addSequential(new WaitCom(6, true)); // drives forward for 2 seconds just to be sure
//		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY));
		
		// THOR auto
		addSequential(new WaitCom(6, true)); // drives forward for 6 seconds; no sensor usage
		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY));
		
	}

	private void middleToLeftSwitch() {
		
		// district auto
//		addSequential(new DriveDistance(50, RobotMap.AUTO_DRIVE_SPEED));
//		addSequential(new StrafeDistance(90, -RobotMap.AUTO_STRAFE_SPEED));
//		addSequential(new DriveDistance(75, RobotMap.AUTO_DRIVE_SPEED + 0.1f));
//		addSequential(new WaitCom(RobotMap.WAIT_TIME));
//		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY));
		
		// states auto
//		addSequential(new DriveDistance(50, RobotMap.AUTO_DRIVE_SPEED));
//		addSequential(new StrafeDistance(160, -RobotMap.AUTO_STRAFE_SPEED));
//		addSequential(new DriveDistance(100, RobotMap.AUTO_DRIVE_SPEED));
//		addSequential(new WaitCom(1, true)); // drives forward for 1 seconds
//		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY));
		
		// THOR auto
		addSequential(new DriveDistance(50, RobotMap.AUTO_DRIVE_SPEED));
		addSequential(new RotateDistance(-90, RobotMap.AUTO_ROTATE_SPEED));
		addSequential(new DriveDistance(160, RobotMap.AUTO_DRIVE_SPEED));
		addSequential(new RotateDistance(90, RobotMap.AUTO_ROTATE_SPEED));
		addSequential(new WaitCom(4, true)); // drives forward for 1 second
		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY));
	}

	private void middleToRightScale() {
		
	}

	private void middleToLeftScale() {
		
	}
}
