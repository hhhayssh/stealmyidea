package com.stealmyidea.model;

import java.util.Date;

public class Idea {
	
	protected Long ideaId;
	
	protected Long ideaNumber;
	
	protected String idea;
	
	protected String ideaDate;
	
	protected String stealStatus;
	
	protected String stealStatusDescription;
	
	protected String greatness;
	
	protected String description;
	
	protected String status;
	
	protected Date statusDate;
	
	protected Date enterDate;
	
	protected Date modificationDate;
	
	public Idea() {
	}
	
	public Idea(Long ideaId, Long ideaNumber, String idea, String ideaDate, String stealStatus, String stealStatusDescription, 
			String greatness, String description, Date enterDate, Date modificationDate) {
		setIdeaId(ideaId);
		setIdeaNumber(ideaNumber);
		setIdea(idea);
		setIdeaDate(ideaDate);
		setStealStatus(stealStatus);
		setStealStatusDescription(stealStatusDescription);
		setGreatness(greatness);
		setDescription(description);
		setEnterDate(enterDate);
		setModificationDate(modificationDate);
	}
	
	public Long getIdeaId() {
		return ideaId;
	}

	public void setIdeaId(Long ideaId) {
		this.ideaId = ideaId;
	}

	public Long getIdeaNumber() {
		return ideaNumber;
	}

	public void setIdeaNumber(Long ideaNumber) {
		this.ideaNumber = ideaNumber;
	}

	public String getIdea() {
		return idea;
	}

	public void setIdea(String idea) {
		this.idea = idea;
	}

	public String getIdeaDate() {
		return ideaDate;
	}

	public void setIdeaDate(String ideaDate) {
		this.ideaDate = ideaDate;
	}

	public String getStealStatus() {
		return stealStatus;
	}

	public void setStealStatus(String stealStatus) {
		this.stealStatus = stealStatus;
	}

	public String getStealStatusDescription() {
		return stealStatusDescription;
	}

	public void setStealStatusDescription(String stealStatusDescription) {
		this.stealStatusDescription = stealStatusDescription;
	}

	public String getGreatness() {
		return greatness;
	}

	public void setGreatness(String greatness) {
		this.greatness = greatness;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	public Date getEnterDate() {
		return enterDate;
	}

	public void setEnterDate(Date enterDate) {
		this.enterDate = enterDate;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public String toString() {
		
		String thisObjectAsAString = "ideaId = " + ideaId + 
									 ", ideaNumber = " + ideaNumber + 
									 ", ideaDate = " + ideaDate + 
									 ", stealStatus = " + stealStatus +
									 ", stealStatusDescription = " + stealStatusDescription + 
									 ", greatness = " + greatness +
									 ", description = " + description + 
									 ", status = " + status + 
									 ", statusDate = " + statusDate +
									 ", enterDate = " + enterDate + 
									 ", modificationDate = " + modificationDate;
		
		return thisObjectAsAString;
	}

}
