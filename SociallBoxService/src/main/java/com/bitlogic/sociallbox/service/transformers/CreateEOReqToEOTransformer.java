package com.bitlogic.sociallbox.service.transformers;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.EventOrganizer;
import com.bitlogic.sociallbox.data.model.requests.CreateEventOrganizerRequest;
import com.bitlogic.sociallbox.service.exception.ServiceException;

public class CreateEOReqToEOTransformer implements Transformer<EventOrganizer, CreateEventOrganizerRequest>{

	
	private static volatile CreateEOReqToEOTransformer instance = null;
	
	private CreateEOReqToEOTransformer(){
		
	}
	public static CreateEOReqToEOTransformer getInstance(){
		if(instance==null){
			synchronized (CreateEOReqToEOTransformer.class) {
				if(instance==null){
					instance = new CreateEOReqToEOTransformer();
				}
			}
		}
		
		return instance;
	}
	@Override
	public EventOrganizer transform(CreateEventOrganizerRequest createOrgRequest)
			throws ServiceException {
		
		EventOrganizer eventOrganizer = new EventOrganizer();
		eventOrganizer.setAddress(createOrgRequest.getAddress());
		eventOrganizer.setEmailId(createOrgRequest.getEmailId());
		eventOrganizer.setIsEnabled(Boolean.TRUE);
		eventOrganizer.setName(createOrgRequest.getName());
		eventOrganizer.setPhone1(createOrgRequest.getPhone1());
		eventOrganizer.setPhone2(createOrgRequest.getPhone2()==null?Constants.BLANK:createOrgRequest.getPhone2());
		eventOrganizer.setPhone3(createOrgRequest.getPhone3()==null ? Constants.BLANK : createOrgRequest.getPhone3());

		return eventOrganizer;
	}
}
