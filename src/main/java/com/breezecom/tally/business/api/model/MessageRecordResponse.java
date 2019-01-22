package com.breezecom.tally.business.api.model;

import java.util.List;

public class MessageRecordResponse {
	private int responseCode;
	private String errorCode;
	private String returnMessage;
	private String additionalData;
	private String originatingSystem;
	private String originatingID;
	private String authToken;
	private String sessionID;
	private String responseType;
	private String environmentID;
	private List<ResponseObject> responseObject;

	@Override
	public String toString() {
		String toStringOutput = "MessageRecordResponse: " 
				+ "\nresponseCode = " + responseCode 
				+ "\nerrorCode = " + errorCode
				+ "\nreturnMessage = " + returnMessage
				+ "\nadditionalData = " + additionalData
				+ "\noriginatingSystem = " + originatingSystem 
				+ "\noriginatingID = " + originatingID 
				+ "\nauthToken = " + authToken 
				+ "\nsessionID = " + sessionID
				+ "\nresponseType = " + responseType
				+ "\nenvironmentID = " + environmentID;
		
		if (responseObject != null) {
			toStringOutput += "\nnumber of response object = " + responseObject.size();
		} else {
			toStringOutput += "!!response object list is null!!";
		}
				
		// Omitting response objects (there're too many)
//		toStringOutput += "\n[";
//		for (ResponseObject obj : responseObject) {
//			toStringOutput += obj.toString();
//		}
//		toStringOutput += "]\n";

		return toStringOutput;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	public String getAdditionalData() {
		return additionalData;
	}

	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}

	public String getOriginatingSystem() {
		return originatingSystem;
	}

	public void setOriginatingSystem(String originatingSystem) {
		this.originatingSystem = originatingSystem;
	}

	public String getOriginatingID() {
		return originatingID;
	}

	public void setOriginatingID(String originatingID) {
		this.originatingID = originatingID;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public String getEnvironmentID() {
		return environmentID;
	}

	public void setEnvironmentID(String environmentID) {
		this.environmentID = environmentID;
	}

	public List<ResponseObject> getResponseObject() {
		return responseObject;
	}

	public void setResponseObject(List<ResponseObject> responseObject) {
		this.responseObject = responseObject;
	}
}
