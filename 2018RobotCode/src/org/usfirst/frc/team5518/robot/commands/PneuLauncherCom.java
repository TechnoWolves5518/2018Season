package org.usfirst.frc.team5518.robot.commands;

import org.usfirst.frc.team5518.robot.OI;
import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.RobotMap;
import org.usfirst.frc.team5518.robot.commands.autonomous.AutoLauncherCom;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */

public class PneuLauncherCom extends Command {

	public boolean isAPressed, wasAPressed, isXPressed, wasXPressed;

	public PneuLauncherCom() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		// System.out.println("SUBSYSTEM NAME: " + Robot.sfSub.getName());
		requires(Robot.sfSub);

	}

	// Called just before this Command runs the first time
	protected void initialize() {
		isAPressed = false;
		wasAPressed = false;
		isXPressed = false;
		wasXPressed = false;
		Robot.sfSub.initNeutral();
	}


	// Called repeatedly when this Command is scheduled to run
	protected void execute() {

		isAPressed = OI.sfController.getRawButton(RobotMap.XBOX_ABTN);
		if (isAPressed != wasAPressed && isAPressed == true) {
			Robot.logger.debug("Got switch button");
			Robot.autoLauncher = new AutoLauncherCom(RobotMap.SWITCH_DELAY);
			Robot.autoLauncher.start();
		}
		wasAPressed = isAPressed;

		isXPressed = OI.sfController.getRawButton(RobotMap.XBOX_XBTN);
		if (isXPressed != wasXPressed && isXPressed == true) {
			Robot.logger.debug("Got scale button");
			Robot.autoLauncher = new AutoLauncherCom(RobotMap.SCALE_DELAY);
			Robot.autoLauncher.start();
		}
		wasXPressed = isXPressed;

	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
