package com.bitlogic.sociallbox.service.dao;

import java.util.List;

import com.bitlogic.sociallbox.data.model.AddressComponentType;
import com.bitlogic.sociallbox.data.model.EventImage;
import com.bitlogic.sociallbox.data.model.Meetup;
import com.bitlogic.sociallbox.data.model.MeetupImage;
import com.bitlogic.sociallbox.data.model.MeetupMessage;
import com.bitlogic.sociallbox.data.model.requests.SaveAttendeeResponse;

public interface MeetupDAO {

	public Meetup createMeetup(Meetup meetup);
	
	public Meetup getMeetup(String id);
	
	public Meetup saveMeetup(Meetup meetup);
	
	public void saveAttendeeResponse(SaveAttendeeResponse attendeeResponse);
	
	public void sendMessageInMeetup(MeetupMessage meetupMessage,String meetupId,Long senderId);
	
	public List<AddressComponentType> getAddressTypes();
	
	public void saveMeetupImages(List<MeetupImage> images);
}
