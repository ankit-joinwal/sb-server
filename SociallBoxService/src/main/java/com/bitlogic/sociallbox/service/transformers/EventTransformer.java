package com.bitlogic.sociallbox.service.transformers;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.Event;
import com.bitlogic.sociallbox.data.model.EventResponse;

public class EventTransformer implements Transformer<EventResponse, Event> {

	
	@Override
	public EventResponse transform(Event event) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.MEETUP_DATE_FORMAT);
		EventResponse createEventResponse = new EventResponse();
		//TODO
		//createEventResponse.setAttendees(meetup.getAttendees());
		
		createEventResponse.setDescription(event.getDescription());
		createEventResponse.setEventDetails(event.getEventDetails());
		createEventResponse.setUuid(event.getUuid());
		createEventResponse.setTitle(event.getTitle());
		//createEventResponse.setImage(event.getImage());
		if(event.getEventImages()!=null && !event.getEventImages().isEmpty()){
			createEventResponse.setDisplayImage(event.getEventImages().get(0));
		}
		createEventResponse.setTags(event.getTags());
		Date startDate = event.getStartDate();
		Date endDate = event.getEndDate();
		createEventResponse.setIsLive(event.getIsLive());
		createEventResponse.setStartDate(dateFormat.format(startDate));
		createEventResponse.setEndDate(dateFormat.format(endDate));
		
		
		
		return createEventResponse;
	}
}
