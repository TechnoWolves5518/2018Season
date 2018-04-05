/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5518.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	
	// DRIVE VARIABLES
	public static double KX = 1;
	public static double KY = 1;
	public static double KZ = 1;
	
	// Decreased speeds for DEMO PURPOSES
//	public static double KX = 0.5;
//	public static double KY = 0.5;
//	public static double KZ = 0.5;
	
	// SPEED VARIABLES
	public static double INTAKE_SPEED = 1;
	public static double SECONDARY_INTAKE_SPEED = 1;
	public static double EXTENDED_INTAKE_SPEED = 0.4;
//	public static double SWITCH_DELAY = .08; // extension time for switch shot in seconds (70 ms)
	public static double SWITCH_DELAY = .06; // extension time for switch shot in seconds (60 ms) DEMO NUMBER
	public static double SCALE_DELAY = .5; // extension time for scale shot in seconds (300 ms)
	public static float AUTO_DRIVE_SPEED = 0.3f;
	public static float AUTO_STRAFE_SPEED = 0.4f;
	public static float AUTO_ROTATE_SPEED = 0.3f;
	
	public static float LINE_DISTANCE = 140;
	public static float SIDE_EXTEND = 80;
	public static float WAIT_TIME = 0.4f;
	public static float ROBOT_LENGTH = 32f;
	public static float ROBOT_WIDTH = 37f;
	public static double SCALE_ALIGNMENT_DIST = 32;
	public static double ALLOWANCE = 2;
	
	// PORT MAPPING
	// drive train
	public static int FRONT_LEFT = 0;
	public static int BACK_LEFT = 1;
	public static int FRONT_RIGHT = 2;
	public static int BACK_RIGHT = 3;
	
	// special Functions 
	public static int LEFT_INTAKE = 4;
    public static int RIGHT_INTAKE = 5;
    public static int LEFT_SECONDARY_INTAKE = 0;
    public static int RIGHT_SECONDARY_INTAKE = 1;
    public static int LEFT_EXTENDED_INTAKE = 2;
    public static int RIGHT_EXTENDED_INTAKE = 3;
    public static int COMPRESSOR = 0; //PCM port
    public static int PNEU_SHOOTER_FORWARD = 1; //PCM port  
    public static int PNEU_SHOOTER_BACKWARD = 2; //PCM port
    public static int PNEU_INTAKE_FORWARD = 0; //PCM port
    public static int PNEU_INTAKE_BACKWARD = 3; //PCM port
	
	// WINGMAN STUFF RIPOFFz
	// axes
	public static int JOYSTICK_XAXIS = 0;
	public static int JOYSTICK_YAXIS = 1;
	public static int JOYSTICK_ZAXIS = 2;
	
	// XBOX 360/ONE CONTROLLER MAPPING
	// axes
	public static int XBOX_LSTICKX = 0;
	public static int XBOX_LSTICKY = 1; 
	public static int XBOX_RSTICKX = 4;
	public static int XBOX_RSTICKY = 5;
	public static int XBOX_LTRIGGER = 2;
	public static int XBOX_RTRIGGER = 3;
	
	// buttons
	public static int XBOX_YBTN = 4;
	public static int XBOX_XBTN = 3;
	public static int XBOX_ABTN = 1;
	public static int XBOX_BBTN = 2; 
	public static int XBOX_RBUMPER = 6;
	public static int XBOX_LBUMPER = 5;
	public static int XBOX_LSTICK = 9;
	public static int XBOX_RSTICK = 10;
	public static int XBOX_START = 8;
	public static int XBOX_BACK = 7;
}
