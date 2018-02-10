package org.usfirst.frc.team5518.robot.subsystems;

import org.usfirst.frc.team5518.robot.OI;
import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

/**
 *
 */
public class DriveTrainSub extends Subsystem {
	
	// Construct and define motor controllers
//	private VictorSP frontLeftMotor = new VictorSP(RobotMap.FRONT_LEFT);
//	private VictorSP backLeftMotor = new VictorSP(RobotMap.BACK_LEFT);
//	private VictorSP frontRightMotor = new VictorSP(RobotMap.FRONT_RIGHT);
//	private VictorSP backRightMotor = new VictorSP(RobotMap.BACK_RIGHT);
	
	private WPI_TalonSRX frontLeftTalon = new WPI_TalonSRX(RobotMap.FRONT_LEFT);
	private WPI_TalonSRX backLeftTalon = new WPI_TalonSRX(RobotMap.BACK_LEFT);
	private WPI_TalonSRX frontRightTalon = new WPI_TalonSRX(RobotMap.FRONT_RIGHT);
	private WPI_TalonSRX backRightTalon = new WPI_TalonSRX(RobotMap.BACK_RIGHT);
	
	private float expiraton = 0.3f;
	
	// Combine all the motor controllers into a drive base
	// private MecanumDrive driveBase = new MecanumDrive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);
	private MecanumDrive driveBase = new MecanumDrive(frontLeftTalon, backLeftTalon, frontRightTalon, backRightTalon);
	
	public DriveTrainSub() {
		
		// setupTalons();
		
		System.out.println("SETTING UP TALONS");
    	
		frontLeftTalon.configNeutralDeadband(0.1, 0);
		backLeftTalon.configNeutralDeadband(0.1, 0);
		frontRightTalon.configNeutralDeadband(0.1, 0);
		backRightTalon.configNeutralDeadband(0.1, 0);
		
		// enable the safety
		frontLeftTalon.setSafetyEnabled(true);
		frontLeftTalon.setExpiration(expiraton);
		backLeftTalon.setSafetyEnabled(true);
		backLeftTalon.setExpiration(expiraton);
		frontRightTalon.setSafetyEnabled(true);
		frontRightTalon.setExpiration(expiraton);
		backRightTalon.setSafetyEnabled(true);
		backRightTalon.setExpiration(expiraton);
		
		frontLeftTalon.setInverted(false);
		backLeftTalon.setInverted(false);
		frontRightTalon.setInverted(false);
		backRightTalon.setInverted(false);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(Robot.driveInputCom);
    }
    
    /**
     * A cool Taha method.
     * @author Taha Bokhari
     * @param drive the y speed (forward/backward) (-0.3, 0, 0) <-- Backwards
     * @param strafe the x speed (side to side) (0, 0.3, 0) <-- Right
     * @param rotate the z rotation (rotation) (0, 0, 0.3) <-- Rotate right
     */
    public void drive(double drive, double strafe, double rotate) {
		// System.out.println("INPUTS drive  " + drive + "  strafe  " + strafe + "  rotate  " + rotate);
		// System.out.println("TALONS FL: " + frontLeftTalon.get() + " BL: " + backLeftTalon.get() + " FR: " + frontRightTalon.get() + " BR: " + backRightTalon.get());
		System.out.println("drive input:  " + OI.driveController.getRawAxis(1));
		driveBase.driveCartesian(drive, strafe, rotate);
		// Use the driveCartesian WPI method, passing in vertical motion, strafing, and tank rotation.
    }
    
    public void stop() {
		driveBase.driveCartesian(0, 0, 0);
		// Stop driving. Failsafe if connection is interrupted or robot code ends.
    }
    
//    public void setupVictors() {
//    		
//    		// enable deadband elimination
//		frontLeftMotor.enableDeadbandElimination(true);
//		backLeftMotor.enableDeadbandElimination(true);
//		frontRightMotor.enableDeadbandElimination(true);
//		backRightMotor.enableDeadbandElimination(true);
//		
//		// enable the safety
//		frontLeftMotor.setSafetyEnabled(true);
//		frontLeftMotor.setExpiration(expiraton);
//		backLeftMotor.setSafetyEnabled(true);
//		backLeftMotor.setExpiration(expiraton);
//		frontRightMotor.setSafetyEnabled(true);
//		frontRightMotor.setExpiration(expiraton);
//		backRightMotor.setSafetyEnabled(true);
//		backRightMotor.setExpiration(expiraton);
//    }
    
    public void setupTalons() {
    	
    	System.out.println("SETTING UP TALONS");
    	
		frontLeftTalon.configNeutralDeadband(0.1, 0);
		backLeftTalon.configNeutralDeadband(0.1, 0);
		frontRightTalon.configNeutralDeadband(0.1, 0);
		backRightTalon.configNeutralDeadband(0.1, 0);
		
		// enable the safety
		frontLeftTalon.setSafetyEnabled(true);
		frontLeftTalon.setExpiration(expiraton);
		backLeftTalon.setSafetyEnabled(true);
		backLeftTalon.setExpiration(expiraton);
		frontRightTalon.setSafetyEnabled(true);
		frontRightTalon.setExpiration(expiraton);
		backRightTalon.setSafetyEnabled(true);
		backRightTalon.setExpiration(expiraton);
    	
//		frontLeftTalon.setInverted(true);
//		backLeftTalon.setInverted(true);
//		frontRightTalon.setInverted(true);
//		backRightTalon.setInverted(true);
    }
    
    public double quadCurve(double val) {
    	if (val >= 0) {
    		val *= val;
		}	
		else {
			val *= val;
			val = -val;
		}
		if (Math.abs(val) < 0.1) {
			val = 0;
		}
		return val;
    }
}

