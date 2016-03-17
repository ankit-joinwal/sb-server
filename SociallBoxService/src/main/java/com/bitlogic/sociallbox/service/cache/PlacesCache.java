package com.bitlogic.sociallbox.service.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.bitlogic.sociallbox.data.model.ext.google.GooglePlaces;
import com.bitlogic.sociallbox.data.model.requests.NearbySearchRequestGoogle;

public class PlacesCache {

	private static volatile PlacesCache instance = null;

	private Map<String, GooglePlaces> placesMap = null;
	
	

	private PlacesCache() {

	}

	public static String createKeyForCache(
			NearbySearchRequestGoogle nearbySearchRequest) {
		return nearbySearchRequest.toString();
	}

	public static PlacesCache getCache() {
		if (instance == null) {
			synchronized (PlacesCache.class) {
				if (instance == null) {
					instance = new PlacesCache();
					instance.placesMap = new ConcurrentHashMap<>();
				}
			}
		}

		return instance;
	}

	public static GooglePlaces getPlacesFromCache(String key) {

		synchronized (PlacesCache.class) {
			return getCache().placesMap.get(key);
		}
	}

	public static GooglePlaces getPlacesFromCache(String key,
			Integer numOfRecPerPage, Integer pageNumber) {

		synchronized (PlacesCache.class) {
			GooglePlaces places = getPlacesFromCache(key);

			if (places == null || places.getResults().isEmpty()
					|| ((places.getResults().size() / numOfRecPerPage) < (pageNumber))) {
				throw new IllegalArgumentException(
						"Invalid Parameters to get Data from Cache [ key="
								+ key + " , numOfRecords=" + numOfRecPerPage
								+ " ,pageNumber=" + pageNumber + " ,size="+places.getResults().size()+ " ]");
			}
			
			int size = places.getResults().size();
			int totalPages = size/numOfRecPerPage;
			int startIdx = (pageNumber-1)*numOfRecPerPage;
			int endIdx =0;
			if(pageNumber==totalPages){

				endIdx = size;
			}else{
				 endIdx = pageNumber*numOfRecPerPage;
			}
			//TODO: Remove this transformation from here
			GooglePlaces places2 = new GooglePlaces();
			places2.setResults(places.getResults().subList(startIdx, endIdx));
			places2.setStatus("OK");
			
			return places2;
		}

	}

	public static void putInCache(String key, GooglePlaces places) {
		synchronized (PlacesCache.class) {
			getCache().placesMap.put(key, places);
		}
	}
	
	public static boolean checkIfPresentInCache(String key){
		synchronized (PlacesCache.class) {
			return getCache().placesMap.containsKey(key);
		}
	}
}
