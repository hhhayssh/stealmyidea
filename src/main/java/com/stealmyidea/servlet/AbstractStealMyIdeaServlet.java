package com.stealmyidea.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.stealmyidea.ApplicationContext;
import com.stealmyidea.StealMyIdeaConstants;
import com.stealmyidea.data.StealMyIdeaDataService;
import com.stealmyidea.model.GenericServerResponse;
import com.stealmyidea.model.Idea;
import com.stealmyidea.util.JSONUtil;
import com.stealmyidea.util.SanitizeUtil;
import com.stealmyidea.util.Util;
import com.stealmyidea.validate.IdeaValidator;

public class AbstractStealMyIdeaServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2021_02_13_11_36_00L;
	
	private static final Logger log = Logger.getLogger(AbstractStealMyIdeaServlet.class);
	
	protected static final String ERROR_JSON_RESPONSE = "{\"status\": \"ERROR\"}";
	
	protected StealMyIdeaDataService dataService;
	
	protected IdeaValidator ideaValidator;
	
	public void init() throws ServletException {
		log.info("Initializing servlet...");
		ApplicationContext.getContext().initialize();
		dataService = new StealMyIdeaDataService(ApplicationContext.getContext().getDataSource());
		ideaValidator = new IdeaValidator();
		log.info("Done initializing servlet.");
    }
	
	protected boolean doesIdeaExist(String idea) {
		
		Idea ideaObject = dataService.getIdea(idea);
		
		if (ideaObject == null) {
			return true;
		}
		
		return false;
	}
	
	protected void writeGenericServerResponse(HttpServletResponse response, GenericServerResponse serverResponse) {
		
		String jsonResponse = null;
		
		try {
			jsonResponse = JSONUtil.createJsonStringFromObject(serverResponse);
		}
		catch (Exception e) {
			log.error("Error writing generic server response!  serverResponse = " + serverResponse);
			jsonResponse = ERROR_JSON_RESPONSE;
		}
		
		try {
			writeJSONResponse(response, jsonResponse);
		}
		catch (Exception e) {
			log.error("Error writing json response! jsonResponse = " + jsonResponse, e);
		}
	}
	
	/**
	 * 
	 * A convenience function for getting a parameter value from a request.  It'll replace
	 * the "url" characters in the value before returning it.
	 * 
	 * @param request
	 * @param parameterName
	 * @return
	 */
	protected String getParameter(HttpServletRequest request, String parameterName){
		
		String value = request.getParameter(parameterName);
		
		String unescapedValue = Util.replaceUrlCharacters(value);
		
		return unescapedValue;
	}
	
	/**
	 * 
	 * A convenience function for reading the body of a request.  Just reads all the lines
	 * that were sent with it.
	 * 
	 * @param request
	 * @return
	 */
	protected String readBody(HttpServletRequest request){
		
		BufferedReader reader = null;
		
		StringBuilder bodyStringBuilder = new StringBuilder();
		try {
			reader = request.getReader();
			
			String line = "";
			while ((line = reader.readLine()) != null){
				bodyStringBuilder.append(line);
			}
		}
		catch (Exception e){
			log.error("Error reading body!", e);
		}
		
		return bodyStringBuilder.toString();
	}
	
	/**
	 * 
	 * A convenience function so we can use the same error message as a response if we need to.
	 * Basically, here so whatever catches the error won't have to think about what to do with it.
	 * 
	 * @param response
	 * @throws IOException
	 */
	protected void writeErrorResponse(HttpServletResponse response) throws IOException {
		writeJSONResponse(response, ERROR_JSON_RESPONSE);
	}
	
	/**
	 * 
	 * A convenience function for writing json to an http response.  Makes sure the content is right
	 * on the response and then writes it using the response's writer.
	 * 
	 * @param response
	 * @param json
	 * @throws IOException
	 */
	protected void writeJSONResponse(HttpServletResponse response, String json) throws IOException {
		response.setContentType("text/plain; charset=UTF-8");

		PrintWriter writer = response.getWriter();
		
		writer.println(json);
	}

}
