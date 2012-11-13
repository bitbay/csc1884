/**
 * ZipTemplateRenderer.java
 *
 * Iplementation of an archive based template renderer.
 *
 * The class render method extracts a given archive into a given directory.
 *
 * @author daniel@bitbay.org
 */
package org.bitbay.csc1884.template;

import org.bitbay.csc1884.utils.*;

import java.io.File;
import java.lang.RuntimeException;

public class ZipTemplateRenderer implements IArchiveTemplateRenderer
{
	// inherited from ITemplateRenderer
	private File folder;
	private String path;
	
	// inherited from IArchiveTemplateRenderer
	private File archiveFile;
	private String archivePath;
	
	/**
	 * Setter for folder
	 * @param File
	 * @return this ZipTemplateRenderer
	 */
	public ZipTemplateRenderer setFolder(File folder)
	{
		this.folder = folder;
		this.path = folder.getPath();
		return this;
	}
	
	/**
	 * Getter for folder
	 * @return File
	 */
	public File getFolder()
	{
		return this.folder;
	}
	
	/**
	 * Setter for path
	 * @param String
	 * @return this ZipTemplateRenderer
	 */
	public ZipTemplateRenderer setPath(String path)
	{
		this.path = path;
		this.folder = new File(path);
		return this;
	}
	
	/**
	 * Getter for path
	 * @return String
	 */
	public String getPath()
	{
		return this.path;
	}
	
	/**
	 * Setter for archiveFile
	 * @param File
	 * @return this ZipTemplateRenderer
	 */
	public ZipTemplateRenderer setArchiveFile(File archiveFile)
	{
		this.archiveFile = archiveFile;
		this.archivePath = archiveFile.getPath();
		return this;
	}
	
	/**
	 * Getter for archiveFile
	 * @return File
	 */
	public File getArchiveFile()
	{
		return this.archiveFile;
	}
	
	/**
	 * Setter for archivePath
	 * @param String
	 * @return this ZipTemplateRenderer
	 */
	public ZipTemplateRenderer setArchivePath(String archivePath)
	{
		this.archivePath = archivePath;
		this.archiveFile = new File(archivePath);
		return this;
	}
	
	/**
	 * Getter for archivePath
	 * @return String
	 */
	public String getArchivePath()
	{
		return this.archivePath;
	}
	
	/**
	 * "renders" (extracts) the template archive to the destination folder.
	 */
	public void renderTemplate() throws RuntimeException
	{
		// check if class is inited
		if(this.folder == null)
			throw new RuntimeException("Can not render template..." +
				"No target folder set!");
		if(this.archiveFile == null)
			throw new RuntimeException("Can not render template..." +
				"No zip archive file set!");
		
		// uncompress archive into folder
		ArchiveUtils.decompressZip(this.archivePath, this.path, false);
	};
}
