package com.bitlogic.sociallbox.service.dao;

import java.util.List;

import com.bitlogic.sociallbox.data.model.UserAndPlaceMapping;

public interface PlaceDAO {

	public void saveUserLikeForPlace(UserAndPlaceMapping mapping);
	
	public List<Long> getUsersWhoLikePlace(String placeId);
}
