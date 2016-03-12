package com.bitlogic.sociallbox.data.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "USER_PLACE_MAPPING")
public class UserAndPlaceMapping {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@Column(name = "USER_ID", nullable = false)
	private Long userId;

	@Column(name = "PLACE_ID", nullable = false)
	private String placeId;

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

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final UserAndPlaceMapping mapping = (UserAndPlaceMapping) obj;
		return (Objects.equals(this.userId, mapping.userId)&& Objects.equals(this.placeId, mapping.placeId));
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(userId,placeId);
	}
}
