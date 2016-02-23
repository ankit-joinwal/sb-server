package com.bitlogic.sociallbox.service.exception;


/** Error Returned from web service
 * @author ajoinwal
 *
 */

public class ServiceException extends RestException{

	private static final long serialVersionUID = 1L;
	
	public ServiceException(){
		super();
	}
	
	public ServiceException(String serviceName,RestErrorCodes errorCode,String message){
		super(errorCode,message);
		this.serviceName = serviceName;
	}
	
	private String serviceName;
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	
}
