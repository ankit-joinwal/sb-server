package com.bitlogic.sociallbox.service.controller;

import java.sql.SQLException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.EntityNotFoundException;
import com.bitlogic.sociallbox.service.exception.MessageDTO;
import com.bitlogic.sociallbox.service.exception.MessageDTO.MessageType;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.exception.ServiceException;
import com.bitlogic.sociallbox.service.exception.UnauthorizedException;

@ControllerAdvice
public class ServiceExceptionAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceExceptionAdvice.class);
	
	@Autowired
	private MessageSource msgSource;
	
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public MessageDTO processServerError(EntityNotFoundException ex) {

		MessageDTO message = null;
		String messageKey = ex.getMessage();
		Locale currentLocale = LocaleContextHolder.getLocale();
		String messageDetails = msgSource.getMessage(messageKey, null,
				currentLocale);
		ex.setMessage(messageDetails);
		LOGGER.error("EntityNotFoundException occurred .",ex);
		message = new MessageDTO( MessageType.ERROR,ex);
		return message;
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public MessageDTO processUnauthorizedError(UnauthorizedException ex) {

		MessageDTO message = null;
		String messageKey = ex.getMessage();
		Locale currentLocale = LocaleContextHolder.getLocale();
		String messageDetails = msgSource.getMessage(messageKey, null,
				currentLocale);
		ex.setMessage(messageDetails);
		LOGGER.error("UnauthorizedException occurred .",ex);
		message = new MessageDTO( MessageType.ERROR,ex);
		return message;
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public MessageDTO processUnauthorizedError(AccessDeniedException ex) {

		MessageDTO message = null;
		Locale currentLocale = LocaleContextHolder.getLocale();
		String messageDetails = msgSource.getMessage("error.login.user.unauthorized", null,
				currentLocale);
		UnauthorizedException unauthorizedException = new UnauthorizedException(RestErrorCodes.ERR_002, messageDetails);
		LOGGER.error("UnauthorizedException occurred .",unauthorizedException);
		message = new MessageDTO( MessageType.ERROR,unauthorizedException);
		return message;
	}


	
	@ExceptionHandler(ClientException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public MessageDTO processClientError(ClientException ex) {

		MessageDTO message = null;
		String messageKey = ex.getMessage();
		Locale currentLocale = LocaleContextHolder.getLocale();
		String messageDetails = msgSource.getMessage(messageKey, null,
				currentLocale);
		ex.setMessage(messageDetails);
		LOGGER.error("ClientException occurred .",ex);
		message = new MessageDTO( MessageType.ERROR,ex);
		return message;
	}

	
	@ExceptionHandler(ServiceException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public MessageDTO processServerError(ServiceException ex) {

		MessageDTO message = null;
		String messageKey = ex.getMessage();
		Locale currentLocale = LocaleContextHolder.getLocale();
		String messageDetails = msgSource.getMessage(messageKey, null,
				currentLocale);
		ex.setMessage(messageDetails);
		LOGGER.error("ServiceException occurred .",ex);
		message = new MessageDTO( MessageType.ERROR,ex);
		return message;
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public MessageDTO processServerError(Exception ex) {
		MessageDTO message = null;
		ServiceException serviceException = new ServiceException(Constants.GEO_SERVICE_NAME,RestErrorCodes.ERR_050,ex.getMessage());
		LOGGER.error("ServiceException occurred .",ex);
		message = new MessageDTO( MessageType.ERROR,serviceException);
		return message;
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public MessageDTO processBadCredentialsError(BadCredentialsException ex) {

		MessageDTO message = null;
		String messageKey = ex.getMessage();
		Locale currentLocale = LocaleContextHolder.getLocale();
		String messageDetails = msgSource.getMessage(messageKey, null,
				currentLocale);
		UnauthorizedException unauthorizedException = new UnauthorizedException(RestErrorCodes.ERR_002, messageDetails);
		LOGGER.error("UnauthorizedException occurred .",unauthorizedException);
		message = new MessageDTO( MessageType.ERROR,unauthorizedException);
		return message;
	}

	
	  @ExceptionHandler({SQLException.class,DataAccessException.class})
	  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	  @ResponseBody
	  public MessageDTO databaseError(Exception exception) {
			MessageDTO message = null;
			ServiceException serviceException = new ServiceException(Constants.GEO_SERVICE_NAME,RestErrorCodes.ERR_051,exception.getMessage());
			LOGGER.error("ServiceException occurred .",serviceException);
			message = new MessageDTO( MessageType.ERROR,serviceException);
			return message;
	  }
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ResponseBody
	public MessageDTO processValidationError(MethodArgumentNotValidException ex) {
		LOGGER.error("MethodArgumentNotValidException occurred .",ex);
		BindingResult result = ex.getBindingResult();
		FieldError error = result.getFieldError();
		return processFieldError(error);
	}


	private MessageDTO processFieldError(FieldError error) {
		MessageDTO message = null;
		if (error != null) {
			Locale currentLocale = LocaleContextHolder.getLocale();
			String msg = msgSource.getMessage(error.getDefaultMessage(), null,
					currentLocale);
			ClientException clientException = new ClientException(RestErrorCodes.ERR_001 , msg);
			message = new MessageDTO(MessageType.ERROR,clientException);
		}
		return message;
	}

}
