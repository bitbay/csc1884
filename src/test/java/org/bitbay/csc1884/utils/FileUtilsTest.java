/**
 * FileUtilsTest.java
 *
 * @author daniel@bitbay.org
 */

package org.bitbay.csc1884.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Unit tests of FileUtilsUtils
 */
public class FileUtilsTest
{
	/**
	 * Test folder creation/removal
	 */
	@Test
	public void folderOperations()
	{
		File file = new File("tmp");
		
		FileUtils.createFolder("tmp");
		assertTrue(file.exists());
		FileUtils.deleteFolder("tmp");
		assertFalse(file.exists());
		
		FileUtils.createFolder(file);
		assertTrue(file.exists());
		FileUtils.deleteFolder(file);
		assertFalse(file.exists());
	}
	
	/**
	 * Test file input/output operations
	 */
	@Test
	public void fileIoOperations()
	{
		File file = new File("test.txt");
		List<String> content = Arrays.asList("first line");
		
		// creating new file
		FileUtils.writeToFile(file, content, false);
		assertTrue(file.exists());
		assertEquals(content, FileUtils.readFileAsStringList(file));
		
		// overwriting file
		content = Arrays.asList("first line", "second line");
		FileUtils.writeToFile(file, content, false);
		assertEquals(content, FileUtils.readFileAsStringList(file));
		
		// appending to existing file
		List<String> newContent = Arrays.asList("new line");
		FileUtils.writeToFile(file, newContent, true);
		
		content = Arrays.asList("first line", "second line", "new line");
		assertEquals(content, FileUtils.readFileAsStringList(file));
		
		file.delete();
	}
	
	/**
	 * Load contents of a file as byte array
	 */
	@Test
	public void loadFileAsByteArray()
	{
		File file = new File("test.txt");
		
		// write data to file
		List<String> aList = Arrays.asList("a");
		FileUtils.writeToFile(file, aList, true);
		
		byte[] bytesLoaded = FileUtils.loadFile(file);
		
		assertEquals(2, bytesLoaded.length);
		// 97 = "a" ascii value
		assertEquals(97, bytesLoaded[0]);
		// 10 = "line feed" ascii value
		assertEquals(10, bytesLoaded[1]);
		
		file.delete();
	}
}
