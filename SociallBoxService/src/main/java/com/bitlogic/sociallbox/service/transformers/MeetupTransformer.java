package com.bitlogic.sociallbox.service.transformers;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.Meetup;
import com.bitlogic.sociallbox.data.model.MeetupMessage;
import com.bitlogic.sociallbox.data.model.requests.MeetupResponse;

public class MeetupTransformer implements Transformer<MeetupResponse, Meetup> {

	
	@Override
	public MeetupResponse transform(Meetup meetup) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.MEETUP_DATE_FORMAT);
		MeetupResponse createMeetupResponse = new MeetupResponse();
		
		createMeetupResponse.setAttendees(meetup.getAttendees());
		createMeetupResponse.setDescription(meetup.getDescription());
		createMeetupResponse.setLocation(meetup.getLocation());
		createMeetupResponse.setOrganizer(meetup.getOrganizer());
		createMeetupResponse.setUuid(meetup.getUuid());
		createMeetupResponse.setTitle(meetup.getTitle());
		
		if(meetup.getEventAtMeetup()!=null){
			createMeetupResponse.setEventAtMeetup(meetup.getEventAtMeetup().getUuid());
		}
		
		Date startDate = meetup.getStartDate();
		Date endDate = meetup.getEndDate();
		createMeetupResponse.setStartDate(dateFormat.format(startDate));
		createMeetupResponse.setEndDate(dateFormat.format(endDate));
		
		if(meetup.getImages()!=null && !meetup.getImages().isEmpty()){
			createMeetupResponse.setDisplayImage(meetup.getImages().get(0));
		}
		
		Date now = new Date();
		for(MeetupMessage meetupMessage : meetup.getMessages()){
			Date messageTime = meetupMessage.getCreateDt();
			long diff = now.getTime() - messageTime.getTime();//in millisecons
			
			int diffDays = (int)(diff / (24 * 60 * 60 * 1000));
			
			if(diffDays>0){
				meetupMessage.setTimeToDisplay(diffDays+"Day ago");
				continue;
			}
			
			int diffHours = (int)(diff / (60 * 60 * 1000) % 24);
			if(diffHours>0){
				meetupMessage.setTimeToDisplay(diffHours+"Hour ago");
				continue;
			}
			
			int diffMinutes =(int) (diff / (60 * 1000) % 60);
			if(diffMinutes>0){
				meetupMessage.setTimeToDisplay(diffMinutes+"Min ago");
				continue;
			}
			
			meetupMessage.setTimeToDisplay("Just Now");
		}
		createMeetupResponse.setMessages(meetup.getMessages());
		
		return createMeetupResponse;
	}
}
