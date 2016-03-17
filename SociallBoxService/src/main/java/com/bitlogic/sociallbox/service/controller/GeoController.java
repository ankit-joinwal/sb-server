package com.bitlogic.sociallbox.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.ext.Places;
import com.bitlogic.sociallbox.data.model.ext.google.GooglePlaces;
import com.bitlogic.sociallbox.data.model.requests.NearbySearchRequestGoogle;
import com.bitlogic.sociallbox.data.model.requests.TextSearchRequest;
import com.bitlogic.sociallbox.service.business.TextSearchService;
import com.bitlogic.sociallbox.service.business.impl.NearbySearchServiceGoogle;

@RestController
@RequestMapping("/api/public/googleplaces")
public class GeoController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(GeoController.class);

	private static final String NEARBY_PLACES_REQUEST = "NearbyPlacesRequest API";
	private static final String PLACE_DETAILS_REQUEST = "PlaceDetailsRequest API";
	@Autowired
	private NearbySearchServiceGoogle nearbySearchServiceGoogle;

	@Autowired
	private TextSearchService textSearchService;

/*	@Autowired
	private PlaceDetailService placeDetailService;*/

	@Override
	public Logger getLogger() {
		return logger;
	}
	
	/**
	 *  @api {get} /api/public/places/nearby?cid=:cid&location=:lattitude,:longitude&radius=:radius&pagetoken=:pagetoken Get Nearby Places
	 *  @apiName Get Nearby Places
	 *  @apiGroup Places
	 *  @apiHeader {String} accept application/json
	 *  @apiParam {Number} cid Mandatory Category Id to search for
	 *  @apiParam {String} lattitude Mandatory Lattitude of User
	 *  @apiParam {String} longitude Mandatory Longitude of User
	 *  @apiParam {String} radius Optional Radius of search in metres (Default to 1000)
	 *  @apiParam {String} pagetoken Optional Next Page token (recieved from previous search)
	 *  @apiSuccess (Success - OK 200) {Object}  response  Response.
	 *  @apiSuccess (Success - OK 200) {String}  response.status   OK.
	 *  @apiSuccess (Success - OK 200) {String}  response.totalRecords   Eg. 20
	 *  @apiSuccess (Success - OK 200) {String}  response.next_page_token  
	 * 	@apiSuccess (Success - OK 200) {Object}  response.results Places Results
	 *  @apiSuccessExample {json} Success-Response:
	 *  {
		  "results": [
		    {
		      "geometry": {
		        "location": {
		          "lat": 28.5834989,
		          "lng": 77.22301709999999
		        }
		      },
		      "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png",
		      "id": "49223a21f97edf98b5b5f57ae1af6816b159988c",
		      "name": "Ploof",
		      "reference": "CmRZAAAA3Go0KtbkR3Xk4YmickmWK8ANFjl-GXvMIdtEPzjcT2DyxFJCcn3tAhnBn1CSfJ-HI4EvqHvivvTqAd37tac_1AeW1YXwaByORyz3gIV7AHj3i3VEktHSqkpLJsO17ZLFEhDAs6EYMT2Sl1egndMIvF06GhQmJYB37MjHgJvvm3WsO4HK4JJjjg",
		      "scope": "GOOGLE",
		      "types": [
		        "restaurant",
		        "food",
		        "point_of_interest",
		        "establishment"
		      ],
		      "vicinity": "13, Lodhi Colony Market, Next to Khubsoorat Salon & Lodhi Sports, New Delhi",
		      "photos": [
		        {
		          "height": 814,
		          "width": 545,
		          "photo_reference": "CmRdAAAAXimKE7JxEimsduGzEWsINVv6bNJz5DSjtp74s11-SCIMQGB6g_uo-JOJlE__zvFc6n8kJZvZk9B77m7sTUt1TJ29CTIIA20nwDcPLx5NQcQJY2cfmXLH_It7fYH1PejqEhCCX5EyqUgILERn3k-9VJNgGhQ8JW2uVMdzpW3ISH8trG6G1DXTwg"
		        }
		      ],
		      "place_id": "ChIJq11ZZh3jDDkR6wx3ISmietM"
		    },
		    {
		      "geometry": {
		        "location": {
		          "lat": 28.5557669,
		          "lng": 77.1953917
		        }
		      },
		      "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png",
		      "id": "be03b7631af32b2dfdcaa35d707a06071f0e91c5",
		      "name": "The Project At Park Balluchi",
		      "reference": "CnRvAAAA_xVwN1AsATfTKb6tnbMh41zoPIgTFhuaCD_DbK2fXxRNnbtuZzmcQ9TD_tiA2eNrTn3rU2FcaCH8ExSnEdRe38beCVChZ__RvGyTwVcGt0fycfQRzEi0SSYD3bSWQAFhbNOAyK94sH5xgxBgwJJqYxIQ-9KQg9hkOBiK5DnSbpdyRxoU2whAIkbZG_9DRFLC050oDy81FdU",
		      "scope": "GOOGLE",
		      "types": [
		        "restaurant",
		        "food",
		        "point_of_interest",
		        "establishment"
		      ],
		      "vicinity": "Inside Deer Park, Hauz Khas Village, New Delhi",
		      "photos": [
		        {
		          "height": 3120,
		          "width": 4208,
		          "photo_reference": "CmRdAAAA8WWja36GVWEK0SZ4Z-k4xofflUyBLJ_YwXpMXHjw18MpUrSwmBuO_W_UrepVnlrHF392vdqfXV4ZfWwIbw4rEqO9wyyYuc8OQu2-jTegvb2rannpZv_xWtCHCnPtRYCpEhD6PI-GF3l6DYvngwHytGOVGhS6Cy876w1dB_3rm3BjtAES0C0yCw"
		        }
		      ],
		      "place_id": "ChIJVVVVnoodDTkR_KNlS1XDIA4"
		    }
		  ],
  		  "status": "OK",
  		  "totalRecords": 20,
  		  "next_page_token": "CoQC_wAAAOIjJW8L1M3Z-Xm5thJZStlql-NqTqtqqMbOwmTR7-k1_2Bem27YcnWGS96mKCwdUiaI7VcMMOoH"
  		}
	 */
	@RequestMapping(value = "/nearby", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public Places getNearbyPlaces(
			@RequestParam(required = true, value = "location") String location,
			@RequestParam(required = false, value = "radius", defaultValue = Constants.DEFAULT_RADIUS) String radius,
			@RequestParam(required = true, value = "cid") Long categoryId,
			@RequestParam(required = false, value = "name") String name,
			@RequestParam(required = false, value = "rankBy") String rankBy,
			@RequestParam(required = false, value = "pagetoken") String pageToken
			)
			{
		String LOG_PREFIX = NEARBY_PLACES_REQUEST;
		logRequestStart(LOG_PREFIX, Constants.PUBLIC_REQUEST_START_LOG, NEARBY_PLACES_REQUEST);
		Places places = null;

		NearbySearchRequestGoogle nearbySearchRequest = new NearbySearchRequestGoogle();
		nearbySearchRequest.setLocation(location);
		//nearbySearchRequest.setName(name);
		nearbySearchRequest.setRadius(radius);
		nearbySearchRequest.setRankBy(rankBy);
		nearbySearchRequest.setCategoryId(categoryId);
		nearbySearchRequest.setPageToken(pageToken);

		places = nearbySearchServiceGoogle.search(nearbySearchRequest);
		logRequestEnd(LOG_PREFIX,NEARBY_PLACES_REQUEST);
		return places;
	}

	@RequestMapping(value = "/tsearch", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public GooglePlaces getPlacesByTextSearch(
			@RequestParam(required = true, value = "query") String query,
			@RequestParam(required = false, value = "location") String location,
			@RequestParam(required = false, value = "radius") String radius,
			@RequestParam(required = false, value = "types") String types,
			@RequestParam(required = false, value = "name") String name,
			@RequestParam(required = false, value = "rankBy") String rankBy)
			{
		logger.info("### Request Recieved | {}",NEARBY_PLACES_REQUEST);
		/*
		 * Object response = validateRequest(authorization);
		 * 
		 * if (response instanceof ServiceException) {
		 * logger.error("### Invalid Authorization header", (ServiceException)
		 * response); throw (ServiceException) response; }
		 */
		GooglePlaces places = null;

		
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

	/*@RequestMapping(value = "/place/{placeId}/detail", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public GooglePlace getPlaceDetails(
			@PathVariable(value = "placeId") String placeId,
			@RequestHeader(required = false, value = Constants.AUTHORIZATION_HEADER) String authorization)
		 {
		String LOG_PREFIX = PLACE_DETAILS_REQUEST;
		logRequestStart(LOG_PREFIX, Constants.PUBLIC_REQUEST_START_LOG, PLACE_DETAILS_REQUEST);
		GooglePlace placeDetails = null;
			PlaceDetailsRequestGoogle placeDetailsRequest = new PlaceDetailsRequestGoogle();
			placeDetailsRequest.setPlaceId(placeId);
			placeDetails = placeDetailService
					.getPlaceDetails(placeDetailsRequest);
		
		logRequestEnd(LOG_PREFIX, PLACE_DETAILS_REQUEST);
		return placeDetails;
	}
	*/
	
}
