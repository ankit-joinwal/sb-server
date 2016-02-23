package com.bitlogic.sociallbox.service.business;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.GAPIConfig;
import com.bitlogic.sociallbox.data.model.ext.Places;
import com.bitlogic.sociallbox.data.model.requests.TextSearchRequest;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.exception.ServiceException;
import com.bitlogic.sociallbox.service.helper.TextSearchHelper;

/**
 * Service for Performing "Text" search.
 * <a href="https://developers.google.com/places/web-service/search#PlaceSearchRequests">
 * @author ajoinwal
 *
 */
@Service
@Transactional
public class TextSearchServiceImpl implements TextSearchService,Constants{

	private static final Logger logger = LoggerFactory.getLogger(TextSearchServiceImpl.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private GAPIConfig gapiConfig;
	
	
	
	public GAPIConfig getGapiConfig() {
		return gapiConfig;
	}


	public void setGapiConfig(GAPIConfig gapiConfig) {
		this.gapiConfig = gapiConfig;
	}


	public RestTemplate getRestTemplate() {
		return restTemplate;
	}


	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}


	

	@Override
	public Places search(TextSearchRequest textSearchRequest){
		Places places= null;
		try{
				places = TextSearchHelper.executeSearch(restTemplate, textSearchRequest, gapiConfig);
				if(places!=null && places.getResults()!=null){
					places.setTotalRecords(places.getResults().size());
				}else{
					logger.info("No Results found for text search {} ",textSearchRequest);
				}
		}catch(ClientException exception){
			logger.error("Error occurred while performing text search",exception);
			throw exception;
		}
		catch(ServiceException exception){
			logger.error("Error occurred while performing text search",exception);
			throw exception;
		}catch(Exception exception){
			logger.error("Error occurred while performing text search",exception);
			throw new ServiceException(GEO_SERVICE_NAME,RestErrorCodes.ERR_050, exception.getMessage());
		}
		return places;
	}
}
