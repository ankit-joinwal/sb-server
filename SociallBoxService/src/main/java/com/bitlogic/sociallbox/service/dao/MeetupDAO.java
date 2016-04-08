package com.bitlogic.sociallbox.service.dao;

import java.util.List;

import com.bitlogic.sociallbox.data.model.AddressComponentType;
import com.bitlogic.sociallbox.data.model.Meetup;
import com.bitlogic.sociallbox.data.model.MeetupAttendeeEntity;
import com.bitlogic.sociallbox.data.model.MeetupImage;
import com.bitlogic.sociallbox.data.model.MeetupMessage;
import com.bitlogic.sociallbox.data.model.requests.SaveAttendeeResponse;

public interface MeetupDAO {

	public Meetup createMeetup(Meetup meetup);
	
	public Meetup getMeetup(String id);
	
	public Meetup saveMeetup(Meetup meetup);
	
	public void saveAttendeeResponse(SaveAttendeeResponse attendeeResponse);
	
	public void sendMessageInMeetup(MeetupMessage meetupMessage,Meetup meetup, MeetupAttendeeEntity attendeeEntity);
	
	public List<AddressComponentType> getAddressTypes();
	
	public void saveMeetupImages(List<MeetupImage> images);
	
	public MeetupAttendeeEntity addAttendee(MeetupAttendeeEntity meetupAttendee);
	
	public MeetupAttendeeEntity getAttendeeById(Long id);
	
	public MeetupAttendeeEntity getMeetupAttendee(Long userId, String meetupId);
	
	public MeetupAttendeeEntity getMeetupAttendeeByAttendeeId(Long attendeeId);
	
	public List<MeetupImage> getMeetupImages(String meetupId);
	
	public List<MeetupMessage> getMeetupMessages(Meetup meetup, Integer page);
	
	public List<MeetupAttendeeEntity> getAttendees(Meetup meetup);
	
	
}
