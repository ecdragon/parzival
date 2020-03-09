package com.ecdragon.parzival.features.development_environment;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ecdragon.parzival.features.environment_context.EnvironmentContextEnum;
import com.ecdragon.parzival.features.git.GitRepository;
import com.ecdragon.parzival.spring.configuration.SpringConfigurationConstants;
import com.ecdragon.parzival.utils.UtilsMacCommand;
import com.ecdragon.parzival.utils.UtilsParzival;

@Component
public class DevelopmentEnvironmentService {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private SpringConfigurationConstants springConfigurationConstants;
	public SpringConfigurationConstants getSpringConfigurationConstants() {
		return springConfigurationConstants;
	}
	@Autowired
	public void setSpringConfigurationConstants(SpringConfigurationConstants springConfigurationConstants) {
		this.springConfigurationConstants = springConfigurationConstants;
	}
	
	public void setupAnEnvironment(
			EnvironmentContextEnum environmentContext, 
			String projectName, 
			String intent,
			String projectIdNoteSuffix, 
			String branchName,
			Integer seed,
			String bundleDirectoryNamePrefix,
			String gitAccountName,
			Boolean cleanQuarantineFlag
			) {
		
		if (gitAccountName != null && !gitAccountName.isEmpty()) {
			environmentContext.setGitAccountName(gitAccountName);
		}
		
		DevelopmentEnvironmentDto developmentEnvironmentDto = new DevelopmentEnvironmentDto();
		
		developmentEnvironmentDto.setProjectBundleSpaceDirectoryFullPath(
				environmentContext.getProjectBundleSpaceDirectoryFullPath());
		
		developmentEnvironmentDto.setEnvironmentContext(environmentContext);
		developmentEnvironmentDto.setProjectName(projectName);
		developmentEnvironmentDto.setIntent(intent);
		developmentEnvironmentDto.setBundleDirectoryNamePrefix(bundleDirectoryNamePrefix);
		developmentEnvironmentDto.setProjectIdNoteSuffix(projectIdNoteSuffix);
		developmentEnvironmentDto.setBranchName(branchName);
		developmentEnvironmentDto.setSeed(seed);
		developmentEnvironmentDto.setCleanQuarantineFlag(cleanQuarantineFlag);
		
		setupDevelopmentEnvironment(developmentEnvironmentDto);
	}
	
	/**
	 * sets up a dev environment, and includes Eclipse
	 */
	public void setupDevelopmentEnvironment(
			DevelopmentEnvironmentDto developmentEnvironmentDto
			) {
		
		if (developmentEnvironmentDto == null) {
			return;
		}
		
		Path projectBundleSpaceDirectoryFullPath = developmentEnvironmentDto.getProjectBundleSpaceDirectoryFullPath();
		String projectName = developmentEnvironmentDto.getProjectName();
		String projectIdNoteSuffix = developmentEnvironmentDto.getProjectIdNoteSuffix();
		String branchName = developmentEnvironmentDto.getBranchName();
		String intent = developmentEnvironmentDto.getIntent();
		String bundleDirectoryNamePrefix = developmentEnvironmentDto.getBundleDirectoryNamePrefix(); 
		EnvironmentContextEnum environmentContext = developmentEnvironmentDto.getEnvironmentContext();
		
		String projectBundleDirectoryName = developmentEnvironmentDto.getProjectBundleDirectoryName();
		Path bundleDirectoryPath = developmentEnvironmentDto.getBundleDirectoryPath();
		Path workspaceDirectoryPath = developmentEnvironmentDto.getWorkspaceDirectoryPath();
		Path projectDirectoryPath = developmentEnvironmentDto.getProjectDirectoryPath();
		
		@SuppressWarnings("unused")
		Boolean cleanQuarantineFlag = developmentEnvironmentDto.getCleanQuarantineFlag();
		
		Path eclipseAppFileLocationPath = springConfigurationConstants.getEclipseAppFileLocationPath();
		Path templateWorkspaceDirectoryPath = springConfigurationConstants.getTemplateWorkspaceDirectoryPath();

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
			
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// START - Clean the quarantine flag xattr from files in Eclipse.app dir
			// Nah, never do this...
			
//			if (Boolean.TRUE.equals(cleanQuarantineFlag)) {
//				// Command to run: "find . -iname '*' -print0 | xargs -0 xattr -d com.apple.quarantine"
//				String[] cleanQuarantineFlagCommandStringArray = {"find", eclipseAppFileLocationPath.toString(), "-iname", "'*'", "-print0", "|", "xargs", "-0", "xattr", "-d", "com.apple.quarantine"};
//				System.out.println("About to clean quarantine flag from all files (recursively through subdirs) in dir: " + eclipseAppFileLocationPath.toString());
//				String cleanQuarantineFlagCommandResults = UtilsMacCommand.run(cleanQuarantineFlagCommandStringArray);
//				System.out.println("cleanQuarantineFlagCommandResults: " + cleanQuarantineFlagCommandResults);
//			}
			
			// END - Clean the quarantine flag xattr from files in Eclipse.app dir
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			
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
			
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// START - Copy Eclipse.app from source to bundle dir
			
			Path bundleEclipseAppPath = Paths.get(bundleDirectoryPath.toString(), "Eclipse.app");
			
			// Exit if this dir already exists
			if (Files.exists(bundleEclipseAppPath)) {
				System.out.println("EXITING AND Not copying Eclipse.app, it already exists at: " + bundleEclipseAppPath.toString());
				return;
			}
			
			// Finally... https://stackoverflow.com/questions/37384068/how-to-copy-and-replace-an-application-via-terminal-in-mac-os
			String[] copyEclipseAppCommand = {"/bin/cp", "-rf", eclipseAppFileLocationPath.toString(), bundleEclipseAppPath.toString()};
			System.out.println("About to copy a fresh Eclipse from: " + eclipseAppFileLocationPath.toString() + " to: " + bundleEclipseAppPath.toString());
			String copyEclipseAppCommandResults = UtilsMacCommand.run(copyEclipseAppCommand);
			System.out.println(copyEclipseAppCommandResults);
//				Runtime.getRuntime().exec(copyEclipseAppCommand);
			System.out.println("Copied a fresh Eclipse from: " + eclipseAppFileLocationPath.toString() + " to: " + bundleEclipseAppPath.toString());
			
			// END - Copy Eclipse.app from source to bundle dir
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			
			// Create project directory, as long as it doesn't exist already (fat chance)
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
			
			Path templateWorkspaceMetadataDirectoryPath = Paths.get(templateWorkspaceDirectoryPath.toString(), ".metadata");
			String[] copyWorkspaceMedataCommand = 
				{"/bin/cp", "-rf", 
					templateWorkspaceMetadataDirectoryPath.toString(), 
					workspaceDirectoryPath.toString()};
			System.out.println("Copying: " +  templateWorkspaceMetadataDirectoryPath.toString() + " to: " + workspaceDirectoryPath.toString());
			String copyWorkspaceMedataCommandResults = UtilsMacCommand.run(copyWorkspaceMedataCommand);
			System.out.println(copyWorkspaceMedataCommandResults);
			System.out.println("Copied: " +  templateWorkspaceMetadataDirectoryPath.toString() + " to: " + workspaceDirectoryPath.toString());
			
			Path orgEclipseUiIdePrefsFilePath = Paths.get(templateWorkspaceDirectoryPath.toString(), "org.eclipse.ui.ide.prefs");
			System.out.println("orgEclipseUiIdePrefsFilePath: " +  orgEclipseUiIdePrefsFilePath.toString());
			Path eclipseAppConfigurationSettingsFilePath = Paths.get(bundleEclipseAppPath.toString(), "Contents/Eclipse/configuration/.settings/org.eclipse.ui.ide.prefs");
			System.out.println("eclipseAppConfigurationSettingsFilePath: " +  eclipseAppConfigurationSettingsFilePath.toString());

			// Edit org.eclipse.ui.ide.prefs
			// Read in prefs as props file
			System.out.println("About to edit: " + orgEclipseUiIdePrefsFilePath);
			final String workspaceDirectoryPathString = workspaceDirectoryPath.toString();
			try (Stream<String> lines = Files.lines(eclipseAppConfigurationSettingsFilePath)) {
				System.out.println("Waiting a sec before writing to prefs file, to make sure it's there...");
				Thread.sleep(1000);
				List<String> replaced = lines
//						.map(line-> line.replaceAll("(.*setMongoStage.*)", "//$&"))
						.map(line-> line.replaceAll("replace_me", workspaceDirectoryPathString))
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
			
			environmentContext.help(developmentEnvironmentDto);
			
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
}
