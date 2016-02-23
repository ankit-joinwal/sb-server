package com.bitlogic.sociallbox.service.exception;

public class EntityNotFoundException extends RestException{

	private static final long serialVersionUID = 1L;
	
	private Object entityId;
	
	public EntityNotFoundException(){
		super();
	}
	
	public EntityNotFoundException(Object entityId , RestErrorCodes errorCode , String message){
		super(errorCode, message);
		this.entityId = entityId;
	}

	public Object getEntityId() {
		return entityId;
	}

	public void setEntityId(Object entityId) {
		this.entityId = entityId;
	}
	
}
