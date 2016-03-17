package com.bitlogic.sociallbox.service.business.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.ZomatoAPIConfig;
import com.bitlogic.sociallbox.data.model.ext.Places;
import com.bitlogic.sociallbox.data.model.requests.NearbySearchRequest;
import com.bitlogic.sociallbox.data.model.requests.NearbySearchRequestZomato;
import com.bitlogic.sociallbox.service.business.NearbySearchService;
import com.bitlogic.sociallbox.service.helper.NearbySearchHelper;
import com.bitlogic.sociallbox.service.utils.LoggingService;

@Component("nearbySearchServiceZomato")
public class NearbySearchServiceZomato extends LoggingService implements NearbySearchService,Constants {

	private static final Logger LOGGER = LoggerFactory.getLogger(NearbySearchServiceZomato.class);
	
	@Autowired
	private ZomatoAPIConfig zomatoAPIConfig;
	
	@Autowired
	private RestTemplate restTemplate;

	
	@Override
	public Logger getLogger() {
		return LOGGER;
	}
	
	@Override
	public Places search(NearbySearchRequest nearbySearchRequest) {
		String LOG_PREFIX = "search";
		logInfo(LOG_PREFIX, "NearbySearchRequest ={}", nearbySearchRequest);
		NearbySearchRequestZomato requestZomato = constructZomatoRequest(nearbySearchRequest);
		Places places = NearbySearchHelper.execute(restTemplate, requestZomato, zomatoAPIConfig);
		
		return places;
	}

	private NearbySearchRequestZomato constructZomatoRequest(NearbySearchRequest nearbySearchRequest){
		String LOG_PREFIX = "constructZomatoRequest";
		logInfo(LOG_PREFIX, "Constructing Zomato Request");
		NearbySearchRequestZomato zomatoRequest = new NearbySearchRequestZomato();
		zomatoRequest.setCategory(nearbySearchRequest.getExternalId());
		zomatoRequest.setRadius(nearbySearchRequest.getRadius());
		zomatoRequest.setLatitude(nearbySearchRequest.getLatitude());
		zomatoRequest.setLongitude(nearbySearchRequest.getLongitude());
		
		if(nearbySearchRequest.getPageNumber()==null || 
				nearbySearchRequest.getPageNumber().isEmpty()){
			zomatoRequest.setStart(ZERO);
			zomatoRequest.setCount(RECORDS_PER_PAGE+BLANK);
		}else{
			Integer page = Integer.parseInt(nearbySearchRequest.getPageNumber());
			int startIdx = (page-1)*Constants.RECORDS_PER_PAGE;
		 	zomatoRequest.setStart(startIdx+BLANK);
		 	zomatoRequest.setCount(RECORDS_PER_PAGE-1+BLANK);
		}

		String sortBy = nearbySearchRequest.getSort();
		String sortOrder = nearbySearchRequest.getOrder();
		zomatoRequest.setSort(NearbySearchRequestZomato.getSortByUsingCode(sortBy).getValue());
		zomatoRequest.setOrder(NearbySearchRequestZomato.getSortOrderUsingCode(sortOrder).getValue());
		logInfo(LOG_PREFIX, "NearbySearchRequestZomato = {}", zomatoRequest);
		return zomatoRequest;
	}
}
