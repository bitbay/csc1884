/**
 * GitHubRepositoryManager.java
 *
 * Simple implementation of a repository manager class using eclipse egit.github
 * library to communicate with github API v3 over https.
 *
 * Includes a very simple (and rather limited) .gitignore parser implementation.
 *
 * @author daniel@bitbay.org
 */
 
package org.bitbay.csc1884.repository;

import org.bitbay.csc1884.utils.*;

import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.*;
import org.eclipse.egit.github.core.util.EncodingUtils;

import java.io.*;
import java.util.*;

public class GitHubRepositoryManager implements IRepositoryManager
{
	private File localFolder;
	private String localPath;
	
	private String repositoryName;
	private Repository repository;
	
	private String user;
	private String pass;
	
	private GitHubClient client;
	private RepositoryService repositoryService;
	private CommitService commitService;
	private DataService dataService;
	private boolean servicesInited;
	
	/**
	 * Getter for localFolder
	 * @return File
	 */
	public File getLocalFolder()
	{
		return this.localFolder;
	}

	/**
	 * Setter for localFolder
	 * @param File
	 * @return GitHubRepositoryManager
	 */
	public GitHubRepositoryManager setLocalFolder(File localFolder)
	{
		this.localFolder = localFolder;
		this.localPath = localFolder.getPath();
		return this;
	}
	
	/**
	 * Getter for localPath
	 * @return String
	 */
	public String getLocalPath()
	{
		return this.localPath;
	}

	/**
	 * Setter for localPath
	 * @param String
	 * @return GitHubRepositoryManager
	 */
	public GitHubRepositoryManager setLocalPath(String localPath)
	{
		this.localPath = localPath;
		this.localFolder = new File(localPath);
		return this;
	}
	
	/**
	 * Getter for repositoryName
	 * @return String
	 */
	public String getRepositoryName()
	{
		return this.repositoryName;
	}

	/**
	 * Setter for repositoryName
	 * @param String
	 * @return GitHubRepositoryManager
	 */
	public GitHubRepositoryManager setRepositoryName(String repositoryName)
	{
		this.repositoryName = repositoryName;
		return this;
	}
	
	/**
	 * Getter for user
	 * @return String
	 */
	public String getUser()
	{
		return this.user;
	}

	/**
	 * Setter for user
	 * @param String
	 * @return GitHubRepositoryManager
	 */
	public GitHubRepositoryManager setUser(String user)
	{
		this.user = user;
		return this;
	}
	
	/**
	 * Getter for pass
	 * @return String
	 */
	public String getPass()
	{
		return this.pass;
	}

	/**
	 * Setter for pass
	 * @param String
	 * @return GitHubRepositoryManager
	 */
	public GitHubRepositoryManager setPass(String pass)
	{
		this.pass = pass;
		return this;
	}
	
	/**
	 * Getter for servicesInited
	 * @return String
	 */
	public boolean getServicesInited()
	{
		return this.servicesInited;
	}
	
	/**
	 * Constructor
	 */
	public GitHubRepositoryManager()
	{
		this.repositoryName = "test-repo";
	}
	
	/**
	 * Constructor
	 * 
	 * @param String	name of the repository
	 */
	public GitHubRepositoryManager(String repositoryName)
	{
		this.repositoryName = repositoryName;
	}
	
	/**
	 * Initializes services used in class for github api interaction
	 */
	public void initServices()
	{
		// Basic HTTPS authentication
		this.client = new GitHubClient();
		client.setCredentials(this.user, this.pass);
		
		this.repositoryService = new RepositoryService(this.client);
		this.commitService = new CommitService(this.client);
        this.dataService = new DataService(this.client);
        
        this.servicesInited = true;
	}
	
	/**
	 * Creates a github repository if no repository exists with name, if does,
	 * do nothing.
	 *
	 * @return Boolean	true if repository successfully created or found
	 */
	public boolean createRepository() throws Exception
	{
		if( this.repositoryName == null || this.repositoryName.equals("") )
			throw new Exception("Can not create noname repository");

		Logger.coloredLog("\nChecking for repository with name " + 
			this.repositoryName + "\n", Logger.ANSI_YELLOW);
		
		// initialize services, if needed.
		if( !this.servicesInited ) initServices();
		
		// first, check if repository already exists..
		this.repository = searchRepository( this.repositoryName );
		
		if( this.repository != null )
		{
			Logger.log( "Repository \"" + this.repositoryName + "\" found. (" +
				this.repository.getWatchers() + ") watching." );
		}
		else
		{
			Logger.log( "Repository \"" + this.repositoryName +
				"\" does not exists. Creating...");
			this.repository = new Repository();
			this.repository.setName(this.repositoryName);
			// patched version of org.eclipse.egit.github.core.Repository needed
			// @see http://developer.github.com/changes/2012-9-28-auto-init-for-repositories/
			this.repository.setAutoInit(true);
			
			// create the repository on github, and 
			this.repository = createGitHubRepo( this.repository );
		}
		
		return (this.repository != null);
	}
	
	/**
	 * Creates a new repository on github
	 *
	 * @param Repository	repository to create
	 * @return Repository	the created github repository
	 */
	private Repository createGitHubRepo(Repository repo) throws RuntimeException
	{	
		try
		{
			repo = this.repositoryService.createRepository( repo );
		}
		catch( IOException ioe )
		{
			throw new RuntimeException(ioe);
		}
			
		return repo;
	}
	
	/**
	 * Searches for a given repository by repo-name.
	 *
	 * TODO: use actual service.searchRepositories(String query)
	 *
	 * @param String	name of the repository to look for
	 * @return Repository	the repository found || null if no match
	 */
	private Repository searchRepository( String repoName )
		throws RuntimeException
	{
		Repository result = null;
		
		try
		{
			for( Repository repo : this.repositoryService.getRepositories(
				this.user) )
			{
				// if repository name match, set the result to the repository
				// found
				Logger.log("Repository with name \"" + repo.getName() + 
					"\" found.");
				if( repo.getName().equals(repoName) )
				{
					result = repo;
				}
			}
		}
		catch( IOException ioe )
		{
			throw new RuntimeException(ioe);
		}
		
		return result;
	}
	
	/**
	 * Creates a new commit in a github repository containing files from
	 * directory.
	 *
	 * Based on two posts (see @see below), with some modifications including:
	 *	- changed variable names to the ones used in the original article
	 *	  (swanson.github.com)
	 *	- recursive Tree building
	 *	- .gitignore implementation
	 *	- Tree generation is not accumulative
	 *	  (prevents files/folders appearing as many times as commits done)
	 * 
	 * TODO: compare previous commits with file changes, so code can reuse
	 * 		blobs already commited...this would save bandwidth, but since git
	 *		garbage collector harvests not referenced blobs, should not be a
	 *		problem not reusing blobs
	 *
	 * @see http://swanson.github.com/blog/2011/07/23/digging-around-the-github-api-take-2.html
	 * @see https://gist.github.com/2337731
	 */
	public void push() throws Exception, RuntimeException
	{
		if(this.repository == null )
			throw new Exception("No repository to push files into!");
		if(this.localPath == null || this.localPath.equals("") )
			throw new Exception("No directory set to track!");

		Logger.coloredLog("\nCreating blobs for commit\n", Logger.ANSI_YELLOW);
		
		try
		{
			// get SHA of latest commit
            String SHA_LATEST_COMMIT = this.repositoryService
            	.getBranches(this.repository).get(0).getCommit().getSha();

            /* To get a continuous commit history, we need to set in the new
             * commit the parent of it (latestCommit.commit).
             *
             * But since there is an error(?) in the implementation of
             * egit.github library, the RepositoryCommit object will hold a
             * reference to the original Commit and Tree objects, but those will
             * have some fields missing.
             *
             * I'll refer to them later, and fill them with the corresponding
             * fields of the RepositoryCommit object...
             */
            RepositoryCommit latestCommit = 
            	commitService.getCommit(this.repository, SHA_LATEST_COMMIT);
            
            // *latestCommit.getCommit().getSha() == null
            // correct it with latestCommit's sha...
            latestCommit.getCommit().setSha( SHA_LATEST_COMMIT );
            
            Logger.log( "SHA_LATEST_COMMIT: " + SHA_LATEST_COMMIT );
			
			// *latestCommit.getCommit().getTree() == null
			// fill it with the real Tree, get it from the Repository with
			// SHA_BASE_TREE reference...
			String SHA_BASE_TREE = latestCommit.getCommit().getTree().getSha();
			Tree baseTree = dataService.getTree(this.repository, SHA_BASE_TREE);
			latestCommit.getCommit().setTree( baseTree );
			
			Logger.log( "SHA_BASE_TREE: " + SHA_BASE_TREE );
			
			// this collection will hold the entries for the files/folders
			Collection<TreeEntry> entries = 
				getEntriesFromFiles(this.localFolder.listFiles());
			
			if( entries.size() == 0 )
			{
				Logger.log("No entries where added!");
				return;
			}
			
			// create new Tree in the repository with the collected TreeEntries
			Tree newTree = this.dataService.createTree(this.repository, entries);
			
			List<Commit> parentCommits = new ArrayList<Commit>();
			parentCommits.add(latestCommit.getCommit());

            // create new commit with latestCommit as parent
            Commit newCommit = new Commit();
            newCommit.setParents(parentCommits);
            newCommit.setTree(newTree);
            newCommit.setMessage("Automated commit at " + 
            	new Date(System.currentTimeMillis()).toLocaleString());
			
			newCommit =
				this.dataService.createCommit(this.repository, newCommit);

			// compare...
			RepositoryCommitCompare repoCompare = this.commitService.compare(
				repository, SHA_LATEST_COMMIT, newCommit.getSha() );
			int changedFiles = repoCompare.getFiles().size();
			
			// if found any changes, update the heads/master reference...
			if( changedFiles > 0 )
			{
		        // create resource with the new commit
		        TypedResource commitResource = new TypedResource();
		        commitResource.setSha(newCommit.getSha());
		        commitResource.setType(TypedResource.TYPE_COMMIT);
		        commitResource.setUrl(newCommit.getUrl());
		        
		        // get master reference and update it the new resource
		        Reference reference = this.dataService.getReference(
		        	this.repository, "heads/master");
		        reference.setObject(commitResource);
		        this.dataService.editReference(this.repository, reference, true);

		        // success
		        Logger.log("\nReference updated with new commit.");
			}
			else
			{
				// no changes made to files, do not update reference
				Logger.coloredLog("\nNo changes found. Reference not updated.",
					Logger.ANSI_CYAN);
			}
        }
        catch (IOException ioe)
        {
            throw new RuntimeException(ioe);
        }
	}
	
	/**
	 * Parse files into TreeEntries.
	 *
	 * @param File[]	array of files to parse
	 * @return Collection<TreeEntry>	the parsed collection
	 */
 	private Collection<TreeEntry> getEntriesFromFiles(File[] files)
	{
		Collection<TreeEntry> entries = new ArrayList<TreeEntry>();
		
	    for (int i = 0; i < files.length; i++)
	    {
	    	//if fileName is not in the ignoreList, entries.add...
	    	if( !isIgnored(files[i]) )
	    	{
		    	// for each file, recursively create a treeEntry
		    	TreeEntry entry = getRecursiveTreeEntries(files[i]);
		    	if( entry != null )
					entries.add(entry);
			}
			else
			{
				Logger.coloredLog("IGNORING: " + getLocalPath(files[i]),
					Logger.ANSI_CYAN);
			}
		}
		
		return entries;
	}
	
	/**
	 * Really simple implementation of a .gitignore patterns matcher.
	 * This pattern matcher could be easily swapped with some third-library to
	 * do the matching (nbgit.FnMatch, jgit.FileNameMatcher, etc).
	 *
	 * Supports:
	 * + Line starting with '#' - comment
	 * + Blank line - no match
	 * - '!' prefix, negating pattern - not supported
	 * + Slash '/' ending - exclude directory
	 * + Slash '/' prefix - root folder match only
	 * - fnmatch patterns - not supported 
	 *
	 * @param File	file to check
	 * @return boolean	if the file should be ignored
	 */
	private boolean isIgnored(File file)
	{
		// the result returned
		boolean ignored = false;
		String strToCheck = getLocalPath(file);
		
		// search for .gitignore in repository base folder
		File gitIgnoreFile = new File(this.localPath + "/.gitignore");
		
		// if .gitignore does not exists, do not ignore files..
		if( gitIgnoreFile.exists() )
		{
			List<String> gitIgnores = 
				FileUtils.readFileAsStringList(gitIgnoreFile);
				
			for( String pattern : gitIgnores )
			{
				// pattern matching flags
				boolean directory = false;
				boolean rootFolderOnly = false;
				boolean comment = false;
				boolean blankLine = false;
				boolean fileName = false;
				
				if(pattern.endsWith("/"))
				{
					directory = true;
					// trim tailing '/'
					pattern = pattern.substring(0, pattern.length()-1);
				}
				if(pattern.startsWith("/")) rootFolderOnly = true;
				if(pattern.startsWith("#")) comment = true;
				if(pattern.isEmpty()) blankLine = true;
				if(!directory && !comment && !blankLine) fileName = true;
				
				// very simple matching
				if( comment || blankLine ) break;
				
				// file type pattern
				if(fileName)
				{
					if(!rootFolderOnly)
					{
						if(strToCheck.endsWith(pattern)) 
						{
							ignored = true;
						}
					}
					else
					{
						if(!strToCheck.contains("/") && 
							strToCheck.endsWith(pattern))
						{
							ignored = true;
						}
					}
				}
				else if(directory && file.isDirectory())
				{	
					if(!rootFolderOnly)
					{
						if(strToCheck.endsWith(pattern)) 
						{
							ignored = true;
						}
					}
					else
					{
						if(!strToCheck.contains("/") && 
							strToCheck.endsWith(pattern))
						{
							ignored = true;
						}
					}
				}
			}
		}
		
		return ignored;
	}
	
	/**
	 * Trims a file full path to path relative to local repository folder.
	 *
	 * @param File	the file to get the path/filename of
	 * @return String	full path with filename relative to repository base
	 *	folder.
	 */
	private String getLocalPath(File file)
	{
		return file.getPath().substring(this.localPath.length()+1,
			file.getPath().length());
	}
	/**
	 * Recursive convert files into TreeEntries
	 *
	 * @param File	the File to convert into TreeEntry
	 * @return TreeEntry	the file converted to treeEntry
	 */
 	private TreeEntry getRecursiveTreeEntries(final File file)
 		throws RuntimeException
	{
		TreeEntry treeEntry = null;
		
	    if (file.exists())
	    {
			if (file.isDirectory())
			{
				// collection of treeEntries representing files and directories
				Collection<TreeEntry> entries = new ArrayList<TreeEntry>();
				
				final File[] files = file.listFiles();
			    for (int i = 0; i < files.length; i++)
			    {
			    	if( !isIgnored(files[i]) )
			    	{
			    		TreeEntry entry = getRecursiveTreeEntries(files[i]);
		    			if( entry != null )	entries.add(entry);
		    		}
		    		else
					{
						Logger.coloredLog("IGNORING: " + getLocalPath(files[i]),
							Logger.ANSI_CYAN);
					}
				}
				
				// empty directories don't get tracked (by git design)
				if( entries.size() > 0 )
				{
					try
					{
				    	Tree newTree = this.dataService.createTree(
				    		this.repository, entries);
					
						// create new TreeEntry
						treeEntry = new TreeEntry();
					
						// set new TreeEntry properties
						treeEntry.setPath(file.getName());
						treeEntry.setMode(TreeEntry.MODE_DIRECTORY);
						treeEntry.setType(TreeEntry.TYPE_TREE);
						treeEntry.setSha(newTree.getSha());
					
						Logger.log("ADDED(folder): " + getLocalPath(file));
				    }
				    catch(IOException ioe )
				    {
				    	throw new RuntimeException(ioe);
				    }
				}
			}
			else
			{
				// create new blob with data
				Blob blob = new Blob();
				blob.setContent(
					EncodingUtils.toBase64(FileUtils.loadFile(file)))
					.setEncoding(Blob.ENCODING_BASE64);
				
				try
				{
					String blob_sha = this.dataService.createBlob(
						this.repository, blob);

					// create new TreeRntry
					treeEntry = new TreeEntry();
					
					// set new TreeEntry properties
					treeEntry.setPath(file.getName());
					treeEntry.setMode(TreeEntry.MODE_BLOB);
					treeEntry.setType(TreeEntry.TYPE_BLOB);
					treeEntry.setSha(blob_sha);
					treeEntry.setSize(blob.getContent().length());
					
					Logger.log("ADDED(file): " + getLocalPath(file));
				}
		        catch(IOException ioe )
		        {
		        	throw new RuntimeException(ioe);
		        }
			}
	    }
	    
    	return treeEntry;
	}
}
