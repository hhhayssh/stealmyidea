package com.stealmyidea.model;

import java.util.ArrayList;
import java.util.List;

import com.stealmyidea.StealMyIdeaConstants;

public class GenericServerResponse {
	
	protected String status;
	
	protected String statusCode;
	
	protected List<String> messages;
	
	protected Object data;
	
	public GenericServerResponse() {
		setMessages(new ArrayList<String>());
	}
	
	public GenericServerResponse(Object data) {
		setSuccess();
		setData(data);
	}
	
	public void setSuccess() {
		setStatus(StealMyIdeaConstants.STATUS_SUCCESS);
	}
	
	public void setError(String message) {
		setError();
		addMessage(message);
	}
	
	public void setError(List<String> messages) {
		setError();
		addMessages(messages);
	}
	
	public void setError() {
		setStatus(StealMyIdeaConstants.STATUS_ERROR);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public void addMessage(String message) {
		messages.add(message);
	}
	
	public void addMessages(List<String> messages) {
		messages.addAll(messages);
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public String thisObjectAsAString() {
		
		String thisObjectAsAString = "status = " + status + 
									 ", statusCode = " + statusCode +
									 ", messages = " + messages + 
									 ", data = " + data;
		
		return thisObjectAsAString;
	}

}
