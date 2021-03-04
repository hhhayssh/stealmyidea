package com.stealmyidea.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.stealmyidea.StealMyIdeaConstants;
import com.stealmyidea.model.GenericServerResponse;
import com.stealmyidea.model.Idea;
import com.stealmyidea.util.JSONUtil;
import com.stealmyidea.util.SanitizeUtil;

public class StealMyIdeaAddServlet extends AbstractStealMyIdeaServlet {
	
	private static final long serialVersionUID = 2021_02_13_11_36_00L;
	
	private static final Logger log = Logger.getLogger(StealMyIdeaAddServlet.class);
	
	public void init() throws ServletException {
		log.info("Initializing add servlet...");
		super.init();
		log.info("Done initializing add servlet.");
    }
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		//Steps to do:
		//	1. Pull out the target and use it to decide what to do.
		//	I'll comment on the lines instead of here...
		
		log.info("Processing request... request = " + request.getRequestURL() + "?" + request.getQueryString());
		
		String jsonResponse = null;
		
		try {
			GenericServerResponse serverResponse = null;

			String body = readBody(request);

			if ("".equals(body)){
				log.error("Error reading body!");
				return;
			}

			//The body should be json and we just have to pull it out.
			JSONObject ideaJsonObject = JSONUtil.createJSONObjectFromString(body);

			String ideaId = ideaJsonObject.optString(StealMyIdeaConstants.JSON_IDEA_ID);
			String ideaNumber = ideaJsonObject.optString(StealMyIdeaConstants.JSON_IDEA_NUMBER);
			String idea = ideaJsonObject.optString(StealMyIdeaConstants.JSON_IDEA);
			
			boolean isIdeaInputOk = SanitizeUtil.areStringCharactersOk(idea, StealMyIdeaConstants.IDEA_MAX_LENGTH, false, false);

			List<String> messages = new ArrayList<String>();
			
			if (isIdeaInputOk) {
				Idea existingIdea = dataService.getIdea(idea);

				if (existingIdea != null) {
					serverResponse = new GenericServerResponse();
					serverResponse.setError();
					serverResponse.setStatusCode(StealMyIdeaConstants.STATUS_CODE_IDEA_ALREADY_EXISTS);

					writeGenericServerResponse(response, serverResponse);
					return;
				}
			}
			else {
				messages.add("That was a ... bad idea.");
				serverResponse = new GenericServerResponse();
				serverResponse.setError();
				serverResponse.setStatusCode(StealMyIdeaConstants.STATUS_CODE_BAD_IDEA);

				writeGenericServerResponse(response, serverResponse);
				return;
			}

			String ideaDate = ideaJsonObject.optString(StealMyIdeaConstants.JSON_IDEA_DATE);
			String stealStatus = ideaJsonObject.optString(StealMyIdeaConstants.JSON_STEAL_STATUS);
			String stealStatusDescription = ideaJsonObject.optString(StealMyIdeaConstants.JSON_STEAL_STATUS_DESCRIPTION);
			String greatness = ideaJsonObject.optString(StealMyIdeaConstants.JSON_GREATNESS);
			String description = ideaJsonObject.optString(StealMyIdeaConstants.JSON_DESCRIPTION);
			
			messages = ideaValidator.validateInput(ideaId, ideaNumber, ideaNumber, ideaDate, stealStatus, stealStatusDescription, 
					greatness, description, null, null, null, null, null, null);
			
			if (messages.size() > 0) {
				serverResponse = new GenericServerResponse();
				serverResponse.setError(messages);
				writeGenericServerResponse(response, serverResponse);
				return;
			}

			Idea savedIdea = saveIdea(ideaId, ideaNumber, idea, ideaDate, stealStatus, stealStatusDescription, greatness, description);

			serverResponse = new GenericServerResponse(savedIdea);

			jsonResponse = JSONUtil.createJsonStringFromObject(serverResponse);
		}
		catch (Exception e) {
			jsonResponse = null;
			log.error("Error processing!", e);
		}
		
		if (jsonResponse == null) {
			jsonResponse = ERROR_JSON_RESPONSE;
		}
		
		writeJSONResponse(response, jsonResponse);
	}
	
	protected Idea saveIdea(String ideaId, String ideaNumber, String idea, String ideaDate, String stealStatus, String stealStatusDescription, String greatness,
			String description) {
		
		Long ideaIdValue = SanitizeUtil.convertStringToLong(ideaId);
		Long ideaNumberValue = SanitizeUtil.convertStringToLong(ideaNumber);
		
		Idea ideaObject = new Idea(ideaIdValue, ideaNumberValue, idea, ideaDate, stealStatus, stealStatusDescription, greatness, description, null, null);
		
		Idea savedIdea = dataService.saveIdea(ideaObject);
		
		return savedIdea;
	}
}
