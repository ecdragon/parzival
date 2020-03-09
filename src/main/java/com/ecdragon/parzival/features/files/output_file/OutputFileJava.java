package com.ecdragon.parzival.features.files.output_file;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.ecdragon.parzival.utils.UtilsParzival;

public class OutputFileJava extends OutputFile {

	private Path pathFromOutputFileProjectToSourceMainJava = Paths.get("src/main/java");
	private Path packagePath;
	
	public static void main(String[] args) {
		System.out.println(Paths.get(Paths.get("data").toString(), Paths.get("src/main/java").toString()));
	}
	@Override
	public void buildDirectoryPathFull() {
		Path pathFromOutputFileProjectToOutputFile = 
				UtilsParzival.concatenatePaths(
						pathFromOutputFileProjectToSourceMainJava, 
						packagePath);
		this.setPathFromOutputFileProjectToOutputFile(pathFromOutputFileProjectToOutputFile);
		super.buildDirectoryPathFull();
	}

	public Path getPathFromOutputFileProjectToSourceMainJava() {
		return pathFromOutputFileProjectToSourceMainJava;
	}
	public void setPathFromOutputFileProjectToSourceMainJava(Path pathFromOutputFileProjectToSourceMainJava) {
		this.pathFromOutputFileProjectToSourceMainJava = pathFromOutputFileProjectToSourceMainJava;
	}
	public Path getPackagePath() {
		return packagePath;
	}
	public void setPackagePath(Path packagePath) {
		this.packagePath = packagePath;
	}
}
