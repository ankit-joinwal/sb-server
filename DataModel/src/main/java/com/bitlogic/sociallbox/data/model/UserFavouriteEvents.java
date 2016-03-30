package com.bitlogic.sociallbox.data.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "USER_FAVOURITE_EVENTS",indexes = { @Index(name = "IDX_USR_EVT_FAV", columnList = "USER_ID") })
public class UserFavouriteEvents {


	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@Column(name = "USER_ID", nullable = false)
	private Long userId;

	@Column(name = "EVENT_ID", nullable = false)
	private String eventId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final UserFavouriteEvents mapping = (UserFavouriteEvents) obj;
		return (Objects.equals(this.userId, mapping.userId)&& Objects.equals(this.eventId, mapping.eventId));
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(userId,eventId);
	}

}
