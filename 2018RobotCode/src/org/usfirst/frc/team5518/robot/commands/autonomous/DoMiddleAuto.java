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
	
	public DoMiddleAuto(Robot.FieldTarget function, String gameData, String cubes) {

		gameDataPub = gameData;
		
		// Check if DS is in autonomous mode, print destination and gamedata to console
		if ( Robot.ds.isAutonomous() ) {
			Robot.logger.verbose("MiddleAuto " + function.toString() + " : using gamedata " + Robot.gameData);
		}

		if (function == FieldTarget.kDoNothing) { // If DO NOTHING is chosen in dashboard
			Robot.logger.verbose("Do nothing.");
			autoNothing(cubes);
		}

		if (function == FieldTarget.kLine) { // If LINE is chosen in dashboard
			Robot.logger.verbose("Drive forward to line from middle pos");
			System.out.println("Drive to line from middle");
			middleToLine();
		}

		if (function == FieldTarget.kSwitch){ // If SWITCH is chosen in dashboard

			if(gameData.charAt(0) == 'R') { // If the switch is on the right
				Robot.logger.verbose("Drive from middle pos to right switch");
				middleToRightSwitch();
				// leftToRightSwitchBehind();
			} else { // If the switch is on the left
				//Drive forward, then pivot right to switch.
				Robot.logger.verbose("Drive from middle pos to left switch");
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
			// Middle path will always go for the switch, so this choose logic will act as if switch was chosen.
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

	}

	private void autoNothing(String numCubes) {
		// test group for a two cube autonomous (not for field use)
//		if (numCubes.equals("two")) {
//			addSequential(new DriveDistance(36, RobotMap.AUTO_DRIVE_SPEED));
//			addSequential(new RotateDistance(-90, 0.3f));
//			addSequential(new DriveDistance(12, RobotMap.AUTO_DRIVE_SPEED));
//			addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY));
//			addSequential(new DriveDistance(12, -RobotMap.AUTO_DRIVE_SPEED));
//			addSequential(new RotateDistance(180, 0.3f));
//			addParallel(new TimedIntakeCom(10));
//			addSequential(new DriveDistance(24, RobotMap.AUTO_DRIVE_SPEED));
//			addSequential(new DriveDistance(24, -RobotMap.AUTO_DRIVE_SPEED));
//			addSequential(new RotateDistance(180, 0.3f));
//			addSequential(new DriveDistance(12, RobotMap.AUTO_DRIVE_SPEED));
//			addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY));
//		}
	}

	private void middleToLine() {
		addSequential(new DriveDistance(RobotMap.LINE_DISTANCE, RobotMap.AUTO_DRIVE_SPEED));
		if (gameDataPub.charAt(0) == 'L') {
			addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY));
		}
	}

	private void middleToRightSwitch() {
		// district auto
//		addSequential(new DriveDistance(70, RobotMap.AUTO_DRIVE_SPEED));
//		addSequential(new StrafeDistance(60, RobotMap.AUTO_STRAFE_SPEED));
//		addSequential(new DriveDistance(75, RobotMap.AUTO_DRIVE_SPEED + 0.1f));
//		addSequential(new WaitCom(RobotMap.WAIT_TIME));
//		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY));
		
		// states auto
		addSequential(new DriveDistance(100, RobotMap.AUTO_DRIVE_SPEED)); // drives forward 120 based on ultrasonic
		addSequential(new WaitCom(2, true)); // drives forward for 2 seconds just to be sure
		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY));
		
	}

	private void middleToLeftSwitch() {
//		addSequential(new DriveDistance(50, RobotMap.AUTO_DRIVE_SPEED));
//		addSequential(new RotateDistance(-90, 0.3f));
//		addSequential(new DriveDistance(85, RobotMap.AUTO_DRIVE_SPEED));
//		addSequential(new RotateDistance(90, 0.3f));
//		addSequential(new DriveDistance(100, RobotMap.AUTO_DRIVE_SPEED));
//		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY));
		
		// district auto
//		addSequential(new DriveDistance(50, RobotMap.AUTO_DRIVE_SPEED));
//		addSequential(new StrafeDistance(90, -RobotMap.AUTO_STRAFE_SPEED));
//		addSequential(new DriveDistance(75, RobotMap.AUTO_DRIVE_SPEED + 0.1f));
//		addSequential(new WaitCom(RobotMap.WAIT_TIME));
//		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY));
		
		// states auto
		addSequential(new DriveDistance(50, RobotMap.AUTO_DRIVE_SPEED));
		addSequential(new StrafeDistance(160, -RobotMap.AUTO_STRAFE_SPEED));
		addSequential(new DriveDistance(50, RobotMap.AUTO_DRIVE_SPEED));
		addSequential(new WaitCom(1, true)); // drives forward for 1 seconds
		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY));
		
		// for practice field
//		addSequential(new DriveDistance(20, RobotMap.AUTO_DRIVE_SPEED, false));
//		addSequential(new StrafeDistance(35, -0.4f));
//		addSequential(new DriveDistance(20, RobotMap.AUTO_DRIVE_SPEED, true));
//		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY));
		
//		addSequential(new DriveDistance(100, RobotMap.AUTO_DRIVE_SPEED, false));
//		addSequential(new AutoLauncherCom(RobotMap.SWITCH_DELAY));LKJ;
	}

	private void middleToRightScale() {
		/*
		addSequential(new DriveDistance(10, 0.3f));
		addSequential(new StrafeDistance(3, 0.2f));
		addSequential(new DriveDistance(15, 0.1f));
		addSequential(new RotateDistance(-90, 0.2f)); //counterclockwise
D

		`addSequential(new AutoLauncherCom(RobotMap.SCALE_DELAY)); // pass in delay for respective target
		*/
	}

	private void middleToLeftScale() {
		/*
		addSequential(new DriveDistance(10, 0.3f));
		addSequential(new StrafeDistance(10, -0.2f)); //hope we go left bois
		addSequential(new DriveDistance(15, 0.3f));
		addSequential(new RotateDistance(90, 0.2f)); //clockwise
		*/
	}
}
