package org.usfirst.frc.team5518.robot.commands;

import org.usfirst.frc.team5518.robot.OI;
import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class FastReverseIntakeCom extends Command {
	
	private double intakeAdjustment;
	
	public FastReverseIntakeCom() {
		// Make this subsystem dependent on the special functions subsystem (hint: use Robot.java)
		// eg. requires(chassis);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		intakeAdjustment = 0;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		
		intakeAdjustment = OI.sfController.getRawAxis(RobotMap.XBOX_LSTICKX) * 0.3;
		
		
		// Make the intake run via the method in the subsystem
		Robot.sfSub.intake(-RobotMap.INTAKE_SPEED, -RobotMap.SECONDARY_INTAKE_SPEED, -RobotMap.EXTENDED_INTAKE_SPEED - 0.2, intakeAdjustment);

	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return !OI.sfController.getRawButton(RobotMap.XBOX_RBUMPER);
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.sfSub.intake(0.0, 0.0, 0.0, 0.0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		Robot.sfSub.intake(0.0, 0.0, 0.0, 0.0);
	}
}
