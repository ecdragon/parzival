package com.ecdragon.parzival.features.environment_context;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ecdragon.parzival.utils.ScResponse;

@Controller
@RequestMapping("/api/environment_context")
public class EnvironmentContextController {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	/**
	 * Examples:
	 *   http://localhost:8183/api/
	 *   http://localhost:8183/api/
	 */
	@RequestMapping(value="/sc/fetch")
	@ResponseBody
	public ScResponse fetchFiles(
//			HttpServletRequest request
			) {
		
		try {
			List<EnvironmentContextDto> environmentContextDtos = 
					EnvironmentContextEnum.getAllTheseDtos();
			
			ScResponse response = 
					ScResponse.createSuccessResponseWithListOfDataRecords(environmentContextDtos);
			
			return response;
		} 
		catch (Exception e) {
			
			ScResponse errorResponse = ScResponse.createErrorResponse(e.getMessage());
			
			return errorResponse;
		}
	}
}
