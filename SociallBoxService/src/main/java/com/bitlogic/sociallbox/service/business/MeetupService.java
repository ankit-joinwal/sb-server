package com.bitlogic.sociallbox.service.business;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.bitlogic.sociallbox.data.model.Meetup;
import com.bitlogic.sociallbox.data.model.MeetupAttendee;
import com.bitlogic.sociallbox.data.model.MeetupImage;
import com.bitlogic.sociallbox.data.model.MeetupMessage;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.UserMeetupActivity;
import com.bitlogic.sociallbox.data.model.UserSocialActivity;
import com.bitlogic.sociallbox.data.model.requests.AddMeetupAttendeesRequest;
import com.bitlogic.sociallbox.data.model.requests.CreateMeetupRequest;
import com.bitlogic.sociallbox.data.model.requests.EditMeetupRequest;
import com.bitlogic.sociallbox.data.model.requests.MeetupResponse;
import com.bitlogic.sociallbox.data.model.requests.SaveAttendeeResponse;

public interface MeetupService {

	public MeetupResponse createMetup(CreateMeetupRequest createMeetupRequest);
	
	public Meetup editMeetup(EditMeetupRequest editMeetupRequest);
	
	public MeetupResponse getMeetup(String deviceId,String meetupId);
	
	public List<MeetupImage> getMeetupImages(String deviceId,String meetupId);
	
	public List<MeetupAttendee> addAttendees(AddMeetupAttendeesRequest editMeetupRequest);
	
	public List<MeetupAttendee> getMeetupAttendees(String meetupId);
	
	public void saveAttendeeResponse(SaveAttendeeResponse attendeeResponse,String deviceId);
	
	public void sendMessageInMeetup(MeetupMessage meetupMessage,String meetupId,Long attendeeId);
	
	public List<MeetupImage> uploadImageToMeetup(Boolean isDp,String deviceId,String imagesURL,List<MultipartFile> images , String meetupId) ;
	
	public List<MeetupMessage> getMeetupMessages(String meetupId,Integer page);
	
	public void cancelMeetup(String deviceId,String meetupId);
	
	public List<UserSocialActivity<UserMeetupActivity>> getUserPastMeetupActivities(User user);
	
	public List<UserSocialActivity<UserMeetupActivity>> getUserUpcomingMeetupActivities(User user);

	public List<MeetupResponse> getPendingMeetupInvites(String deviceId);
}
