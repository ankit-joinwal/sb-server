package com.bitlogic.sociallbox.service.business.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.UserEventActivity;
import com.bitlogic.sociallbox.data.model.UserMeetupActivity;
import com.bitlogic.sociallbox.data.model.UserSocialActivity;
import com.bitlogic.sociallbox.data.model.UserSocialActivity.ActivityTime;
import com.bitlogic.sociallbox.service.business.EventService;
import com.bitlogic.sociallbox.service.business.MeetupService;
import com.bitlogic.sociallbox.service.business.MySociallBoxService;
import com.bitlogic.sociallbox.service.dao.SmartDeviceDAO;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.exception.UnauthorizedException;
import com.bitlogic.sociallbox.service.utils.GeoUtils;
import com.bitlogic.sociallbox.service.utils.LoggingService;

@Service
@Transactional
public class MySociallBoxServiceImpl extends LoggingService implements MySociallBoxService,Constants{

	private static final Logger LOGGER = LoggerFactory.getLogger(MySociallBoxServiceImpl.class);
	
	@Override
	public Logger getLogger() {
		return LOGGER;
	}
	
	@Autowired
	private MeetupService meetupService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private SmartDeviceDAO smartDeviceDAO;
	
	
	@Override
	public List<UserSocialActivity<?>> getMySociallBox(ActivityTime timeline,
			String deviceId,String userLocation) {
		String LOG_PREFIX = "MySociallBoxServiceImpl-getMySociallBox";
		 User user = this.smartDeviceDAO.getUserInfoFromDeviceId(deviceId);
		 if(user==null){
			 throw new UnauthorizedException(RestErrorCodes.ERR_002, ERROR_LOGIN_USER_UNAUTHORIZED);
		 }
		 
		// Parse Location is Format Lattitude,Longitude
		Map<String, Double> cordinatesMap = GeoUtils
					.getCoordinatesFromLocation(userLocation);
			
		List<UserSocialActivity<?>> socialActivities = new ArrayList<UserSocialActivity<?>>();
		if(timeline == ActivityTime.UPCOMING){
			List<UserSocialActivity<UserMeetupActivity>> meetupActivities = this.meetupService.getUserUpcomingMeetupActivities(user);
			socialActivities.addAll(meetupActivities);
			List<UserSocialActivity<UserEventActivity>> eventActivities = this.eventService.getUserUpcomingEvents(user,cordinatesMap);
			socialActivities.addAll(eventActivities);
			
		}else{
			List<UserSocialActivity<UserMeetupActivity>> activities = this.meetupService.getUserPastMeetupActivities(user);
			socialActivities.addAll(activities);
			List<UserSocialActivity<UserEventActivity>> eventActivities = this.eventService.getUserPastEvents(user,cordinatesMap);
			socialActivities.addAll(eventActivities);
		}
		/*Collections.sort(socialActivities,new Comparator<UserSocialActivity<?>>() {
			@Override
			public int compare(UserSocialActivity<?> o1,
					UserSocialActivity<?> o2) {
				// TODO Auto-generated method stub
				return 0;
			}
		});*/

		Collections.sort(socialActivities);
		logInfo(LOG_PREFIX, "Found {} activities of user {}", user.getName());
		return socialActivities;
	}
}
