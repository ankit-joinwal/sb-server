package com.bitlogic.sociallbox.service.business;

import com.bitlogic.sociallbox.data.model.ext.Places;
import com.bitlogic.sociallbox.data.model.requests.NearbySearchRequest;

public interface NearbySearchService {

	public Places search(NearbySearchRequest nearbySearchRequest) ;
}
