package org.usfirst.frc.team5518.robot.commands;

import org.usfirst.frc.team5518.robot.OI;
import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.RobotMap;
import org.usfirst.frc.team5518.robot.commands.autonomous.AutoLauncherCom;

import edu.wpi.first.wpilibj.command.Command;

/**
 * A command that polls if certain buttons where pressed and
 * execute a function for the launcher accordingly.
 */
public class PneuLauncherCom extends Command {

	/** Joystick button states */
	private boolean isAPressed, wasAPressed, isXPressed, wasXPressed;
	
	/** Command to run if certain button pressed */
	private AutoLauncherCom command;
	
	/**
	 * Construct a new command to handle launcher functions
	 */
	public PneuLauncherCom() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.sfSub);
	}

	/*
	 * Called just before this Command runs the first time(non-Javadoc)
	 * @see edu.wpi.first.wpilibj.command.Command#initialize()
	 */
	protected void initialize() {
		isAPressed = false;
		wasAPressed = false;
		isXPressed = false;
		wasXPressed = false;
		Robot.sfSub.initNeutral();
	}


	/*
	 * Called repeatedly when this Command is scheduled to run(non-Javadoc)
	 * @see edu.wpi.first.wpilibj.command.Command#execute()
	 */
	protected void execute() {
		// check if A button pressed for the first time
		isAPressed = OI.sfController.getRawButton(RobotMap.XBOX_ABTN);
		if (isAPressed != wasAPressed && isAPressed) {
			// Robot.sfSub.shootSwitch();
			command = new AutoLauncherCom(RobotMap.SWITCH_DELAY);
		}
		wasAPressed = isAPressed;

		// check if B button pressed for the first time
		isXPressed = OI.sfController.getRawButton(RobotMap.XBOX_XBTN);
		if (isXPressed != wasXPressed && isXPressed) {
			// Robot.sfSub.shootScale();
			command = new AutoLauncherCom(RobotMap.SCALE_DELAY);
		}
		wasXPressed = isXPressed;

	}

	/*
	 * Make this return true when this Command no longer needs to run execute()(non-Javadoc)
	 * @see edu.wpi.first.wpilibj.command.Command#isFinished()
	 */
	protected boolean isFinished() {
		return false;
	}

	/*
	 * Called once after isFinished returns true(non-Javadoc)
	 * @see edu.wpi.first.wpilibj.command.Command#end()
	 */
	protected void end() {
		// start command when this one ends
		if (command != null)
			command.start();
	}

	/*
	 * Called when another command which requires one or more of the same
	 * subsystems is scheduled to run(non-Javadoc)
	 * @see edu.wpi.first.wpilibj.command.Command#interrupted()
	 */
	protected void interrupted() {
		// do nothing here
	}
}
