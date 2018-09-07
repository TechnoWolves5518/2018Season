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
	public static double KX = 0.8;
	public static double KY = 0.8;
	public static double KZ = 0.8;
	
	// Decreased speeds for DEMO PURPOSES
	// Make sure robot is not in granny mode during competition
//	public static double KX = 0.5;
//	public static double KY = 0.5;
//	public static double KZ = 0.5;
	
	// SPEED VARIABLES
	public static double INTAKE_SPEED = 0.8;
	public static double SECONDARY_INTAKE_SPEED = 1;
	public static double EXTENDED_INTAKE_SPEED = 0.6;
	public static float AUTO_DRIVE_SPEED = 0.3f;
	public static float AUTO_STRAFE_SPEED = 0.4f;
	public static float AUTO_ROTATE_SPEED = 0.3f;
	
	// MISC VALUES
	public static double SWITCH_DELAY = .06; // extension time for switch shot in seconds (70 ms)
	public static double SCALE_DELAY = .5; // extension time for scale shot in seconds (500 ms)
	
	public static float LINE_DISTANCE = 140;
	public static float SIDE_EXTEND = 80;
	public static double SCALE_ALIGNMENT_DIST = 32;
	public static double ALLOWANCE = 2;
	
	public static float ROBOT_LENGTH = 32f;
	public static float ROBOT_WIDTH = 37f;
	
	public static float WAIT_TIME = 0.4f;
	
	public static double CONTROLLER_CUSTOM_DEADBAND = 0.03;
	
	// PORT MAPPING
	// drive train
	public static int FRONT_LEFT = 0;
	public static int BACK_LEFT = 1;
	public static int FRONT_RIGHT = 2;
	public static int BACK_RIGHT = 3;
	
	// special functions
		// PWM
	public static int LEFT_SPIKY_INTAKE = 0;
    public static int RIGHT_SPIKY_INTAKE = 1;
    public static int LEFT_ARM_INTAKE = 2;
    public static int RIGHT_ARM_INTAKE = 3;
    public static int LEFT_INTAKE = 4;
    public static int RIGHT_INTAKE = 5;
    		// PCM
    public static int COMPRESSOR = 0;
    public static int PNEU_SHOOTER_FORWARD = 1;  
    public static int PNEU_SHOOTER_BACKWARD = 2;
    public static int PNEU_INTAKE_FORWARD = 0;
    public static int PNEU_INTAKE_BACKWARD = 3;
	
    // sensors
    		// ultrasonic
    public static int ULTRA_PING = 8;
    public static int ULTRA_ECHO = 9;
    		// encoders
    public static int LEFT_ENC_A = 0;
    public static int LEFT_ENC_B = 1;
    public static int RIGHT_ENC_A = 2;
    public static int RIGHT_ENC_B = 3;
    		// gyroscope
    public static double GYRO_PID_KP = 0.05;
    public static double GYRO_PID_KI = 0;
    public static double GYRO_PID_KD = 0.05;
    public static double GYRO_PID_SPEED = 0.4;
    
	// WINGMAN STUFF
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
