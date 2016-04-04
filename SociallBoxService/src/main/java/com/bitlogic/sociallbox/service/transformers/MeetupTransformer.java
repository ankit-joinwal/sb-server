package com.bitlogic.sociallbox.service.transformers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.Meetup;
import com.bitlogic.sociallbox.data.model.SocialDetailType;
import com.bitlogic.sociallbox.data.model.UserSocialDetail;
import com.bitlogic.sociallbox.data.model.requests.MeetupResponse;
import com.bitlogic.sociallbox.data.model.response.UserPublicProfile;

public class MeetupTransformer implements Transformer<MeetupResponse, Meetup> {

	private static volatile MeetupTransformer instance = null;
	
	private MeetupTransformer() {
	}
	
	public static MeetupTransformer getInstance(){
		if(instance == null){
			synchronized (MeetupTransformer.class) {
				if(instance==null){
					instance = new MeetupTransformer();
				}
			}
		}
		return instance;
	}
	
	@Override
	public MeetupResponse transform(Meetup meetup) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.MEETUP_RESPONSE_DATE_FORMAT);
		MeetupResponse createMeetupResponse = new MeetupResponse();
		
		createMeetupResponse.setDescription(meetup.getDescription());
		createMeetupResponse.setLocation(meetup.getLocation());
		createMeetupResponse.setUuid(meetup.getUuid());
		createMeetupResponse.setTitle(meetup.getTitle());
		createMeetupResponse.setStatus(meetup.getStatus());
		
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
		
		if(meetup.getOrganizer()!=null){
			UserPublicProfile userPublicProfile = new UserPublicProfile(meetup.getOrganizer());
			if(meetup.getOrganizer().getSocialDetails()!=null){
				Set<UserSocialDetail> socialDetailsToReturn = new HashSet<>();
				Set<UserSocialDetail> socialDetails = meetup.getOrganizer().getSocialDetails();
				for(UserSocialDetail socialDetail : socialDetails){
					if(socialDetail.getSocialDetailType() == SocialDetailType.USER_PROFILE_PIC){
						socialDetailsToReturn.add(socialDetail);
						break;
					}
				}
				userPublicProfile.setSocialDetails(socialDetailsToReturn);
				createMeetupResponse.setOrganizer(userPublicProfile);
			}
		}
		
		//TODO: Another API to get meetup messages
		/*Date now = new Date();
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
		createMeetupResponse.setMessages(meetup.getMessages());*/
		
		return createMeetupResponse;
	}
}
