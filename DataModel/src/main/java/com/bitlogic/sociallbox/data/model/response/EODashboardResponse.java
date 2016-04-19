package com.bitlogic.sociallbox.data.model.response;

import java.util.ArrayList;
import java.util.List;

import com.bitlogic.sociallbox.data.model.UserMessage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="dashboard")
public class EODashboardResponse {

	private Integer events = 0;
	private Integer meetups = 0;
	private Integer interestedUsers = 0;
	private Integer registeredUsers = 0;
	
	List<UserMessage> messages = new ArrayList<UserMessage>();
	@JsonProperty("attendees_by_month")
	List<AttendeesInMonth> attendeesInMonths = new ArrayList<>();
	
	@JsonRootName("attendees-by-month")
	public static class AttendeesInMonth{
		@JsonProperty
		private String month;
		@JsonProperty
		private Integer attendees;
		@JsonIgnore
		private List<String> eventIds = new ArrayList<>();
		
		public List<String> getEventIds() {
			return eventIds;
		}
		public void setEventIds(List<String> eventIds) {
			this.eventIds = eventIds;
		}
		public String getMonth() {
			return month;
		}
		public void setMonth(String month) {
			this.month = month;
		}
		public Integer getAttendees() {
			return attendees;
		}
		public void setAttendees(Integer attendees) {
			this.attendees = attendees;
		}
		
	}

	public List<AttendeesInMonth> getAttendeesInMonths() {
		return attendeesInMonths;
	}

	public void setAttendeesInMonths(List<AttendeesInMonth> attendeesInMonths) {
		this.attendeesInMonths = attendeesInMonths;
	}

	public Integer getEvents() {
		return events;
	}

	public void setEvents(Integer events) {
		this.events = events;
	}

	public Integer getMeetups() {
		return meetups;
	}

	public void setMeetups(Integer meetups) {
		this.meetups = meetups;
	}

	public Integer getInterestedUsers() {
		return interestedUsers;
	}

	public void setInterestedUsers(Integer interestedUsers) {
		this.interestedUsers = interestedUsers;
	}

	public Integer getRegisteredUsers() {
		return registeredUsers;
	}

	public void setRegisteredUsers(Integer registeredUsers) {
		this.registeredUsers = registeredUsers;
	}

	public List<UserMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<UserMessage> messages) {
		this.messages = messages;
	}
	
	
}
