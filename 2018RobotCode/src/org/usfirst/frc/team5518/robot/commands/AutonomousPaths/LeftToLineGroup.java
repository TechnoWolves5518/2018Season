package org.usfirst.frc.team5518.robot.commands.AutonomousPaths;

import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.commands.DriveDistance;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class LeftToLineGroup extends CommandGroup {

    public LeftToLineGroup() {
    	Robot.logger.debug("LeftToLineGroup.java");
    	Robot.logger.debug("Moving forward");
    	addSequential(new DriveDistance(7, 0.1f));
    	//done
    	
    }
}
