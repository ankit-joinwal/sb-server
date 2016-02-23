package com.bitlogic.sociallbox.service.business;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.Meetup;
import com.bitlogic.sociallbox.data.model.MeetupMessage;
import com.bitlogic.sociallbox.data.model.requests.CreateMeetupRequest;
import com.bitlogic.sociallbox.data.model.requests.EditMeetupRequest;
import com.bitlogic.sociallbox.data.model.requests.SaveAttendeeResponse;

public interface MeetupService {

	public Meetup createMetup(CreateMeetupRequest createMeetupRequest);
	
	public Meetup getMeetup(String meetupId);
	
	public Meetup addAttendees(EditMeetupRequest editMeetupRequest);
	
	public void saveAttendeeResponse(SaveAttendeeResponse attendeeResponse);
	
	public void sendMessageInMeetup(MeetupMessage meetupMessage,String meetupId,String senderId);
	
	public void uploadImageToMeetup(String deviceId,String imagesURL,List<MultipartFile> images , String meetupId) ;

}
