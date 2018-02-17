package org.usfirst.frc.team5518.robot.subsystems;

import org.usfirst.frc.team5518.robot.Robot;
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

	private VictorSP leftSecondaryMotor;
	private VictorSP rightSecondaryMotor;
	
	/** Pneumatic components */
	private Compressor compressor;
	private DoubleSolenoid doubleSolenoid;
	
	private double start;
	private double time;
	
	public SpecialFunctionsSub() {
		// init components
		leftMotor = new VictorSP(RobotMap.LEFT_INTAKE);
		rightMotor = new VictorSP(RobotMap.RIGHT_INTAKE);
		
		leftSecondaryMotor = new VictorSP(RobotMap.LEFT_SECONDARY_INTAKE);
		rightSecondaryMotor = new VictorSP(RobotMap.RIGHT_SECONDARY_INTAKE);
		
		compressor = new Compressor(RobotMap.COMPRESSOR);
		doubleSolenoid = new DoubleSolenoid(RobotMap.DS_FORWARD, RobotMap.DS_BACKWARD);
		
		compressor.setClosedLoopControl(true); // refill compressor automatically
		compressor.start(); // turn compressor on
		
		leftMotor.setInverted(true);
		leftSecondaryMotor.setInverted(true);
		
		// enable safety on motor controllers
		leftMotor.setSafetyEnabled(false);
		rightMotor.setSafetyEnabled(false);
		
		time = 0;
	}
	
	/**
	 * Set a default command for the subsystem
	 */
    public void initDefaultCommand() {
        // No need to set default command
        //setDefaultCommand(new MySpecialCommand());
    }
    
	public void shootSwitch() {
		Robot.logger.debug("SHOOT SWITCH");
		time = System.currentTimeMillis() + RobotMap.SWITCH_DELAY;
		while (System.currentTimeMillis() <= time) {
			doubleSolenoid.set(DoubleSolenoid.Value.kForward); // extend all the cylinders via solenoids
			Robot.logger.debug("SHOOT SWITCH");
		}
		Robot.logger.debug("Exited shoot switch loop");
		doubleSolenoid.set(DoubleSolenoid.Value.kReverse); // retract all the cylinders via solenoids (or NULL)
	}
	
	public void shootScale() {
		System.out.println("SHOOT SCALE");
		time = System.currentTimeMillis() + RobotMap.SCALE_DELAY;
		while (System.currentTimeMillis() <= time) {
			doubleSolenoid.set(DoubleSolenoid.Value.kForward); // extend all the cylinders via solenoids
			System.out.println("SHOOT SCALE");
		}
		doubleSolenoid.set(DoubleSolenoid.Value.kReverse); // retract all the cylinders via solenoids (or NULL)
	}
	
	public void initNeutral() {
		doubleSolenoid.set(DoubleSolenoid.Value.kOff);
	}
	public void pForward() {
		doubleSolenoid.set(DoubleSolenoid.Value.kForward);
	}
	public void pReverse() {
		doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
	}
	
	public void intake(double speed, double speed2) {
		// set speed of both motors via the motor controllers
		leftMotor.set(speed);
		rightMotor.set(speed);
		leftSecondaryMotor.set(speed2);
		rightSecondaryMotor.set(speed2);
	}

}

