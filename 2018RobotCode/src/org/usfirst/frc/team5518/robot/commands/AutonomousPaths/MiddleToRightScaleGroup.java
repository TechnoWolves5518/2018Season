package org.usfirst.frc.team5518.robot.commands.AutonomousPaths;

import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.commands.DriveDistance;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class MiddleToRightScaleGroup extends CommandGroup {

    public MiddleToRightScaleGroup() {
    	Robot.logger.debug("MiddleToRightScaleGroup.java");
    	Robot.logger.debug("Driving forward 1");
    	addSequential(new DriveDistance(12, 0.1f));
    	Robot.logger.debug("Strafe Right");
    	//Strafe right
    	Robot.logger.debug("strafe again");
    	addSequential(new DriveDistance(12, 0.1f));
    	//rotate
    	//shoot
    }
}
