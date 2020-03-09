package com.ecdragon.parzival.features.home_page;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomePageController {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * Examples:
	 *   http://localhost:8183
	 *   http://localhost:8183/home
	 *   http://localhost:8183/index.html
	 */
	@RequestMapping(value = {"/home", "/*", "/index.html"})
	@ResponseBody
	public String homePage(
			@RequestParam(value = "id", required = false) String idStringIn
			) {
		
		return "Parzival is OK!";
	}
	
	/**
	 * Examples:
	 *   http://localhost:8183/admin
	 */
	@RequestMapping(value = {"/admin"})
	public ModelAndView admin(
			@RequestParam(value = "id", required = false) String idStringIn
			) {
		
		Map<String, String> model = new HashMap<>();
		try {
//			model.put("dir", dirString);
//			model.put("thisUrl", "/browser");
		} 
		catch (Exception e) {
			logger.error("error doing admin thing", e);
		}
		ModelAndView modelAndViewHomePage = new ModelAndView("admin", model);
		
		return modelAndViewHomePage;
	}
}
