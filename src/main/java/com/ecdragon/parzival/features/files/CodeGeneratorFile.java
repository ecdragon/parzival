package com.ecdragon.parzival.features.files;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.ecdragon.parzival.utils.UtilsParzival;

public class CodeGeneratorFile {

	private Path directoryPathFull;
	private String fileName;
	private String fileExtension;
	private String fileNameFull;
	private Path filePathFull;
	
	/**
	 * To build a CodeGeneratorFile using no-arg constructor:
	 *   - Set the 2 paths here
	 *   - Call buildDirectoryPathFull() on this object
	 *   - Set fileName and fileExtension and directoryPathFull
	 *   - Call buildFileNameFull
	 */
	public CodeGeneratorFile() {
		// No-arg constructor
	}
	
	public CodeGeneratorFile(
			String fileName,
			String fileExtension,
			Path... directoryPaths
			) {
		// Build fileNameFull - uses name + ext
		this.fileName = fileName;
		this.fileExtension = fileExtension;
		this.buildFileNameFull(fileName, fileExtension);
		
		// Build directoryPathFull - uses dir + fileNameFull
		this.buildDirectoryPathFull(directoryPaths);
		
		// Build the full file path
		this.buildFilePathFull();
	}
	
	public void buildFileNameFull(String fileName, String fileExtension) {
		String fileNameFullBuilt = null;
		
		// If both are null, name = null
		if (fileName == null && fileExtension == null) {
			fileNameFullBuilt = null;
		}
		// Else if name = null, check if local name was set and use it if so with non-null extension
		else if (fileName == null) {
			if (this.fileName != null) {
				fileNameFullBuilt = this.fileName + "." + fileExtension;
			}
			else {
				fileNameFullBuilt = "." + fileExtension;
			}
			this.fileExtension = fileExtension;
		}
		// Else if extension = null, check if local extension was set and use it if so with non-null name
		else if (fileExtension == null) {
			if (this.fileExtension != null) {
				fileNameFullBuilt = fileName + "." + this.fileExtension;
			}
			else {
				fileNameFullBuilt = fileName;
			}
			this.fileName = fileName;
		}
		// Else both are not null to use what's passed in
		else {
			fileNameFullBuilt = fileName + "." + fileExtension;
			this.fileName = fileName;
			this.fileExtension = fileExtension;
		}
		this.fileNameFull = fileNameFullBuilt;
		return;
	}
	
	public void buildDirectoryPathFull() {
		// Base version does nothing, since there's no path
	}
	
	public void buildFilePathFull() {
		if (this.directoryPathFull != null) {
			String directoryPathFullString = this.directoryPathFull.toString();
			if (this.fileNameFull != null) {
				this.filePathFull = Paths.get(directoryPathFullString, this.fileNameFull);
			}
			else {
				this.filePathFull = Paths.get(directoryPathFullString);
			}
		}
		else {
			if (this.fileNameFull != null) {
				this.filePathFull = Paths.get(this.fileNameFull);
			}
		}
	}
	
	public void buildDirectoryPathFull(Path... paths) {
		if (paths == null) {
			return;
		}
		for (Path path : paths) {
			if (path == null) {
				continue;
			}
			else {
				directoryPathFull = UtilsParzival.concatenatePaths(directoryPathFull, path);
			}
		}
	}
	
	public Path getDirectoryPathFull() {
		return directoryPathFull;
	}
	public void setDirectoryPathFull(Path directoryPathFull) {
		this.directoryPathFull = directoryPathFull;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileExtension() {
		return fileExtension;
	}
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
	public String getFileNameFull() {
		return fileNameFull;
	}
	public void setFileNameFull(String fileNameFull) {
		this.fileNameFull = fileNameFull;
	}
	public Path getFilePathFull() {
		return filePathFull;
	}
	public void setFilePathFull(Path filePathFull) {
		this.filePathFull = filePathFull;
	}
}
