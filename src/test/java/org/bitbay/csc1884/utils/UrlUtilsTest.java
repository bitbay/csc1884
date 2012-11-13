/**
 * UrlUtilsTest.java
 *
 * @author daniel@bitbay.org
 */

package org.bitbay.csc1884.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;

import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

/**
 * Unit tests of UrlUtilsRenderer
 */
public class UrlUtilsTest
{
	private File folder;
	private String srcUrl =
		"https://s3.amazonaws.com/cs-production/challenges/1884/src.zip";
		
	/**
	 * Define some files we will test against
	 */
	@Before
	public void createTempFolder()
	{
		folder = new File("tmp");
		folder.mkdir();
	}
	
	/**
	 * Testing download source from site
	 */
	@Test
	public void downloadSrcToFile()
	{
		String srcLocal = UrlUtils.download(srcUrl, folder.getPath());
		File file = new File(srcLocal);
		assertTrue(file.exists());
		file.delete();
	}
	
	// Rule for catching thrown exceptions, allowing message inspection
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void wrongSrcDownload() throws RuntimeException
	{
		thrown.expect(RuntimeException.class);
		thrown.expectMessage("unknown protocol: hppt");
		String srcLocal = UrlUtils.download("hppt://fakedomain.org/file.zip",
			folder.getPath());
	}
	
	@Test
	public void wrongFolderDownload() throws RuntimeException
	{
		thrown.expect(RuntimeException.class);
		thrown.expectMessage("fake/src.zip (No such file or directory)");
		String srcLocal = UrlUtils.download(srcUrl,"fake");
	}
	
	/**
	 * Cleanup after tests
	 */
	@After
	public void cleanupTempFolder()
	{
		folder.delete();
	}
}
