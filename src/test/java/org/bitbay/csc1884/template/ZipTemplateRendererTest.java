/**
 * ZipTemplateRendererTest.java
 *
 * @author daniel@bitbay.org
 */

package org.bitbay.csc1884.template;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

/**
 * Unit tests of ZipTemplateRenderer
 */
public class ZipTemplateRendererTest
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
	
	/**
	 * Test default state of ZipTemplateRenderer
	 */
	@Test
	public void defaultState()
	{
		ZipTemplateRenderer ztr = new ZipTemplateRenderer();
		assertNull(ztr.getFolder());
		assertNull(ztr.getPath());
		assertNull(ztr.getArchiveFile());
		assertNull(ztr.getArchivePath());
	}
	
	/**
	 * Test updating ZipTemplateRenderer fields
	 */
	@Test
	public void updateFolderField()
	{
		ZipTemplateRenderer ztr = new ZipTemplateRenderer();
		File file = new File("test");
		assertEquals(file, ztr.setFolder(file).getFolder());
		assertEquals("test", ztr.getPath());
	}
	
	@Test
	public void updatePathField()
	{
		ZipTemplateRenderer ztr = new ZipTemplateRenderer();
		assertEquals("test", ztr.setPath("test").getPath());
		File file = new File("test");
		assertEquals(file, ztr.getFolder());
	}
	
	@Test
	public void updateArchiveFileField()
	{
		ZipTemplateRenderer ztr = new ZipTemplateRenderer();
		File file = new File("test.zip");
		assertEquals(file, ztr.setArchiveFile(file).getArchiveFile());
		assertEquals("test.zip", ztr.getArchivePath());
	}
	
	@Test
	public void updateArchivePathField()
	{
		ZipTemplateRenderer ztr = new ZipTemplateRenderer();
		assertEquals("test.zip", ztr.setArchivePath("test.zip").getArchivePath());
		File file = new File("test.zip");
		assertEquals(file, ztr.getArchiveFile());
	}
	
	/**
	 * Test method renderTemplate
	 */
	
	// Rule for catching thrown exceptions, allowing message inspection
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void nullFolderRenderTemplate() throws RuntimeException
	{
		thrown.expect(RuntimeException.class);
		thrown.expectMessage("Can not render template...No target folder set!");
		ZipTemplateRenderer ztr = new ZipTemplateRenderer();
		ztr.renderTemplate();
	}
	
	@Test
	public void nullArchiveRenderTemplate() throws RuntimeException
	{
		thrown.expect(RuntimeException.class);
		thrown.expectMessage("Can not render template...No zip archive file set!");
		ZipTemplateRenderer ztr = new ZipTemplateRenderer();
		ztr.setPath("test");
		ztr.renderTemplate();
	}
	
	@Test
	public void renderTemplateFromFile()
	{
		ZipTemplateRenderer ztr = new ZipTemplateRenderer();
		ztr.setPath("tmp");
		ztr.setArchivePath("assets/test.zip");
		ztr.renderTemplate();
		assertTrue(file.exists());
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
