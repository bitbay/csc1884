/**
 * FileUtils.java
 *
 * Series of static helper functions to achieve various file/folder operations
 * used in the application
 *
 * @author daniel@bitbay.org
 */

package org.bitbay.csc1884.utils;

import java.io.*;
import java.util.*;

public class FileUtils
{
	/**
	 * Creates a folder
	 *
	 * @param String	the name of the folder to be created
	 * @return File	the file instance of the created folder
	 */
	public static File createFolder( String folderName )
	{
		Logger.coloredLog("Creating folder...\n", Logger.ANSI_YELLOW);
		
		File folder = new File(folderName);
		
		createFolder(folder);
		
		return folder;
	}
	
	/**
	 * Creates a folder
	 *
	 * @param File	the folder to be created
	 * @return File	the file instance of the created folder
	 */
	public static File createFolder(File folder)
	{
		if (!folder.exists())
		{
			if (folder.mkdir())
			{
				Logger.log("Directory created.");
			}
			else
			{
				Logger.coloredLog("Failed to create directory!",
					Logger.ANSI_RED);
			}
		}
		
		return folder;
	}
	
	/**
	 * Deletes an existing folder
	 *
	 * @param String	name of the directory to delete
	 * @return boolean	true, if folder deleted
	 */
	public static boolean deleteFolder(String dir)
	{
		Logger.coloredLog("Removing directory " + dir + "\n",
			Logger.ANSI_YELLOW);
		
		File fileDir = new File(dir);
		boolean result = false;
		
		if (fileDir.isDirectory())
		{
			result = deleteRecursively( fileDir );
		}
		
		return result;
	}
	
	/**
	 * Deletes an existing folder
	 *
	 * @param File	the directory to delete
	 * @return boolean	true, if folder deleted
	 */
	public static boolean deleteFolder(File file)
	{
		Logger.coloredLog("\nRemoving directory " + file.getName() + "\n",
			Logger.ANSI_YELLOW);
		
		boolean result = false;
		
		if (file.isDirectory())
		{
			result = deleteRecursively( file );
		}
		
		return result;
	}
	
	/**
	 * Deletes a file or recursively deletes a folder
	 *
	 * @param File	the file to delete
	 * @return boolean	true if successfully deleted
	 */
	private static boolean deleteRecursively(final File file)
	{
	    if (file.exists())
	    {
	        final File[] files = file.listFiles();
    	    for (int i = 0; i < files.length; i++)
    	    {
    	        if (files[i].isDirectory())
    	        {
    	            deleteRecursively(files[i]);
    	        }
    	        else
    	        {
    	        	Logger.log("Deleting: " + files[i]);
    	            files[i].delete();
    	        }
    	    }
	    }
    	return (file.delete());
	}
	
	/**
	 * Reads file to list of strings
	 *
	 * @see http://www.java2s.com/Tutorial/Java/0180__File/ReadLinesreadfiletolistofstrings.htm
	 *
	 * @param File	the file to read
	 * @return List<String>	list of lines read from file
	 */
	public static List<String> readFileAsStringList(File file)
		throws RuntimeException
	{
		if (!file.exists()) {
			return new ArrayList<String>();
		}
		List<String> results = new ArrayList<String>();
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
		
			String line = reader.readLine();
			while (line != null)
			{
				results.add(line);
				line = reader.readLine();
			}
			reader.close();
		}
		catch( IOException ioe )
		{
			throw new RuntimeException(ioe);
		}
		
		return results;
	}
	
	/**
	 * Writes collection of String to file
	 *
	 * @param File	the file to create
	 * @param List<String>	content to write
	 * @param boolean	if true, append to the end of file, if false, create new
	 *	file
	 */
	public static File writeToFile(File file, List<String> content,
		boolean append) throws RuntimeException
	{
		try
		{
            PrintWriter pw = new PrintWriter(new FileWriter(file, append));
            
            // for every string in list
            for( String s : content )
            {
            	if( s != null )
            	{
            		pw.println(s);
            	}
            }
            
            pw.close();
            return file;
        }
        catch (IOException ioe)
        {
            // "Could not write to file"
            throw new RuntimeException(ioe);
        }
	}
	
	/**
	 * Loads File content into a byteArray
	 *
	 * @param File	file to load data from
	 * @return byte[]	contents of the file
	 */
	public static byte[] loadFile(File file) throws RuntimeException
	{
		byte[] bytes = null;
		
		try
		{
			InputStream is = new FileInputStream(file);

			long length = file.length();
			if (length > Integer.MAX_VALUE)
			{
			    // File is too large
			    throw new RuntimeException("File is too large "+file.getName());
			}
			bytes = new byte[(int)length];

			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length
			       && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0)
			{
			    offset += numRead;
			}

			if (offset < bytes.length)
			{
			    throw new RuntimeException("Could not completely read file " +
			    	file.getName());
			}
			
			is.close();
		}
		catch( IOException ioe )
		{
			throw new RuntimeException(ioe);
		}
		
	    return bytes;
	}
}
