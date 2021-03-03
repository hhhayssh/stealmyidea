package com.stealmyidea.validate;

import java.util.ArrayList;
import java.util.List;

import com.stealmyidea.StealMyIdeaConstants;
import com.stealmyidea.util.SanitizeUtil;

public class IdeaValidator {
	
	public List<String> validateInput(String ideaId, String ideaNumber, String idea, String ideaDate, String stealStatus, String stealStatusDescription, String greatness,
			String description, String enterDate, String modificationDate, String offset, String size, String sortFieldName, String sortDirection) {
		
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

}
