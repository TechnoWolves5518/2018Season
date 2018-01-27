package org.usfirst.frc.team5518.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * [INSERT PURPOSE HERE: WHAT DOES THE CLASS DO & ACHIEVE?]
 *
 * @author [INSERT NAME OF WHOEVER WROTE THIS CODE]
 */
public class SpecialFunctionsSub extends Subsystem {
	
	/** Define 2 motor controllers for intake */
	
	/** Define compressor object */

	/** Define 3 (double) solenoids for launcher */
	
	/**
	 * Constructor for initializing components
	 */
	public SpecialFunctionsSub() {
		// init components
		
		// enable safety on motor controllers
		
		// turn compressor on
		
	}
	
	/**
	 * Shoot the power cube at a target. This method will be called
	 * upon a button press on the joystick and will also be used
	 * to fire and immediately retract the pneumatic cylinders.
	 */
	public void shoot() {
		// extend all the cylinders via solenoids
		
		// delay thread to allow time for cylinders to fully extend
		
		// retract all the cylinders via solenoids
	}
	
	/**
	 * Power the two intake motors to get power cubes onto
	 * the roobit. This method will be called while a button on
	 * the joystick is held down.
	 * 
	 * @param speed Set the speed of the intake motor controllers
	 * with the speed ranging from -1 and 1. Set positive values to
	 * enable the motors to spin 'forwards' and negative values to
	 * enable the motors to spin 'backwards.'
	 */
	public void intake(double speed) {
		// set speed of both motors via the motor controllers
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

