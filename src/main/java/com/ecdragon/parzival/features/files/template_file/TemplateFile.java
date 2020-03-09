package com.ecdragon.parzival.features.files.template_file;

import java.nio.file.Path;

import com.ecdragon.parzival.features.files.CodeGeneratorFile;

public class TemplateFile extends CodeGeneratorFile {

	private Path pathFromRootToTemplateFileProject;
	private Path pathFromTemplateFileProjectToTemplateFile;
	
	/**
	 * To build a TemplateFile using no-arg constructor:
	 *   - Set fileName and fileExtension
	 *   - Call buildFileNameFull
	 *   - Set the 2 paths here
	 *   - Call buildDirectoryPathFull() on this object
	 */
	public TemplateFile() {
		// No-arg constructor
	}
	
	public TemplateFile(
			String fileName,
			String fileExtension,
			Path pathFromRootToTemplateFileProject,
			Path pathFromTemplateFileProjectToTemplateFile
			) {
		super(fileName, fileExtension, pathFromRootToTemplateFileProject, pathFromTemplateFileProjectToTemplateFile);
		this.pathFromRootToTemplateFileProject = pathFromRootToTemplateFileProject;
		this.pathFromTemplateFileProjectToTemplateFile = pathFromTemplateFileProjectToTemplateFile;
	}
	
	@Override
	public void buildDirectoryPathFull() {
		super.buildDirectoryPathFull(
				pathFromRootToTemplateFileProject, 
				pathFromTemplateFileProjectToTemplateFile
				);
	}
	
	public Path getPathFromRootToTemplateFileProject() {
		return pathFromRootToTemplateFileProject;
	}
	public void setPathFromRootToTemplateFileProject(Path pathFromRootToTemplateFileProject) {
		this.pathFromRootToTemplateFileProject = pathFromRootToTemplateFileProject;
	}
	public Path getPathFromTemplateFileProjectToTemplateFile() {
		return pathFromTemplateFileProjectToTemplateFile;
	}
	public void setPathFromTemplateFileProjectToTemplateFile(Path pathFromTemplateFileProjectToTemplateFile) {
		this.pathFromTemplateFileProjectToTemplateFile = pathFromTemplateFileProjectToTemplateFile;
	}
}
