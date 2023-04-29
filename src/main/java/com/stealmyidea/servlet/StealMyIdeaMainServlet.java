package com.stealmyidea.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.stealmyidea.StealMyIdeaConstants;
import com.stealmyidea.model.GenericServerResponse;
import com.stealmyidea.model.Idea;
import com.stealmyidea.util.SanitizeUtil;
import com.stealmyidea.util.Util;

//
public class StealMyIdeaMainServlet extends AbstractStealMyIdeaServlet {
	
	private static final long serialVersionUID = 2021_02_13_11_36_00L;
	
	private static final Logger log = Logger.getLogger(StealMyIdeaMainServlet.class);

	protected static final String PARAMETER_NAME_TARGET = "target";

	protected static final String TARGET_IDEAS = "ideas";
	protected static final String TARGET_IDEA = "idea";
	protected static final String TARGET_IDEA_COUNT = "ideaCount";
	protected static final String TARGET_EXPORT_IDEAS = "exportIdeas";
	
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

	public void init() throws ServletException {
		log.info("Initializing main servlet...");
		super.init();
		log.info("Done initializing main servlet.");
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Processing request... request = " + request.getRequestURL() + "?" + request.getQueryString());

		String jsonResponse = null;

		try {
			
//			if (Math.random() > 0.5) {
//				throw new Exception("blah");
//			}
			
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
				List<String> messages = ideaValidator.validateInput(ideaId, ideaNumber, idea, ideaDate, stealStatus, stealStatusDescription, 
						greatness, description, enterDate, modificationDate, offset, size, sortFieldName, sortDirection);
				
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
			else if (TARGET_IDEA_COUNT.equals(target)) {
				Integer ideaCount = dataService.getIdeaCount();
				String ideaCountString = null;
				if (ideaCount != null) {
					ideaCountString = ideaCount.toString();
				}
				Map<String, String> ideaCountData = new HashMap<String, String>();
				ideaCountData.put("ideaCount", ideaCountString);
				serverResponse = new GenericServerResponse(ideaCountData);
				writeGenericServerResponse(response, serverResponse);
				return;
			}
			else if (TARGET_EXPORT_IDEAS.equals(target)) {
				List<Idea> allIdeas = dataService.getAllIdeas();
				
				String header = "idea_id,idea_number,idea,idea_date,steal_status,steal_status_description,greatness,description,status,status_date,enter_date,modification_date";
				String ideasToExport = Util.convertIdeasToCSV(allIdeas, header);
				
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
				String exportDate = formatter.format(new Date());
				
				String filename = "idea-export-" + exportDate + ".csv";
				
				response.setContentType("text/csv");
		        response.setContentLength(ideasToExport.length());

				response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
				
				OutputStream outputStream = response.getOutputStream();
				
				Util.writeBufferedBytes(ideasToExport.getBytes(), outputStream);
				
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
}
