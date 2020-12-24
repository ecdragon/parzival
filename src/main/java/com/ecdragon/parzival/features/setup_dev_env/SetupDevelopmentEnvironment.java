package com.ecdragon.parzival.features.setup_dev_env;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ecdragon.parzival.features.git.GitRepository;
import com.ecdragon.parzival.utils.UtilsParzival;

/**
 * THIS HAS MOVED TO DevelopmentEnvironmentService::setupDevelopmentEnvironment() !!!!
 */
public class SetupDevelopmentEnvironment {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	// Find local project absolute path
	public static final Path thisProjectDirectoryPath = Paths.get("").toAbsolutePath();
	
	// Set the template workspace path
	public static final Path templateEclipseDirectoryPath = Paths.get(thisProjectDirectoryPath.toString(), "eclipse_workspace_templates/2019-12-R_1");
	
	public static final Path templateWorkspaceDirectoryPath = Paths.get(templateEclipseDirectoryPath.toString(), "template_workspace");
	
	// Set the eclipse app location
	public static final Path eclipseAppFileLocationPath = Paths.get(templateEclipseDirectoryPath.toString(), "Eclipse.app");
	
	private Path projectBundleSpaceDirectoryFullPath;
	private String projectName;
	private String projectIdNoteSuffix;
	private String branchName;
	private String intent;
	private String bundleDirectoryNamePrefix; 
	private EnvironmentContext environmentContext;
	
	private String projectBundleDirectoryName;
	private Path bundleDirectoryPath;
	private Path workspaceDirectoryPath;
	private Path projectDirectoryPath;
	private Integer seed;

	public static void main(String[] args) {
		
		// Result - abc/
		setupAnEnvironment(
				EnvironmentContext.AbcBitbucket, // Context 
				"git_repo_name", // project name
				"ABC - Git Repo Name - initial for testing", // intent.txt 
				null, // project suffix
				null, // branch name - can include "/", be null (defaults to master), or "master"
				null, // seed - used in docker stuff now
				null, // bundle directory name prefix
				null // gitAccountName - for getting random source like Spring...
				);
	}
	
	public static void setupAnEnvironment(
			EnvironmentContext environmentContext, 
			String projectName, 
			String intent,
			String projectIdNoteSuffix, 
			String branchName,
			Integer seed,
			String bundleDirectoryNamePrefix,
			String gitAccountName
			) {
		
		if (gitAccountName != null && !gitAccountName.isEmpty()) {
			environmentContext.setGitAccountName(gitAccountName);
		}
		
		setupAnEnvironment(
				environmentContext, 
				projectName,
				intent,
				projectIdNoteSuffix, 
				branchName,
				seed,
				bundleDirectoryNamePrefix
				);
	}
	
	public static void setupAnEnvironment(
			EnvironmentContext environmentContext, 
			String projectName, 
			String intent,
			String projectIdNoteSuffix, 
			String branchName,
			Integer seed,
			String bundleDirectoryNamePrefix
			) {
		
		SetupDevelopmentEnvironment setupDevelopmentEnvironment = new SetupDevelopmentEnvironment();
		
		setupDevelopmentEnvironment.setProjectBundleSpaceDirectoryFullPath(
				environmentContext.getProjectBundleSpaceDirectoryFullPath());
		
		setupDevelopmentEnvironment.setEnvironmentContext(environmentContext);
		setupDevelopmentEnvironment.setProjectName(projectName);
		setupDevelopmentEnvironment.setIntent(intent);
		setupDevelopmentEnvironment.setBundleDirectoryNamePrefix(bundleDirectoryNamePrefix);
		setupDevelopmentEnvironment.setProjectIdNoteSuffix(projectIdNoteSuffix);
		setupDevelopmentEnvironment.setBranchName(branchName);
		setupDevelopmentEnvironment.setSeed(seed);
		
		setupDevelopmentEnvironment.setupDevelopmentEnvironment();
	}
	
	/**
	 * THIS HAS MOVED TO DevelopmentEnvironmentService::setupDevelopmentEnvironment() !!!!
	 * sets up a dev environment, and includes Eclipse
	 */
	public void setupDevelopmentEnvironment() {

        // Construct date-time folder prefix
        LocalDateTime now = LocalDateTime.now();
        String nowDateTimeSortableString = UtilsParzival.generateDateTimeSortableString(now);
        String dateTimeDirectoryNamePrefix = nowDateTimeSortableString + "_";
        String bundleDirectoryNamePrefixActual = "";
        if (bundleDirectoryNamePrefix != null && !bundleDirectoryNamePrefix.isEmpty()) {
        	String separator = "_";
        	if (bundleDirectoryNamePrefix.endsWith(separator)) {
        		separator = "";
        	}
        	bundleDirectoryNamePrefixActual = bundleDirectoryNamePrefix + separator;
        }

        projectBundleDirectoryName = bundleDirectoryNamePrefixActual + dateTimeDirectoryNamePrefix + projectName;
        if (projectIdNoteSuffix != null && !projectIdNoteSuffix.isEmpty()) {
        	projectBundleDirectoryName += "_" + projectIdNoteSuffix;
        }
        System.out.println("projectBundleDirectoryName: " + projectBundleDirectoryName);
		
		bundleDirectoryPath = Paths.get(projectBundleSpaceDirectoryFullPath.toString(), projectBundleDirectoryName);
		
		workspaceDirectoryPath = Paths.get(bundleDirectoryPath.toString(), "workspace");
		
		projectDirectoryPath = Paths.get(workspaceDirectoryPath.toString(), projectName);
		
		// Main try block
		try {

			// If the space directory base path isn't there, error (example: /Data/eclipse_projects)
			if (!Files.exists(environmentContext.getProjectBundleSpaceDirectoryBasePath())) {
				throw new Exception("ProjectBundleSpaceDirectoryBasePath doesn't exist: " + environmentContext.getProjectBundleSpaceDirectoryBasePath().toString());
			}
			
			// If the space directory path isn't there, try to create it (example: /Data/eclipse_projects/d)
			if (!Files.exists(projectBundleSpaceDirectoryFullPath)) {
				
				// Create the missing dir
				Files.createDirectories(projectBundleSpaceDirectoryFullPath);
				
				// Check again...
				if (!Files.exists(projectBundleSpaceDirectoryFullPath)) {
					throw new Exception("projectBundleSpaceDirectoryFullPath doesn't exist: " + projectBundleSpaceDirectoryFullPath.toString());
				}
			}
			
			// Create bundle directory as long as it doesn't exist already (fat chance)
			if (!Files.exists(bundleDirectoryPath)) {
				Files.createDirectory(bundleDirectoryPath);
				System.out.println("Created bundle directory: " + bundleDirectoryPath.toString());
			}
			else {
				System.out.println("EXITING AND Not creating bundle directory: " + bundleDirectoryPath.toString() + " - it already exists!");
				return;
			}
			
			// Create workspace directory as long as it doesn't already exist (fat chance)
			if (!Files.exists(workspaceDirectoryPath)) {
				Files.createDirectory(workspaceDirectoryPath);
				System.out.println("Created workspace directory: " + workspaceDirectoryPath.toString());
			}
			else {
				System.out.println("EXITING AND Not creating workspace directory: " + workspaceDirectoryPath.toString() + " - it already exists!");
				return;
			}
			
			// Copy Eclipse.app from source to bundle dir
			Path bundleEclipseAppPath = Paths.get(bundleDirectoryPath.toString(), "Eclipse.app");
			if (!Files.exists(bundleEclipseAppPath)) {
				
				// Finally... https://stackoverflow.com/questions/37384068/how-to-copy-and-replace-an-application-via-terminal-in-mac-os
				String[] copyEclipseAppCommand = {"/bin/cp", "-rf", eclipseAppFileLocationPath.toString(), bundleEclipseAppPath.toString()};
				System.out.println("About to copy a fresh Eclipse from: " + eclipseAppFileLocationPath.toString() + " to: " + bundleEclipseAppPath.toString());
				String copyEclipseAppCommandResults = RunMacCommand.run(copyEclipseAppCommand);
				System.out.println(copyEclipseAppCommandResults);
//				Runtime.getRuntime().exec(copyEclipseAppCommand);
				System.out.println("Copied a fresh Eclipse from: " + eclipseAppFileLocationPath.toString() + " to: " + bundleEclipseAppPath.toString());
			}
			else {
				System.out.println("EXITING AND Not copying Eclipse.app, it already exists at: " + bundleEclipseAppPath.toString());
				return;
			}
			
			// Create project directory, as long as it doesn't exist already (fat chance)
			// No more! now using git command, so only need to check that it already does NOT exist...
			if (Files.exists(projectDirectoryPath)) {
				System.out.println("ERROR - EXITING - project directory: " + projectDirectoryPath.toString() + " - already exists!");
				return;
			}
			
			String gitCloneCommand = 
					environmentContext.generateGitCloneCommand(
							projectName,
							branchName
							);
			System.out.println("About to clone: " + gitCloneCommand + " into directory: " + projectDirectoryPath.toString());
			
			boolean cloneSuccessfull = 
					GitRepository.cloneRepository(
//								gitRepositoryUrlFull, 
							workspaceDirectoryPath, 
							projectName, 
//								branchName,
							environmentContext.getGitRepositoryUserName(),
							environmentContext.getGitRepositoryUserEmail(),
							gitCloneCommand
							);
			
			if (!cloneSuccessfull) {
				System.out.println("Failed to clone repo. Unsuccessful. Exiting.");
				return;
			}
			
			try {
				if (isDirEmpty(projectDirectoryPath)) {
					throw new Exception();
				}
			} 
			catch (Exception e) {
				System.out.println("Failed to clone repo. Directory empty. Exiting.");
				return;
			}
			
			System.out.println("Successfully cloned repo.");
			
			// Pause a little
			Thread.sleep(2000);
			
			// Copy .metadata template to workspace folder 
			Path templateWorkspaceMetadataDirectoryPath = Paths.get(templateWorkspaceDirectoryPath.toString(), ".metadata");
			String[] copyWorkspaceMedataCommand = 
				{"/bin/cp", "-rf", 
					templateWorkspaceMetadataDirectoryPath.toString(), 
					workspaceDirectoryPath.toString()};
			System.out.println("Copying: " +  templateWorkspaceMetadataDirectoryPath.toString() + " to: " + workspaceDirectoryPath.toString());
			String copyWorkspaceMedataCommandResults = RunMacCommand.run(copyWorkspaceMedataCommand);
			System.out.println(copyWorkspaceMedataCommandResults);
			System.out.println("Copied: " +  templateWorkspaceMetadataDirectoryPath.toString() + " to: " + workspaceDirectoryPath.toString());
			
			Path orgEclipseUiIdePrefsFilePath = Paths.get(templateWorkspaceDirectoryPath.toString(), "org.eclipse.ui.ide.prefs");
			System.out.println("orgEclipseUiIdePrefsFilePath: " +  orgEclipseUiIdePrefsFilePath.toString());
			Path eclipseAppConfigurationSettingsFilePath = Paths.get(bundleEclipseAppPath.toString(), "Contents/Eclipse/configuration/.settings/org.eclipse.ui.ide.prefs");
			System.out.println("eclipseAppConfigurationSettingsFilePath: " +  eclipseAppConfigurationSettingsFilePath.toString());

			// Edit org.eclipse.ui.ide.prefs
			// Read in prefs as props file
			System.out.println("About to edit: " + orgEclipseUiIdePrefsFilePath);
			try (Stream<String> lines = Files.lines(eclipseAppConfigurationSettingsFilePath)) {
				System.out.println("Waiting a sec before writing to prefs file, to make sure it's there...");
				Thread.sleep(1000);
				List<String> replaced = lines
						.map(line-> line.replaceAll("replace_me", workspaceDirectoryPath.toString()))
						.collect(Collectors.toList());
				Files.write(eclipseAppConfigurationSettingsFilePath, replaced, StandardOpenOption.WRITE);
				System.out.println("Lines replaced: ");
				for (String line : replaced) {
					System.out.println(line);
				}
			}
			catch (Exception e) {
				System.out.println("Unable to replace 'replace_me' token in prefs file and stream it to overwrite the prefs file in Eclipse.app: " + e.getMessage());
				throw e;
			}
			
			// Create intent.txt file with intent in it, append if already there
			Path intentTxtPath = Paths.get(bundleDirectoryPath.toString(), "intent.txt");
			Files.createFile(intentTxtPath);
			if (intent == null || intent.isEmpty()) {
				intent = "Creating this project just because it seemed like a good idea at the time!";
			}
			Files.write(intentTxtPath, intent.getBytes(), StandardOpenOption.WRITE);
			
			
			// Open Eclipse after sleeping a little
			System.out.println("Waiting a sec before opening Eclipse");
			Thread.sleep(2000);
			System.out.println("Opening Eclipse");
			
			String[] openEclipseAppCommand = {"open", "-a", bundleEclipseAppPath.toString(), "--args", "-data", workspaceDirectoryPath.toString()};
			ProcessBuilder processBuilder = new ProcessBuilder(openEclipseAppCommand);
			Process process = processBuilder.start();
			try (InputStream processInputStream = process.getInputStream();
					InputStreamReader processInputStreamReader = new InputStreamReader(processInputStream);
					BufferedReader reader = new BufferedReader(processInputStreamReader);
					) {
				
				System.out.println("Open Eclipse command executed. Reading output from command:");
				String line = null;
				do {
					while ((line = reader.readLine()) != null) {
						System.out.println(line);
					}
				}
				while (process.isAlive());
				System.out.println("Done reading output from open eclipse command.");
			} 
			catch (Exception e) {
				System.out.println("Threw exception / failed reading command output from run eclipse command");
				throw e;
			}
			
			environmentContext.help(this);
			
			System.out.println("Success!");
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed!");
		}
	}
	
	public static boolean isDirEmpty(final Path directory) throws Exception {
		
	    try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
	        return !dirStream.iterator().hasNext();
	    } 
	    catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public static boolean copyFolder(Path src, Path dest) {
		try {
			Iterator<Path> pathIterator = Files.walk(src).iterator();
			boolean failed = false;
			while(!failed && pathIterator.hasNext()) {
				Path source = pathIterator.next();
				failed = copy(source, dest.resolve(src.relativize(source)));
			}
			if (failed) {
				return false;
			}
			return true; 
		} 
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean copy(Path source, Path dest, CopyOption... copyOption) {
	    try {
	    	if (copyOption != null) {
	    		Files.copy(source, dest, copyOption);
	    	}
	    	else {
	    		Files.copy(source, dest);
	    	}
	        return true;
	    } 
	    catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public static void enterSudoPassword() {
		try {
			String[] cmd = {"/bin/bash","-c","echo password | sudo -S ls"};
			Process pb = Runtime.getRuntime().exec(cmd);
			
			String line;
			BufferedReader input = new BufferedReader(new InputStreamReader(pb.getInputStream()));
			while ((line = input.readLine()) != null) {
				System.out.println(line);
			}
			input.close();
			System.out.println("");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Path getProjectBundleSpaceDirectoryFullPath() {
		return projectBundleSpaceDirectoryFullPath;
	}
	public void setProjectBundleSpaceDirectoryFullPath(Path projectBundleSpaceDirectoryFullPath) {
		this.projectBundleSpaceDirectoryFullPath = projectBundleSpaceDirectoryFullPath;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectIdNoteSuffix() {
		return projectIdNoteSuffix;
	}
	public void setProjectIdNoteSuffix(String projectIdNoteSuffix) {
		this.projectIdNoteSuffix = projectIdNoteSuffix;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getIntent() {
		return intent;
	}
	public void setIntent(String intent) {
		this.intent = intent;
	}
	public String getBundleDirectoryNamePrefix() {
		return bundleDirectoryNamePrefix;
	}
	public void setBundleDirectoryNamePrefix(String bundleDirectoryNamePrefix) {
		this.bundleDirectoryNamePrefix = bundleDirectoryNamePrefix;
	}
	public EnvironmentContext getEnvironmentContext() {
		return environmentContext;
	}
	public void setEnvironmentContext(EnvironmentContext environmentContext) {
		this.environmentContext = environmentContext;
	}
	public String getProjectBundleDirectoryName() {
		return projectBundleDirectoryName;
	}
	public void setProjectBundleDirectoryName(String projectBundleDirectoryName) {
		this.projectBundleDirectoryName = projectBundleDirectoryName;
	}
	public Path getBundleDirectoryPath() {
		return bundleDirectoryPath;
	}
	public void setBundleDirectoryPath(Path bundleDirectoryPath) {
		this.bundleDirectoryPath = bundleDirectoryPath;
	}
	public Path getWorkspaceDirectoryPath() {
		return workspaceDirectoryPath;
	}
	public void setWorkspaceDirectoryPath(Path workspaceDirectoryPath) {
		this.workspaceDirectoryPath = workspaceDirectoryPath;
	}
	public Path getProjectDirectoryPath() {
		return projectDirectoryPath;
	}
	public void setProjectDirectoryPath(Path projectDirectoryPath) {
		this.projectDirectoryPath = projectDirectoryPath;
	}
	public Integer getSeed() {
		return seed;
	}
	public void setSeed(Integer seed) {
		this.seed = seed;
	}
}
