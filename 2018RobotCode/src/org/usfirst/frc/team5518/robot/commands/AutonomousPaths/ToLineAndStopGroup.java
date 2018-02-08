package org.usfirst.frc.team5518.robot.commands.AutonomousPaths;

import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.commands.DriveDistance;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ToLineAndStopGroup extends CommandGroup {

    public ToLineAndStopGroup() {
        // e.g. addSequential(new Command1());
    	Robot.logger.debug("To Line And Stop");
    	Robot.logger.debug("Moving forward");
    	addSequential(new DriveDistance(7,0.1f));
    }
}
