package com.bitlogic.sociallbox.service.exception;

public class RestException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public RestException(){
		super();
	}
	
	public RestException(RestErrorCodes errorCode, String message){
		super();
		this.errorCode = errorCode;
		this.message = message;
	}
	
	private RestErrorCodes errorCode;
	
	private String message;

	public RestErrorCodes getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(RestErrorCodes errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
