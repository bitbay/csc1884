/**
 * IArchiveTemplateRenderer.java
 *
 * Interface for template renderer implementations based on file archives.
 *
 * Designed as fluent interface
 * @see http://en.wikipedia.org/wiki/Fluent_interface
 *
 * @author daniel@bitbay.org
 */
 
package org.bitbay.csc1884.template;

import java.io.File;

public interface IArchiveTemplateRenderer extends ITemplateRenderer
{
	/**
	 * archiveFile and archivePath both point to the file where the archive 
	 * template renderer should look for it's base archive to extract
	 */
	 
	// getter/setter for archiveFile field
	public IArchiveTemplateRenderer setArchiveFile(File file);
	public File getArchiveFile();
	
	// getter/setter for archivePath field
	public IArchiveTemplateRenderer setArchivePath(String string);
	public String getArchivePath();
}
