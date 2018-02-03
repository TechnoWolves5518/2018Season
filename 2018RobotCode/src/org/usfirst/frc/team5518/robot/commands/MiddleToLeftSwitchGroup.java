package org.usfirst.frc.team5518.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.Logger;


/**
 *
 */
public class MiddleToLeftSwitchGroup extends CommandGroup {

    public MiddleToLeftSwitchGroup() {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    	
    	Robot.logger.debug("Driving forward 1");
    	addSequential(new DriveDistance(12, 0.2f));
//    	Robot.logger.debug("Driving right");
//    	addSequential(new StrafeDistance(48, 0.3f));
    	Robot.logger.debug("Driving forward 2");
    	addSequential(new DriveDistance(24, 0.5f));
    	Robot.logger.debug("Done with Command Group");
    	// add shoot command
    	
    }
}
