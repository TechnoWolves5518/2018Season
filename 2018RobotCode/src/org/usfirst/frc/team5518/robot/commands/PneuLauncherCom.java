package org.usfirst.frc.team5518.robot.commands;

import org.usfirst.frc.team5518.robot.OI;
import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.RobotMap;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */

public class PneuLauncherCom extends Command {
	// hello
	public boolean isAPressed, wasAPressed, isXPressed, wasXPressed;
	private Button aButton = new JoystickButton(Robot.m_oi.xbox, RobotMap.XBOX_ABTN);
	private Button xButton = new JoystickButton(Robot.m_oi.xbox, RobotMap.XBOX_XBTN);
	
	public PneuLauncherCom() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.sfSub);
    	
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.sfSub.shootSwitch();
    }
    
  
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	isAPressed = Robot.m_oi.xbox.getRawButton(RobotMap.XBOX_ABTN);
    	if (isAPressed != wasAPressed && isAPressed == true){
    		Robot.sfSub.shootSwitch();    		
    	}
    	wasAPressed = isAPressed;
    	
    	isXPressed = Robot.m_oi.xbox.getRawButton(RobotMap.XBOX_XBTN);
    	if (isXPressed != wasXPressed && isXPressed == true){
    		Robot.sfSub.shootScale();
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
