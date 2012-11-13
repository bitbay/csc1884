/**
 * ITemplateRenderer.java
 *
 * Interface for template renderer implementations
 *
 * @author daniel@bitbay.org
 */
package org.bitbay.csc1884.template;

import java.io.File;

public interface ITemplateRenderer
{
	/**
	 * folder and path both point to the folder where the template should render
	 * it's output
	 */
	 
	// getter/setter for folder field
	public ITemplateRenderer setFolder(File file);
	public File getFolder();
	
	// getter/setter for path field
	public ITemplateRenderer setPath(String string);
	public String getPath();
	
	// public methods
	public void renderTemplate();
}
