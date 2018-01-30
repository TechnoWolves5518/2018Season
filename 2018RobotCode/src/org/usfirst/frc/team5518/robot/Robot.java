/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5518.robot;

import org.usfirst.frc.team5518.robot.commands.MecanumDriveCom;
import org.usfirst.frc.team5518.robot.subsystems.DriveTrainSub;
import org.usfirst.frc.team5518.robot.subsystems.SpecialFunctionsSub;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {
	
	// Calls to the subsystems and commands, as well as the OI
	// IMPORTANT 
	// Whenever you create a subsystem it is automagically registered with the Scheduler 
	// class (look in the Scheduler constructor). That means that whenever you call 
	// Scheduler.getInstance().run() it basically calls the SubSystems command execute() 
	// method of every SubSystem registered.
	
	/**
	 * Subsystems
	 */
	public static final DriveTrainSub driveTrainSub = new DriveTrainSub();
	public static final MecanumDriveCom driveInputCom = new MecanumDriveCom();
	public static final SpecialFunctionsSub sfSub = new SpecialFunctionsSub();
	
	/**
	 * Joystick (operator) interface
	 */
	public static OI m_oi;
	
	/**
	 * Autonomous definitions
	 */
	private boolean isDebug = true;  // Set to false during competition.
	private DriverStation ds;
	private enum AutoFunction {
	                kScale,
	                kSwitch,
		            kLine,
	                kChoose,   // Choose best based on gameData.
	                kDoNothing
	                }
	private AutoFunction kAutoFunction = AutoFunction.kChoose;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_oi = new OI();
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		System.out.println("Auto init.");
		
		// Get robot location from DriverStation.
		int teamStation; 
		teamStation = ds.getLocation();
		
		// Get autoFunction instructions from DriverStation.
		//AutoFunction auto = ds.getAutoFunction();
		AutoFunction autoFunction = AutoFunction.kScale;
		
		// Get game data from DriverStation.
		String gameData;
		gameData = ds.getGameSpecificMessage();
		
		// Handle autonomous based on starting position.
		// teamStation = 1 (Left)
		if (teamStation == 1) {
			if (isDebug) {
				System.out.println("Auto Position = 1 ");
			}
			//doAutoLeft(autoFunction, gameData);
		}
		// teamStation = 2 (Middle)
		else if (teamStation == 2) {
			if (isDebug){
				System.out.println("Auto Position = 2 ");
			}
			doAutoMiddle(autoFunction, gameData);
		}
		// teamStation = 3 (Right)
		else {
			if (isDebug){
				System.out.println("Auto Position = 3 ");
			}
			//doAutoRight(autoFunction, gameData);
		}
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		
		if (isDebug) {
			System.out.println("Auto periodic.");
		}
	}

	@Override
	public void teleopInit() {
		
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
	
	/**
	 * doAutoMiddle
	 *   This function handles autonomous form the middle starting position.
	 *   When autoFunction is:
	 *    - LINE then drive forward and stop.
	 *    - SWITCH or CHOOSE then drive forward or Left and launch the cube.
	 *    - SCALE then drive right or left to Scale and launch the cube.
	 *    - DONOTHING then do nothing.
	 * @param function
	 * @param gameData
	 */
	private void doAutoMiddle(AutoFunction function, String gameData) {
		// Send log msg.
		if (isDebug || ds.isAutonomous()) {
			System.out.println("AutoMiddle " + function.toString() + " : using gamedata "+gameData);
		}
		
		// DONOTHING : Do nothing.
		if (function == AutoFunction.kDoNothing) {
			if (isDebug) {
				System.out.println("Do nothing.");
			}
			return;
		}
		
		// kLINE : Drive forward and stop.
		if (function == AutoFunction.kLine){
			if (isDebug){
				System.out.println("Drive forward and stop.");
			}
			//autoToAutoLine();
			return;
		}
		
		// kSWITCH or kCHOOSE : Determine gameData and launch in switch.		
		if (function == AutoFunction.kSwitch || function == AutoFunction.kChoose){
			// Check gameData for which side to launch into.
			if(gameData.charAt(0) == 'R')
			{
				//Drive forward and launch.
				if (isDebug){
					System.out.println("Drive forward and launch.");
				}
				//autoMiddleToSwitch();
			} 
			// gameData(0) == 'L'
			else {
				//Drive left to Switch and launch.
				if (isDebug){
					System.out.println("Drive left to switch and launch.");
				}
				//autoMiddleLeftToSwitch();
			}
			return;
		}
		
		// kSCALE : Determine gameData and launch in scale.
		if (function == AutoFunction.kScale){
			// Check gameData for which side to drive to.
			if (gameData.charAt(1) == 'R'){
				// Drive right to Scale and launch.
				if (isDebug){
					System.out.println("Drive right to scale and launch.");
				}
				//autoMiddleDriveRightToScale();
			}
			// gameData(1) == 'L'
			else {
				// Drive left to scale and launch.
				if (isDebug){
					System.out.println("Drive left to scale and launch.");
				}
				//autoMiddleDriveLeftToScale();
			}
		}
		return;	
	}//end doAutoMiddle()
}
