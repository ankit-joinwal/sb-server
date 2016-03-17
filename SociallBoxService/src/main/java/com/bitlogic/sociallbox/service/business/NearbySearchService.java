package com.bitlogic.sociallbox.service.business;

import com.bitlogic.sociallbox.data.model.ext.Places;
import com.bitlogic.sociallbox.data.model.requests.NearbySearchRequest;
import com.bitlogic.sociallbox.service.business.impl.NearbySearchServiceGoogle;
import com.bitlogic.sociallbox.service.business.impl.NearbySearchServiceZomato;

/**
 * Non Database Transactional Service for performing search of nearby places.
 * For different data sources of {@link Places},there will be different implementations of this. 
 * @author ajoinwal
 *
 * @see NearbySearchServiceZomato
 * @see NearbySearchServiceGoogle
 */
public interface NearbySearchService {

	public Places search(NearbySearchRequest nearbySearchRequest) ;
}
