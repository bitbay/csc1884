/**
 * GitHubRepositoryManagerTest.java
 *
 * Live tests not performed:
 *	GitHubRepositoryManager.createRepository();
 *	GitHubRepositoryManager.push();
 *
 * @author daniel@bitbay.org
 */

package org.bitbay.csc1884.repository;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.File;

/**
 * Unit tests of GitHubRepositoryManagerRenderer
 */
public class GitHubRepositoryManagerTest
{
	/**
	 * Test default state of GitHubRepositoryManager
	 */
	@Test
	public void defaultState()
	{
		GitHubRepositoryManager rpm = new GitHubRepositoryManager();
		
		assertNull(rpm.getLocalFolder());
		assertNull(rpm.getLocalPath());
		assertNull(rpm.getUser());
		assertNull(rpm.getPass());
		assertFalse(rpm.getServicesInited());
		assertEquals("test-repo", rpm.getRepositoryName());
		
		rpm = new GitHubRepositoryManager("gitrepo");
		assertEquals("gitrepo", rpm.getRepositoryName());
	}

	/**
	 * Test updating blob fields
	 */
	@Test
	public void updateFields()
	{
		GitHubRepositoryManager rpm = new GitHubRepositoryManager();
		File folder = new File("tmp");
		assertEquals("tmp", rpm.setLocalPath("tmp").getLocalPath());
		assertEquals(folder, rpm.setLocalFolder(folder).getLocalFolder());
		assertEquals("user", rpm.setUser("user").getUser());
		assertEquals("pass", rpm.setPass("pass").getPass());
		assertEquals("gitrepo", rpm.setRepositoryName("gitrepo")
			.getRepositoryName());
	}
	
	/**
	 * Test of public methods
	 */
	@Test
	public void initClient()
	{
		GitHubRepositoryManager rpm = new GitHubRepositoryManager();
		rpm.initServices();
		assertTrue(rpm.getServicesInited());
	}
}
