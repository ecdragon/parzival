package com.ecdragon.parzival.features.git;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ecdragon.parzival.features.setup_dev_env.RunMacCommand;
import com.ecdragon.parzival.utils.UtilsParzival;

public class GitRepository {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	/*
	 * Doing it command-style...
	 * how to add username / email?
	 * https://stackoverflow.com/questions/34597186/use-a-different-user-email-and-user-name-for-git-config-based-upon-remote-clone
	 * Basically:
	 * git clone ssh://git@git.mycorp.com:1234/groupA/projectA.git
	 * cd projectA
	 * git config user.email "mkobit@mycorp.com"
	 * git config user.name "m.kobit"
	 * 
	 * ...or another way is to init, configure, then pull (vs clone then config)
	 * https://serverfault.com/questions/815043/how-to-give-username-password-to-git-clone-in-a-script-but-not-store-credential
	 * mkdir repo
	 * cd repo
	 * git init
	 * git config user.email "email"
	 * git config user.name "user"
	 * git pull https://user:password@github.com/name/repo.git master
	 */
	
	public static boolean cloneRepository(
//			String url, 
			Path parentDirectoryPath,
			String repositoryName,
//			String branchName,
			String repositoryLocalUserName,
			String repositoryLocalUserEmail,
			String gitCloneCommand
			) {
		
		try {
			String methodLabel = "In cloneRepository(): ";
			
//			String branchParam = "";
//			if (branchName != null && !branchName.isEmpty()) {
//				branchParam = " --branch " + branchName + " ";
//			}
//			String gitCloneCommand = "git clone " + url + branchParam;
			logger.info("About to run clone command: " + gitCloneCommand);
			// Run git clone command
			RunMacCommand.runCommands(
					"cd " + parentDirectoryPath.toString(),
					gitCloneCommand
					);
			
			// Construct Path to local repo folder
			Path localRepoFolderPath = Paths.get(parentDirectoryPath.toString(), repositoryName);
			
			// Construct path to .git file
			Path localRepoGitFilePath = Paths.get(localRepoFolderPath.toString(), ".git");
			
			// Check to see if .git is there...
			if (!Files.exists(localRepoGitFilePath)) {
				logger.error(methodLabel + "localRepoGitFilePath doesn't exist, git clone must have failed! Should have existed: " + localRepoGitFilePath.toString());;
				return false;
			}
			
			// If need to create local user, do that
			if (!UtilsParzival.isNullOrEmpty(repositoryLocalUserName)) {
				RunMacCommand.runCommands(
						"cd " + localRepoFolderPath.toString(),
						"git config user.name \"" + repositoryLocalUserName + "\""
						);
			}
			
			// Pause
			Thread.sleep(50L);
			
			// If need to create local email, do that
			if (!UtilsParzival.isNullOrEmpty(repositoryLocalUserEmail)) {
				RunMacCommand.runCommands(
						"cd " + localRepoFolderPath.toString(),
						"git config user.email \"" + repositoryLocalUserEmail + "\""
						);
			}
			
			// Pause
			Thread.sleep(50L);
			
			return true;
		}	
		catch (Exception e) {
			logger.error("Exception thrown while cloning repo", e);
			return false;
		}
	}
	
	public static boolean cloneRepository2(
			String url, 
			File emptyDirectoryPath, 
			String branchName,
			String repositoryLocalUserName,
			String repositoryLocalUserEmail
			) {
		
		CloneCommand cloneCommand = Git.cloneRepository();
		cloneCommand.setURI(url);
		cloneCommand.setDirectory(emptyDirectoryPath);
		if (branchName != null && !branchName.isEmpty()) {
			cloneCommand.setBranch(branchName);
		}
		try (Git gitObject = cloneCommand.call()) {
//				Git.cloneRepository()
//				.setURI(url)
//				.setDirectory(emptyDirectoryPath)
//				.call()
			
			Repository gitRepository = gitObject.getRepository();

			// Get the repo dir
			File gitRepositoryDirectoryPath = gitRepository.getDirectory();
			
			// Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
			System.out.println("Success! Repo at: " + gitRepositoryDirectoryPath.toString());
			
			// Get the config for editing
			StoredConfig gitRepositoryConfig = gitRepository.getConfig();
			
			// Optionally set username or email for this repository
//			setUsernameOrEmailForGitRepository(emptyDirectoryPath, repositoryLocalUserName, repositoryLocalUserEmail);
			boolean saveTheConfig = false;
			if (repositoryLocalUserName != null && !repositoryLocalUserName.isEmpty()) {
				saveTheConfig = true;
				gitRepositoryConfig.setString("user", null, "name", repositoryLocalUserName);
			}
			if (repositoryLocalUserEmail != null && !repositoryLocalUserEmail.isEmpty()) {
				saveTheConfig = true;
				gitRepositoryConfig.setString("user", null, "email", repositoryLocalUserEmail);
			}
			if (saveTheConfig) {
				gitRepositoryConfig.save();
			}
			
			// A little sleep to let git config persist
			Thread.sleep(100L);
			
			// Print the resulting configuration for this repo
			File gitRepositoryConfigFile = new File(gitRepositoryDirectoryPath, "config");
			try (Scanner gitRepositoryConfigFileScanner = new Scanner(gitRepositoryConfigFile)) {
				while (gitRepositoryConfigFileScanner.hasNextLine()) {
					System.out.println(gitRepositoryConfigFileScanner.nextLine());
				}
			}
			catch (Exception e) {
				throw e;
			}
			
			return true;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	}
	
	public static void setUsernameOrEmailForGitRepository(
			File gitRepositoryDirectoryFullPath,
			String repositoryLocalUserName,
			String repositoryLocalUserEmail
			)
					 throws Exception {
		
		String methodLabel = "In setUsernameAndEmailForGitRepository(): ";
		try {
//			File local = new File(GitBlitSuite.REPOSITORIES, "refchecks/verify-wc");
			if (gitRepositoryDirectoryFullPath == null) {
				throw new Exception(methodLabel + "gitRepositoryDirectoryFullPath cannot be null");
			}
			Git gitObject = Git.open(gitRepositoryDirectoryFullPath);
			if (repositoryLocalUserName != null && !repositoryLocalUserName.isEmpty()) {
				gitObject.getRepository().getConfig().setString("user", null, "name", repositoryLocalUserName);
			}
			if (repositoryLocalUserEmail != null && !repositoryLocalUserEmail.isEmpty()) {
				gitObject.getRepository().getConfig().setString("user", null, "email", repositoryLocalUserEmail);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
