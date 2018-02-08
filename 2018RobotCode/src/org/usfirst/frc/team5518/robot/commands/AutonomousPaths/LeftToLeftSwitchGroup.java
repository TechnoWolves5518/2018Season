package org.usfirst.frc.team5518.robot.commands.AutonomousPaths;

import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.commands.DriveDistance;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class LeftToLeftSwitchGroup extends CommandGroup {

    public LeftToLeftSwitchGroup() {
    	Robot.logger.debug("LeftToLeftSwitchGroup.java");
    	Robot.logger.debug("Moving forward");
    	addSequential(new DriveDistance(7, 0.1f));
    	//Turn Right
    	addSequential(new DriveDistance(7, 0.2f));
    	//SHOOT
    }
}
