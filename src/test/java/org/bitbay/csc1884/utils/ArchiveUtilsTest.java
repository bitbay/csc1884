/**
 * ArchiveUtilsTest.java
 *
 * @author daniel@bitbay.org
 */

package org.bitbay.csc1884.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertTrue;

import java.io.File;

/**
 * Unit tests of ArchiveUtils
 */
public class ArchiveUtilsTest
{
	private File folder;
	private File file;
	
	/**
	 * Define some files we will test against
	 */
	@Before
	public void createTempFolder()
	{
		folder = new File("tmp");
		folder.mkdir();
		file = new File("tmp/file.txt");
	}
	
	@Test
	public void testUnZipArchive()
	{
		ArchiveUtils.decompressZip("assets/test.zip", "tmp", false);
		assertTrue(file.exists());
	}
	
	/**
	 * Testing invalid calls
	 */
	 
	// Rule for catching thrown exceptions, allowing message inspection
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testNonExistentArchiveUnzip()
	{
		thrown.expect(RuntimeException.class);
		thrown.expectMessage("assets/fake.zip (No such file or directory)");
		ArchiveUtils.decompressZip("assets/fake.zip", "tmp", false);
	}
	
	@Test
	public void testNullArchiveUnzip()
	{
		thrown.expect(NullPointerException.class);
		ArchiveUtils.decompressZip(null, "tmp", false);
	}
	
	@Test
	public void testNullFolderUnzip()
	{
		thrown.expect(NullPointerException.class);
		ArchiveUtils.decompressZip("assets/test.zip", null, false);
	}
	
	/**
	 * Cleanup after tests
	 */
	@After
	public void cleanupTempFolder()
	{
		file.delete();
		folder.delete();
	}
}
