package com.bitlogic.sociallbox.service.exception;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.NONE)
public class MessageDTO implements Serializable{

	public MessageDTO() {
		super();
	}
	private static final long serialVersionUID = 1L;
	@XmlElement
	private MessageType messageType;
	
	@XmlElement(name="errorDetails")
	private RestException exception = new RestException();
	
	public MessageDTO(MessageType messageType,RestException exception){
		
		this.messageType = messageType;
		this.exception = exception;
	}
	
	public enum MessageType{
		INFO, SUCCESS, WARNING, ERROR
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
	public RestException getException() {
		return exception;
	}
	public void setException(RestException exception) {
		this.exception = exception;
	}
	
	
	
}
