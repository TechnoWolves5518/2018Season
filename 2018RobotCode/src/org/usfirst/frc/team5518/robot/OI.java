/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5518.robot;

import org.usfirst.frc.team5518.robot.commands.ForwardIntakeCom;
import org.usfirst.frc.team5518.robot.commands.WingReleaseCom;
import org.usfirst.frc.team5518.robot.commands.ReverseIntakeCom;
import org.usfirst.frc.team5518.robot.commands.autonomous.AutoLauncherCom;
import org.usfirst.frc.team5518.robot.commands.autonomous.RotateDistance;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	
	public static Joystick driveController = new Joystick(0); // Create XBOX controller from USB port 0
	public static Joystick sfController = new Joystick(1); // Create XBOX controller from USB port 1
	// public static Joystick flight = new Joystick(0); // Create flight controller from USB port 0
	
	private Button lBumperSF = new JoystickButton(sfController, RobotMap.XBOX_LBUMPER);
	
	private Button bButtonSF = new JoystickButton(sfController, RobotMap.XBOX_BBTN);
	private Button yButtonSF = new JoystickButton(sfController, RobotMap.XBOX_YBTN);
	private Button aButtonSF = new JoystickButton(sfController, RobotMap.XBOX_ABTN);
	private Button xButtonSF = new JoystickButton(sfController, RobotMap.XBOX_XBTN);
	
	private Button xButtonDrive = new JoystickButton(driveController, RobotMap.XBOX_XBTN);
	private Button bButtonDrive = new JoystickButton(driveController, RobotMap.XBOX_BBTN);
	private Button yButtonDrive = new JoystickButton(driveController, RobotMap.XBOX_YBTN);
	
	public OI() {
		
		lBumperSF.whileHeld(new WingReleaseCom());
		
		aButtonSF.whenPressed(new AutoLauncherCom(RobotMap.SWITCH_DELAY));
		xButtonSF.whenPressed(new AutoLauncherCom(RobotMap.SCALE_DELAY));
		
		bButtonSF.whileHeld(new ForwardIntakeCom());
		yButtonSF.whileHeld(new ReverseIntakeCom());
		
		xButtonDrive.whenPressed(new RotateDistance(-90f, 0.3f));
		bButtonDrive.whenPressed(new RotateDistance(90f, 0.3f));
		yButtonDrive.whenPressed(new RotateDistance(90f, 0.3f));
		
	}
	
	
}




