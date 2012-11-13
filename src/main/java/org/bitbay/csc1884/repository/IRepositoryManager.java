/**
 * IRepositoryManager.java
 *
 * Interface for RepositoryManager implementations.
 * A RepositoryManager should be able to:
 *	- track a 'localFolder' for files,
 *	- use 'user' and 'pass' for service authentifications
 *	- create a repository 'createRepository'
 *	- 'push' the tracked files to a remote repository service
 *
 * Designed as fluent interface
 * @see http://en.wikipedia.org/wiki/Fluent_interface
 *
 * @author daniel@bitbay.org
 */
package org.bitbay.csc1884.repository;

import java.io.File;
import java.lang.Exception;
import java.lang.RuntimeException;

public interface IRepositoryManager
{
	// localFolder field getter/setter
	public IRepositoryManager setLocalFolder(File file);
	public File getLocalFolder();
	
	// repositoryName field getter/setter
	public IRepositoryManager setRepositoryName(String string);
	public String getRepositoryName();
	
	// user field getter/setter
	public IRepositoryManager setUser(String string);
	public String getUser();
	
	// pass field getter/setter
	public IRepositoryManager setPass(String string);
	public String getPass();
	
	// public methods
	public boolean createRepository() throws Exception;
	public void push() throws Exception, RuntimeException;
}
