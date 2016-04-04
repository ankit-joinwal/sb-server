package com.bitlogic.sociallbox.data.model.notifications;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class GCMNotificationResponse {

	@JsonProperty("multicast_id")
	private Integer multicastId;
	
	@JsonProperty("status")
	private ResponseStatus status;
	
	@JsonProperty("success")
	private Integer successMessageCount;
	
	@JsonProperty("failure")
	private Integer failedMessageCount;
	
	private List<Result> results;
	
	public ResponseStatus getStatus() {
		return status;
	}


	public void setStatus(ResponseStatus status) {
		this.status = status;
	}


	public Integer getMulticastId() {
		return multicastId;
	}


	public void setMulticastId(Integer multicastId) {
		this.multicastId = multicastId;
	}


	public Integer getSuccessMessageCount() {
		return successMessageCount;
	}


	public void setSuccessMessageCount(Integer successMessageCount) {
		this.successMessageCount = successMessageCount;
	}


	public Integer getFailedMessageCount() {
		return failedMessageCount;
	}


	public void setFailedMessageCount(Integer failedMessageCount) {
		this.failedMessageCount = failedMessageCount;
	}


	public List<Result> getResults() {
		return results;
	}


	public void setResults(List<Result> results) {
		this.results = results;
	}


	@JsonIgnoreProperties(ignoreUnknown=true)
	public static final class Result{
		
		@JsonProperty("message_id")
		private String messageId;
		
		@JsonProperty("registration_id")
		private String deviceGcmId;
		
		@JsonProperty("error")
		private String errorMessage;

		public String getMessageId() {
			return messageId;
		}

		public void setMessageId(String messageId) {
			this.messageId = messageId;
		}

		public String getDeviceGcmId() {
			return deviceGcmId;
		}

		public void setDeviceGcmId(String deviceGcmId) {
			this.deviceGcmId = deviceGcmId;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}
		
		@Override
		public String toString() {
			return "Result [messageId = "+this.messageId + 
					" , GCM Id = "+this.deviceGcmId+
					" , error = "+this.errorMessage+ " ]";
		}
	}
	
	@Override
	public String toString() {
		return "NotificationResponse [multicastId = "+this.multicastId+
				" , SuccessMessageCount = "+this.successMessageCount+
				" , FailedMessageCount = "+this.failedMessageCount+
				" , Results = "+this.results+ " ]";
	}
}
