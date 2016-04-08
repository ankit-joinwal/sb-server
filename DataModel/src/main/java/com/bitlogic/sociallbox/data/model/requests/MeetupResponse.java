package com.bitlogic.sociallbox.data.model.requests;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.bitlogic.sociallbox.data.model.Location;
import com.bitlogic.sociallbox.data.model.MeetupImage;
import com.bitlogic.sociallbox.data.model.MeetupStatus;
import com.bitlogic.sociallbox.data.model.response.UserPublicProfile;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.org.apache.xpath.internal.operations.Bool;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class MeetupResponse  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XmlElement
	@JsonProperty("id")
	private String uuid;

	@XmlElement
	@JsonProperty("title")
	private String title;

	@XmlElement
	private String description;

	@XmlElement
	@JsonProperty("location")
	private Location location;

	@XmlElement
	@JsonProperty("start_date")
	private String startDate;

	@XmlElement
	@JsonProperty("end_date")
	private String endDate;

	@XmlElement
	@JsonProperty("organizer")
	private UserPublicProfile organizer;
	
	@JsonProperty("status")
	private MeetupStatus status;
	
	@XmlElement
	@JsonProperty("meetup_access_url")
	private String url;
	
	@JsonProperty("display_pic")
	private MeetupImage displayImage;
	
	@XmlElement
	@JsonProperty("event_at_meetup")
	private String eventAtMeetup;
	
	@JsonProperty("user_actions")
	private List<UserAction> userActions = new ArrayList<UserAction>();
	
	public static final class UserAction{
		
		public UserAction(){
			
		}
		
		public UserAction(UserActionType actionType){
			this.actionType = actionType;
			this.value = actionType.getDefaultValue();
		}
		
		@JsonProperty("action_type")
		private UserActionType actionType;
		
		private Boolean value;

		public UserActionType getActionType() {
			return actionType;
		}

		public void setActionType(UserActionType actionType) {
			this.actionType = actionType;
		}

		public Boolean getValue() {
			return value;
		}

		public void setValue(Boolean value) {
			this.value = value;
		}
		
	}
	
	public enum UserActionType{
		CAN_VIEW("can_view",Boolean.TRUE),
		CAN_EDIT("can_edit",Boolean.FALSE),
		CAN_INVITE("can_invite",Boolean.FALSE),
		CAN_UPLOAD_IMAGE("can_upload_image",Boolean.FALSE),
		CAN_MESSAGE("can_message",Boolean.FALSE),
		CAN_CANCEL("can_cancel",Boolean.FALSE);
		
		private String actionName;
		private Boolean defaultValue;
		
		private UserActionType(String actionName,Boolean defaultValue){
			this.actionName = actionName;
			this.defaultValue = defaultValue;
		}
		
		public String getActionName(){
			return this.actionName;
		}
		
		public Boolean getDefaultValue(){
			return this.defaultValue;
		}
		
		public static List<UserAction> getOrganizerActions(){
			UserActionType[] actionTypes = values();
			
			List<UserActionType> userActionTypes = Arrays.asList(actionTypes);
			List<UserAction> organizerActions = new ArrayList<UserAction>(userActionTypes.size());
			for(UserActionType actionType : userActionTypes){
				UserAction userAction = new UserAction(actionType);
				userAction.setValue(Boolean.TRUE);
				organizerActions.add(userAction);
			}
			return organizerActions;
		}
		
		public static List<UserAction> getReadOnlyActions(){
			UserActionType[] actionTypes = values();
			
			List<UserActionType> userActionTypes = Arrays.asList(actionTypes);
			List<UserAction> readOnlyActions = new ArrayList<UserAction>(userActionTypes.size());
			for(UserActionType actionType : userActionTypes){
				UserAction userAction = new UserAction(actionType);
				readOnlyActions.add(userAction);
			}
			
			return readOnlyActions;
		}
		
		public static List<UserAction> getAdminActions(){
			UserActionType[] actionTypes = values();
			List<UserActionType> userActionTypes = Arrays.asList(actionTypes);
			List<UserAction> adminActions = new ArrayList<UserAction>(userActionTypes.size());
			for(UserActionType actionType : userActionTypes){
				UserAction userAction = new UserAction(actionType);
				if(actionType== CAN_UPLOAD_IMAGE || actionType== CAN_MESSAGE){
					userAction.setValue(Boolean.TRUE);
				}
				adminActions.add(userAction);
			}
			
			return adminActions;
		}
		
		public static List<UserAction> getNonAdminActions(){
			UserActionType[] actionTypes = values();
			List<UserActionType> userActionTypes = Arrays.asList(actionTypes);
			List<UserAction> nonAdminActions = new ArrayList<UserAction>(userActionTypes.size());
			for(UserActionType actionType : userActionTypes){
				UserAction userAction = new UserAction(actionType);
				if(actionType== CAN_UPLOAD_IMAGE || actionType== CAN_MESSAGE){
					userAction.setValue(Boolean.TRUE);
				}
				nonAdminActions.add(userAction);
			}
			
			return nonAdminActions;
		}
	}
	
	public List<UserAction> getUserActions() {
		return userActions;
	}

	public void setUserActions(List<UserAction> userActions) {
		this.userActions = userActions;
	}

	public MeetupStatus getStatus() {
		return status;
	}

	public void setStatus(MeetupStatus status) {
		this.status = status;
	}

	public MeetupImage getDisplayImage() {
		return displayImage;
	}

	public void setDisplayImage(MeetupImage displayImage) {
		this.displayImage = displayImage;
	}

	public String getEventAtMeetup() {
		return eventAtMeetup;
	}

	public void setEventAtMeetup(String eventAtMeetup) {
		this.eventAtMeetup = eventAtMeetup;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
	
	

	public UserPublicProfile getOrganizer() {
		return organizer;
	}

	public void setOrganizer(UserPublicProfile organizer) {
		this.organizer = organizer;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}



}
