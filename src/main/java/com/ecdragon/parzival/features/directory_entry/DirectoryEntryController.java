package com.ecdragon.parzival.features.directory_entry;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ecdragon.parzival.features.local_file.LocalFileService;
import com.ecdragon.parzival.utils.ScResponse;
import com.ecdragon.parzival.utils.UtilsParzival;

@Controller
@RequestMapping("/api/directory_entry")
public class DirectoryEntryController {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private LocalFileService localFileService;
	
	@RequestMapping(value="/sc/fetch-user-home")
	@ResponseBody
	public ScResponse fetchUserHome() {

		try {
			String userHomeDir = System.getProperty("user.home");
			ScResponse response = 
					ScResponse.createSuccessResponseWithOneDataRecord(userHomeDir);
			
			return response;
		} 
		catch (Exception e) {
			ScResponse errorResponse = ScResponse.createErrorResponse(e.getMessage());
			return errorResponse;
		}
	}
	
	@RequestMapping(value="/sc/fetch-files")
	@ResponseBody
	public ScResponse fetchFiles(
			@RequestParam(value = "pomXml", required = false) String pomXmlString,
			@RequestParam(value = "directoryPath", required = false) String directoryPathString,
			HttpServletRequest request
			) {
		
		try {
			Path directoryPath = UtilsParzival.getPath(directoryPathString);
			
			List<File> fileFilesInDirectoryPath = UtilsParzival.listFilesInDirectoryPath(directoryPath);
			
			List<DirectoryEntryDto> fileDirectoryEntries = 
					fileFilesInDirectoryPath
					.stream()
					.map(DirectoryEntryDto::convertFromFileToDirectoryEntryDto)
					.collect(Collectors.toList());
			
			ScResponse response = 
					ScResponse.createSuccessResponseWithListOfDataRecords(fileDirectoryEntries);
			
			return response;
		} 
		catch (Exception e) {
			
			ScResponse errorResponse = ScResponse.createErrorResponse(e.getMessage());
			
			return errorResponse;
		}
	}
	
	@RequestMapping(value="/sc/fetch-directories")
	@ResponseBody
	public ScResponse fetchDirectories(
			@RequestParam(value = "pomXml", required = false) String pomXmlString,
			@RequestParam(value = "directoryPath", required = false) String directoryPathString,
			HttpServletRequest request
			) {
		
		try {
			Path directoryPath = UtilsParzival.getPath(directoryPathString);
			
			List<File> directoryFilesInDirectoryPath = UtilsParzival.listDirectoriesInDirectoryPath(directoryPath);
			
			List<DirectoryEntryDto> directoryEntries = 
					directoryFilesInDirectoryPath
					.stream()
					.map(DirectoryEntryDto::convertFromFileToDirectoryEntryDto)
					.collect(Collectors.toList());
			
			if (directoryPathString != null && !directoryPathString.equals("/")) {
				directoryEntries.add(new DirectoryEntryDto(".."));
			}
			
			ScResponse response = 
					ScResponse.createSuccessResponseWithListOfDataRecords(directoryEntries);
			
			return response;
		} 
		catch (Exception e) {
			
			ScResponse errorResponse = ScResponse.createErrorResponse(e.getMessage());
			
			return errorResponse;
		}
	}
	
	public LocalFileService getLocalFileService() {
		return localFileService;
	}
	@Autowired
	public void setLocalFileService(LocalFileService localFileService) {
		this.localFileService = localFileService;
	}
}
