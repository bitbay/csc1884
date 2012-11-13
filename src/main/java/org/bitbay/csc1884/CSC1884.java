/**
 * CSC1884.java
 *
 * A configurable class to automate repository creation with a template and a
 * downloaded file.
 *
 * Tasks:
 *
 * 1. create initial directory structure
 * 2. download a zip file
 * 3. unzip the contents of the downloaded archive
 * 4. render the template using the provided templateRenderer
 * 5. create the git repository on github
 * 6. commit the local files to a local repo
 * 7. push local repo to the remote master
 * 8. cleanup the working directory
 *
 * Exceptions throughout the application are "unchecked exceptions" of type
 * RuntimeExceptions. It's up to the user to implement more sophisticated ones.
 * Based on O'Reilly best practices for exception handling
 * @see http://onjava.com/pub/a/onjava/2003/11/19/exceptions.html
 *
 * @author daniel@bitbay.org
 */
 
package org.bitbay.csc1884;

import java.net.*;
import java.io.*;
import java.util.*;
import org.bitbay.csc1884.utils.*;
import org.bitbay.csc1884.repository.*;
import org.bitbay.csc1884.template.ITemplateRenderer;

public class CSC1884
{
	// Temporary local folder where contents of the to-be-created repository
	// get downloaded and unzipped
	private File workingFolder;
	private String workingFolderPath = "./tmp";
	
	// URL of the source to be downloaded into the temporary folder
	private String srcUrl =
		"https://s3.amazonaws.com/cs-production/challenges/1884/src.zip";
	
	// base name for the repository
	private String repositoryName = "csc1884-repo";
		
	// templateRenderer
	private ITemplateRenderer templateRenderer;
	
	// List of files/folders to add to .gitignore
	private List<String> ignoreList = new ArrayList<String>(
		Arrays.asList(".DS_Store"));
	
	/**
	 * Getter for workingFolder
	 * @return File
	 */
	public File getWorkingFolder()
	{
		return workingFolder;
	}

	/**
	 * Setter for workingFolder
	 * @param File
	 */
	public void setWorkingFolder(File workingFolder)
	{
		this.workingFolder = workingFolder;
		this.workingFolderPath = workingFolder.getPath();
	}
	
	/**
	 * Getter for workingFolderPath
	 * @return String
	 */
	public String getWorkingFolderPath()
	{
		return workingFolderPath;
	}

	/**
	 * Setter for workingFolderPath
	 * @param String
	 */
	public void setWorkingFolderPath(String workingFolderPath)
	{
		this.workingFolderPath = workingFolderPath;
		this.workingFolder = new File(workingFolderPath);
	}
	
	/**
	 * Getter for srcUrl
	 * @return String
	 */
	public String getSrcUrl()
	{
		return srcUrl;
	}

	/**
	 * Setter for srcUrl
	 * @param String
	 */
	public void setSrcUrl(String srcUrl)
	{
		this.srcUrl = srcUrl;
	}
	
	/**
	 * Getter for repositoryName
	 * @return String
	 */
	public String getRepositoryName()
	{
		return repositoryName;
	}

	/**
	 * Setter for repositoryName
	 * @param String
	 */
	public void setRepositoryName(String repositoryName)
	{
		this.repositoryName = repositoryName;
	}
	
	/**
	 * Getter for templateRenderer
	 * @return ITemplateRenderer
	 */
	public ITemplateRenderer getTemplateRenderer()
	{
		return templateRenderer;
	}
	/**
	 * Setter for templateRenderer
	 * @param ITemplateRenderer
	 */
	public void setTemplateRenderer(ITemplateRenderer templateRenderer)
	{
		this.templateRenderer = templateRenderer;
	}
	
	/**
	 * Getter for ignoreList
	 * @return List<String>
	 */
	public List<String> getIgnoreList()
	{
		return ignoreList;
	}
	
	/**
	 * Setter for ignoreList
	 * @param List<String>
	 */
	public void setIgnoreList(List<String> ignoreList)
	{
		this.ignoreList = ignoreList;
	}
	
	/**
	 * Add file name to ignoreList
	 * @param String
	 */
	public void addFileNameToIgnoreList( String fileName )
	{
		if( !ignoreList.contains(fileName ) )
			ignoreList.add(fileName);
	}
	
	
	/**
	 * Starts the process chain
	 *
	 * @see class header above for details.
	 */
	public void execute()
	{
		// the chain is separated in individual functions for testability
		
		createWorkingFolder();
		
		deploySourceFile();
		
		/**
		 * TODO: change to dynamically created content
		 *  (as in challenge description)
		 */
		// for now, use IArchiveTemplateRenderer
		this.templateRenderer.renderTemplate();
		
		createGitIgnoreFile();
		
		pushToGitHub();
		
		cleanupWorkingFolder();
	}
	
	/**
	 * Create the working folder (later deleted)
	 */
	private void createWorkingFolder() throws RuntimeException
	{
		if( this.workingFolder == null &&
			(this.workingFolderPath == null ||
				this.workingFolderPath.equals("")))
			throw new RuntimeException("Class needs a valid working folder!");
		
		if( this.workingFolder == null )
		{
			this.workingFolder = new File(this.workingFolderPath);
		}
		else
		{
			// sanitize working folder. If it exists and is a directory, delete 
			// it. If it is a file, refuse to continue.
			if( this.workingFolder.exists() )
			{
				if(this.workingFolder.isFile())
				{
					throw new
						RuntimeException("Working folder exists, and is a file!");
				}
				if(this.workingFolder.isDirectory())
				{
					Logger.log("Deleting existing working directory.");
					FileUtils.deleteFolder(this.workingFolder);
				}
			}
		}
		
		// create temporary directory for the local repository...
		this.workingFolder = FileUtils.createFolder(this.workingFolder);
	}
	
	/**
	 * Downloads and deploys the sourceFile
	 */
	private void deploySourceFile()
	{
		// download the source file...
		String downloadedFile = 
			UrlUtils.download( srcUrl, this.workingFolderPath );
		
		// unzip content to src...
		ArchiveUtils.decompressZip(downloadedFile,
			this.workingFolderPath, true);
	}
	
	/**
	 * Creates a .gitignore file into the working directory
	 */
	private void createGitIgnoreFile()
	{
		/**
		 * create .gitignore file as in 
	 	 * https://github.com/cloudspokes/jenkins-test/blob/master/.gitignore
	 	 * if no filenames are give, creates an empty one (if .gitignore file
	 	 * does not already exists). If .gitfile present, appends to the end.
	 	 */
		File gitIgnore = new File(this.workingFolderPath.concat("/.gitignore"));
		FileUtils.writeToFile(gitIgnore, ignoreList, true);
	}
	
	/**
	 * Creates a github repository and pushes the files in the working folder
	 * into it (respecting -with some restrictions- the .gitignore file)
	 *
	 * If a repository already exists with the desired name, creates a new
	 * commit, maintaining push/commit history.
	 */
	private void pushToGitHub() throws RuntimeException
	{
		// only GitHub implemented in this application
		IRepositoryManager rpm = new GitHubRepositoryManager(repositoryName);
		rpm.setUser( System.getenv("GH_USER") );
		rpm.setPass( System.getenv("GH_PASS") );
		try
		{
			// create a git repo on github or bitbucket...
			rpm.createRepository();
			
			// add folder to track
			rpm.setLocalFolder(this.workingFolder);
			
			// push the files to the repo...
			rpm.push();
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Deletes the working folder
	 */
	private void cleanupWorkingFolder()
	{
		// cleanup temporary folder...
		FileUtils.deleteFolder(this.workingFolder);
	}
}
