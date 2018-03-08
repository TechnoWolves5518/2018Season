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
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import org.usfirst.frc.team5518.robot.subsystems.AutoDriveSub;
import org.usfirst.frc.team5518.robot.commands.*;
import org.usfirst.frc.team5518.robot.commands.autonomous.AutoLauncherCom;
import org.usfirst.frc.team5518.robot.commands.autonomous.DoLeftAuto;
import org.usfirst.frc.team5518.robot.commands.autonomous.DoMiddleAuto;
import org.usfirst.frc.team5518.robot.commands.autonomous.DoRightAuto;
import org.usfirst.frc.team5518.robot.commands.autonomous.DriveDistance;
import org.usfirst.frc.team5518.robot.commands.autonomous.StrafeDistance;
import org.usfirst.frc.team5518.robot.subsystems.DriveTrainSub;
import org.usfirst.frc.team5518.robot.subsystems.SpecialFunctionsSub;
import org.usfirst.frc.team5518.robot.Logger;

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
	public static AutoLauncherCom autoLauncher;
	
	public static SpecialFunctionsSub sfSub;
	
//	public static AutoDriveSub autoDriveSub;
	public static DriveDistance driveDistance;
	public static StrafeDistance strafeDistance;
	
	public static Logger logger = new Logger();
	
	public static OI m_oi;
	public static DriverStation ds;
	//public static SmartDashboard smartDS;
	
	Command autonomousCommand; // create placeholder for chosen command
	
	private boolean optionalPath; // Choose to go on the optional paths (red paths on paper, front instead of back)
	
	// Needed information to know which auto path to do
	public static String       gameData;
	private enum RobotLocation {rl_left, rl_middle, rl_right}

	public enum FieldTarget {
        kScale,
        kSwitch,
        kLine,
        kChoose,   // Choose best based on gameData.
        kDoNothing
        }
	
	private FieldTarget chosenAutoFunction;
	//private boolean      isBackPath; // Default: Back=True, Toggle=False (i.e. Front)
	private SendableChooser<String>        pathChooser;
	private SendableChooser<RobotLocation> robotLocationChooser;
	private SendableChooser<FieldTarget> fieldTargetChooser;
	private SendableChooser<String> twoCube;
	
	// Variables we retrieve from the Smart Dashboard
	private String path          = "Unknown";  // This tells whether the robot will cross the field in the front or back
	private RobotLocation robotLocation;       // This is the ROBOT location on the field (not driver team location)
	private FieldTarget fieldTargetList;
	private String numCubes;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		sfSub = new SpecialFunctionsSub();
		driveTrainSub = new DriveTrainSub();
		m_oi          = new OI();
		ds            = DriverStation.getInstance();
		CameraServer.getInstance().startAutomaticCapture();  // Camera Setup
		
		pathChooser = new SendableChooser<String>();
		robotLocationChooser = new SendableChooser<RobotLocation>();
		fieldTargetChooser = new SendableChooser<FieldTarget>();
		
		// CHOOSE ROBOT STARTING POSITION
		robotLocationChooser.addDefault("Middle", RobotLocation.rl_middle);
		robotLocationChooser.addObject("Left", RobotLocation.rl_left);
		robotLocationChooser.addObject("Right", RobotLocation.rl_right);
		
		// CHOOSE AUTONOMOUS TARGET
		fieldTargetChooser.addDefault("Do Nothing", fieldTargetList.kDoNothing);
		fieldTargetChooser.addObject("Line", fieldTargetList.kLine);
		fieldTargetChooser.addObject("Switch", fieldTargetList.kSwitch);
		fieldTargetChooser.addObject("Scale", fieldTargetList.kScale);
		fieldTargetChooser.addObject("Choose", fieldTargetList.kChoose);
		
		// CHOOSE FRONT OR BACK PATH (only applies to left and right switch paths)
		pathChooser.addDefault("Front Path","front");
		pathChooser.addObject("Back Path","back");
		
		// CHOOSE ONE OR TWO CUBE AUTO
		twoCube.addDefault("One Cube", "one");
		twoCube.addDefault("Two Cubes", "two");
		
		// PUT CHOOSER DATA ON THE DASHBOARD
		SmartDashboard.putData("Robot Location", robotLocationChooser);
		SmartDashboard.putData("Autonomous Goal", fieldTargetChooser);
		SmartDashboard.putData("Path to Take", pathChooser);
		SmartDashboard.putData("Number of Cubes", twoCube);
		
		// Set to FALSE for competition.
		logger.setDebug(false); //Must be false during competition
		logger.setVerbose(false);
		
//		driveTrainSub = new DriveTrainSub();
//		autoDriveSub = new AutoDriveSub();
		
		driveInputCom = new MecanumDriveCom();
//		driveDistance = new DriveDistance(0, 0);
//		strafeDistance = new StrafeDistance(0, 0);
		
		// Autonomous data initial.
		gameData        = "";
		//robotLocation   = -1;
		chosenAutoFunction    = FieldTarget.kDoNothing;
		optionalPath    = true;
		numCubes = "one";
		
		// Default.
		autonomousCommand = null;
		
		sfSub.pReverse();
		sfSub.lockWings();
		
		driveTrainSub.calibrateGyro();
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

		// Define robot data needed only for autonomous.
		gameData        = ds.getGameSpecificMessage();
		robotLocation   = robotLocationChooser.getSelected();
		chosenAutoFunction    = fieldTargetChooser.getSelected();
		//isBackPath    = SmartDashboard.getBoolean("Path_Back", false);
		path          = pathChooser.getSelected();
		robotLocation = robotLocationChooser.getSelected();
		numCubes = twoCube.getSelected();

		//System.out.println("isBackPath   : " + isBackPath);
		// Log information
		logger.debug("----------READING DASHBOARD DATA----------");
		logger.debug("Game Data = " + gameData + " Robot Location = " + robotLocation);
		logger.info("path         : " + path);
		logger.info("Field Target : " + chosenAutoFunction);
		logger.info("Number of Cubes : " + numCubes);
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

		driveTrainSub.resetEncoders();
		logger.debug("Auto init.  **** ");
		readDashBoard();
		
		// Handle autonomous based on starting position.
		if (robotLocation == RobotLocation.rl_left){
			logger.debug("Auto Position = left ");
			autonomousCommand = new DoLeftAuto(chosenAutoFunction, gameData, numCubes);
		}
		else if (robotLocation == RobotLocation.rl_middle){
			logger.debug("Auto Position = middle ");
			autonomousCommand = new DoMiddleAuto(chosenAutoFunction, gameData, numCubes);
		}
		else if (robotLocation == RobotLocation.rl_right){
			logger.debug("Auto Position = right ");
			autonomousCommand = new DoRightAuto(chosenAutoFunction, gameData, numCubes);
		} 
		else {
			logger.debug("Auto Position = UNKNOWN ");
			// Include code for this possibility
		}
		
		sfSub.pReverse();
		
		autonomousCommand.start();
		
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// Cancel autonomous.
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
		sfSub.pReverse();
		sfSub.lockWings();
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
	
}
