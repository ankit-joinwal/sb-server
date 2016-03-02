package com.bitlogic.sociallbox.service.business.impl;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.GAPIConfig;
import com.bitlogic.sociallbox.data.model.ext.PlaceDetails;
import com.bitlogic.sociallbox.data.model.requests.PlaceDetailsRequest;
import com.bitlogic.sociallbox.service.business.PlaceDetailService;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.exception.ServiceException;
import com.bitlogic.sociallbox.service.helper.PlaceDetailsHelper;

@Service
@Transactional
public class PlaceDetailsServiceImpl implements PlaceDetailService,Constants{
	private static final Logger logger = LoggerFactory.getLogger(PlaceDetailsServiceImpl.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private GAPIConfig gapiConfig;

	@Override
	public PlaceDetails getPlaceDetails(PlaceDetailsRequest placeDetailsRequest)
			 {
		PlaceDetails placeDetails = null;
		try{
			placeDetails = PlaceDetailsHelper.executeSearch(restTemplate, placeDetailsRequest, gapiConfig);
			
		}catch(ClientException exception){
			logger.error("Error occurred while retrieving place details ",exception);
			throw exception;
		}catch(ServiceException exception){
			logger.error("Error occurred while retrieving place details ",exception);
			throw exception;
		}catch(Exception exception){
			logger.error("Error occurred while retrieving place details",exception);
			throw new ServiceException(GEO_SERVICE_NAME,RestErrorCodes.ERR_050, exception.getMessage());
		}
		return placeDetails;
	}
	
	
}
