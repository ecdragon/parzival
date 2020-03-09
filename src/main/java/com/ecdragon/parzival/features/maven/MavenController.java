package com.ecdragon.parzival.features.maven;

import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ecdragon.parzival.features.local_file.LocalFileService;
import com.ecdragon.parzival.utils.ApiResponse;
import com.ecdragon.parzival.utils.ScResponse;
import com.ecdragon.parzival.utils.UtilsParzival;

@Controller
@RequestMapping("/api/maven")
public class MavenController {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	// Autowired properties
	private MavenService mavenService;
	private LocalFileService localFileService;
	
	/**
	 * Examples:
	 *   http://localhost:8183/api/maven/findDependenciesInPomXml?pomXml=
	 */
	@RequestMapping(value="/findDependenciesInPomXml")
	@ResponseBody
	public ApiResponse findDependenciesInPomXml(
			@RequestParam(value = "pomXml", required = false) String pomXmlString
			) {
		
		try {
			List<MavenDependency> results = mavenService.findDependenciesInPomXmlString(pomXmlString);
			
			ApiResponse response = 
					ApiResponse.createSuccessResponseWithListOfDataRecords(results);
			
			return response;
		} 
		catch (Exception e) {
			
			ApiResponse errorResponse = ApiResponse.createErrorResponse(e.getMessage());
			
			return errorResponse;
		}
	}
	
	/**
	 * Examples:
	 *   http://localhost:8183/api/maven/cleanDependenciesInPomXmlLocalFileToLocalFile?fullPathToLocalFileDirectory=X&localFileName=Y
	 *   http://localhost:8183/api/maven/cleanDependenciesInPomXmlLocalFileToLocalFile?fullPathToLocalFileDirectory=/Users/user/Data/projects/generic/someproject&localFileName=pom.xml
	 */
	@RequestMapping(value="/cleanDependenciesInPomXmlLocalFileToLocalFile")
	@ResponseBody
	public ScResponse cleanDependenciesInPomXmlLocalFileToLocalFile(
			@RequestParam(value = "fullPathToLocalFileDirectory", required = false) String fullPathToLocalFileDirectoryString,
			@RequestParam(value = "localFileName", required = false) String localFileName
			) {
		
		try {
			String pomXmlLocalFile = 
					localFileService.convertLocalFileToString(
							fullPathToLocalFileDirectoryString, 
							localFileName);
			
			List<MavenDependency> mavenDependencies = mavenService.findDependenciesInPomXmlString(pomXmlLocalFile);
			
			if (mavenDependencies == null) {
				throw new Exception("Could not parse maven dependencies from input pomXml");
			}
			
			String cleanedXmlString = mavenService.createPomXmlDependenciesStringFromMavenDependencies(mavenDependencies);
			
			String newFileName = "pom-" + UtilsParzival.generateDateTimeNowSortableString() + ".xml";
			try {
				Files.writeString(UtilsParzival.getPath(fullPathToLocalFileDirectoryString, newFileName), cleanedXmlString);
			}
			catch (Exception e) {
				// Do nothing
			}
			
			ScResponse response = 
					ScResponse.createSuccessResponseWithOneDataRecord("Success - wrote new pom to: " + fullPathToLocalFileDirectoryString + "/" + newFileName);
			
			return response;
		} 
		catch (Exception e) {
			
			ScResponse errorResponse = ScResponse.createErrorResponse(e.getMessage());
			
			return errorResponse;
		}
	}
	
	/**
	 * Examples:
	 *   http://localhost:8183/api/maven/cleanDependenciesInPomXmlLocalFile?fullPathToLocalFileDirectory=X&localFileName=Y
	 *   http://localhost:8183/api/maven/cleanDependenciesInPomXmlLocalFile?fullPathToLocalFileDirectory=/Users/user/Data/projects/generic/someproject&localFileName=pom.xml
	 */
	@RequestMapping(value="/cleanDependenciesInPomXmlLocalFile")
	@ResponseBody
	public ApiResponse cleanDependenciesInPomXmlLocalFile(
			@RequestParam(value = "fullPathToLocalFileDirectory", required = false) String fullPathToLocalFileDirectoryString,
			@RequestParam(value = "localFileName", required = false) String localFileName
			) {
		
		try {
			String pomXmlLocalFile = 
					localFileService.convertLocalFileToString(
							fullPathToLocalFileDirectoryString, 
							localFileName);
			
			List<MavenDependency> mavenDependencies = mavenService.findDependenciesInPomXmlString(pomXmlLocalFile);
			
			if (mavenDependencies == null) {
				throw new Exception("Could not parse maven dependencies from input pomXml");
			}
			
			String cleanedXmlString = mavenService.createPomXmlDependenciesStringFromMavenDependencies(mavenDependencies);
			
			String newFileName = "pom-" + UtilsParzival.generateDateTimeNowSortableString() + ".xml";
			try {
				Files.writeString(UtilsParzival.getPath(fullPathToLocalFileDirectoryString, newFileName), cleanedXmlString);
			}
			catch (Exception e) {
				// Do nothing
			}
			
			ApiResponse response = 
					ApiResponse.createSuccessResponseWithOneDataRecord(cleanedXmlString);
			
			return response;
		} 
		catch (Exception e) {
			
			ApiResponse errorResponse = ApiResponse.createErrorResponse(e.getMessage());
			
			return errorResponse;
		}
	}
	
	/**
	 * Examples:
	 *   http://localhost:8183/api/maven/cleanDependenciesInPomXml?pomXml=<bigstringpom>
	 */
	@RequestMapping(value="/cleanDependenciesInPomXml")
	@ResponseBody
	public ApiResponse cleanDependenciesInPomXml(
			@RequestParam(value = "pomXml", required = false) String pomXmlString
			) {
		
		try {
			List<MavenDependency> mavenDependencies = mavenService.findDependenciesInPomXmlString(pomXmlString);
			
			if (mavenDependencies == null) {
				throw new Exception("Could not parse maven dependencies from input pomXml");
			}
			
			String cleanedXmlString = mavenService.createPomXmlDependenciesStringFromMavenDependencies(mavenDependencies);
			
			ApiResponse response = 
					ApiResponse.createSuccessResponseWithOneDataRecord(cleanedXmlString);
			
			return response;
		} 
		catch (Exception e) {
			
			ApiResponse errorResponse = ApiResponse.createErrorResponse(e.getMessage());
			
			return errorResponse;
		}
	}
	
	public MavenService getMavenService() {
		return mavenService;
	}
	@Autowired
	public void setMavenService(MavenService mavenService) {
		this.mavenService = mavenService;
	}
	public LocalFileService getLocalFileService() {
		return localFileService;
	}
	@Autowired
	public void setLocalFileService(LocalFileService localFileService) {
		this.localFileService = localFileService;
	}
}
