package com.ecdragon.parzival.features.development_environment;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ecdragon.parzival.features.environment_context.EnvironmentContextEnum;
import com.ecdragon.parzival.utils.ScResponse;

@Controller
@RequestMapping("/api/development_environment")
public class DevelopmentEnvironmentController {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private DevelopmentEnvironmentService developmentEnvironmentService;
	public DevelopmentEnvironmentService getDevelopmentEnvironmentService() {
		return developmentEnvironmentService;
	}
	@Autowired
	public void setDevelopmentEnvironmentService(DevelopmentEnvironmentService developmentEnvironmentService) {
		this.developmentEnvironmentService = developmentEnvironmentService;
	}
	
	/**
	 * Examples:
	 *   http://localhost:8183/api/development_environment/setup-an-environment
	 */
	@RequestMapping(value="/setup-an-environment")
	@ResponseBody
	public ScResponse setupAnEnvironment(
			@RequestParam(value = "environmentContextName", required = false) String environmentContextNameStringIn,
			@RequestParam(value = "projectName", required = false) String projectNameStringIn,
			@RequestParam(value = "intent", required = false) String intentStringIn,
			@RequestParam(value = "branchName", required = false) String branchNameStringIn,
			@RequestParam(value = "gitAccountName", required = false) String gitAccountNameStringIn,
			HttpServletRequest request
			) {
		
		try {
			EnvironmentContextEnum environmentContextEnum = 
					EnvironmentContextEnum.findByName(environmentContextNameStringIn);
			
			String branchName = null;
			if (branchNameStringIn != null 
					&& !branchNameStringIn.isEmpty()
					&& !branchNameStringIn.equalsIgnoreCase("null")) {
				branchName = branchNameStringIn;
			}
			
			String gitAccountName = null;
			if (gitAccountNameStringIn != null 
					&& !gitAccountNameStringIn.isEmpty()
					&& !gitAccountNameStringIn.equalsIgnoreCase("null")) {
				gitAccountName = gitAccountNameStringIn;
			}
			
			developmentEnvironmentService.setupAnEnvironment(
					environmentContextEnum, 
					projectNameStringIn, 
					intentStringIn, 
					null, 
					branchName, 
					null, 
					null, 
					gitAccountName,
					null
					);
			
			ScResponse response = 
					ScResponse.createSuccessResponseWithOneDataRecord("Success");
			
			return response;
		} 
		catch (Exception e) {
			ScResponse errorResponse = ScResponse.createErrorResponse(e.getMessage());
			return errorResponse;
		}
	}
	
	/**
	 * Examples:
	 *   http://localhost:8183/api/development_environment/setup-parzival
	 */
	@RequestMapping(value="/setup-parzival")
	@ResponseBody
	public ScResponse setupParzival() {

		try {
			EnvironmentContextEnum environmentContextEnum = 
					EnvironmentContextEnum.findByName("GenericGithubHttps");
			
			developmentEnvironmentService.setupAnEnvironment(
					environmentContextEnum, 
					"parzival", 
					"Setting up initial Parzival env", 
					null, 
					null, 
					null, 
					null, 
					"ecdragon",
					Boolean.TRUE
					);
			
			ScResponse response = 
					ScResponse.createSuccessResponseWithOneDataRecord("Success");
			
			return response;
		} 
		catch (Exception e) {
			ScResponse errorResponse = ScResponse.createErrorResponse(e.getMessage());
			return errorResponse;
		}
	}
}
