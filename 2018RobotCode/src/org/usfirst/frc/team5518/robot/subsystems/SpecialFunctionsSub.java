package org.usfirst.frc.team5518.robot.subsystems;

import org.usfirst.frc.team5518.robot.RobotMap;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * [INSERT PURPOSE HERE: WHAT DOES THE CLASS DO & ACHIEVE?]
 *
 * @author [INSERT NAME OF WHOEVER WROTE THIS CODE]
 */
public class SpecialFunctionsSub extends Subsystem {
	
	private VictorSP leftMotor = new VictorSP(RobotMap.LEFT_INTAKE);
	private VictorSP rightMotor = new VictorSP(RobotMap.RIGHT_INTAKE);
	
	private Compressor compressor = new Compressor(RobotMap.COMPRESSOR);
	private DoubleSolenoid doubleSolenoid = new DoubleSolenoid(RobotMap.DS_FORWARD, RobotMap.DS_BACKWARD);
	
	public SpecialFunctionsSub() {
		
		compressor.setClosedLoopControl(true); // refill compressor automatically
		compressor.start(); // turn compressor on
		
		// enable safety on motor controllers
		leftMotor.setSafetyEnabled(true);
		rightMotor.setSafetyEnabled(true);
		
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
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

