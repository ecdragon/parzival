package com.ecdragon.parzival.spring.configuration;

import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.ecdragon.parzival.utils.UtilsParzival;

@Configuration
public class SpringConfigurationConstants {
	
    private static final Logger logger = LoggerFactory.getLogger(SpringConfigurationConstants.class);

    static {
    	logger.info("In " + MethodHandles.lookup().lookupClass().getSimpleName() + ": static init block.");
    }
    
    /**
     * This is the path from app dir to eclipse template dir. Default: eclipse_workspace_templates/2019-12-R_1
     */
    private String pathStringFromAppWorkingDirToEclipseTemplateDir;
    /**
     * This is the folder within the eclipse directory path that contains template files, default = "template_workspace"
     */
    private String templateWorkspacePath;
    /**
     * This is the name of the Eclipse app file. Default: "Eclipse.app"
     */
    private String eclipseAppName;
    private Path templateEclipseDirectoryPath;
    private Path templateWorkspaceDirectoryPath;
    private Path eclipseAppFileLocationPath;

	private String springProfilesActive;

	private String appVersion;
	private String appName;

	private String serverPort;
	
	public String getPathStringFromAppWorkingDirToEclipseTemplateDir() {
		return pathStringFromAppWorkingDirToEclipseTemplateDir;
	}
	@Value("${pathStringFromAppWorkingDirToEclipseTemplateDir:eclipse_workspace_templates/2019-12-R_1}")
	public void setPathStringFromAppWorkingDirToEclipseTemplateDir(
			String pathStringFromAppWorkingDirToEclipseTemplateDir) {
		this.pathStringFromAppWorkingDirToEclipseTemplateDir = pathStringFromAppWorkingDirToEclipseTemplateDir;
	}
	public String getTemplateWorkspacePath() {
		return templateWorkspacePath;
	}
	@Value("${templateWorkspacePath:template_workspace}")
	public void setTemplateWorkspacePath(String templateWorkspacePath) {
		this.templateWorkspacePath = templateWorkspacePath;
	}
	public String getEclipseAppName() {
		return eclipseAppName;
	}
	@Value("${eclipseAppName:Eclipse.app}")
	public void setEclipseAppName(String eclipseAppName) {
		this.eclipseAppName = eclipseAppName;
	}
	public Path getTemplateEclipseDirectoryPath() {
		if (templateEclipseDirectoryPath == null) {
			String pathLocal = "";
			if (getPathStringFromAppWorkingDirToEclipseTemplateDir() != null 
					&& !getPathStringFromAppWorkingDirToEclipseTemplateDir().isBlank()) {
				pathLocal = getPathStringFromAppWorkingDirToEclipseTemplateDir();
			}
			Path appWorkingDirPath = UtilsParzival.getThisAppWorkingDirectoryPath();
			templateEclipseDirectoryPath = 
					Paths.get(
							appWorkingDirPath.toString(), 
							pathLocal
							);
		}
		return templateEclipseDirectoryPath;
	}
	public void setTemplateEclipseDirectoryPath(Path templateEclipseDirectoryPath) {
		this.templateEclipseDirectoryPath = templateEclipseDirectoryPath;
	}
	public Path getTemplateWorkspaceDirectoryPath() {
		if (templateWorkspaceDirectoryPath == null) {
			String templateWorkspacePathLocal = "";
			if (getTemplateWorkspacePath() != null && !getTemplateWorkspacePath().isBlank()) {
				templateWorkspacePathLocal = getTemplateWorkspacePath();
			}
			templateWorkspaceDirectoryPath = 
					Paths.get(
							getTemplateEclipseDirectoryPath().toString(), 
							templateWorkspacePathLocal
							);
		}
		return templateWorkspaceDirectoryPath;
	}
	public void setTemplateWorkspaceDirectoryPath(Path templateWorkspaceDirectoryPath) {
		this.templateWorkspaceDirectoryPath = templateWorkspaceDirectoryPath;
	}
	public Path getEclipseAppFileLocationPath() {
		if (eclipseAppFileLocationPath == null) {
			eclipseAppFileLocationPath = 
					Paths.get(
							getTemplateEclipseDirectoryPath().toString(), 
							getEclipseAppName()
							);
		}
		return eclipseAppFileLocationPath;
	}
	public void setEclipseAppFileLocationPath(Path eclipseAppFileLocationPath) {
		this.eclipseAppFileLocationPath = eclipseAppFileLocationPath;
	}
	
	public String getSpringProfilesActive() {
		return springProfilesActive;
	}
	@Value("${spring.profiles.active:}")
	public void setSpringProfilesActive(String springProfilesActive) {
		this.springProfilesActive = springProfilesActive;
	}
	
	public String getAppVersion() {
		return appVersion;
	}
	@Value("${app.version}")
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public String getAppName() {
		return appName;
	}
	@Value("${app.name}")
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
    public String getServerPort() {
		return serverPort;
	}
	@Value("${server.port}")
    public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
}
