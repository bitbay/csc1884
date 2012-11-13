/**
 * UrlUtils.java
 *
 * URL related functions. For now, just a "download" function.
 *
 * @author daniel@bitbay.org
 */

package org.bitbay.csc1884.utils;

import java.net.*;
import java.io.*;

public class UrlUtils
{
	/**
	 * Downloads a sourceURL file to a destination directory.
	 * Adobe "recipe"
	 * 
	 * @see:
	 * http://cookbooks.adobe.com/post_Download_a_file_from_a_URL_in_Java-17947.html
	 *
	 * @param String	source url to download
	 * @param String	destination folder to download the url to
	 * @return String	the "absolute" path of the downloaded file
	 */
	public static String download(String srcURL, String destDir)
	{
		Logger.coloredLog("\nDownloading " + srcURL + " to " + destDir + "\n",
			Logger.ANSI_YELLOW);
		// TODO: sanitize params srcURL and destDir...
		
		// make sure destination directory ends with "/" for later concating
		if( !destDir.endsWith("/") ) destDir = destDir.concat("/");
		
		// get the filename
		String fileName = srcURL.substring( srcURL.lastIndexOf('/')+1,
			srcURL.length() );
		String output = destDir + fileName;
		
		try
		{
			/*
			 * Get a connection to the URL and start up
			 * a buffered reader.
			 */
			long startTime = System.currentTimeMillis();
			
			URL url = new URL(srcURL);
			
			Logger.log("Connecting to " + url.getHost() + "...\n");
			
			url.openConnection();
			InputStream reader = url.openStream();
			
			/*
			 * Setup a buffered file writer to write
			 * out what we read from the website.
			 */
			FileOutputStream writer = new FileOutputStream(output);
			byte[] buffer = new byte[153600];
			int totalBytesRead = 0;
			int bytesRead = 0;

			Logger.log("Reading ZIP file 150KB blocks at a time.\n");

			while ((bytesRead = reader.read(buffer)) > 0)
			{  
				writer.write(buffer, 0, bytesRead);
				buffer = new byte[153600];
				totalBytesRead += bytesRead;
			}

			long endTime = System.currentTimeMillis();

			Logger.log("Done. " +
				(new Integer(totalBytesRead).toString()) + " bytes read (" +
				(new Long(endTime - startTime).toString()) + " millseconds).");
				
			writer.close();
			reader.close();
		}
		catch (MalformedURLException murle)
		{
			//murle.printStackTrace();
			throw new RuntimeException(murle);
		}
		catch (IOException ioe)
		{
			//ioe.printStackTrace();
			throw new RuntimeException(ioe);
		}

		return output;
	}
}
