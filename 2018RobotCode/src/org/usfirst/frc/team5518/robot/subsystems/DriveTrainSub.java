package org.usfirst.frc.team5518.robot.subsystems;

import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

/**
 *
 */
public class DriveTrainSub extends Subsystem {
	
	private boolean isTestBot = false;
	
	// Construct and define motor controllers
	private WPI_TalonSRX frontLeftTalon = new WPI_TalonSRX(RobotMap.FRONT_LEFT);
	private WPI_TalonSRX backLeftTalon = new WPI_TalonSRX(RobotMap.BACK_LEFT);
	private WPI_TalonSRX frontRightTalon = new WPI_TalonSRX(RobotMap.FRONT_RIGHT);
	private WPI_TalonSRX backRightTalon = new WPI_TalonSRX(RobotMap.BACK_RIGHT);
	
	private VictorSP frontLeftMotor = new VictorSP(RobotMap.FRONT_LEFT);
	private VictorSP backLeftMotor = new VictorSP(RobotMap.BACK_LEFT);
	private VictorSP frontRightMotor = new VictorSP(RobotMap.FRONT_RIGHT);
	private VictorSP backRightMotor = new VictorSP(RobotMap.BACK_RIGHT);
	
	private float expiraton = 0.3f; // Motor Safety expiration period
	
	// Combine all the motor controllers into a drive base
	private MecanumDrive driveBase = new MecanumDrive(frontLeftTalon, backLeftTalon, frontRightTalon, backRightTalon);
	
	public DriveTrainSub() {
    		
		if (!isTestBot) {
			setupTalons(frontLeftTalon);
			setupTalons(backLeftTalon);
			setupTalons(frontRightTalon);
			setupTalons(backRightTalon);
		}
		else {
			setupVictors(frontLeftMotor);
			setupVictors(backLeftMotor);
			setupVictors(frontRightMotor);
			setupVictors(backRightMotor);
		}
		
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(Robot.driveInputCom);
    }
    
    /**
     * Used for tele-operated control of the drive train
     * @author Taha Bokhari
     * @param drive the y speed (forward/backward) (-0.3, 0, 0) <-- Backwards
     * @param strafe the x speed (side to side) (0, 0.3, 0) <-- Right
     * @param rotate the z rotation (rotation) (0, 0, 0.3) <-- Rotate right
     */
    public void drive(double drive, double strafe, double rotate) {
		
    		System.out.println("INPUTS drive  " + drive + "  strafe  " + strafe + "  rotate  " + rotate);
		System.out.println("TALONS FL: " + frontLeftTalon.get() + " BL: " + backLeftTalon.get() + " FR: " + frontRightTalon.get() + " BR: " + backRightTalon.get());
		
		// Use the driveCartesian WPI method, passing in vertical motion, strafing, and tank rotation.
		driveBase.driveCartesian(drive, strafe, rotate);
		
    }
    
    public void stop() {
    		// Stop driving. Failsafe if connection is interrupted or robot code ends.
    		driveBase.driveCartesian(0, 0, 0);
    }
    
    public double quadCurve(double val) {
    		
    		if (val >= 0) { // Apply a quadratic curve to the inputs of the controller (preserving positive/negative values)
    			val *= val;
		} else {
			val *= val;
			val = -val;
		}
    		
		if (Math.abs(val) < 0.1) { // Apply custom deadband directly to inputs
			val = 0;
		}
		
		return val;
    }
    
    public double applySpeedCap(double speed, double cap) {
    		if (speed > cap) {
    			speed = cap;
    		}
    		return speed;
    }
    
    public void setupTalons(WPI_TalonSRX talon) {
    		System.out.println("SETTING UP TALONS");
    		
    		talon.configNeutralDeadband(0.1, 0);
    		talon.setSafetyEnabled(true);
    		talon.setExpiration(expiraton);
    		talon.setInverted(false);
    }
    
    public void setupVictors(VictorSP victor) {
		System.out.println("SETTING UP VICTORS");
		
		victor.enableDeadbandElimination(true);
		victor.setSafetyEnabled(true);
		victor.setExpiration(expiraton);
		victor.setInverted(false);
}
    
}

