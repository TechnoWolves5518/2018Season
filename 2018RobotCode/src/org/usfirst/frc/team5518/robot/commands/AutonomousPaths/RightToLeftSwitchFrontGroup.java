package org.usfirst.frc.team5518.robot.commands.AutonomousPaths;

import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.commands.DriveDistance;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class RightToLeftSwitchFrontGroup extends CommandGroup {

    public RightToLeftSwitchFrontGroup() {
    	Robot.logger.debug("RightToLeftSwitchFrontGroup.java");
    	Robot.logger.debug("Moving forward");
    	addSequential(new DriveDistance(7, 0.1f));
    	//Turn Left
    	addSequential(new DriveDistance(7, 0.1f));
    	//Turn Right
    	addSequential(new DriveDistance(7, 0.1f));
    	//Shoot
    }
}
