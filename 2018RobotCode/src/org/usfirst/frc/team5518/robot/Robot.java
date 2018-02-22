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
	
	/** Subsystems */
	public static DriveTrainSub driveTrainSub;
	public static SpecialFunctionsSub sfSub;

	/** Logger class */
	private Logger logger;
	
	/** Operator interface and driver station */
	public static OI oi;
	public static DriverStation ds;
	
	/** Command to execute in autonomous */
	private Command autonomousCommand;
	
	/** Location choices in autonomous */
	public enum RobotLocation {
		rl_left, 
		rl_middle, 
		rl_right
	}

	/** Target choices in autonomous */
	public enum FieldTarget {
        kScale,
        kSwitch,
        kLine,
        kChoose,   // Choose best based on gameData.
        kDoNothing
    }
	
	/** Autonomous choice and choosers */
	private SendableChooser<String> pathChooser;
	private SendableChooser<RobotLocation> robotLocationChooser;
	private SendableChooser<FieldTarget> fieldTargetChooser;
	
	/** Data retrieved from the SmartDashboard and DriverStation */
	private static String gameData;
	private RobotLocation robotLocation;       // This is the ROBOT location on the field (not driver team location)
	private FieldTarget chosenAutoFunction;
	private String path = "Unknown";  // This tells whether the robot will cross the field in the front or back

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		// init subsystem
		driveTrainSub = new DriveTrainSub();
		sfSub = new SpecialFunctionsSub();
		
		// init other tools
		oi = new OI();
		ds = DriverStation.getInstance();
		logger = Logger.getInstance();
		// CameraServer.getInstance().startAutomaticCapture();  // Camera Setup
		
		// init sendable choosers
		pathChooser = new SendableChooser<String>();
		robotLocationChooser = new SendableChooser<RobotLocation>();
		fieldTargetChooser = new SendableChooser<FieldTarget>();
		
		// ADD ROBOT STARTING POSITION
		robotLocationChooser.addDefault("Middle", RobotLocation.rl_middle);
		robotLocationChooser.addObject("Left", RobotLocation.rl_left);
		robotLocationChooser.addObject("Right", RobotLocation.rl_right);
		
		// ADD AUTONOMOUS TARGET
		fieldTargetChooser.addDefault("Do Nothing", FieldTarget.kDoNothing);
		fieldTargetChooser.addObject("Line", FieldTarget.kLine);
		fieldTargetChooser.addObject("Switch", FieldTarget.kSwitch);
		fieldTargetChooser.addObject("Scale", FieldTarget.kScale);
		fieldTargetChooser.addObject("Choose", FieldTarget.kChoose);
		
		// ADD FRONT OR BACK PATH (only applies to left and right switch paths)
		pathChooser.addDefault("Front Path","front");
		pathChooser.addObject("Back Path","back");
		
		// PUT CHOOSER DATA ON THE DASHBOARD
		SmartDashboard.putData("Robot Location", robotLocationChooser);
		SmartDashboard.putData("Autonomous Goal", fieldTargetChooser);
		SmartDashboard.putData("Path to Take", pathChooser);
		
		// Set to FALSE for competition.
		logger.setDebug(true); //Must be false during competition
		
		// Set initial autonomous data
		gameData = "";
		chosenAutoFunction = FieldTarget.kDoNothing;
		
		// calibrate gyro (ALWAYS DO LAST)
		driveTrainSub.calibrateGyro();
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		// Also set DriverStation gameData to ""
		gameData = "";
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
		// reset encoders and get data
		logger.debug("Auto init.  **** ");
		driveTrainSub.resetEncoders();
		readDashboard();
		
		// Handle autonomous based on starting position.
		if (robotLocation == RobotLocation.rl_left){
			logger.debug("Auto Position = left ");
			autonomousCommand = new DoLeftAuto(chosenAutoFunction, gameData);
		}
		else if (robotLocation == RobotLocation.rl_middle){
			logger.debug("Auto Position = middle ");
			autonomousCommand = new DoMiddleAuto(chosenAutoFunction, gameData);
		}
		else if (robotLocation == RobotLocation.rl_right){
			logger.debug("Auto Position = right ");
			autonomousCommand = new DoRightAuto(chosenAutoFunction, gameData);
		} 
		else {
			logger.debug("Auto Position = UNKNOWN ");
			// Include code for this possibility
		}
		
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
		if (autonomousCommand.isRunning()) {
			autonomousCommand.cancel();
		}
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
	 * Helper method to read info from dashboard
	 * at the beginning of autonomous
	 */
	private void readDashboard() {
		// Get data needed only for autonomous.
		gameData = ds.getGameSpecificMessage();
		robotLocation = robotLocationChooser.getSelected();
		chosenAutoFunction = fieldTargetChooser.getSelected();
		path = pathChooser.getSelected();

		// Log information
		logger.debug("----------READING DASHBOARD DATA----------");
		logger.debug("Game Data = " + gameData + " Robot Location = " + robotLocation);
		logger.info("path         : " + path);
		logger.info("Field Target : " + chosenAutoFunction);
	}
	
}
