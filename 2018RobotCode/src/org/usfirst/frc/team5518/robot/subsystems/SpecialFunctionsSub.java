package org.usfirst.frc.team5518.robot.subsystems;

import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.RobotMap;
import org.usfirst.frc.team5518.robot.commands.autonomous.AutoLauncherCom;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
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
	
	// Pneumatic Components
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
    
	public void initNeutral() {
		doubleSolenoid.set(DoubleSolenoid.Value.kOff); // turn off all signals to solenoid
	}
	public void pForward() {
		doubleSolenoid.set(DoubleSolenoid.Value.kForward); // set solenoid to forward (called in exec of AutoLauncherCom)
	}
	public void pReverse() {
		doubleSolenoid.set(DoubleSolenoid.Value.kReverse); // set solenoid to reverse (called in end of AutoLauncherCom, doesn't actually do anything, not wired)
	}
	
	
	public void intake(double speed, double speed2) {
		// set speed of both motors via the motor controllers
		leftMotor.set(speed);
		rightMotor.set(speed);
		leftSecondaryMotor.set(speed2);
		rightSecondaryMotor.set(speed2);
	}

}

