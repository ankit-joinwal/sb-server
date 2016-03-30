package com.bitlogic.sociallbox.service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.EventType;
import com.bitlogic.sociallbox.data.model.response.EntityCollectionResponse;
import com.bitlogic.sociallbox.service.business.EventTypeService;

@RestController
@RequestMapping("/api/public/events/types")
public class EventTypePublicController extends BaseController {
	private static final String GET_EVENT_TYPES_API = "GetEventTypes API";
	private static final Logger logger = LoggerFactory.getLogger(EventTypePublicController.class);
	
	@Override
	public Logger getLogger() {
		return logger;
	}
	@Autowired
	private EventTypeService eventService;
	
	/**
	 *  @api {get} /api/public/events/types Get All Event Types
	 *  @apiName Get All Event Types
	 *  @apiGroup Events
	 *  @apiHeader {String} accept application/json
	 *  @apiSuccess (Success - OK 200) {Object}  response  Response.
	 *  @apiSuccess (Success - OK 200) {String}  response.status   Eg.Success.
	 * 	@apiSuccess (Success - OK 200) {Object}  response.data Event Types
	 *  @apiSuccessExample {json} Success-Response: 
	 * 
		{
		  "status": "Success",
		  "data": [
		    {
		      "id": 2,
		      "name": "music",
		      "description": "Music",
		      "displayOrder": 2,
		      "color": "#56bde8"
		    },
		    {
		      "id": 3,
		      "name": "nightlife",
		      "description": "NightLife",
		      "displayOrder": 3,
		      "color": "#7ed321"
		    },
		    {
		      "id": 4,
		      "name": "foodanddrink",
		      "description": "Food n Drinks",
		      "displayOrder": 4,
		      "color": "#f5a623"
		    },
		    {
		      "id": 5,
		      "name": "startup",
		      "description": "Startup",
		      "displayOrder": 5,
		      "color": "#3b5998"
		    },
		    {
		      "id": 6,
		      "name": "education",
		      "description": "Education",
		      "displayOrder": 6,
		      "color": "#f8e71c"
		    },
		    {
		      "id": 7,
		      "name": "travel",
		      "description": "Travel",
		      "displayOrder": 7,
		      "color": "#8b572a"
		    },
		    {
		      "id": 8,
		      "name": "exhibition",
		      "description": "Exhibition",
		      "displayOrder": 9,
		      "color": "#55acee"
		    },
		    {
		      "id": 9,
		      "name": "entertainment",
		      "description": "Entertainment",
		      "displayOrder": 8,
		      "color": "#9013fe"
		    },
		    {
		      "id": 10,
		      "name": "sports",
		      "description": "Sports",
		      "displayOrder": 10,
		      "color": "#429a0c"
		    },
		    {
		      "id": 11,
		      "name": "spiritual",
		      "description": "Spirituality",
		      "displayOrder": 11,
		      "color": "#d0021b"
		    }
		  ],
		  "page": 1,
		  "nextPage": null,
		  "total_records": 10
		}
	 */
	@RequestMapping(method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<EventType> getEventTypes(){
		logRequestStart(GET_EVENT_TYPES_API, Constants.PUBLIC_REQUEST_START_LOG, GET_EVENT_TYPES_API);
		List<EventType> eventTypes = eventService.getAllEventTypesExceptShopping();
		EntityCollectionResponse<EventType> collectionResponse = new EntityCollectionResponse<>();
		collectionResponse.setData(eventTypes);
		collectionResponse.setPage(1);
		collectionResponse.setStatus("Success");
		collectionResponse.setTotalRecords(eventTypes.size());
		logRequestEnd(GET_EVENT_TYPES_API, GET_EVENT_TYPES_API);
		return collectionResponse;
	}
	
}
