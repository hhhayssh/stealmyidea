package com.stealmyidea.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.stealmyidea.StealMyIdeaConstants;
import com.stealmyidea.model.Idea;

public class JSONUtil {
	
	private static final Logger log = Logger.getLogger(JSONUtil.class);

	/**
	 * 
	 * This function converts the given ideas into a json formatted string.
	 * 
	 * @param seasons
	 * @return
	 */
	public static String ideasToJSONString(List<Idea> ideas){
		
		JSONArray jsonArray = ideasToJSONArray(ideas);
		
		String json = jsonArray.toString();
		
		return json;
	}
	
	/**
	 * 
	 * This function converts the given ideas into a json array.
	 * 
	 * @param seasons
	 * @return
	 */
	public static JSONArray ideasToJSONArray(List<Idea> ideas){
		
		JSONArray jsonArray = new JSONArray();
		
		for (int index = 0; index < ideas.size(); index++){
			Idea idea = ideas.get(index);
			JSONObject jsonObject = ideaToJSONObject(idea);
			jsonArray.put(jsonObject);
		}
		
		return jsonArray;

	}
	
	/**
	 * 
	 * This function converts the given idea to a json formatted string.
	 * 
	 * @param season
	 * @return
	 */
	public static String ideaToJSONString(Idea idea){
		
		JSONObject jsonObject = ideaToJSONObject(idea);
		
		String json = jsonObject.toString();
		
		return json;
	}
	
	/**
	 * 
	 * This function converts the given season to a json object.  If the season
	 * has weeks in it, it'll convert the weeks too.  Otherwise, it won't set
	 * the weeks variable.
	 * 
	 * @param season
	 * @return
	 */
	public static JSONObject ideaToJSONObject(Idea idea){
		
		JSONObject jsonObject = new JSONObject();

		jsonObject.put(StealMyIdeaConstants.JSON_IDEA_ID, idea.getIdeaId());
		jsonObject.put(StealMyIdeaConstants.JSON_IDEA_NUMBER, idea.getIdeaNumber());
		jsonObject.put(StealMyIdeaConstants.JSON_IDEA, idea.getIdea());
		jsonObject.put(StealMyIdeaConstants.JSON_IDEA_DATE, idea.getIdeaDate());
		jsonObject.put(StealMyIdeaConstants.JSON_STEAL_STATUS, idea.getStealStatus());
		jsonObject.put(StealMyIdeaConstants.JSON_STEAL_STATUS_DESCRIPTION, idea.getStealStatusDescription());
		jsonObject.put(StealMyIdeaConstants.JSON_GREATNESS, idea.getGreatness());
		jsonObject.put(StealMyIdeaConstants.JSON_DESCRIPTION, idea.getDescription());
		jsonObject.put(StealMyIdeaConstants.JSON_ENTER_DATE, dateToString(idea.getEnterDate()));
		jsonObject.put(StealMyIdeaConstants.JSON_MODIFICATION_DATE, dateToString(idea.getModificationDate()));
		
		return jsonObject;
	}
	
	public static String dateToString(Date date) {
		
		if (date == null) {
			return null;
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(StealMyIdeaConstants.DEFAULT_DATE_FORMAT);
		
		String dateString = dateFormat.format(date);
		
		return dateString;
	}
	
	/**
	 * 
	 * Creates a new json object from the given string (which should be in the
	 * json format).
	 * 
	 * @param json
	 * @return
	 */
	public static JSONObject createJSONObjectFromString(String json){
		
		JSONObject object = null;
		
		try {
			object = new JSONObject(json);
		}
		catch (Exception e){
			log.error("Error creating json object from string! json = " + json, e);
		}
		
		return object;
	}
	
	public static String createJsonStringFromObject(Object object) {
		
		String jsonString = null;
		
		try {
			JSONObject jsonObject = new JSONObject(object);
			jsonString = jsonObject.toString();
		}
		catch (Exception e) {
			log.error("Error creating json string from object! object = " + object, e);
		}
		
		return jsonString;
		
	}

}
