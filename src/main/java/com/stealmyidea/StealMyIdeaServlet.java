package com.stealmyidea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.stealmyidea.model.GenericServerResponse;
import com.stealmyidea.model.Idea;

public class StealMyIdeaServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(StealMyIdeaServlet.class);

	protected static final String PARAMETER_NAME_TARGET = "target";

	protected static final String TARGET_IDEAS = "ideas";
	protected static final String TARGET_IDEA = "idea";
	
	//(ideaId, ideaNumber, idea, ideaDate, stealStatus, stealStatusDescription, greatness, description, enterDate, modificationDate)
	protected static final String PARAMETER_NAME_IDEA_ID = "ideaId";
	protected static final String PARAMETER_NAME_IDEA_NUMBER = "ideaNumber";
	protected static final String PARAMETER_NAME_IDEA = "idea";
	protected static final String PARAMETER_NAME_IDEA_DATE = "ideaDate";
	protected static final String PARAMETER_NAME_STEAL_STATUS = "stealStatus";
	protected static final String PARAMETER_NAME_STEAL_STATUS_DESCRIPTION = "stealStatusDescription";
	protected static final String PARAMETER_NAME_GREATNESS = "greatness";
	protected static final String PARAMETER_NAME_DESCRIPTION = "description";
	protected static final String PARAMETER_NAME_ENTER_DATE = "enterDate";
	protected static final String PARAMETER_NAME_MODIFICATION_DATE = "modificationDate";
	
	protected static final String PARAMETER_NAME_SORT_FIELD_NAME = "sortFieldName";
	protected static final String PARAMETER_NAME_SORT_DIRECTION = "sortDirection";
	protected static final String PARAMETER_NAME_OFFSET = "offset";
	protected static final String PARAMETER_NAME_SIZE = "size";
	
	protected static final String ERROR_JSON_RESPONSE = "{\"status\": \"ERROR\"}";
	
	protected StealMyIdeaDataService dataService;
	
	public void init() throws ServletException {
		log.info("Initializing servlet...");
		ApplicationContext.getContext().initialize();
		dataService = new StealMyIdeaDataService(ApplicationContext.getContext().getDataSource());
		log.info("Done initializing servlet.");
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Processing request... request = " + request.getRequestURL() + "?" + request.getQueryString());

		String jsonResponse = null;
		
		try {
			GenericServerResponse serverResponse = new GenericServerResponse();

			String target = getParameter(request, PARAMETER_NAME_TARGET);

			String ideaId = getParameter(request, PARAMETER_NAME_IDEA_ID);
			String ideaNumber = getParameter(request, PARAMETER_NAME_IDEA_NUMBER);
			String idea = getParameter(request, PARAMETER_NAME_IDEA);
			String ideaDate = getParameter(request, PARAMETER_NAME_IDEA_DATE);
			String stealStatus = getParameter(request, PARAMETER_NAME_STEAL_STATUS);
			String stealStatusDescription = getParameter(request, PARAMETER_NAME_STEAL_STATUS_DESCRIPTION);
			String greatness = getParameter(request, PARAMETER_NAME_GREATNESS);
			String description = getParameter(request, PARAMETER_NAME_DESCRIPTION);
			String enterDate = getParameter(request, PARAMETER_NAME_ENTER_DATE);
			String modificationDate = getParameter(request, PARAMETER_NAME_MODIFICATION_DATE);

			String sortFieldName = getParameter(request, PARAMETER_NAME_SORT_FIELD_NAME);
			String sortDirection = getParameter(request, PARAMETER_NAME_SORT_DIRECTION);
			String offset = getParameter(request, PARAMETER_NAME_OFFSET);
			String size = getParameter(request, PARAMETER_NAME_SIZE);

			if (TARGET_IDEAS.equals(target)) {
				List<String> messages = isInputOk(ideaId, ideaNumber, idea, ideaDate, stealStatus, stealStatusDescription, 
						greatness, description, enterDate, modificationDate, sortFieldName, sortDirection, offset, size);
				
				if (messages.size() > 0) {
					serverResponse = new GenericServerResponse();
					serverResponse.setError(messages);
					writeGenericServerResponse(response, serverResponse);
					return;
				}
				
				if (sortFieldName == null) {
					sortFieldName = StealMyIdeaConstants.DEFAULT_SORT_FIELD_NAME;
				}
				
				if (sortDirection == null) {
					sortDirection = StealMyIdeaConstants.DEFAULT_SORT_DIRECTION;
				}
				
				Long ideaIdValue = SanitizeUtil.convertStringToLong(ideaId);
				Long ideaNumberValue = SanitizeUtil.convertStringToLong(ideaNumber);
				Date enterDateValue = SanitizeUtil.convertStringToDate(enterDate);
				Date modificationDateValue = SanitizeUtil.convertStringToDate(modificationDate);
				Integer offsetValue = SanitizeUtil.convertStringToInteger(offset);
				Integer sizeValue = SanitizeUtil.convertStringToInteger(size);

				if (offsetValue == null) {
					offsetValue = StealMyIdeaConstants.DEFAULT_OFFSET;
				}

				if (sizeValue == null) {
					sizeValue = StealMyIdeaConstants.DEFAULT_SIZE;
				}

				List<Idea> ideas = dataService.getIdeas(ideaIdValue, ideaNumberValue, idea, ideaDate, stealStatus, stealStatusDescription, greatness, 
						description, enterDateValue, modificationDateValue, sortFieldName, sortDirection, offsetValue, sizeValue);
				
				serverResponse = new GenericServerResponse(ideas);
				writeGenericServerResponse(response, serverResponse);
				return;
			}
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
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		//Steps to do:
		//	1. Pull out the target and use it to decide what to do.
		//	I'll comment on the lines instead of here...
		
		log.info("Processing request... request = " + request.getRequestURL() + "?" + request.getQueryString());
		
		String jsonResponse = null;
		
		try {
			String target = getParameter(request, PARAMETER_NAME_TARGET);

			GenericServerResponse serverResponse = null;


			if (TARGET_IDEA.equals(target)) {

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

				boolean isIdeaOk = isIdeaOk(idea);

				if (isIdeaOk) {
					Idea existingIdea = dataService.getIdea(idea);

					if (existingIdea != null) {
						serverResponse = new GenericServerResponse();
						serverResponse.setError();
						serverResponse.setStatusCode(StealMyIdeaConstants.STATUS_CODE_IDEA_ALREADY_EXISTS);

						writeGenericServerResponse(response, serverResponse);
						return;
					}
				}

				String ideaDate = ideaJsonObject.optString(StealMyIdeaConstants.JSON_IDEA_DATE);
				String stealStatus = ideaJsonObject.optString(StealMyIdeaConstants.JSON_STEAL_STATUS);
				String stealStatusDescription = ideaJsonObject.optString(StealMyIdeaConstants.JSON_STEAL_STATUS_DESCRIPTION);
				String greatness = ideaJsonObject.optString(StealMyIdeaConstants.JSON_GREATNESS);
				String description = ideaJsonObject.optString(StealMyIdeaConstants.JSON_DESCRIPTION);
				
				List<String> messages = isInputOk(ideaId, ideaNumber, ideaNumber, ideaDate, stealStatusDescription, stealStatusDescription, 
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
	
		
	protected List<String> isInputOk(String ideaId, String ideaNumber, String idea, String ideaDate, String stealStatus, String stealStatusDescription, String greatness,
			String description, String enterDate, String modificationDate, String offset, String size, String sortFieldName, String sortDirection) {
		
		/*
		 var idea = $('#idea').val();
		
	
	var description = $('#description').val();
	if (description.length > 500){
		$('#message-container-description').append('<span>Dude!  This isn\'t a dissertation (use 500 characters or less).</span>');
		error = true;
	}
		 */
		List<String> messages = new ArrayList<String>();
		
		boolean isIdeaIdOk = SanitizeUtil.isValueOkAsLong(ideaId, true, true, Long.valueOf(0), null);
		if (!isIdeaIdOk) {
			messages.add("Doing a little hacking, eh buddy?");
		}
		
		boolean isIdeaNumberOk = SanitizeUtil.isValueOkAsLong(ideaNumber, true, true, Long.valueOf(0), null);
		if (!isIdeaNumberOk) {
			messages.add("I'm ... sorry ... the number you dialed is ... not ... available.");
		}

		boolean isIdeaOk = SanitizeUtil.areStringCharactersOk(idea, StealMyIdeaConstants.IDEA_MAX_LENGTH, true, true);
		if (!isIdeaOk) {
			messages.add("Keep the name brief, buddy (use 100 characters or less).");
		}
		
		boolean isIdeaDate = SanitizeUtil.areStringCharactersOk(ideaDate, StealMyIdeaConstants.IDEA_DATE_MAX_LENGTH, true, true);
		if (!isIdeaDate) {
			messages.add("Tl;dr ... Your idea date is too long (use 100 characters or less).");
		}

		boolean isStealStatusOk = SanitizeUtil.areStringCharactersOk(stealStatus, StealMyIdeaConstants.STEAL_STATUS_MAX_LENGTH, true, true);
		if (!isStealStatusOk) {
			messages.add("Just the status, ma'am (use 100 characters or less).");
		}
		
		boolean isStealStatusDescriptionOk = SanitizeUtil.areStringCharactersOk(stealStatusDescription, StealMyIdeaConstants.STEAL_STATUS_DESCRIPTION_MAX_LENGTH, true, true);
		if (!isStealStatusDescriptionOk) {
			messages.add("If it's that involved, consult a lawyer (use 500 characters or less).");
		}
		
		boolean isGreatnessOk = SanitizeUtil.areStringCharactersOk(greatness, StealMyIdeaConstants.GREATNESS_MAX_LENGTH, true, true);
		if (!isGreatnessOk) {
			messages.add("Come on now, it ain't that great (use 100 characters or less).");
		}
		boolean isDescriptionOk = SanitizeUtil.areStringCharactersOk(description, StealMyIdeaConstants.DESCRIPTION_MAX_LENGTH, true, true);
		if (!isDescriptionOk) {
			messages.add("Dude!  This isn't a dissertation (use 500 characters or less).");
		}
		
		boolean isEnterDateOk = SanitizeUtil.isValueOkAsDate(enterDate, true, false);
		if (!isEnterDateOk) {
			messages.add("Rejected!");
		}
		
		boolean isModificationDateOk = SanitizeUtil.isValueOkAsDate(enterDate, true, false);
		if (!isModificationDateOk) {
			messages.add("Nope!");
		}
		
		boolean isOffsetOk = SanitizeUtil.isValueOkAsInteger(offset, true, true, Integer.valueOf(0), null);
		if (!isOffsetOk) {
			messages.add("Bzzzt ... wrong!");
		}
		
		boolean isSizeOk = SanitizeUtil.isValueOkAsInteger(size, true, true, Integer.valueOf(0), StealMyIdeaConstants.DEFAULT_SIZE);
		if (!isSizeOk) {
			messages.add("I'm afraid I can't do that, Hal.");
		}
		
		boolean isSortFieldNameOk = SanitizeUtil.areStringCharactersOk(sortFieldName, StealMyIdeaConstants.SORT_FIELD_NAME_MAX_LENGTH, true, true);
		if (!isSortFieldNameOk) {
			messages.add("Uhh....");
		}
		else {
			if (sortFieldName != null) {
				isSortFieldNameOk = SanitizeUtil.isValueInValues(sortFieldName, StealMyIdeaConstants.SORT_FIELD_NAMES);
				if (!isSortFieldNameOk) {
					messages.add("Chaos ... total chaos.");
				}
			}
		}
		
		boolean isSortDirectionOk = SanitizeUtil.areStringCharactersOk(sortDirection, StealMyIdeaConstants.SORT_DIRECTION_MAX_LENGTH, true, true);
		if (!isSortDirectionOk) {
			messages.add("The shooting woes continue...");
		}
		else {
			if (sortDirection != null) {
				isSortDirectionOk = SanitizeUtil.isValueInValues(sortDirection, StealMyIdeaConstants.SORT_DIRECTIONS);
				if (!isSortDirectionOk) {
					messages.add("Jim Marshall is going the wrong way!");
				}
			}
		}
		
		return messages;
	}
	
	protected boolean isIdeaOk(String idea) {
		
		boolean isIdeaOk = SanitizeUtil.areStringCharactersOk(idea, StealMyIdeaConstants.IDEA_MAX_LENGTH, true, true);
		if (!isIdeaOk) {
			return false;
		}
		
		return isIdeaOk;
	}
	
	protected Idea saveIdea(String ideaId, String ideaNumber, String idea, String ideaDate, String stealStatus, String stealStatusDescription, String greatness,
			String description) {
		
		Long ideaIdValue = SanitizeUtil.convertStringToLong(ideaId);
		Long ideaNumberValue = SanitizeUtil.convertStringToLong(ideaNumber);
		
		Idea ideaObject = new Idea(ideaIdValue, ideaNumberValue, idea, ideaDate, stealStatus, stealStatusDescription, greatness, description, null, null);
		
		Idea savedIdea = dataService.saveIdea(ideaObject);
		
		return savedIdea;
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
