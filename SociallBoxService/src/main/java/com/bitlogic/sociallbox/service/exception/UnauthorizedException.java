package com.bitlogic.sociallbox.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value=HttpStatus.UNAUTHORIZED,reason="User cannot be authenticated")
public class UnauthorizedException extends RestException{
	private static final long serialVersionUID = 1L;

	public UnauthorizedException() {
		super();
	}
	
	public UnauthorizedException(RestErrorCodes errorCode , String message){
		super(errorCode, message);
	}
}
