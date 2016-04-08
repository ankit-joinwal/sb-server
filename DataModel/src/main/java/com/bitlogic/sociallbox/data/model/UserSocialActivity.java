package com.bitlogic.sociallbox.data.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserSocialActivity<V> implements Comparable<UserSocialActivity<?>> {
	
	@JsonProperty("type")
	private UserSocialActivity.ActivityType activityType;
	
	@JsonProperty("time")
	private String activityTime;
	
	@JsonIgnore
	private Date activityDate;
	
	@JsonProperty
	private V detail;
	
	public V getDetail() {
		return detail;
	}

	public void setDetail(V detail) {
		this.detail = detail;
	}

	
	public Date getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}


	public enum ActivityTime{
		UPCOMING("upcoming"),
		PAST("past");
		
		private String time;
		
		private ActivityTime(String time){
			this.time = time;
		}
		
		public String getTime(){
			return this.time;
		}
		
		public static ActivityTime getActivityTime(String time){
			ActivityTime[] activityTimes = values();
			for(ActivityTime activityTime : activityTimes){
				if(activityTime.getTime().equalsIgnoreCase(time)){
					return activityTime;
				}
			}
			return null;
		}
	}
	public enum ActivityType{
		MEETUP,
		EVENT
	}

	public UserSocialActivity.ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(UserSocialActivity.ActivityType activityType) {
		this.activityType = activityType;
	}

	public String getActivityTime() {
		return activityTime;
	}

	public void setActivityTime(String activityTime) {
		this.activityTime = activityTime;
	}
	
	@Override
	public int compareTo(UserSocialActivity<?> o) {
		
		Date activityDate = ((UserSocialActivity<?> )o).activityDate;
		return activityDate.compareTo(this.activityDate);
	}
}
