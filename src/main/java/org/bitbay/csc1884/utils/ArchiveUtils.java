/**
 * ArchiveUtils.java
 *
 * Class with static functions to handle archived file decompression(/compression)
 * Compressing is not implemented, just zip archive decompression.
 *
 * @author daniel@bitbay.org
 */
 
package org.bitbay.csc1884.utils;

import java.io.*;
import java.util.zip.*;
import java.lang.RuntimeException;

public class ArchiveUtils
{
	// size of the buffer used in the process
	private static int BUFFER_SIZE = 2048;
	
	/**
	 * Decompress Zip file to a directory, optionally deleting the zip file.
	 *
	 * Oracle "recipe" with a touch of existing file/directory handling
	 *
	 * @see:
	 * http://www.oracle.com/technetwork/articles/java/compress-1565076.html
	 * http://android-er.blogspot.com.es/2011/04/unzip-compressed-file-using-javautilzip.html
	 */
	public static void decompressZip(String zipFile, String destDir,
		boolean deleteZip) throws RuntimeException
	{
		Logger.coloredLog("\nDecompressing " + zipFile + " to " + destDir +
			"\n", Logger.ANSI_YELLOW);
		
		// make sure destination directory ends with "/" for later concating
		if( !destDir.endsWith("/") ) destDir = destDir.concat("/");
		
		try
		{
			BufferedOutputStream bos = null;
			FileInputStream fis = new FileInputStream(zipFile);
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
			ZipEntry zipEntry;
			
			// read all entries (files/directories) from zip file
			while((zipEntry = zis.getNextEntry()) != null)
			{
				String zipEntryName = zipEntry.getName();
				File file = new File(destDir + zipEntryName);
				
				// if file already exists...
				if( file.exists())
				{
					// ...do nothing
					Logger.coloredLog("Skipping: " + zipEntry +
						", already exists", Logger.ANSI_CYAN);
				}
				else
				{
					// if file is a directory...
					if( zipEntry.isDirectory()){
						// ...create it
						file.mkdirs();
					}
					else
					{
						// file IS a file, extract...
						Logger.log("Extracting: " + zipEntry);
						
						byte data[] = new byte[BUFFER_SIZE];
						FileOutputStream fos = new FileOutputStream(file);
						bos = new BufferedOutputStream(fos, BUFFER_SIZE);
						int count;
						
						while((count = zis.read(data, 0, BUFFER_SIZE)) != -1)
						{
							bos.write(data, 0, count);
						}
						
						bos.flush();
						bos.close();
					}
				}
			}
			zis.close();
		}
		catch (FileNotFoundException ffe)
		{
			throw new RuntimeException(ffe);
		}
		catch (IOException ioe)
		{
			throw new RuntimeException(ioe);
		}
		
		// optionally delete the zip archive
		if(deleteZip)
		{
			File zip = new File(zipFile);
			if( zip.exists() )
			{
				zip.delete();
			}
		}
	}
}
