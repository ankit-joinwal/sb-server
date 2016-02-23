package com.bitlogic.sociallbox.service.business;

import com.bitlogic.sociallbox.data.model.ext.PlaceDetails;
import com.bitlogic.sociallbox.data.model.requests.PlaceDetailsRequest;

public interface PlaceDetailService {
	public PlaceDetails getPlaceDetails(PlaceDetailsRequest placeDetailsRequest);
}
