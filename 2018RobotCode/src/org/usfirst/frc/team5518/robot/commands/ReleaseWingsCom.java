package org.usfirst.frc.team5518.robot.commands;

import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.RobotMap;
import org.usfirst.frc.team5518.robot.OI;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ReleaseWingsCom extends Command {

    public ReleaseWingsCom() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.sfSub);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
//		if (Robot.ds.getMatchTime() < 30) {
//			if (OI.sfController.getRawAxis(RobotMap.XBOX_RTRIGGER) == 1 && OI.sfController.getRawButton(RobotMap.XBOX_RBUMPER)) {
//    			Robot.sfSub.turnServosLeft();
//    		}
//    		else if (OI.sfController.getRawAxis(RobotMap.XBOX_LTRIGGER) == 1 && OI.sfController.getRawButton(RobotMap.XBOX_RBUMPER)) {
//    			Robot.sfSub.turnServosRight();
//    		}
//		}
		if (OI.sfController.getRawAxis(RobotMap.XBOX_RTRIGGER) == 1 && OI.sfController.getRawButton(RobotMap.XBOX_RBUMPER)) {
			Robot.sfSub.turnServosLeft();
		}
		else if (OI.sfController.getRawAxis(RobotMap.XBOX_LTRIGGER) == 1 && OI.sfController.getRawButton(RobotMap.XBOX_RBUMPER)) {
			Robot.sfSub.turnServosRight();
		}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
