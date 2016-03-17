package com.bitlogic.sociallbox.service.business;

import com.bitlogic.sociallbox.data.model.ext.Place;
import com.bitlogic.sociallbox.data.model.requests.PlaceDetailsRequest;

public interface PlaceDetailService {
	public Place getPlaceDetails(PlaceDetailsRequest placeDetailsRequest);
}
