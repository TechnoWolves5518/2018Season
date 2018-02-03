/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5518.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team5518.robot.commands.MecanumDriveCom;
import org.usfirst.frc.team5518.robot.commands.DriveDistance;
import org.usfirst.frc.team5518.robot.commands.StrafeDistance;
import org.usfirst.frc.team5518.robot.subsystems.AutoDriveSub;
import org.usfirst.frc.team5518.robot.commands.*;
import org.usfirst.frc.team5518.robot.subsystems.DriveTrainSub;
import org.usfirst.frc.team5518.robot.subsystems.SpecialFunctionsSub;
import org.usfirst.frc.team5518.robot.Logger;

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
	public static DriveTrainSub driveTrainSub;
	public static MecanumDriveCom driveInputCom;
	
	public static SpecialFunctionsSub sfSub;
	
	public static AutoDriveSub autoDriveSub;
	public static DriveDistance driveDistance;
	public static StrafeDistance strafeDistance;
	
	public static Logger logger = new Logger();

	public static OI m_oi;
	public static DriverStation ds;
	
	// Robot Commands.
	Command autonomousCommand;
	CommandGroup autoCommandGroup;
	Command toLineAndStop;
	CommandGroup middleToLeftSwitchGroup;
	Command autoCommand;
	
	// DriveStation Custom data
	private enum AutoFunction {
	                kScale,
	                kSwitch,
		            kLine,
	                kChoose,   // Choose best based on gameData.
	                kDoNothing
	                }
	
	// Custom definitions.
	private String       gameData;
	private int          robotLocation;
	private AutoFunction autoFunction;
	
	
	

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_oi          = new OI();
		ds            = DriverStation.getInstance();
		
		driveTrainSub = new DriveTrainSub();
		sfSub = new SpecialFunctionsSub();
		autoDriveSub = new AutoDriveSub();
		
		driveInputCom = new MecanumDriveCom();
//		driveDistance = new DriveDistance(0, 0);
//		strafeDistance = new StrafeDistance(0, 0);
		
		// Autonomous data initial.
		gameData        = "";
		robotLocation   = -1;
		autoFunction    = AutoFunction.kSwitch;
		
		// Initialize all autonomous commands.
		toLineAndStop = new ToLineAndStopCom();
		middleToLeftSwitchGroup = new MiddleToLeftSwitchGroup();
		
		// Default.
		autonomousCommand = toLineAndStop;
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		gameData = "";
		// Also set DriverStation gameData to ""
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

		autoDriveSub.resetEncoders();

		logger.debug("Auto init.");

		// Define robot data needed only for autonomous.
		gameData        = ds.getGameSpecificMessage();
		robotLocation   = ds.getLocation();
		autoFunction    = AutoFunction.kSwitch;		
		logger.info("gameData = " + gameData + " location = " + robotLocation);
		// Handle autonomous based on starting position.
		// robotLocation = 1 (Left)
		if (robotLocation == 1){
			logger.debug("Auto Position = 1 ");
			autonomousCommand = doAutoLeft(autoFunction, gameData);

		}
		// robotLocation = 2 (Middle)
		else if (robotLocation ==2){
			logger.debug("Auto Position = 2 ");

			autonomousCommand = doAutoMiddle(autoFunction, gameData);
			// autoCommandGroup = doAutoMiddle(autoFunction, gameData);
		}
		// robotLocation = 3 (Right)
		else {
			logger.debug("Auto Position = 3 ");

			//doAutoRight(autoFunction, gameData);
		} 
		autonomousCommand.start();
		// autoCommandGroup.start();
    
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();

		// autoDriveSub.autoDrive(12f, 0f, 0f, 0.2f, 0f, 0f);
	}

	@Override
	public void teleopInit() {
		// Cancel autonomous.
		autonomousCommand.cancel();
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
		System.out.println("Running in test mode.");
	}
	
	/**
	 * doAutoLeft
	 * This function handles autonomous from the left starting position.
	 * When autoFunction is:
	 *   - LINE then drive forward and stop.
	 *   - SWITCH or SCALE then drive to specified switch/scale.
	 *   - DONOTHING then do nothing.
	 *   - CHOOSE then go the best route.
	 * @param function
	 * @param gameData
	 */
	private Command doAutoLeft(AutoFunction function, String gameData){
		Command commandToRun = toLineAndStop;
		
		if (ds.isAutonomous()){
			logger.debug("AutoLeft " + function.toString() + " : using gamedata " + gameData);
		}
		
		if (function == AutoFunction.kDoNothing){
			logger.debug("Do nothing.");
			return commandToRun;
		}
		
		if (function == AutoFunction.kSwitch){
			if(gameData.charAt(0) == 'R')
			{
				//Forward, right, then pivot right to switch.
				logger.debug("Forward, right, then pivot right to switch.");
				//return new MiddleToRightSwitch();
			} 
			// gameData(0) == 'L'
			else {
				//Drive forward, then pivot right to switch.
				logger.debug("Drive forward, then pivot right to switch.");
				return new LeftToLeftSwitch();
			}
		}
		
		return commandToRun;
	}
	
	/**
	 * doAutoMiddle
	 *   This function handles autonomous from the middle starting position.
	 *   When autoFunction is:
	 *    - LINE then drive forward and stop.
	 *    - SWITCH or CHOOSE then drive forward or Left and launch the cube.
	 *    - SCALE then drive right or left to Scale and launch the cube.
	 *    - DONOTHING then do nothing.
	 * @param function
	 * @param gameData
	 */
	private CommandGroup doAutoMiddle(AutoFunction function, String gameData){
		CommandGroup commandToRun = middleToLeftSwitchGroup;
		
		// Send log msg.
		if (ds.isAutonomous()){
			logger.debug("AutoMiddle "+function.toString()+" : using gamedata "+gameData);
			//Gamedata gives the layout of the switches. (Example - LRL - Left Right Left)
			
		}
		
		// DONOTHING : Do nothing.
		if (function == AutoFunction.kDoNothing){
			logger.debug("Do nothing.");
			// return commandToRun;
		}
		
		// kLINE : Drive forward and stop.
		if (function == AutoFunction.kLine){
			logger.debug("Drive forward and stop.");
			// return toLineAndStop;
		}
		
		// kSWITCH or kCHOOSE : Determine gameData and launch in switch.		
		if (function == AutoFunction.kSwitch || function == AutoFunction.kChoose){
			// Check gameData at postion[0] for Switch.
			if(gameData.charAt(0) == 'R')
			{
				//Drive forward and launch.
				logger.debug("Drive forward and launch.");
				// return new MiddleToRightSwitch();
			} 
			// gameData(0) == 'L'
			else {
				//Drive left to Switch and launch.
				logger.debug("Drive left to switch and launch.");
				// return new MiddleToLeftSwitch();
				return middleToLeftSwitchGroup;
			}
		}
		
		// kSCALE : Determine gameData and launch in scale.
		if (function == AutoFunction.kScale){
			// Check gameData position[1] for Scale.
			if (gameData.charAt(1) == 'R'){
				// Drive right to Scale and launch.
				logger.debug("Drive right to scale and launch.");
				// return new MiddleToRightScale();
			}
			// gameData(1) == 'L'
			else {
				// Drive left to scale and launch.
				logger.debug("Drive left to scale and launch.");
				// return new MiddleToLeftScale();
			}
		}
		return commandToRun;	
	}//end doAutoMiddle()
	
}
