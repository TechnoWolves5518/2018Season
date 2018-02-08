package org.usfirst.frc.team5518.robot.subsystems;

import org.usfirst.frc.team5518.robot.RobotMap;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * The Special Functions subsystem serves to operate pneumatics
 * on the robot for launching the power cube either on a scale or
 * a switch as well as intaking a power cube for loading it onto
 * the robot.
 *
 * @author Armaan Syed
 * @author Taha Bokhari
 */
public class SpecialFunctionsSub extends Subsystem {
	
	/** The motor controllers for intaking a powercube */
	private VictorSP leftMotor;
	private VictorSP rightMotor;
	
	/** Pneumatic components */
	private Compressor compressor;
	private DoubleSolenoid doubleSolenoid;
	
	public SpecialFunctionsSub() {
		// init components
		leftMotor = new VictorSP(RobotMap.LEFT_INTAKE);
		rightMotor = new VictorSP(RobotMap.RIGHT_INTAKE);
		compressor = new Compressor(RobotMap.COMPRESSOR);
		doubleSolenoid = new DoubleSolenoid(RobotMap.DS_FORWARD, RobotMap.DS_BACKWARD);
		
		//compressor.setClosedLoopControl(true); // refill compressor automatically
		compressor.start(); // turn compressor on
		
		// enable safety on motor controllers
		leftMotor.setSafetyEnabled(true);
		rightMotor.setSafetyEnabled(true);
		
	}
	
	/**
	 * Set a default command for the subsystem
	 */
    public void initDefaultCommand() {
        // No need to set default command
        //setDefaultCommand(new MySpecialCommand());
    }
    
	public void shootSwitch() {
		
		doubleSolenoid.set(DoubleSolenoid.Value.kForward); // extend all the cylinders via solenoids
		Timer.delay(.06); // delay thread to allow time for cylinders to half extend (for switch)
		doubleSolenoid.set(DoubleSolenoid.Value.kReverse); // retract all the cylinders via solenoids
		
	}
	
	public void shootScale() {
		
		doubleSolenoid.set(DoubleSolenoid.Value.kForward); // extend all the cylinders via solenoids
		Timer.delay(.15); // delay thread to allow time for cylinders to fully extend
		doubleSolenoid.set(DoubleSolenoid.Value.kReverse); // retract all the cylinders via solenoids

	}
	
	public void intake(double speed) {
		// set speed of both motors via the motor controllers
		leftMotor.set(speed);
		rightMotor.set(speed);
	}

}

