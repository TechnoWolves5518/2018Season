/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
/**
 * This is the robot class
 */
package org.usfirst.frc.team5518.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team5518.robot.commands.MecanumDriveCom;
import org.usfirst.frc.team5518.robot.commands.DriveDistance;
import org.usfirst.frc.team5518.robot.commands.StrafeDistance;
import org.usfirst.frc.team5518.robot.commands.AutonomousPaths.LeftToLeftScaleGroup;
import org.usfirst.frc.team5518.robot.commands.AutonomousPaths.LeftToLeftSwitchGroup;
import org.usfirst.frc.team5518.robot.commands.AutonomousPaths.LeftToRightScaleGroup;
import org.usfirst.frc.team5518.robot.commands.AutonomousPaths.LeftToRightSwitchBehindGroup;
import org.usfirst.frc.team5518.robot.commands.AutonomousPaths.MiddleToLeftScaleGroup;
import org.usfirst.frc.team5518.robot.commands.AutonomousPaths.MiddleToLeftSwitchGroup;
import org.usfirst.frc.team5518.robot.commands.AutonomousPaths.MiddleToRightScaleGroup;
import org.usfirst.frc.team5518.robot.commands.AutonomousPaths.MiddleToRightSwitchGroup;
import org.usfirst.frc.team5518.robot.commands.AutonomousPaths.RightToLeftScaleGroup;
import org.usfirst.frc.team5518.robot.commands.AutonomousPaths.RightToLeftScaleOptionalGroup;
import org.usfirst.frc.team5518.robot.commands.AutonomousPaths.RightToLeftSwitchBehindGroup;
import org.usfirst.frc.team5518.robot.commands.AutonomousPaths.RightToLeftSwitchFrontGroup;
import org.usfirst.frc.team5518.robot.commands.AutonomousPaths.RightToRightScaleGroup;
import org.usfirst.frc.team5518.robot.commands.AutonomousPaths.RightToRightSwitchGroup;
import org.usfirst.frc.team5518.robot.commands.AutonomousPaths.ToLineAndStopGroup;
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
	//public static SmartDashboard smartDS;
	
	// Robot Commands.
	Command autonomousCommand;
	CommandGroup autoCommandGroup;
	Command toLineAndStop;
	CommandGroup middleToLeftSwitchGroup;
	Command autoCommand;
	
	// DriveStation Custom data
	private enum FieldTarget {
	                kScale,
	                kSwitch,
		            kLine,
	                kChoose,   // Choose best based on gameData.
	                kDoNothing
	                }
	private boolean optionalPath; // Choose to go on the optional paths (red paths on paper, front instead of back)
	
	// Custom definitions.
	private String       gameData;
	private enum RobotLocation {rl_left, rl_middle, rl_right}

	
	private FieldTarget autoFunction;
	//private boolean      isBackPath; // Default: Back=True, Toggle=False (i.e. Front)
	private SendableChooser<String>        pathChooser;
	private SendableChooser<RobotLocation> robotLocationChooser;
	private SendableChooser<FieldTarget> fieldTargetChooser;
	
	// Variables we retrieve from the Smart Dashboard
	private String testGameData  = "Unknown";
	private String path          = "Unknown";  // This tells whether the robot will cross the field in the front or back
	private RobotLocation robotLocation;       // This is the ROBOT location on the field (not driver team location)
	private FieldTarget fieldTarget;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_oi          = new OI();
		ds            = DriverStation.getInstance();
		CameraServer.getInstance().startAutomaticCapture();  // Camera Setup
		
		// For the combo box in the dashboard
		pathChooser = new SendableChooser<String>();
		pathChooser.addDefault("Front","front");
		pathChooser.addObject("Back","back");
		SmartDashboard.putData("Path", pathChooser);
		
		robotLocationChooser = new SendableChooser<RobotLocation>();
		robotLocationChooser.addDefault("Left", RobotLocation.rl_left);
		robotLocationChooser.addObject("Middle", RobotLocation.rl_middle);
		robotLocationChooser.addObject("Right", RobotLocation.rl_right);
		SmartDashboard.putData("Robot Location", robotLocationChooser);
		
		fieldTargetChooser = new SendableChooser<FieldTarget>();
		fieldTargetChooser.addDefault("Scale", fieldTarget.kScale);
		fieldTargetChooser.addObject("Switch", fieldTarget.kSwitch);
		fieldTargetChooser.addObject("Line", fieldTarget.kLine);
		fieldTargetChooser.addObject("Choose", fieldTarget.kChoose);
		fieldTargetChooser.addObject("Do Nothing", fieldTarget.kDoNothing);
		SmartDashboard.putData("Field Target", fieldTargetChooser);

		
		// Set to FALSE for competition.
		logger.setDebug(true); //Must be false during competition
		
		driveTrainSub = new DriveTrainSub();
		sfSub = new SpecialFunctionsSub();
		autoDriveSub = new AutoDriveSub();
		
		driveInputCom = new MecanumDriveCom();
//		driveDistance = new DriveDistance(0, 0);
//		strafeDistance = new StrafeDistance(0, 0);
		
		// Autonomous data initial.
		gameData        = "";
		//robotLocation   = -1;
		autoFunction    = FieldTarget.kSwitch;
		optionalPath    = true;
		
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
	
	public void readDashBoard() {
;
		
		// Define robot data needed only for autonomous.
		testGameData  = SmartDashboard.getString("Test Game Data", "Nothing Found");
		//isBackPath    = SmartDashboard.getBoolean("Path_Back", false);
		path          = pathChooser.getSelected();
		robotLocation = robotLocationChooser.getSelected();
		
		logger.info("testGameData : " + testGameData);
		//System.out.println("isBackPath   : " + isBackPath);
		logger.info("path         : " + path);
		logger.info("location     : " + robotLocation);
		logger.info("Field Target : " + fieldTarget);
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
		// autoDriveSub.resetGyro();
		
		logger.debug("Auto init.  **** ");
		readDashBoard();
//		
//		// Define robot data needed only for autonomous.
//		gameData        = ds.getGameSpecificMessage();
//		//robotLocation   = ds.getLocation();
//		autoFunction    = FieldTarget.kSwitch;	
//		
//		logger.debug("gameData = " + gameData + " Driver Station = " + robotLocation);
//		
//		// Handle autonomous based on starting position.
//		if (robotLocation == RobotLocation.rl_left){
//			logger.debug("Auto Position = left ");
//			autonomousCommand = doAutoLeft(autoFunction, gameData);
//		}
//		else if (robotLocation == RobotLocation.rl_middle){
//			logger.debug("Auto Position = middle ");
//			autonomousCommand = doAutoMiddle(autoFunction, gameData);
//		}
//		else if (robotLocation == RobotLocation.rl_right){
//			logger.debug("Auto Position = right ");
//			autonomousCommand = doAutoRight(autoFunction, gameData);
//		} 
//		else {
//			logger.debug("Auto Position = UNKNOWN ");
//		}
//		autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		float distance = (float) (69);
		float angleSetPoint = 180;
		
		// autoDriveSub.autoDrive(distance, 0.3f);
		// autoDriveSub.autoStrafe(distance, -0.3f);
		autoDriveSub.autoRotate(angleSetPoint, 0.2f);
	}

	@Override
	public void teleopInit() {
		// Cancel autonomous.
		autonomousCommand.cancel();
		autoDriveSub.resetEncoders();
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
	private Command doAutoLeft(FieldTarget function, String gameData){
		Command commandToRun = toLineAndStop;
		
		if (ds.isAutonomous()){
			logger.debug("AutoLeft " + function.toString() + " : using gamedata " + gameData);
		}
		
		if (function == FieldTarget.kDoNothing){
			logger.debug("Do nothing.");
			return commandToRun;
		}
		
		if (function == FieldTarget.kSwitch){
			if(gameData.charAt(0) == 'R')
			{
				//Forward, right, then pivot right to switch.
				logger.debug("Forward, right, then pivot right to switch.");
				return new LeftToRightSwitchBehindGroup();
			} 
			// gameData(0) == 'L'
			else {
				//Drive forward, then pivot right to switch.
				logger.debug("Drive forward, then pivot right to switch.");
				return new LeftToLeftSwitchGroup();
			}			
		}
		if (function == FieldTarget.kScale) {
			if (gameData.charAt(1) == 'R') {
				//Drive to right scale
				logger.debug("Drive to right scale");
				return new LeftToRightScaleGroup();
			}
			//gameData(1) == 'L'
			else {
				//Drive to left scale
				logger.debug("Drive to left scale");
				return new LeftToLeftScaleGroup();
			}
		}
		return commandToRun;
	}
	
	/**
	 * doAutoRight
	 * This function handles autonomous from the right starting position.
	 * When autoFunction is:
	 *   - LINE then drive forward and stop.
	 *   - SWITCH or SCALE then drive to specified switch/scale.
	 *   - DONOTHING then do nothing.
	 *   - CHOOSE then go the best route.
	 * @param function
	 * @param gameData
	 */
	
	private Command doAutoRight(FieldTarget function, String gameData){
		Command commandToRun = toLineAndStop;
		
		if (ds.isAutonomous()){
			logger.debug("AutoRight " + function.toString() + " : using gamedata " + gameData);
		}
		
		if (function == FieldTarget.kDoNothing){
			logger.debug("Do nothing.");
			return commandToRun;
		}
		
		if (function == FieldTarget.kSwitch){
			if(gameData.charAt(0) == 'R')
			{
				//Forward, right, then pivot right to switch.
				logger.debug("Forward, right, then pivot right to switch.");
				return new RightToRightSwitchGroup();
			} 
			// gameData(0) == 'L'
			else {
				//Drive forward, then pivot right to switch.
				logger.debug("Drive forward, then pivot right to switch.");
				if (optionalPath != true) {
					logger.debug("Optional = False");
					return new RightToLeftSwitchBehindGroup();
				} else {
					logger.debug("Optional = True");
					return new RightToLeftSwitchFrontGroup();
				}
				
			}			
		}
		
		if (function == FieldTarget.kScale) {
			if (gameData.charAt(1) == 'R') {
				//Drive to right scale
				logger.debug("Drive to right scale");
				return new RightToRightScaleGroup();
				}
			//gameData(1) == 'L'
			else {
				//Drive to left scale
				logger.debug("Drive to left scale");
				
				if (optionalPath != true) {
					return new RightToLeftScaleGroup();
				} else {
					return new RightToLeftScaleOptionalGroup();
				}
			}
		}
		return commandToRun;
	}  //End of doAutoRight
	
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
	private CommandGroup doAutoMiddle(FieldTarget function, String gameData){
		CommandGroup commandToRun = middleToLeftSwitchGroup;
		
		// Send log msg.
		if (ds.isAutonomous()){
			logger.debug("AutoMiddle "+function.toString()+" : using gamedata "+gameData);
			//Gamedata gives the layout of the switches. (Example - LRL - Left Right Left)
			
		}
		
		// DONOTHING : Do nothing.
		if (function == FieldTarget.kDoNothing){
			logger.debug("Do nothing.");
			// return commandToRun;
		}
		
		// kLINE : Drive forward and stop.
		if (function == FieldTarget.kLine){
			logger.debug("Drive forward and stop.");
			return new ToLineAndStopGroup();
		}
		
		// kSWITCH or kCHOOSE : Determine gameData and launch in switch.		
		if (function == FieldTarget.kSwitch || function == FieldTarget.kChoose){
			// Check gameData at postion[0] for Switch.
			if(gameData.charAt(0) == 'R')
			{
				//Drive forward and launch.
				logger.debug("Drive forward and launch.");
				// return new MiddleToRightSwitch();
				return new MiddleToRightSwitchGroup();
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
		if (function == FieldTarget.kScale){
			// Check gameData position[1] for Scale.
			if (gameData.charAt(1) == 'R'){
				// Drive right to Scale and launch.
				logger.debug("Drive right to scale and launch.");
			    return new MiddleToRightScaleGroup();
			}
			// gameData(1) == 'L'
			else {
				// Drive left to scale and launch.
				logger.debug("Drive left to scale and launch.");
				return new MiddleToLeftScaleGroup();
			}
		}
		return commandToRun;	
	}//end doAutoMiddle()
	
}
