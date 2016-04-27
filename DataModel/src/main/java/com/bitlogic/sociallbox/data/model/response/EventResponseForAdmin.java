package com.bitlogic.sociallbox.data.model.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("event_response")
public class EventResponseForAdmin {
	@JsonProperty("event")
	private EventResponse eventResponse;
	
	@JsonProperty("total_meetups")
	private Integer totalMeetups = 0;
	@JsonProperty("total_int_users")
	private Integer totalInterestedUsers = 0;
	@JsonProperty("total_reg_users")
	private Integer totalRegisteredUsers = 0;
	
	@JsonProperty("daily_user_stats")
	private List<DailyEventUserStatistics> dailyUserStatistics = new ArrayList<>();
	
	@JsonProperty("daily_meetup_stats")
	private List<DailyEventMeetupStatistics> dailyMeetupStatistics = new ArrayList<>();
	
	
	public EventResponse getEventResponse() {
		return eventResponse;
	}

	public void setEventResponse(EventResponse eventResponse) {
		this.eventResponse = eventResponse;
	}

	public Integer getTotalMeetups() {
		return totalMeetups;
	}

	public void setTotalMeetups(Integer totalMeetups) {
		this.totalMeetups = totalMeetups;
	}

	public Integer getTotalInterestedUsers() {
		return totalInterestedUsers;
	}

	public void setTotalInterestedUsers(Integer totalInterestedUsers) {
		this.totalInterestedUsers = totalInterestedUsers;
	}

	public Integer getTotalRegisteredUsers() {
		return totalRegisteredUsers;
	}

	public void setTotalRegisteredUsers(Integer totalRegisteredUsers) {
		this.totalRegisteredUsers = totalRegisteredUsers;
	}

	public List<DailyEventUserStatistics> getDailyUserStatistics() {
		return dailyUserStatistics;
	}

	public void setDailyUserStatistics(
			List<DailyEventUserStatistics> dailyUserStatistics) {
		this.dailyUserStatistics = dailyUserStatistics;
	}

	public List<DailyEventMeetupStatistics> getDailyMeetupStatistics() {
		return dailyMeetupStatistics;
	}

	public void setDailyMeetupStatistics(
			List<DailyEventMeetupStatistics> dailyMeetupStatistics) {
		this.dailyMeetupStatistics = dailyMeetupStatistics;
	}

	public static class DailyEventUserStatistics{
		private String date;
		@JsonProperty("int_users")
		private Integer interestedUsers;
		@JsonProperty("reg_users")
		private Integer registeredUsers;
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
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
		
	}
	
	public static class DailyEventMeetupStatistics{
		private String date;
		@JsonProperty("meetup_count")
		private Integer meetupCount;
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public Integer getMeetupCount() {
			return meetupCount;
		}
		public void setMeetupCount(Integer meetupCount) {
			this.meetupCount = meetupCount;
		}
		
	}
}
