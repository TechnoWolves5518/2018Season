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

public class WingReleaseCom extends Command {

	public boolean isAPressed, wasAPressed, isXPressed, wasXPressed;
	public boolean activateWings;

	public double time; public double timeout;
	
	public WingReleaseCom() {
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
		activateWings = false;
		time = 0;
		timeout = 1000;
		Robot.sfSub.initNeutral();
	}
	
	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		
		// activateWings = OI.sfController.getRawButton(RobotMap.XBOX_RBUMPER);
		
		activateWings = OI.sfController.getRawButtonPressed(RobotMap.XBOX_RBUMPER);
		
		if (activateWings) {
			Robot.sfSub.activateWings();
			System.out.println("Activate wings");
			time = System.currentTimeMillis();
		}
		if ((System.currentTimeMillis() - time) > timeout) {
			Robot.sfSub.lockWings();
		}
		
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
