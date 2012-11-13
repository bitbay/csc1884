/**
 * Main.java
 *
 * Application demo Main file using repository creating class CSC1884.
 *
 * Hard-coded values include:
 *	System environment variables
 *		$GH_USER
 *		$GH_PASS
 *
 *	Main.java
 *		String WORKING_FOLDER
 *		String TEMPLATE_ARCHIVE
 *		String SOURCE_ZIP_URL
 *		String REPOSITORY_NAME
 *
 *	ArchiveUtils.java
 *		int BUFFER_SIZE
 *
 *	Logger.java
 *		boolean LOG
 *
 *	CSC1884.java
 *		List<String> ignoreList
 *
 * @author daniel@bitbay.org
 */

import org.bitbay.csc1884.CSC1884;
import org.bitbay.csc1884.template.*;

public class Main
{	
	public static void main(String[] args)
	{
		// folder where the class creates temporary files
		String WORKING_FOLDER = "./tmp";
		
		// archive containing the template of the repository (all files that
		// gets pushed to the repository, excluding the downloaded src folder)
		String TEMPLATE_ARCHIVE = "./assets/repo-template.zip";
		
		// url of the source zip
		String SOURCE_ZIP_URL = 
			"https://s3.amazonaws.com/cs-production/challenges/1884/src.zip";
		
		// name of the repository to create
		String REPOSITORY_NAME = "test-repo";
		
		// a simple zip-extraction based template renderer
		IArchiveTemplateRenderer templateRenderer = new ZipTemplateRenderer();
		templateRenderer.setArchivePath(TEMPLATE_ARCHIVE);
		templateRenderer.setPath(WORKING_FOLDER);
		
		/**
		 * Check if invoked with arguments.
		 *
		 * Argument must be a valid url to a zip file. If none provided, use 
		 * default asset from challenge.
		 *
		 */
		if (args.length > 0 && !args[0].equals("") )
		{
			// if invoked with an url as argument from the command line
			SOURCE_ZIP_URL = args[0];
		}
		
		// create new instance
		CSC1884 csc1884 = new CSC1884();
		
		// configure some parameters, overriding defaults...
		csc1884.setSrcUrl(SOURCE_ZIP_URL);
		csc1884.setWorkingFolderPath(WORKING_FOLDER);
		csc1884.setTemplateRenderer(templateRenderer);
		csc1884.setRepositoryName(REPOSITORY_NAME);
		
		// usage example of additional .gitignore entries
		csc1884.addFileNameToIgnoreList("__MACOSX/");
		
		// kick-off the process chain
		csc1884.execute();
	}
}
