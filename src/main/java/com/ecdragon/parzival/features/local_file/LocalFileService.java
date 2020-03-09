package com.ecdragon.parzival.features.local_file;

import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ecdragon.parzival.utils.UtilsParzival;

@Component
public class LocalFileService {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	public String convertLocalFileToString(String fullPathToLocalFileDirectoryString, String localFileName) {
		
		String methodLabel = "convertLocalFileToString(String fullPathToLocalFileDirectoryString): ";
		
		try {
			Path fullPathToLocalFilePath = UtilsParzival.getPath(fullPathToLocalFileDirectoryString, localFileName);
			String fileContents = convertLocalFileToString(fullPathToLocalFilePath);
			return fileContents;
		}
		catch (Exception e) {
			logger.error(methodLabel + "Exception converting string to file to string: " + e.getMessage(), e);
			return null;
		}
	}

	public String convertLocalFileToString(String fullPathToFileString) {
		
		String methodLabel = "convertLocalFileToString(String fullPathToFileString): ";
		
		try {
			Path fullPathToFilePath = Paths.get(fullPathToFileString);
			String fileContents = convertLocalFileToString(fullPathToFilePath);
			return fileContents;
		}
		catch (Exception e) {
			logger.error(methodLabel + "Exception converting string to file to string: " + e.getMessage(), e);
			return null;
		}
	}
	
	public String convertLocalFileToString(Path fullPathToFilePath) {
		
		String methodLabel = "convertLocalFileToString(Path fullPathToFilePath): ";
		
		try {
			String fileContents = Files.readString(fullPathToFilePath);
			return fileContents;
		}
		catch (Exception e) {
			logger.error(methodLabel + "Exception converting string to file to string: " + e.getMessage(), e);
			return null;
		}
	}
}
