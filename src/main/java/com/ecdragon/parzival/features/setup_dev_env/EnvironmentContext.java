package com.ecdragon.parzival.features.setup_dev_env;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * From: https://git-scm.com/book/en/v2/Git-on-the-Server-The-Protocols
To clone a Git repository over SSH, you can specify an ssh:// URL like this:
$ git clone ssh://[user@]server/project.git
Or you can use the shorter scp-like syntax for the SSH protocol:
$ git clone [user@]server:project.git
 */
public enum EnvironmentContext {
	
	AbcBitbucket(true, "Data/projects", "abc", "abc-bitbucket", null, "Firstname Lastname", 
			"user@abc.com", null, GitProtocol.ssh_short, null, null, Boolean.TRUE) {
		@Override
		public void help(SetupDevelopmentEnvironment setupDevelopmentEnvironment) {
			// Do nothing
		}
	}
	;
	
	private EnvironmentContext(
			boolean useUserHomeDirAsBaseBase,
			String projectBundleSpaceDirectoryBasePathString,
			String projectBundleSpaceDirectoryName,
			String gitHost,
			String gitAccountName,
			String gitRepositoryUserName,
			String gitRepositoryUserEmail,
			String gitAccountUsername,
			GitProtocol gitProtocol,
			String gitHttpsUsername,
			String gitHttpsPassword,
			Boolean useGitExtension
			) {
		this.gitRepositoryUserName = gitRepositoryUserName;
		this.gitRepositoryUserEmail = gitRepositoryUserEmail;
		this.projectBundleSpaceDirectoryBasePath = 
				useUserHomeDirAsBaseBase ?
						((projectBundleSpaceDirectoryBasePathString == null) ?
								// Use just home dir
								getUserHomeDirPath()
								// Use home dir + specified path added
								: Paths.get(getUserHomeDirPath().toString(), projectBundleSpaceDirectoryBasePathString))
						// Use just specified path
						: Paths.get(projectBundleSpaceDirectoryBasePathString);
		this.projectBundleSpaceDirectoryName = projectBundleSpaceDirectoryName;
		this.gitAccountUsername = gitAccountUsername;
		generateProjectBundleSpaceDirectoryFullPath();
		this.setGitSshUrlHost(gitHost);
		this.gitAccountName = gitAccountName;
		generateGitSshUrl();
		System.out.println(this.name() + " : " + this.gitSshUrl + " : " + this.gitAccountName + " : " + this.gitRepositorySshUrlBaseName);
		this.gitProtocol = gitProtocol;
		this.gitHost = gitHost;
		this.gitHttpsUsername = gitHttpsUsername;
		this.gitHttpsPassword = gitHttpsPassword;
		this.useGitExtension = useGitExtension;
		generateGitHttpsUrl();
	}
	
	private static Path userHomeDirPath;
	public static Path getUserHomeDirPath() {
		if (userHomeDirPath == null) {
			userHomeDirPath = Paths.get(System.getProperty("user.home")); 
		}
		return userHomeDirPath;
	}
	
	private Path projectBundleSpaceDirectoryBasePath;
	private String projectBundleSpaceDirectoryName;
	private Path projectBundleSpaceDirectoryFullPath;
	// This can be an entry in .ssh/config !!
	private String gitSshUrlHost;
	private String gitSshUrl;
	private String gitAccountName;
	private String gitRepositorySshUrlBaseName;
	private String gitRepositoryUserName;
	private String gitRepositoryUserEmail;
	private String gitAccountUsername;
	private GitProtocol gitProtocol;
	private String gitHttpsUsername;
	private String gitHttpsPassword;
	private String gitHttpsUrl;
	private String gitHost;
	private Boolean useGitExtension;
	
	public abstract void help(SetupDevelopmentEnvironment setupDevelopmentEnvironment);

	public String generateGitCloneCommand(
			String projectName,
			String branchName
			) {
		
		String gitExtensionSnippet = "";
		if (!Boolean.FALSE.equals(getUseGitExtension())) {
			gitExtensionSnippet = ".git";
		}
		String gitRepositoryUrlProjectSnippet = "/" + projectName + gitExtensionSnippet;
		GitProtocol gitProtocolToUse = getGitProtocol();
		String gitRepositoryUrlFull = null;
		
		String branchParam = "";
		if (branchName != null && !branchName.isEmpty()) {
			branchParam = " --branch " + branchName + " ";
		}
		String gitCloneCommand = null;
		switch (gitProtocolToUse) {
			case ssh_short:
				gitRepositoryUrlFull = getGitRepositorySshUrlBaseName() + gitRepositoryUrlProjectSnippet;
				gitCloneCommand = "git clone " + gitRepositoryUrlFull + branchParam;
				break;
			case https:
				gitRepositoryUrlFull = getGitHttpsUrl() + gitRepositoryUrlProjectSnippet;
				gitCloneCommand = "git clone " + gitRepositoryUrlFull + branchParam;
				break;
			case ssh_long:
//				"ssh://codecommit-bs/v1/repos/codecommit_middle_earth";
				String gitAccountNameSnippet = getGitAccountName();
				if (gitAccountNameSnippet != null) {
					gitAccountNameSnippet += "/";
				}
				else {
					gitAccountNameSnippet = "";
				}
				gitCloneCommand = "git clone " + "ssh://" + getGitHost() + "/" + gitAccountNameSnippet + projectName;
				break;
			default:
				break;
		}

		System.out.println("git clone command: " + gitCloneCommand);
		return gitCloneCommand;
	}

	public Path getProjectBundleSpaceDirectoryBasePath() {
		return projectBundleSpaceDirectoryBasePath;
	}
	public void setProjectBundleSpaceDirectoryBasePath(Path projectBundleSpaceDirectoryBasePath) {
		this.projectBundleSpaceDirectoryBasePath = projectBundleSpaceDirectoryBasePath;
	}
	public String getProjectBundleSpaceDirectoryName() {
		return projectBundleSpaceDirectoryName;
	}
	public void setProjectBundleSpaceDirectoryName(String projectBundleSpaceDirectoryName) {
		this.projectBundleSpaceDirectoryName = projectBundleSpaceDirectoryName;
	}
	public Path getProjectBundleSpaceDirectoryFullPath() {
		return projectBundleSpaceDirectoryFullPath;
	}
	public void setProjectBundleSpaceDirectoryFullPath(Path projectBundleSpaceDirectoryFullPath) {
		this.projectBundleSpaceDirectoryFullPath = projectBundleSpaceDirectoryFullPath;
	}
	public void generateProjectBundleSpaceDirectoryFullPath() {
		this.projectBundleSpaceDirectoryFullPath = 
				projectBundleSpaceDirectoryName == null ?
						projectBundleSpaceDirectoryBasePath
						: Paths.get(projectBundleSpaceDirectoryBasePath.toString(), projectBundleSpaceDirectoryName);
	}
	public String getGitSshUrlHost() {
		return gitSshUrlHost;
	}
	public void setGitSshUrlHost(String gitSshUrlHost) {
		this.gitSshUrlHost = gitSshUrlHost;
		this.gitSshUrl = "git@" + gitSshUrlHost + ":";
	}
	public String getGitSshUrl() {
		return gitSshUrl;
	}
	public void setGitSshUrl(String gitSshUrl) {
		this.gitSshUrl = gitSshUrl;
	}
	public String getGitAccountName() {
		return gitAccountName;
	}
	public void setGitAccountName(String gitAccountName) {
		this.gitAccountName = gitAccountName;
		generateGitSshUrl();
		generateGitHttpsUrl();
	}
	public void generateGitSshUrl() {
		this.gitRepositorySshUrlBaseName = 
				gitSshUrl 
				+ (gitAccountName == null ? "" : gitAccountName);
	}
	public void generateGitHttpsUrl() {
		// Should be: https://username:password@gitHost/gitAccountName/repoName.git
		String passwordSnippet = gitHttpsPassword == null ? "" : ":" + gitHttpsPassword;
		this.gitHttpsUrl = "https://" + gitHttpsUsername + passwordSnippet + "@" + gitHost + "/" + gitAccountName;
		System.out.println("https url: " + this.gitHttpsUrl);
	}
	public String getGitRepositorySshUrlBaseName() {
		return gitRepositorySshUrlBaseName;
	}
	public String getGitRepositorySshUrlBaseName(String gitAccountName) {
		return gitSshUrl + gitAccountName;
	}
	public void setGitRepositorySshUrlBaseName(String gitRepositorySshUrlBaseName) {
		this.gitRepositorySshUrlBaseName = gitRepositorySshUrlBaseName;
	}
	public String getGitRepositoryUserName() {
		return gitRepositoryUserName;
	}
	public void setGitRepositoryUserName(String gitRepositoryUserName) {
		this.gitRepositoryUserName = gitRepositoryUserName;
	}
	public String getGitRepositoryUserEmail() {
		return gitRepositoryUserEmail;
	}
	public void setGitRepositoryUserEmail(String gitRepositoryUserEmail) {
		this.gitRepositoryUserEmail = gitRepositoryUserEmail;
	}
	public String getGitAccountUsername() {
		return gitAccountUsername;
	}
	public void setGitAccountUsername(String gitAccountUsername) {
		this.gitAccountUsername = gitAccountUsername;
	}
	public GitProtocol getGitProtocol() {
		return gitProtocol;
	}
	public void setGitProtocol(GitProtocol gitProtocol) {
		this.gitProtocol = gitProtocol;
	}
	public String getGitHttpsUsername() {
		return gitHttpsUsername;
	}
	public void setGitHttpsUsername(String gitHttpsUsername) {
		this.gitHttpsUsername = gitHttpsUsername;
	}
	public String getGitHttpsPassword() {
		return gitHttpsPassword;
	}
	public void setGitHttpsPassword(String gitHttpsPassword) {
		this.gitHttpsPassword = gitHttpsPassword;
	}
	public String getGitHttpsUrl() {
		return gitHttpsUrl;
	}
	public String getGitHost() {
		return gitHost;
	}
	public void setGitHost(String gitHost) {
		this.gitHost = gitHost;
	}
	public Boolean getUseGitExtension() {
		return useGitExtension;
	}
	public void setUseGitExtension(Boolean useGitExtension) {
		this.useGitExtension = useGitExtension;
	}
}
