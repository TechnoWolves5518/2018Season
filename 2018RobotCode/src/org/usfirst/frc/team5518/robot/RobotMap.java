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
	public static double KX = 0.3;
	public static double KY = 0.3;
	public static double KZ = 0.3;
	
	// SPECIAL FUNCTIONS VARIABLES
	public static double INTAKE_SPEED = 0.5;
	public static double SWITCH_DELAY = 0.06; // extension time for switch shot in seconds (60 milliseconds)
	public static double SCALE_DELAY = 0.25; // extension time for scale shot in seconds (250 milliseconds)
	
	// PORT MAPPING
	// drive train
	public static int FRONT_LEFT = 0;
	public static int BACK_LEFT = 1;
	public static int FRONT_RIGHT = 2;
	public static int BACK_RIGHT = 3;
	
	// special functions 
	public static int LEFT_INTAKE = 4;
    public static int RIGHT_INTAKE = 5;	
    public static int COMPRESSOR = 0; //PCM port
    public static int DS_FORWARD = 1; //PCM port 
    public static int DS_BACKWARD = 2; //PCM port 
    public static int SOLENOID = 3; //PCM port 
	
	// WINGMAN STUFF RIPOFF
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
