package com.breezecom.tally.business.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("unused")
public class MessageRecordRequest {
	private String requestType;
	private String originatingSystem;
	private String originatingID;
	private String authToken;
	private String sessionID;
	private String environmentID;
	private RequestTypeObject requestTypeObject;

	// Constructors
	// - request from beginning
	public MessageRecordRequest() {
		this(1, "1900-01-01", "2100-01-01");
	}
	
	// - request from last count (exclusive)
	public MessageRecordRequest(int count) {
		this(count, "1900-01-01", "2100-01-01");
	}

	// - request from arbitrary count / time frame
	public MessageRecordRequest(int callRefNo, String start, String end) {
		this.requestType = "RETRIEVE_TENANT_SERVICE_STATS_REQ";
		this.originatingSystem = "ggfa-01";
		this.originatingID = "ggfa-01";
		this.authToken = "ggfa-01";
		this.sessionID = "ggfa-01";
		this.environmentID = "PROD";
		this.requestTypeObject = new RequestTypeObject(callRefNo, start, end);
	}
	
	// Accessors
	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
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

	public String getEnvironmentID() {
		return environmentID;
	}

	public void setEnvironmentID(String environmentID) {
		this.environmentID = environmentID;
	}

	public RequestTypeObject getRequestTypeObject() {
		return requestTypeObject;
	}

	public void setRequestTypeObject(RequestTypeObject requestTypeObject) {
		this.requestTypeObject = requestTypeObject;
	}

	// Inner class
	public class RequestTypeObject {
		private int callRefNo;
		private String startDateTime;
		private String endDateTime;

		// Inner class constructor
		public RequestTypeObject(int callRefNo, String startDateTime, String endDateTime) {
			super();
			this.callRefNo = callRefNo;
			this.startDateTime = startDateTime;
			this.endDateTime = endDateTime;
		}

		// Inner class accessors
		public int getCallRefNo() {
			return callRefNo;
		}

		public void setCallRefNo(int callRefNo) {
			this.callRefNo = callRefNo;
		}

		public String getStartDateTime() {
			return startDateTime;
		}

		public void setStartDateTime(String startDateTime) {
			this.startDateTime = startDateTime;
		}

		public String getEndDateTime() {
			return endDateTime;
		}

		public void setEndDateTime(String endDateTime) {
			this.endDateTime = endDateTime;
		}
	
	}

}
