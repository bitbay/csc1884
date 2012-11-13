# Description

This is a demo application (Main) for the configurable class of automated repository creation (CSC1884). The execution chain includes the following steps:
* create initial directory structure
* download a zip file, unzip the contents
* render the template using the provided templateRenderer
* create the git repository on github
* commit the local files to a local repo
* push local repo to the remote master
* cleanup the working directory

Classes are tested with JUnit, source for test classes can be found inside the folder ./src/test.

The applications only dependecies are <strong>org.eclipse.egit.github</strong> (included in source form, read below "Application internals - GitHubRepositoryManager implementation"), <strong>com.google.gson</strong> (needed by org.eclipse.egit.github, included in ./lib as .jar), and for testing <strong>org.junit</strong> (included in ./lib as .jar)

Other files included are:
* assets/repo-template.zip - archive containing repository template, used by ZipTemplateRenderer
* assets/test.zip - archive containing empty "file.txt", used in unit-testing

# Installation / deployment

## Setting up project folder

Create a folder in Your workspace:
<pre>$ cd ~/workspace
$ mkdir csc1884</pre>

## Getting source

Unpack the submitted zip archive:
<pre>$ unzip csc1884.zip</pre>
	
or alternatively get source from git:
<pre>$ git clone https://github.com/bitbay/csc1884.git</pre>

## Configuration

The applications runtime behaviour are controlled with the following variables (a.k.a "hard-coded"), found in the specified classes:

System environment variables
* $GH_USER - github account name
* $GH_PASS - github account password

Main.java
* String WORKING_FOLDER - the folder will be created, and used as temporary space for storing working data
* String TEMPLATE_ARCHIVE - the zip archive containing the repositroy template (without the 'src' folder), will be used by ZipTemplateRenderer
* String SOURCE_ZIP_URL - the URL where the archive containing the 'src' folder is located and downloaded from
* String REPOSITORY_NAME - the desired name for the to-be-created repository

ArchiveUtils.java
* int BUFFER_SIZE - buffer size for reading the archive

Logger.java
* boolean LOG - application logging can be turned off setting this variable to false

CSC1884.java
* List<String> ignoreList and the corresponding method of the class, addFileNameToIgnoreList( String fileName ) - controls the isIgnored parser of the GitHubManager class. See class for details and limitations.

## Compiling the application

The application uses ANT for the compiling, deploying and testing, so compilation is as straightforward as:
<pre>$ ant all</pre>

This will compile all source classes into the ./build folder, and create an executable jar file in the ./dist folder. The libraries the application depends on get deployed next to the .jar file, in the ./dist/lib folder. There are some assets for now the application uses at runtime, these get deployed into the ./dist/assets folder.

To run the application, from inside the ./dist folder type and run:
<pre>$ java -jar CSC1884.jar</pre>

By default, the application logs debug messages to the console. To turn them off, set the Logger class LOG field to false.

To see all the available ANT tasks, type 
<pre>$ ant help</pre>
or simply
<pre>$ ant</pre>
from the project directory (where the build.xml is).

## Running tests, creating reports

The application uses JUnit for unit testing. To run all the available tests and create a report, run
<pre>$ ant test-all</pre>

After it finishes, open up <strong>./build/test/reports/index.html</strong> in a web-browser to see the results.

# Application Internals

## ITemplateRenderer interface

The class uses an interface for creating the repository template. This package includes an implementation of a simple ZipTemplateRenderer (simply decompresses a zip archive - repo-template.zip - containing the template), but extending the <strong>org.bitbay.csc1884.template.ITemplateRenderer</strong> interface implementing it's getters/setters for defining a destination folder, and it's renderTemplate method one could easily swap this with another, more complex template renderer (dynamic content rendering)...it's up to the user.

## GitHubRepositoryManager implementation

The <strong>org.bitbay.csc1884.repository.GitHubRepositoryManager</strong> class uses the eclipse git library to communicate with the Github API (v3) over https connection, using enviromnet variables with github account details ($GH_USER and $GH_PASS). There was a recent change in the version 3 API of Github, allowing the initialization of repositories as they get created (see github developer post at http://developer.github.com/changes/2012-9-28-auto-init-for-repositories/). Since this usefull feature was still not reflected in the eclipse github library, i made some changes to the original Repository class to include them. For easy deploying, the modified library is included with this application in the org.eclipse.*, and can be cloned from https://github.com/bitbay/egit-github.git (pull request already sent).

The GitHubRepositoryManagers features include a simple (i MEAN it), rather limited .gitignore parser, just enough to handle the challenges requirements - ignoring files and folders. For a full description of its features read the corresponding comments of the GitHubRepositoryManager.isIgnored method.
The manager supports multiple commits to the same repository - if a repository to be created is found, it is used to create a new commit over the last one. This way, a full push/commit history with diffs are all the time present in the repository. New commits are only pushed into the remote origin, if there are some actual changes made to the files.

Happy pushing, and do not hesitate to contact me with stuff related to this application!

