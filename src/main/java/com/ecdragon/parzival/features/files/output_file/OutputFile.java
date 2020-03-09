package com.ecdragon.parzival.features.files.output_file;

import java.nio.file.Path;

import com.ecdragon.parzival.features.files.CodeGeneratorFile;

public class OutputFile extends CodeGeneratorFile {

	private Path pathFromRootToOutputFileProject;
	private Path pathFromOutputFileProjectToOutputFile;
	
	@Override
	public void buildDirectoryPathFull() {
		super.buildDirectoryPathFull(
				pathFromRootToOutputFileProject, 
				pathFromOutputFileProjectToOutputFile
				);
	}

	public Path getPathFromRootToOutputFileProject() {
		return pathFromRootToOutputFileProject;
	}
	public void setPathFromRootToOutputFileProject(Path pathFromRootToOutputFileProject) {
		this.pathFromRootToOutputFileProject = pathFromRootToOutputFileProject;
	}
	public Path getPathFromOutputFileProjectToOutputFile() {
		return pathFromOutputFileProjectToOutputFile;
	}
	public void setPathFromOutputFileProjectToOutputFile(Path pathFromOutputFileProjectToOutputFile) {
		this.pathFromOutputFileProjectToOutputFile = pathFromOutputFileProjectToOutputFile;
	}
}
