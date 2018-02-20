package org.usfirst.frc.team5518.robot.subsystems;

import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.RobotMap;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Servo;
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
	
	private Servo leftWingServo;
	private Servo rightWingServo;
	
	// Pneumatic Components
	private Compressor compressor;
	private DoubleSolenoid doubleSolenoid;
	
	public SpecialFunctionsSub() {
		// init components
		
		leftWingServo = new Servo(RobotMap.LEFT_SERVO);
		rightWingServo = new Servo(RobotMap.RIGHT_SERVO);

		
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
		
	}
	
	/**
	 * Set a default command for the subsystem
	 */
    public void initDefaultCommand() {
        // No need to set default command
        setDefaultCommand(Robot.deployWingsCom);
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
	
	public void turnServosLeft() {
		leftWingServo.set(0);
		rightWingServo.set(1);
	}
	
	public void turnServosRight() {
		leftWingServo.set(1);
		rightWingServo.set(0);
	}
	
	public void intake(double speed, double speed2) {
		// set speed of both motors via the motor controllers
		leftMotor.set(speed);
		rightMotor.set(speed);
		leftSecondaryMotor.set(speed2);
		rightSecondaryMotor.set(speed2);
	}

}

