package com.bitlogic.sociallbox.service.business;

import java.util.List;

import com.bitlogic.sociallbox.data.model.Category;
import com.bitlogic.sociallbox.data.model.SourceSystemForPlaces;
import com.bitlogic.sociallbox.data.model.ext.Place;
import com.bitlogic.sociallbox.data.model.ext.Places;
import com.bitlogic.sociallbox.data.model.requests.NearbySearchRequest;
import com.bitlogic.sociallbox.data.model.requests.PlaceDetailsRequest;
import com.bitlogic.sociallbox.data.model.response.UserFriend;

/**
 * Service for searching nearby places based on category
 * @author ajoinwal
 *
 */
public interface PlacesService {

	/**
	 * Places are searched based on following parameters:
	 * {@link SourceSystemForPlaces} present in {@link Category}
	 * Depending upon the {@link SourceSystemForPlaces} in request category,
	 * appropriate source system will be contacted for places search.
	 * @param nearbySearchRequest Search Request passed from controller
	 * @return List of Places. 
	 * @see Category
	 * @see SourceSystemForPlaces
	 */
	public Places searchNearby(NearbySearchRequest nearbySearchRequest);
	
	public Place getPlaceDetails(PlaceDetailsRequest placeDetailsRequest);
	
	public List<UserFriend> getFriendsWhoLikePlace(String deviceId,String placeId);
	
	public void saveUserPlaceLike(String deviceId, String placeId);
}
