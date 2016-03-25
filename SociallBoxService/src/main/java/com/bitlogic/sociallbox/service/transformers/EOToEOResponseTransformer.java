package com.bitlogic.sociallbox.service.transformers;

import com.bitlogic.sociallbox.data.model.EventOrganizer;
import com.bitlogic.sociallbox.data.model.response.EventOrganizerProfile;
import com.bitlogic.sociallbox.service.exception.ServiceException;

public class EOToEOResponseTransformer implements Transformer<EventOrganizerProfile, EventOrganizer>{

	private static volatile EOToEOResponseTransformer eoResponseTransformer =null;
	
	private EOToEOResponseTransformer() {

	}
	
	public static EOToEOResponseTransformer getInstance(){
		 if (eoResponseTransformer == null) {
	            synchronized (EOToEOResponseTransformer.class) {
	                // Double check
	                if (eoResponseTransformer == null) {
	                	eoResponseTransformer = new EOToEOResponseTransformer();
	                }
	            }
	        }
		return eoResponseTransformer;
	}
	
	@Override
	public EventOrganizerProfile transform(EventOrganizer eventOrganizer)
			throws ServiceException {
		
		EventOrganizerProfile eventOrganizerResp = new EventOrganizerProfile();
		eventOrganizerResp.setUuid(eventOrganizer.getUuid());
		eventOrganizerResp.setAddress(eventOrganizer.getAddress());
		eventOrganizerResp.setEmailId(eventOrganizer.getEmailId());
		eventOrganizerResp.setName(eventOrganizer.getName());
		eventOrganizerResp.setPhone1(eventOrganizer.getPhone1());
		eventOrganizerResp.setPhone2(eventOrganizer.getPhone2());
		eventOrganizerResp.setPhone3(eventOrganizer.getPhone3());
		eventOrganizerResp.setWebsite(eventOrganizer.getWebsite());
		eventOrganizerResp.setCoverPic(eventOrganizer.getCoverPic());
		eventOrganizerResp.setProfilePic(eventOrganizer.getProfilePic());
		
		return eventOrganizerResp;
	}
}
