package com.bitlogic.sociallbox.service.business.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.Meetup;
import com.bitlogic.sociallbox.data.model.MeetupImage;
import com.bitlogic.sociallbox.data.model.SocialBoxConfig;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.notifications.Notification;
import com.bitlogic.sociallbox.data.model.notifications.NotificationMessage;
import com.bitlogic.sociallbox.data.model.notifications.NotificationType;
import com.bitlogic.sociallbox.service.business.NotificationService;
import com.bitlogic.sociallbox.service.dao.MeetupDAO;
import com.bitlogic.sociallbox.service.dao.UserDAO;
import com.bitlogic.sociallbox.service.utils.LoggingService;
import com.bitlogic.sociallbox.service.utils.NotificationUtils;

@Service("notificationService")
@Transactional
public class NotificationServiceImpl extends LoggingService implements NotificationService,Constants{

	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);
	@Override
	public Logger getLogger() {
		return LOGGER;
	}
	
	@Autowired
	private SocialBoxConfig socialBoxConfig;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private MeetupDAO meetupDAO;
	
	@Override
	public void notifyAboutNewFriend(User actor , List<User> friends){
		String lOG_PREFIX = "NotificationServiceImpl-notifyAboutNewFriend";
		
		//Prepare Notification
		Notification newFriendNotification = new Notification();
		
		//Type
		newFriendNotification.setType(NotificationType.NEW_FRIEND_NOTIFICATION.getType());
		
		//Reciever Ids
		List<Long> friendIds = new ArrayList<Long>(friends.size());
		for(User friend : friends){
			friendIds.add(friend.getId());
		}
		newFriendNotification.setRecieverIds(friendIds);
		
		//Prepare Notification Message
		NotificationMessage notificationMessage = new NotificationMessage();
		//Prepare Notification Payload
		NotificationMessage.NotificationPayload notificationPayload = new NotificationMessage.NotificationPayload();
		notificationPayload.setTitle(NEW_FRIEND_JOINED_NOT_TITLE);
		notificationPayload.setBody(String.format(NEW_FRIEND_JOINED_NOT_BODY,actor.getName()));
		
		//Set Notification Payload in Notification Message
		notificationMessage.setNotificationPayload(notificationPayload);
		
		//Prepare Data Payload
		NotificationMessage.DataPayload dataPayload = new NotificationMessage.DataPayload();
		dataPayload.setActor(actor.getName());
		dataPayload.setType(NotificationType.NEW_FRIEND_NOTIFICATION.getType());
		dataPayload.setTarget(NEW_FRIEND_JOINED_NOT_TARGET);
		dataPayload.setIcon(actor.getProfilePic());
		//Set Data payload in Notification Message
		notificationMessage.setDataPayload(dataPayload);
		
		//Set NotificationMessage in Notification 
		newFriendNotification.setNotificationMessage(notificationMessage);
		
		logInfo(lOG_PREFIX, "Sending Notification Request {}",newFriendNotification);
		NotificationUtils.send(restTemplate, newFriendNotification, socialBoxConfig);
		
		logInfo(lOG_PREFIX, "Notification sent");
	}
	

	
	@Override
	public void notifyAboutMeetupInvite(User actor, Meetup meetup,
			List<Long> attendeeIds) {
		String lOG_PREFIX = "NotificationServiceImpl-notifyAboutMeetupInvite";
		//Prepare Notification
		Notification meetupInviteNotification = new Notification();
		
		//Type
		meetupInviteNotification.setType(NotificationType.NEW_MEETUP_NOTIFICATION.getType());
		
		//Reciever Ids
		meetupInviteNotification.setRecieverIds(attendeeIds);
		
		//Prepare Notification Message
		NotificationMessage notificationMessage = new NotificationMessage();
		
		//Prepare Notification Payload
		NotificationMessage.NotificationPayload notificationPayload = new NotificationMessage.NotificationPayload();
		notificationPayload.setTitle(NEW_MEETUP_INVITE_NOT_TITLE);
		notificationPayload.setBody(String.format(NEW_MEETUP_INVITE_NOT_BODY,actor.getName(),meetup.getTitle()));
		
		//Set Notification Payload in Notification Message
		notificationMessage.setNotificationPayload(notificationPayload);
		
		//Prepare Data Payload
		NotificationMessage.DataPayload dataPayload = new NotificationMessage.DataPayload();
		dataPayload.setActor(actor.getName());
		dataPayload.setType(NotificationType.NEW_MEETUP_NOTIFICATION.getType());
		dataPayload.setTarget(String.format(NEW_MEETUP_INVITE_NOT_TARGET,meetup.getTitle()));
		dataPayload.setIcon(actor.getProfilePic());
		
		String meetupsBaseURL = socialBoxConfig.getMeetupsBaseUrl();
		dataPayload.setActionURL(meetupsBaseURL+meetup.getUuid());
		
		//Set Data payload in Notification Message
		notificationMessage.setDataPayload(dataPayload);
		
		meetupInviteNotification.setNotificationMessage(notificationMessage);
		
		logInfo(lOG_PREFIX, "Sending Notification Request {}",meetupInviteNotification);
		NotificationUtils.send(restTemplate, meetupInviteNotification, socialBoxConfig);
		
		logInfo(lOG_PREFIX, "Notification sent");
	}
	
	@Override
	public void notifyAboutMeetupPhoto(User actor, List<MeetupImage> photos,
			Meetup meetup) {
		
		String LOG_PREFIX = "NotificationServiceImpl-notifyAboutMeetupPhoto";
		//Prepare Notification
		Notification meetupPhotoNotification = new Notification();
		
		//Type
		meetupPhotoNotification.setType(NotificationType.MEETUP_PHOTO_NOTIFICATION.getType());
		
		//Reciever Ids
		List<Long> attendeeIds = this.meetupDAO.getAttendeeIdsExcept(meetup, actor.getId());
		if(attendeeIds==null || attendeeIds.isEmpty()){
			return;
		}
		meetupPhotoNotification.setRecieverIds(attendeeIds);
		
		//Prepare Notification Message
		NotificationMessage notificationMessage = new NotificationMessage();
		
		//Prepare Notification Payload
		NotificationMessage.NotificationPayload notificationPayload = new NotificationMessage.NotificationPayload();
		notificationPayload.setTitle(String.format(NEW_MEETUP_PHOTO_NOT_TITLE,meetup.getTitle()));
		notificationPayload.setBody(String.format(NEW_MEETUP_PHOTO_NOT_BODY,actor.getName()));
		
		//Set Notification Payload in Notification Message
		notificationMessage.setNotificationPayload(notificationPayload);
		
		//Prepare Data Payload
		NotificationMessage.DataPayload dataPayload = new NotificationMessage.DataPayload();
		dataPayload.setActor(actor.getName());
		dataPayload.setType(NotificationType.MEETUP_PHOTO_NOTIFICATION.getType());
		dataPayload.setTarget(String.format(NEW_MEETUP_PHOTO_NOT_TARGET,meetup.getTitle()));
		dataPayload.setIcon(actor.getProfilePic());
		dataPayload.setImage(photos.get(0).getUrl());
		
		
		String meetupsBaseURL = socialBoxConfig.getMeetupsBaseUrl();
		dataPayload.setActionURL(meetupsBaseURL+meetup.getUuid());
		
		//Set Data payload in Notification Message
		notificationMessage.setDataPayload(dataPayload);
		
		meetupPhotoNotification.setNotificationMessage(notificationMessage);
		
		logInfo(LOG_PREFIX, "Sending Notification Request {}",meetupPhotoNotification);
		NotificationUtils.send(restTemplate, meetupPhotoNotification, socialBoxConfig);
		
		logInfo(LOG_PREFIX, "Notification sent");
	}
	
	@Override
	public void notifyAboutMeetupModification(User actor, Meetup meetup) {
		String LOG_PREFIX = "NotificationServiceImpl-notifyAboutMeetupModification";
		//Prepare Notification
		Notification meetupUpdateNotification = new Notification();
		
		//Type
		meetupUpdateNotification.setType(NotificationType.MEETUP_UPDATE_NOTIFICATION.getType());
		
		//Reciever Ids
		List<Long> attendeeIds = this.meetupDAO.getAttendeeIdsExcept(meetup, actor.getId());
		if(attendeeIds==null || attendeeIds.isEmpty()){
			return;
		}
		meetupUpdateNotification.setRecieverIds(attendeeIds);
		
		//Prepare Notification Message
		NotificationMessage notificationMessage = new NotificationMessage();
		
		//Prepare Notification Payload
		NotificationMessage.NotificationPayload notificationPayload = new NotificationMessage.NotificationPayload();
		notificationPayload.setTitle(String.format(MEETUP_MODIFY_NOT_TITLE,meetup.getTitle()));
		notificationPayload.setBody(String.format(MEETUP_MODIFY_NOT_BODY,actor.getName()));
		
		//Set Notification Payload in Notification Message
		notificationMessage.setNotificationPayload(notificationPayload);
			
		//Prepare Data Payload
		NotificationMessage.DataPayload dataPayload = new NotificationMessage.DataPayload();
		dataPayload.setActor(actor.getName());
		dataPayload.setType(NotificationType.MEETUP_UPDATE_NOTIFICATION.getType());
		dataPayload.setTarget(String.format(MEETUP_MODIFY_NOT_TARGET,meetup.getTitle()));
		dataPayload.setIcon(actor.getProfilePic());
		
		String meetupsBaseURL = socialBoxConfig.getMeetupsBaseUrl();
		dataPayload.setActionURL(meetupsBaseURL+meetup.getUuid());
		
		//Set Data payload in Notification Message
		notificationMessage.setDataPayload(dataPayload);
		
		meetupUpdateNotification.setNotificationMessage(notificationMessage);
		
		logInfo(LOG_PREFIX, "Sending Notification Request {}",meetupUpdateNotification);
		NotificationUtils.send(restTemplate, meetupUpdateNotification, socialBoxConfig);
		
		logInfo(LOG_PREFIX, "Notification sent");
	}
	
	@Override
	public void notifyAboutMeetupCancellation(User actor, Meetup meetup) {
		String LOG_PREFIX = "NotificationServiceImpl-notifyAboutMeetupCancellation";
		
		//Prepare Notification
		Notification meetupCancelNotification = new Notification();
		
		//Type
		meetupCancelNotification.setType(NotificationType.MEETUP_CANCEL_NOTIFICATION.getType());
		
		//Reciever Ids
		List<Long> attendeeIds = this.meetupDAO.getAttendeeIdsExcept(meetup, actor.getId());
		if(attendeeIds==null || attendeeIds.isEmpty()){
			return;
		}
		meetupCancelNotification.setRecieverIds(attendeeIds);
		
		//Prepare Notification Message
		NotificationMessage notificationMessage = new NotificationMessage();
		
		//Prepare Notification Payload
		NotificationMessage.NotificationPayload notificationPayload = new NotificationMessage.NotificationPayload();
		notificationPayload.setTitle(String.format(MEETUP_CANCEL_NOT_TITLE,meetup.getTitle()));
		notificationPayload.setBody(String.format(MEETUP_CANCEL_NOT_BODY,actor.getName()));
		
		//Set Notification Payload in Notification Message
		notificationMessage.setNotificationPayload(notificationPayload);
		
		//Prepare Data Payload
		NotificationMessage.DataPayload dataPayload = new NotificationMessage.DataPayload();
		dataPayload.setActor(actor.getName());
		dataPayload.setType(NotificationType.MEETUP_CANCEL_NOTIFICATION.getType());
		dataPayload.setTarget(String.format(MEETUP_CANCEL_NOT_TARGET,meetup.getTitle()));
		dataPayload.setIcon(actor.getProfilePic());
		
		String meetupsBaseURL = socialBoxConfig.getMeetupsBaseUrl();
		dataPayload.setActionURL(meetupsBaseURL+meetup.getUuid());
		
		//Set Data payload in Notification Message
		notificationMessage.setDataPayload(dataPayload);
		
		meetupCancelNotification.setNotificationMessage(notificationMessage);
		
		logInfo(LOG_PREFIX, "Sending Notification Request {}",meetupCancelNotification);
		NotificationUtils.send(restTemplate, meetupCancelNotification, socialBoxConfig);
		
		logInfo(LOG_PREFIX, "Notification sent");
	}
	
	@Override
	public void notifyAboutMeetupMessage(User actor, Meetup meetup) {
		String LOG_PREFIX = "NotificationServiceImpl-notifyAboutMeetupMessage";
		//Prepare Notification
		Notification meetupMessageNotification = new Notification();
		
		//Type
		meetupMessageNotification.setType(NotificationType.MEETUP_MESSAGE_NOTIFICATION.getType());
		
		//Reciever Ids
		List<Long> attendeeIds = this.meetupDAO.getAttendeeIdsExcept(meetup, actor.getId());
		if(attendeeIds==null || attendeeIds.isEmpty()){
			return;
		}
		meetupMessageNotification.setRecieverIds(attendeeIds);
		
		//Prepare Notification Message
		NotificationMessage notificationMessage = new NotificationMessage();
		
		//Prepare Notification Payload
		NotificationMessage.NotificationPayload notificationPayload = new NotificationMessage.NotificationPayload();
		notificationPayload.setTitle(String.format(MEETUP_MESSAGE_NOT_TITLE,meetup.getTitle()));
		notificationPayload.setBody(String.format(MEETUP_MESSAGE_NOT_BODY,actor.getName()));
		
		//Set Notification Payload in Notification Message
		notificationMessage.setNotificationPayload(notificationPayload);
		
		//Prepare Data Payload
		NotificationMessage.DataPayload dataPayload = new NotificationMessage.DataPayload();
		dataPayload.setActor(actor.getName());
		dataPayload.setType(NotificationType.MEETUP_MESSAGE_NOTIFICATION.getType());
		dataPayload.setTarget(String.format(MEETUP_MESSAGE_NOT_TARGET,meetup.getTitle()));
		dataPayload.setIcon(actor.getProfilePic());
		String meetupsBaseURL = socialBoxConfig.getMeetupsBaseUrl();
		dataPayload.setActionURL(meetupsBaseURL+meetup.getUuid());
		
		//Set Data payload in Notification Message
		notificationMessage.setDataPayload(dataPayload);
		
		meetupMessageNotification.setNotificationMessage(notificationMessage);
		
		logInfo(LOG_PREFIX, "Sending Notification Request {}",meetupMessageNotification);
		NotificationUtils.send(restTemplate, meetupMessageNotification, socialBoxConfig);
		
		logInfo(LOG_PREFIX, "Notification sent");
	}
}
