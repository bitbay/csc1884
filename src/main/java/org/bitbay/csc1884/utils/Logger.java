/**
 * Logger.java
 *
 * Simple System.out logger with colored output support.
 *
 * @author daniel@bitbay.org
 */
package org.bitbay.csc1884.utils;

public class Logger
{
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	
	// use this to turn logging off
	private static boolean LOG = true;
	
	/**
	 * Log a message to the system console.
	 *
	 * @param String	the message to log
	 */
	public static void log(String message)
	{
		if( LOG )
		{
			System.out.println(message);
		}
	}
	
	/**
	 * Log a message to the systems console with selected color
	 *
	 * @param String	the message to log
	 * @param String	the ansi-color of the message
	 */
	public static void coloredLog(String message, String color)
	{
		if( LOG )
		{
			System.out.println( color + message + "\u001B[0m");
		}
	}
}
