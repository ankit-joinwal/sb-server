package com.bitlogic.sociallbox.service.transformers;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.Event;
import com.bitlogic.sociallbox.data.model.response.EventDetailsResponse;
import com.bitlogic.sociallbox.data.model.response.EventResponse;

public class EventTransformer implements Transformer<EventResponse, Event> {

	private static volatile EventTransformer instance = null;
	
	private EventTransformer(){
		
	}
	
	public static EventTransformer getInstance(){
		if(instance==null){
			synchronized (EventTransformer.class) {
				if(instance==null){
					instance = new EventTransformer();
				}
			}
		}
		return instance;
	}
	
	@Override
	public EventResponse transform(Event event) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.EVENT_RESPONSE_DATE_FORMAT);
		EventResponse createEventResponse = new EventResponse();
		//TODO
		//createEventResponse.setAttendees(meetup.getAttendees());
		
		createEventResponse.setDescription(event.getDescription());
		createEventResponse.setEventDetails(new EventDetailsResponse(event.getEventDetails()));
		createEventResponse.setUuid(event.getUuid());
		createEventResponse.setTitle(event.getTitle());
		//createEventResponse.setImage(event.getImage());
		if(event.getEventImages()!=null && !event.getEventImages().isEmpty()){
			createEventResponse.setDisplayImage(event.getEventImages().get(0));
		}
		createEventResponse.setTags(event.getTags());
		Date startDate = event.getStartDate();
		Date endDate = event.getEndDate();
		createEventResponse.setStartDate(dateFormat.format(startDate));
		createEventResponse.setEndDate(dateFormat.format(endDate));
		
		
		
		return createEventResponse;
	}
}
