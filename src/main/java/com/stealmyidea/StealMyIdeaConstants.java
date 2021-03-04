package com.stealmyidea;

import java.util.Arrays;
import java.util.List;

public interface StealMyIdeaConstants {
	
	public static final String STEAL_MY_IDEA_FILENAME_PROPERTY = "stealmyidea.properties.filename";
	
	public static final String STATUS_SUCCESS = "SUCCESS";
	
	public static final String STATUS_ERROR = "ERROR";
	
	public static final String STATUS_DELETED = "DELETED";
	
	public static final String STATUS_AVAILABLE = "AVAILABLE"; 
	
	public static final String STATUS_CODE_IDEA_ALREADY_EXISTS = "IDEA_ALREADY_EXISTS";
	
	public static final String STATUS_CODE_BAD_IDEA = "BAD_IDEA";
	
	public static final int IDEA_MAX_LENGTH = 100;
	
	public static final int IDEA_DATE_MAX_LENGTH = 100;
	
	public static final int STEAL_STATUS_MAX_LENGTH = 100;
	
	public static final int STEAL_STATUS_DESCRIPTION_MAX_LENGTH = 500;
	
	public static final int GREATNESS_MAX_LENGTH = 100;
	
	public static final int DESCRIPTION_MAX_LENGTH = 500;
	
	public static final int SORT_FIELD_NAME_MAX_LENGTH = 22;
	
	public static final int SORT_DIRECTION_MAX_LENGTH = 10;
	
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'hh24:mm:ss";
	
	public static final int DATE_LENGTH = DEFAULT_DATE_FORMAT.length();
	
	public static final String SORT_FIELD_NAME_IDEA_ID = "ideaId";
	
	public static final String SORT_FIELD_NAME_IDEA_NUMBER = "ideaNumber";
	
	public static final String SORT_FIELD_NAME_IDEA = "idea";
	
	public static final String SORT_FIELD_NAME_IDEA_DATE = "ideaDate";
	
	public static final String SORT_FIELD_NAME_STEAL_STATUS = "stealStatus";
	
	public static final String SORT_FIELD_NAME_STEAL_STATUS_DESCRIPTION = "stealStatusDescription";
	
	public static final String SORT_FIELD_NAME_GREATNESS = "greatness";
	
	public static final String SORT_FIELD_NAME_DESCRIPTION = "description";
	
	public static final String SORT_FIELD_NAME_ENTER_DATE = "enterDate";
	
	public static final String SORT_FIELD_NAME_MODIFICATION_DATE = "modificationDate";
	
	public static final String DEFAULT_SORT_FIELD_NAME = SORT_FIELD_NAME_IDEA_ID;
	
	public static final List<String> SORT_FIELD_NAMES = Arrays.asList(new String[] {SORT_FIELD_NAME_IDEA_ID,
			SORT_FIELD_NAME_IDEA_NUMBER, SORT_FIELD_NAME_IDEA, SORT_FIELD_NAME_IDEA_DATE, SORT_FIELD_NAME_STEAL_STATUS,
			SORT_FIELD_NAME_STEAL_STATUS_DESCRIPTION, SORT_FIELD_NAME_GREATNESS, SORT_FIELD_NAME_DESCRIPTION, SORT_FIELD_NAME_ENTER_DATE,
			SORT_FIELD_NAME_MODIFICATION_DATE});
	
	public static final String SORT_DIRECTION_ASCENDING = "ascending";
	
	public static final String SORT_DIRECTION_DESCENDING = "descending";
	
	public static final String DEFAULT_SORT_DIRECTION = SORT_DIRECTION_DESCENDING;
	
	public static final List<String> SORT_DIRECTIONS = Arrays.asList(new String[] {SORT_DIRECTION_ASCENDING, SORT_DIRECTION_DESCENDING});
	
	public static final int DEFAULT_OFFSET = 0;
	
	public static final int DEFAULT_SIZE = 25;

	public static final String JSON_IDEA_ID = "ideaId";
	
	public static final String JSON_IDEA_NUMBER = "ideaNumber";
	
	public static final String JSON_IDEA = "idea";
	
	public static final String JSON_IDEA_DATE = "ideaDate";
	
	public static final String JSON_STEAL_STATUS = "stealStatus";
	
	public static final String JSON_STEAL_STATUS_DESCRIPTION = "stealStatusDescription";
	
	public static final String JSON_GREATNESS = "greatness";
	
	public static final String JSON_DESCRIPTION = "description";
	
	public static final String JSON_ENTER_DATE = "enterDate";
	
	public static final String JSON_MODIFICATION_DATE = "modificationDate";
}
