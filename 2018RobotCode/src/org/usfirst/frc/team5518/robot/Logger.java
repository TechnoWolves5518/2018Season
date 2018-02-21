package org.usfirst.frc.team5518.robot;

public class Logger {
	
	private boolean isDebug = false; //false during competitions
	private boolean isVerbose = false;
	private boolean isInfo = true;
	
	private static Logger mInstance;
	
	public static Logger getInstance() {
		if (mInstance == null)
			mInstance = new Logger();
		
		return mInstance;
	}
	
	public void setDebug(boolean status){
		isDebug = status;
	}
	
	public void info(String s){
		if (isInfo){
			System.out.println(s);
		}
	}
	
	public void debug(String s){
		if (isDebug){
			System.out.println(s);
		}
	}
	
	public void verbose(String s){
		if (isVerbose){
			System.out.println(s);
		}
	}
}
