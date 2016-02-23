package com.bitlogic.sociallbox.service.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.GAPIConfig;
import com.bitlogic.sociallbox.data.model.ext.PlaceDetails;
import com.bitlogic.sociallbox.data.model.requests.NearbySearchRequest;
import com.bitlogic.sociallbox.data.model.requests.PlaceDetailsRequest;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.exception.ServiceException;

public class PlaceDetailsHelper implements Constants{

	private static final Logger logger = LoggerFactory.getLogger(PlaceDetailsHelper.class);
	
	public static PlaceDetails executeSearch(RestTemplate restTemplate,PlaceDetailsRequest placeDetailsRequest,GAPIConfig gapiConfig) throws ClientException,ServiceException{
		StringBuilder url = new StringBuilder(gapiConfig.getPlaceDetailsURL());
		url.append(gapiConfig.getDataExchangeFormat() + Constants.QUESTIONMARK);
		url.append(PlaceDetailsRequest.PlaceDetailsRequestParams.PLACEID.getName()
				+ Constants.EQUAL
				+ placeDetailsRequest.getPlaceId());
		url.append(Constants.AMP
				+ NearbySearchRequest.NearbySearchRequestParamNames.KEY
						.getName() + Constants.EQUAL + gapiConfig.getGapiKey());
		logger.info("### Inside PlaceDetailsHelper.executeSearch | URL : {} "
				+ url.toString());

		logger.info("### Executing Search ###");
		ResponseEntity<PlaceDetails> placesResponse = restTemplate.exchange(
				url.toString(), HttpMethod.GET, null,
				new ParameterizedTypeReference<PlaceDetails>() {
				});
		HttpStatus returnStatus = placesResponse.getStatusCode();
		boolean isSuccess = returnStatus.is2xxSuccessful();
		if(isSuccess){
			logger.info("### Search successful for url : {} "+url.toString());
			
		}else{
			if(returnStatus.is4xxClientError()){
				throw new ClientException(RestErrorCodes.ERR_010,ERROR_GAPI_CLIENT_REQUEST);
			}else if (returnStatus.is5xxServerError()){
				throw new ServiceException("GAPI",RestErrorCodes.ERR_010,Constants.ERROR_GAPI_WEBSERVICE_ERROR);
			}
		}
		
		
		return placesResponse.getBody();
	}
}
