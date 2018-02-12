/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5518.robot;

import org.usfirst.frc.team5518.robot.commands.ForwardIntakeCom;
import org.usfirst.frc.team5518.robot.commands.PneuLauncherCom;
import org.usfirst.frc.team5518.robot.commands.ReverseIntakeCom;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	
	public static Joystick driveController = new Joystick(0); // Create XBOX controller from USB port 0
	public static Joystick sfController = new Joystick(1); // Create XBOX controller from USB port 0
	// public static Joystick flight = new Joystick(0); // Create flight controller from USB port 0
	
	private Button lBumper = new JoystickButton(sfController, RobotMap.XBOX_LBUMPER);
	private Button bButton = new JoystickButton(sfController, RobotMap.XBOX_BBTN);
	private Button yButton = new JoystickButton(sfController, RobotMap.XBOX_YBTN);
	
	public OI() {
		lBumper.whileHeld(new PneuLauncherCom());
		bButton.whileHeld(new ForwardIntakeCom());
		yButton.whileHeld(new ReverseIntakeCom());
	}
	
	
}
