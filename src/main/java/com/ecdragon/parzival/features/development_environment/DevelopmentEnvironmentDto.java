package com.ecdragon.parzival.features.development_environment;

import java.lang.invoke.MethodHandles;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ecdragon.parzival.features.environment_context.EnvironmentContextEnum;

public class DevelopmentEnvironmentDto {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private Path projectBundleSpaceDirectoryFullPath;
	private String projectName;
	private String projectIdNoteSuffix;
	private String branchName;
	private String intent;
	private String bundleDirectoryNamePrefix; 
	private EnvironmentContextEnum environmentContext;
	
	private String projectBundleDirectoryName;
	private Path bundleDirectoryPath;
	private Path workspaceDirectoryPath;
	private Path projectDirectoryPath;
	private Integer seed;
	
	private Boolean cleanQuarantineFlag;
	
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
	public EnvironmentContextEnum getEnvironmentContext() {
		return environmentContext;
	}
	public void setEnvironmentContext(EnvironmentContextEnum environmentContext) {
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
	public Boolean getCleanQuarantineFlag() {
		return cleanQuarantineFlag;
	}
	public void setCleanQuarantineFlag(Boolean cleanQuarantineFlag) {
		this.cleanQuarantineFlag = cleanQuarantineFlag;
	}
}
