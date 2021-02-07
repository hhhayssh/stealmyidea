package com.stealmyidea;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SanitizeUtil {
	
	public static boolean isValueOkAsInteger(String value, boolean isNullOk, boolean isBlankOk, Integer minimumValue, Integer maximumValue) {
		
		if (value == null) {
			if (isNullOk) {
				return true;
			}
			return false;
		}
		
		if (Util.isBlank(value)) {
			if (isBlankOk) {
				return true;
			}
			return false;
		}
		
		try {
			Integer integerValue = convertStringToInteger(value);
			
			if (integerValue == null) {
				return false;
			}
			
			if (minimumValue != null && integerValue < minimumValue) {
				return false;
			}
			
			if (maximumValue != null && integerValue > maximumValue) {
				return false;
			}
		}
		catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	public static final Integer convertStringToInteger(String value) {
		
		if (Util.isBlank(value)) {
			return null;
		}
		
		Integer integerValue = Integer.valueOf(value);
		
		return integerValue;
	}
	
	public static boolean isValueOkAsLong(String value, boolean isNullOk, boolean isBlankOk, Long minimumValue, Long maximumValue) {
		
		if (value == null) {
			if (isNullOk) {
				return true;
			}
			return false;
		}
		
		if (Util.isBlank(value)){
			if (isBlankOk) {
				return true;
			}
			return false;
		}
		
		try {
			Long longValue = convertStringToLong(value);
			
			if (longValue == null) {
				return false;
			}
			
			if (minimumValue != null && longValue < minimumValue) {
				return false;
			}
			
			if (maximumValue != null && longValue > maximumValue) {
				return false;
			}
		}
		catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	public static final Long convertStringToLong(String value) {
		
		if (Util.isBlank(value)) {
			return null;
		}
		
		Long longValue = Long.valueOf(value);
		
		return longValue;
	}
	
	public static Date convertStringToDate(String value) throws ParseException {
		
		if (value == null) {
			return null;
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(StealMyIdeaConstants.DEFAULT_DATE_FORMAT);
		
		Date date = simpleDateFormat.parse(value);
		
		return date;
	}
	
	public static boolean isValueOkAsDate(String value, boolean isNullOk, boolean isEmptyOk) {
		
		if (value == null) {
			if (isNullOk) {
				return true;
			}
			
			return false;
		}
		
		boolean isEmpty = Util.isBlank(value);
		if (isEmpty) {
			if (isEmptyOk) {
				return true;
			}
			
			return false;
		}
		
		boolean areStringCharactersOk = areStringCharactersOk(value, StealMyIdeaConstants.DATE_LENGTH, isNullOk, isEmptyOk);
		
		if (!areStringCharactersOk) {
			return false;
		}
		
		try {
			convertStringToDate(value);
		}
		catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	public static boolean areStringCharactersOk(String value, int maxLength, boolean isNullOk, boolean isEmptyOk) {
		
		if (value == null) {
			if (isNullOk) {
				return true;
			}
			return false;
		}
		
		boolean isEmpty = Util.isBlank(value);
		if (isEmpty) {
			if (isEmptyOk) {
				return true;
			}
			return false;
		}
		
		int length = value.length();
		
		if (!isEmptyOk && length == 0) {
			return false;
		}
		
		if (length >= maxLength) {
			return false;
		}
		
		char[] characters = value.toCharArray();
		
		for (int index = 0; index < characters.length; index++) {
			char character = characters[index];
			
			if (!isCharacterOk(character)) {
				return false;
			}
		}
		
		return true;
	}
	
	public static final boolean isCharacterOk(char character) {
		
		int characterAsInteger = (int)character;
		
		if (characterAsInteger >= 32 && characterAsInteger <= 126) {
			return true;
		}
		else if (characterAsInteger == 9 || characterAsInteger == 10 || characterAsInteger == 13) {
			return true;
		}
		
		return false;
	}
	
	public static final boolean isValueInValues(String value, List<String> values) {
		
		if (values == null || values.size() == 0) {
			return false;
		}
		
		if (values.contains(value)){
			return true;
		}
		
		return false;
	}

}
