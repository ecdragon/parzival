package com.ecdragon.parzival.features.environment_context;

import java.nio.file.Path;

import com.ecdragon.parzival.utils.GitProtocolEnum;

public class EnvironmentContextDto {

	private String name;
	private Path projectBundleSpaceDirectoryBasePath;
	private String projectBundleSpaceDirectoryName;
	private Path projectBundleSpaceDirectoryFullPath;
	private String gitSshUrlHost;
	private String gitSshUrl;
	private String gitAccountName;
	private String gitRepositorySshUrlBaseName;
	private String gitRepositoryUserName;
	private String gitRepositoryUserEmail;
	private String gitAccountUsername;
	private GitProtocolEnum gitProtocol;
	private String gitHttpsUsername;
	private String gitHttpsPassword;
	private String gitHttpsUrl;
	private String gitHost;
	private Boolean useGitExtension;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getGitSshUrlHost() {
		return gitSshUrlHost;
	}
	public void setGitSshUrlHost(String gitSshUrlHost) {
		this.gitSshUrlHost = gitSshUrlHost;
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
	}
	public String getGitRepositorySshUrlBaseName() {
		return gitRepositorySshUrlBaseName;
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
	public GitProtocolEnum getGitProtocol() {
		return gitProtocol;
	}
	public void setGitProtocol(GitProtocolEnum gitProtocol) {
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
	public void setGitHttpsUrl(String gitHttpsUrl) {
		this.gitHttpsUrl = gitHttpsUrl;
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
