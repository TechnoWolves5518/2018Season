package org.usfirst.frc.team5518.robot.subsystems;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class AutoDriveSub extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	public static final double kDistancePerRevolution = 8 * Math.PI;
    public static final double kPulsesPerRevolution = 1440;
    public static final double kDistancePerPulse = kDistancePerRevolution / kPulsesPerRevolution;
	
    private Encoder leftEncoder = new Encoder(1, 2, false, EncodingType.k4X);
    private Encoder rightEncoder = new Encoder(3, 4, true, EncodingType.k4X);
    
	public AutoDriveSub() {
		leftEncoder.setDistancePerPulse(kDistancePerPulse);
        rightEncoder.setDistancePerPulse(kDistancePerPulse);
        
        leftEncoder.setMaxPeriod(0.1);
        leftEncoder.setMinRate(10);
        
        
        
        leftEncoder.reset();
        rightEncoder.reset();
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void autoDrive(float dist) {
    		
    }
    
}

