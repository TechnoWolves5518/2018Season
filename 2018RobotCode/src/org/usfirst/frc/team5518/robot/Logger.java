package org.usfirst.frc.team5518.robot;

public class Logger {
	private static boolean isDebug = false; //false during competitions
	private static boolean isVerbose = false;
	private static boolean isInfo = true;
	
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
