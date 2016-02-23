package com.bitlogic.sociallbox.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.ext.PlaceDetails;
import com.bitlogic.sociallbox.data.model.ext.Places;
import com.bitlogic.sociallbox.data.model.requests.NearbySearchRequest;
import com.bitlogic.sociallbox.data.model.requests.PlaceDetailsRequest;
import com.bitlogic.sociallbox.data.model.requests.TextSearchRequest;
import com.bitlogic.sociallbox.service.business.NearbySearchService;
import com.bitlogic.sociallbox.service.business.PlaceDetailService;
import com.bitlogic.sociallbox.service.business.TextSearchService;

@RestController
@RequestMapping("/api/public/places")
public class GeoController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(GeoController.class);

	@Autowired
	private NearbySearchService nearbySearchService;

	@Autowired
	private TextSearchService textSearchService;

	@Autowired
	private PlaceDetailService placeDetailService;

	public void setPlaceDetailService(PlaceDetailService placeDetailService) {
		this.placeDetailService = placeDetailService;
	}

	public void setTextSearchService(TextSearchService textSearchService) {
		this.textSearchService = textSearchService;
	}

	public void setNearbySearchService(NearbySearchService nearbySearchService) {
		this.nearbySearchService = nearbySearchService;
	}

	@RequestMapping(value = "/nearby", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public Places getNearbyPlaces(
			@RequestParam(required = true, value = "location") String location,
			@RequestParam(required = false, value = "radius", defaultValue = Constants.DEFAULT_RADIUS) String radius,
			@RequestParam(required = true, value = "cid") Long categoryId,
			@RequestParam(required = false, value = "name") String name,
			@RequestParam(required = false, value = "rankBy") String rankBy,
			@RequestHeader(required = false, value = Constants.AUTHORIZATION_HEADER) String authorization)
			{
		logger.info(
				"### Nearby search Request recieved .Authorization : {} ###",
				authorization);

		/*
		 * Object response = validateRequest(authorization);
		 * 
		 * if (response instanceof ServiceException) {
		 * logger.error("### Invalid Authorization header", (ServiceException)
		 * response); throw (ServiceException) response; }
		 */
		Places places = null;

	
		NearbySearchRequest nearbySearchRequest = new NearbySearchRequest();
		nearbySearchRequest.setLocation(location);
		nearbySearchRequest.setName(name);
		nearbySearchRequest.setRadius(radius);
		nearbySearchRequest.setRankBy(rankBy);
		nearbySearchRequest.setCategoryId(categoryId);

		places = nearbySearchService.search(nearbySearchRequest);
		
		return places;
	}

	@RequestMapping(value = "/tsearch", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public Places getPlacesByTextSearch(
			@RequestParam(required = true, value = "query") String query,
			@RequestParam(required = false, value = "location") String location,
			@RequestParam(required = false, value = "radius") String radius,
			@RequestParam(required = false, value = "types") String types,
			@RequestParam(required = false, value = "name") String name,
			@RequestParam(required = false, value = "rankBy") String rankBy,
			@RequestHeader(required = false, value = Constants.AUTHORIZATION_HEADER) String authorization)
			{
		logger.info("### Text search Request recieved ###");
		/*
		 * Object response = validateRequest(authorization);
		 * 
		 * if (response instanceof ServiceException) {
		 * logger.error("### Invalid Authorization header", (ServiceException)
		 * response); throw (ServiceException) response; }
		 */
		Places places = null;

		
			TextSearchRequest textSearchRequest = new TextSearchRequest();
			textSearchRequest.setLocation(location);
			textSearchRequest.setName(name);
			textSearchRequest.setRadius(radius);
			textSearchRequest.setRankBy(rankBy);
			textSearchRequest.setTypes(types);
			textSearchRequest.setQuery(query);

			places = textSearchService.search(textSearchRequest);
		
		return places;
	}

	@RequestMapping(value = "/place/{placeId}/detail", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public PlaceDetails getPlaceDetails(
			@PathVariable(value = "placeId") String placeId,
			@RequestHeader(required = false, value = Constants.AUTHORIZATION_HEADER) String authorization)
		 {
		logger.info("### Place detail request recieved ###");
		/*
		 * Object response = validateRequest(authorization);
		 * 
		 * if (response instanceof ServiceException) {
		 * logger.error("### Invalid Authorization header", (ServiceException)
		 * response); throw (ServiceException) response; }
		 */
		PlaceDetails placeDetails = null;
			PlaceDetailsRequest placeDetailsRequest = new PlaceDetailsRequest();
			placeDetailsRequest.setPlaceId(placeId);
			placeDetails = placeDetailService
					.getPlaceDetails(placeDetailsRequest);
		

		return placeDetails;
	}
}
