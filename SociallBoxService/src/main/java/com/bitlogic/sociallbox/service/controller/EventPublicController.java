package com.bitlogic.sociallbox.service.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.Event;
import com.bitlogic.sociallbox.data.model.EventImage;
import com.bitlogic.sociallbox.data.model.response.EntityCollectionResponse;
import com.bitlogic.sociallbox.data.model.response.EventResponse;
import com.bitlogic.sociallbox.data.model.response.SingleEntityResponse;
import com.bitlogic.sociallbox.service.business.EventService;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.transformers.Transformer;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory.Transformer_Types;

@RestController
@RequestMapping("/api/public/events")
public class EventPublicController extends BaseController implements Constants{
	private static final Logger logger = LoggerFactory.getLogger(EventPublicController.class);
	private static final String GET_EVENT_API = "GetEvent API";
	private static final String GET_EVENT_IMAGES_API = "GetEventImages API";
	private static final String GET_EVENT_IMAGE_BY_NAME_API = "GetEventImageByName API";
	private static final String GET_PERSONALIZEZ_EVENT_FOR_USER_API = "GetPersonalizedEventsForUser API";
	private static final String GET_EVENTS_BY_TYPE_API = "GetEventsOfType API";
	private static final String GET_UPCOMING_EVENTS_BY_ORG_API = "GetUpcomingEventsOfOrg API";
	
	@Override
	public Logger getLogger() {
		return logger;
	}
	
	@Autowired
	private EventService eventService;
	
	@RequestMapping(value="/{eventId}",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<EventResponse> getEvent(@PathVariable String eventId) {
		logRequestStart(GET_EVENT_API, PUBLIC_REQUEST_START_LOG, GET_EVENT_API);
		logInfo(GET_EVENT_API, "event id = {}", eventId);
		Transformer<EventResponse, Event> transformer = (Transformer<EventResponse, Event>) TransformerFactory.getTransformer(Transformer_Types.EVENT_TRANS);
		EventResponse createEventResponse = transformer.transform(eventService.get(eventId));
		SingleEntityResponse<EventResponse> entityResponse = new SingleEntityResponse<>();
		entityResponse.setData(createEventResponse);
		entityResponse.setStatus(SUCCESS_STATUS);
		logRequestEnd(GET_EVENT_API, GET_EVENT_API);
		return entityResponse;
	}
	
	@RequestMapping(value="/{eventId}/images",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<EventImage> getEventImages(@PathVariable String eventId){
		logRequestStart(GET_EVENT_IMAGES_API, PUBLIC_REQUEST_START_LOG, GET_EVENT_IMAGES_API);
		logInfo(GET_EVENT_IMAGES_API, "Event id = {} ", eventId);
		List<EventImage> eventImages = this.eventService.getEventImages(eventId);
		EntityCollectionResponse<EventImage> collectionResponse = new EntityCollectionResponse<>();
		collectionResponse.setPage(1);
		collectionResponse.setStatus(SUCCESS_STATUS);
		collectionResponse.setData(eventImages);
		logRequestEnd(GET_EVENT_IMAGES_API, GET_EVENT_IMAGES_API);
		return collectionResponse;
	}
	
	@RequestMapping(value="/{eventId}/images/{imageName}",method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<InputStreamResource> getEventImage(@PathVariable String eventId,
								@PathVariable String imageName){
		
		logRequestStart(GET_EVENT_IMAGE_BY_NAME_API, PUBLIC_REQUEST_START_LOG, GET_EVENT_IMAGE_BY_NAME_API);
		logInfo(GET_EVENT_IMAGE_BY_NAME_API, "Event id ={} , image name = {} ",eventId,imageName  );
		String filePath = Constants.EVENT_IMAGE_STORE_PATH+File.separator+eventId+File.separator+imageName+".jpg";
		logInfo(GET_EVENT_IMAGE_BY_NAME_API, "File Path = {}", filePath);
		File file = new File(filePath);
		if(!file.exists()){
			logError(GET_EVENT_IMAGE_BY_NAME_API, "Image not found with name = {}", imageName);
			throw new ClientException(RestErrorCodes.ERR_003, ERROR_IMAGE_NOT_FOUND);
		}
		InputStream inputStream = null;
		try{
			inputStream = new FileInputStream(file);
		}catch(FileNotFoundException exception){
			logError(GET_EVENT_IMAGE_BY_NAME_API, "Image not found with name = {}", imageName);
			throw new ClientException(RestErrorCodes.ERR_003, ERROR_IMAGE_NOT_FOUND);
		}
		logRequestEnd(GET_EVENT_IMAGE_BY_NAME_API, GET_EVENT_IMAGE_BY_NAME_API);
		 return ResponseEntity.ok()
		            .body(new InputStreamResource(inputStream));
	}
	
	/**
	*  @api {get} /api/public/events/personalized?city=:city&country=:country&id=:userId&lat=:lat&lon=:lon&page=:page Get Events For You
	*  @apiName Get Events For You
	*  @apiGroup Events
	*  @apiHeader {String} accept application/json
	*  @apiParam {Number} city Mandatory User City(Obtained from location)
	*  @apiParam {Number} country Mandatory User Country(Obtained from location)
	*  @apiParam {Number} userId Mandatory For logged in user , otherwise optional
	*  @apiParam {String} lat Mandatory Lattitude of User
	*  @apiParam {String} lon Mandatory Longitude of User
	*  @apiParam {String} page Optional Next Page Number To search[Will be blank in intial request, then pass previous search response's (page+1)]
	*  @apiParam {String} order Optional Ignore this for now
	*  @apiSuccess (Success - OK 200) {Object}  response  Response.
	*  @apiSuccess (Success - OK 200) {String}  response.status   OK.
	*  @apiSuccess (Success - OK 200) {String}  response.page This is Current page number. Increment by 1 to get next 20 records.
	*  @apiSuccess (Success - OK 200) {String}  response.total_records   Eg. 20
	*  @apiSuccess (Success - OK 200) {Object}  response.data Events list
	*  @apiSuccessExample {json} Success-Response:
	* 
		{
	    "status": "Success",
	    "data": [
	        {
	            "title": "Smaaash Cyberhub",
	            "description": "<p><b>General Offer: Play 9 Games at Smaaash Gurgaon @ Rs 555 AI (Monday to Thursday) and @ Rs 888 AI (Friday to Sunday)11am to 11pm</b><br/>- Finger Coaster<br/>- Super Keeper (5 shots)<br/>- Vulcan Force<br/>- Harley Simulator<br/>- King of Hammer<br/>- Speed of Light<br/>- Nitro Wheelie<br/>- Pacman Basket<br/>- Over the Top<br/><br/><b>College Student Offer: Rs. 699 per student</b><br/>Valid from Monday to Thursday<br/>Not Valid Without College Student ID card<br/>Valid between 11 AM and 5 PM<br/>Which gives Single Access to College Students to all Games at Smaaash Gurgaon along with Bowling and Cricket.<br/><br/><b>Rs. 399- All days, Games combo. Any 3 Simulation Games or Any 3 Virtual Reality Games. Any 3 Arcade Games. 5 Shots at SupeerKeeper</b><br/></p>",
	            "tags": [
	                {
	                    "name": "hhour",
	                    "description": "HappyHours"
	                },
	                {
	                    "name": "family",
	                    "description": "Family"
	                }
	            ],
	            "id": "2c9f8ff353bd8bf50153bd9ea0a10000",
	            "distance_from_src": "14 Kms",
	            "event_detail": {
	                "location": {
	                    "lattitude": 28.483705,
	                    "longitude": 77.107372,
	                    "name": "DLF Cyber City, Tower B, 8th Rd, DLF Phase 2, Sector 24, Gurgaon, DELHI 122 002, India",
	                    "locality": "Gurgaon"
	                },
	                "organizer": {
	                    "name": "XYZ Entertainment",
	                    "address": {
	                        "street": "Kalkaji",
	                        "city": "New Delhi",
	                        "state": "Delhi",
	                        "country": "India",
	                        "zip_code": "110019"
	                    },
	                    "phone1": "+91 7838250407",
	                    "phone2": "",
	                    "phone3": "",
	                    "id": "2c9f8ff353af06840153af0a742c0000",
	                    "email_id": "xyz@gmail.com",
	                    "profile_pic": null,
	                    "cover_pic": null,
	                    "website": "http://www.test.com"
	                },
	                "booking_url": "https://in.bookmyshow.com"
	            },
	            "start_date": "Tue, 1 Mar 12:30 PM",
	            "end_date": "Mon, 2 May 08:30 PM",
	            "is_user_fav": false,
	            "display_image": {
	                "id": 1,
	                "name": "smash.jpg",
	                "url": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/events/2c9f8ff353bd8bf50153bd9ea0a10000/smash.jpg",
	                "displayOrder": 1,
	                "mimeType": null
	            },
	            "is_free": false
	        },
	        {
	            "title": "Arijit Singh Live in Concert",
	            "description": "<p>Carnival Media presents the nation's absolute heart-throb, Arijit Singh, with the Grand Symphony live in concert. <br/>The Grand Symphony Orchestra is a 45 -piece band of some of the most talented musicians from across the world. This large instrumental ensemblethat contains Drums, Violin, Grand Piano, Cello, Harp, Flute, French Horns, Trumpet, Viola and much more will be performing in sync with Arijit Singh.<br/>The scintillating and soulful voice behind the hits like Tum hi ho, Muskurane ki vajah and Sooraj Dooba Hai Yaaron will be singing his hits in ways like never before.<br/>This musical extravaganza will be held on four places, i.e. Singapore, Chandigarh, Indore and Srilanka. This will be a ticketed event and entry will be reserved only for Ticket holders.<br/></p>",
	            "tags": [
	                {
	                    "name": "rock",
	                    "description": "Rock"
	                },
	                {
	                    "name": "bollywood",
	                    "description": "Bollywood"
	                },
	                {
	                    "name": "family",
	                    "description": "Family"
	                },
	                {
	                    "name": "sufi",
	                    "description": "Sufi"
	                }
	            ],
	            "id": "2c9f8ff353bd8bf50153bdab6f440001",
	            "distance_from_src": "9 Kms",
	            "event_detail": {
	                "location": {
	                    "lattitude": 28.5285479,
	                    "longitude": 77.2194538,
	                    "name": "Select Citywalk, Neu-Delhi, Delhi, India",
	                    "locality": "Saket"
	                },
	                "organizer": {
	                    "name": "XYZ Entertainment",
	                    "address": {
	                        "street": "Kalkaji",
	                        "city": "New Delhi",
	                        "state": "Delhi",
	                        "country": "India",
	                        "zip_code": "110019"
	                    },
	                    "phone1": "+91 7838250407",
	                    "phone2": "",
	                    "phone3": "",
	                    "id": "2c9f8ff353af06840153af0a742c0000",
	                    "email_id": "xyz@gmail.com",
	                    "profile_pic": null,
	                    "cover_pic": null,
	                    "website": "http://www.test.com"
	                },
	                "booking_url": "https://in.bookmyshow.com"
	            },
	            "start_date": "Fri, 11 Mar 06:30 PM",
	            "end_date": "Fri, 11 Mar 10:30 PM",
	            "is_user_fav": true,
	            "display_image": {
	                "id": 2,
	                "name": "arjit.jpg",
	                "url": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/events/2c9f8ff353bd8bf50153bdab6f440001/arjit.jpg",
	                "displayOrder": 1,
	                "mimeType": null
	            },
	            "is_free": true
	        },
	        {
	            "title": "Songdew Media Nights - Live Music",
	            "description": "Songdew invites you to a night of rock music with some of the most sought after bands in the country. The energy and enthusiasm of the music is sure to make your heart pump. Dont miss the opportunity to be a part of the excitement.",
	            "tags": [
	                {
	                    "name": "rock",
	                    "description": "Rock"
	                },
	                {
	                    "name": "bollywood",
	                    "description": "Bollywood"
	                },
	                {
	                    "name": "family",
	                    "description": "Family"
	                },
	                {
	                    "name": "sufi",
	                    "description": "Sufi"
	                }
	            ],
	            "id": "2c9f8ff353bd8bf50153bdaf23470002",
	            "distance_from_src": "9 Kms",
	            "event_detail": {
	                "location": {
	                    "lattitude": 28.52721,
	                    "longitude": 77.2170872,
	                    "name": "Hard Rock Cafe, Saket, Delhi, India",
	                    "locality": "Hard Rock Cafe"
	                },
	                "organizer": {
	                    "name": "XYZ Entertainment",
	                    "address": {
	                        "street": "Kalkaji",
	                        "city": "New Delhi",
	                        "state": "Delhi",
	                        "country": "India",
	                        "zip_code": "110019"
	                    },
	                    "phone1": "+91 7838250407",
	                    "phone2": "",
	                    "phone3": "",
	                    "id": "2c9f8ff353af06840153af0a742c0000",
	                    "email_id": "xyz@gmail.com",
	                    "profile_pic": null,
	                    "cover_pic": null,
	                    "website": "http://www.test.com"
	                },
	                "booking_url": "https://in.bookmyshow.com"
	            },
	            "start_date": "Tue, 12 Apr 06:30 PM",
	            "end_date": "Tue, 12 Apr 10:30 PM",
	            "is_user_fav": false,
	            "display_image": {
	                "id": 3,
	                "name": "songdew.jpg",
	                "url": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/events/2c9f8ff353bd8bf50153bdaf23470002/songdew.jpg",
	                "displayOrder": 1,
	                "mimeType": null
	            },
	            "is_free": true
	        },
	        {
	            "title": "DISCOVERY One Month Weekend Theatre Workshop-Batch 1",
	            "description": "<p><b>Here is what they want you to know:</b><br/><br/>Essentially, through individual, team and cumulative exercises, activities and participation they intend to guide, mentor and extend a support mechanism to seekers and aspirants to know, realise and therefore, develop their acting potential and eventually hone their skills and talents in a valid direction. This means, in strict senses, 'how to go about it?' and of course, with 'where' and a little 'when'. The workshop, with months of perseverance and experience of people behind it, assures you a shred by shred detailing in improvisations, scenic studies and comprehension, actor's vision, blockages, characterization, the utility of observance and wait and myriad of inspiration which may provoke you to pull that 'string'..anywhere. This workshop, eclectic in method, like a beautiful bird flying in sky, is designed in a such a way that it becomes vast, enormous and gigantic as the 'greed for need' arises. And of course, aspirants are humbly expected to keep journals of their work. Plus, any experience or exposure in theatre, cinema or performing art or any related school is candidly acknowledged, however, it is not mandatory at all. This workshop is and shall always remain open for any/every/each one.<br/><br/></p>",
	            "tags": [
	                {
	                    "name": "rock",
	                    "description": "Rock"
	                },
	                {
	                    "name": "bollywood",
	                    "description": "Bollywood"
	                },
	                {
	                    "name": "family",
	                    "description": "Family"
	                },
	                {
	                    "name": "sufi",
	                    "description": "Sufi"
	                }
	            ],
	            "id": "2c9f8ff353bd8bf50153bdbcd0840003",
	            "distance_from_src": "9 Kms",
	            "event_detail": {
	                "location": {
	                    "lattitude": 28.52721,
	                    "longitude": 77.2170872,
	                    "name": "Hard Rock Cafe, Saket, Delhi, India",
	                    "locality": "Hard Rock Cafe"
	                },
	                "organizer": {
	                    "name": "XYZ Entertainment",
	                    "address": {
	                        "street": "Kalkaji",
	                        "city": "New Delhi",
	                        "state": "Delhi",
	                        "country": "India",
	                        "zip_code": "110019"
	                    },
	                    "phone1": "+91 7838250407",
	                    "phone2": "",
	                    "phone3": "",
	                    "id": "2c9f8ff353af06840153af0a742c0000",
	                    "email_id": "xyz@gmail.com",
	                    "profile_pic": null,
	                    "cover_pic": null,
	                    "website": "http://www.test.com"
	                },
	                "booking_url": "https://in.bookmyshow.com"
	            },
	            "start_date": "Tue, 12 Apr 06:30 PM",
	            "end_date": "Tue, 12 Apr 10:30 PM",
	            "is_user_fav": false,
	            "display_image": {
	                "id": 4,
	                "name": "disc.jpg",
	                "url": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/events/2c9f8ff353bd8bf50153bdbcd0840003/disc.jpg",
	                "displayOrder": 1,
	                "mimeType": null
	            },
	            "is_free": false
	        },
	        {
	            "title": "Worlds Of Wonder",
	            "description": "<p><span id="selectionBoundary_1459349793081_5889249769145433" class="rangySelectionBoundary">﻿</span>Spread over 20 acres, the WOW - Amusement park offers 20 world class attractions. It is segregated into the following entertainment zones:<br/>-The road show<br/>-The teen zone and la fiesta<br/>-The family zone<br/>The road show is meant for adventure lovers and consists of seven thrilling attractions. La Fiesta is a zone meant for family and kids. It comprises of 13 attractions. All rides at the WOW Amusement Park are certified for safety by Tuv Nord, a German certifying body. It also includes the largest man-made lake in the entire NCR.<br/>Come and have a memorable day with your loved ones filled with fun and adventure!<span id="selectionBoundary_1459349793081_02449337906340232" class="rangySelectionBoundary">﻿</span><br/></p>",
	            "tags": [
	                {
	                    "name": "hhour",
	                    "description": "HappyHours"
	                },
	                {
	                    "name": "family",
	                    "description": "Family"
	                }
	            ],
	            "id": "2c9f8ff353c815f60153c81a44e20000",
	            "distance_from_src": "14 Kms",
	            "event_detail": {
	                "location": {
	                    "lattitude": 28.483705,
	                    "longitude": 77.107372,
	                    "name": "No. 2, Behind TGIP Mall, Entertainment City, Delhi, NCR 201301, India",
	                    "locality": "Noida"
	                },
	                "organizer": {
	                    "name": "XYZ Entertainment",
	                    "address": {
	                        "street": "Kalkaji",
	                        "city": "New Delhi",
	                        "state": "Delhi",
	                        "country": "India",
	                        "zip_code": "110019"
	                    },
	                    "phone1": "+91 7838250407",
	                    "phone2": "",
	                    "phone3": "",
	                    "id": "2c9f8ff353af06840153af0a742c0000",
	                    "email_id": "xyz@gmail.com",
	                    "profile_pic": null,
	                    "cover_pic": null,
	                    "website": "http://www.test.com"
	                },
	                "booking_url": "https://in.bookmyshow.com/national-capital-region-ncr/events/wow-amusement-park/ET00035886"
	            },
	            "start_date": "Tue, 1 Mar 12:30 PM",
	            "end_date": "Tue, 2 May 08:30 PM",
	            "is_user_fav": false,
	            "display_image": {
	                "id": 5,
	                "name": "wow.jpg",
	                "url": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/events/2c9f8ff353c815f60153c81a44e20000/wow.jpg",
	                "displayOrder": 1,
	                "mimeType": null
	            },
	            "is_free": false
	        }
	    ],
	    "page": 1,
	    "nextPage": null,
	    "total_records": 5
	}
	*/
	@RequestMapping(value="/personalized",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<EventResponse> getPersonalizedEvents(
												@RequestParam(required = true, value = "lat") String latitude,
												@RequestParam(required = true, value = "lon") String longitude,
												@RequestParam(required = false, value = "id") Long userId,
												@RequestParam(required = true, value = "city") String city,
												@RequestParam(required = true, value = "country") String country,
												@RequestParam(required=false,value="page") Integer page){

		logRequestStart(GET_PERSONALIZEZ_EVENT_FOR_USER_API, PUBLIC_REQUEST_START_LOG, GET_PERSONALIZEZ_EVENT_FOR_USER_API);
		logInfo(GET_PERSONALIZEZ_EVENT_FOR_USER_API, " City {} , Country {} ,user {} ", city,country,userId);
		if(page==null){
			page = new Integer(1);
		}
		String location = latitude +","+longitude;
		List<EventResponse> events = this.eventService.getEventsForUser(location,userId,city, country,page);
		EntityCollectionResponse<EventResponse> collectionResponse = new EntityCollectionResponse<>();
		collectionResponse.setData(events);
		collectionResponse.setStatus("Success");
		collectionResponse.setPage(page);
		collectionResponse.setTotalRecords(events.size());
		
		logRequestEnd(GET_PERSONALIZEZ_EVENT_FOR_USER_API, GET_PERSONALIZEZ_EVENT_FOR_USER_API);
		return collectionResponse;
	}
	
	
	/**
	*  @api {get} /api/public/events/types/:type?city=:city&country=:country&id=:userId&lat=:lat&lon=:lon&page=:page Get Events By Event-Type
	*  @apiName Get Events By Event-Type
	*  @apiGroup Events
	*  @apiHeader {String} accept application/json
	*  @apiParam {Number} type Mandatory Event Type Name 
	*  @apiParam {Number} city Mandatory User City(Obtained from location)
	*  @apiParam {Number} country Mandatory User Country(Obtained from location)
	*  @apiParam {Number} userId Mandatory For logged in user , otherwise optional
	*  @apiParam {String} lat Mandatory Lattitude of User
	*  @apiParam {String} lon Mandatory Longitude of User
	*  @apiParam {String} page Optional Next Page Number To search[Will be blank in intial request, then pass previous search response's (page+1)]
	*  @apiParam {String} order Optional Ignore this for now
	*  @apiSuccess (Success - OK 200) {Object}  response  Response.
	*  @apiSuccess (Success - OK 200) {String}  response.status   OK.
	*  @apiSuccess (Success - OK 200) {String}  response.page This is Current page number. Increment by 1 to get next 20 records.
	*  @apiSuccess (Success - OK 200) {String}  response.total_records   Eg. 20
	*  @apiSuccess (Success - OK 200) {Object}  response.data Events list
	*  @apiSuccessExample {json} Success-Response:
	* 
		{
	    "status": "Success",
	    "data": [
	        {
	            "title": "Smaaash Cyberhub",
	            "description": "<p><b>General Offer: Play 9 Games at Smaaash Gurgaon @ Rs 555 AI (Monday to Thursday) and @ Rs 888 AI (Friday to Sunday)11am to 11pm</b><br/>- Finger Coaster<br/>- Super Keeper (5 shots)<br/>- Vulcan Force<br/>- Harley Simulator<br/>- King of Hammer<br/>- Speed of Light<br/>- Nitro Wheelie<br/>- Pacman Basket<br/>- Over the Top<br/><br/><b>College Student Offer: Rs. 699 per student</b><br/>Valid from Monday to Thursday<br/>Not Valid Without College Student ID card<br/>Valid between 11 AM and 5 PM<br/>Which gives Single Access to College Students to all Games at Smaaash Gurgaon along with Bowling and Cricket.<br/><br/><b>Rs. 399- All days, Games combo. Any 3 Simulation Games or Any 3 Virtual Reality Games. Any 3 Arcade Games. 5 Shots at SupeerKeeper</b><br/></p>",
	            "tags": [
	                {
	                    "name": "hhour",
	                    "description": "HappyHours"
	                },
	                {
	                    "name": "family",
	                    "description": "Family"
	                }
	            ],
	            "id": "2c9f8ff353bd8bf50153bd9ea0a10000",
	            "distance_from_src": "14 Kms",
	            "event_detail": {
	                "location": {
	                    "lattitude": 28.483705,
	                    "longitude": 77.107372,
	                    "name": "DLF Cyber City, Tower B, 8th Rd, DLF Phase 2, Sector 24, Gurgaon, DELHI 122 002, India",
	                    "locality": "Gurgaon"
	                },
	                "organizer": {
	                    "name": "XYZ Entertainment",
	                    "address": {
	                        "street": "Kalkaji",
	                        "city": "New Delhi",
	                        "state": "Delhi",
	                        "country": "India",
	                        "zip_code": "110019"
	                    },
	                    "phone1": "+91 7838250407",
	                    "phone2": "",
	                    "phone3": "",
	                    "id": "2c9f8ff353af06840153af0a742c0000",
	                    "email_id": "xyz@gmail.com",
	                    "profile_pic": null,
	                    "cover_pic": null,
	                    "website": "http://www.test.com"
	                },
	                "booking_url": "https://in.bookmyshow.com"
	            },
	            "start_date": "Tue, 1 Mar 12:30 PM",
	            "end_date": "Mon, 2 May 08:30 PM",
	            "is_user_fav": false,
	            "display_image": {
	                "id": 1,
	                "name": "smash.jpg",
	                "url": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/events/2c9f8ff353bd8bf50153bd9ea0a10000/smash.jpg",
	                "displayOrder": 1,
	                "mimeType": null
	            },
	            "is_free": false
	        },
	        {
	            "title": "Arijit Singh Live in Concert",
	            "description": "<p>Carnival Media presents the nation's absolute heart-throb, Arijit Singh, with the Grand Symphony live in concert. <br/>The Grand Symphony Orchestra is a 45 -piece band of some of the most talented musicians from across the world. This large instrumental ensemblethat contains Drums, Violin, Grand Piano, Cello, Harp, Flute, French Horns, Trumpet, Viola and much more will be performing in sync with Arijit Singh.<br/>The scintillating and soulful voice behind the hits like Tum hi ho, Muskurane ki vajah and Sooraj Dooba Hai Yaaron will be singing his hits in ways like never before.<br/>This musical extravaganza will be held on four places, i.e. Singapore, Chandigarh, Indore and Srilanka. This will be a ticketed event and entry will be reserved only for Ticket holders.<br/></p>",
	            "tags": [
	                {
	                    "name": "rock",
	                    "description": "Rock"
	                },
	                {
	                    "name": "bollywood",
	                    "description": "Bollywood"
	                },
	                {
	                    "name": "family",
	                    "description": "Family"
	                },
	                {
	                    "name": "sufi",
	                    "description": "Sufi"
	                }
	            ],
	            "id": "2c9f8ff353bd8bf50153bdab6f440001",
	            "distance_from_src": "9 Kms",
	            "event_detail": {
	                "location": {
	                    "lattitude": 28.5285479,
	                    "longitude": 77.2194538,
	                    "name": "Select Citywalk, Neu-Delhi, Delhi, India",
	                    "locality": "Saket"
	                },
	                "organizer": {
	                    "name": "XYZ Entertainment",
	                    "address": {
	                        "street": "Kalkaji",
	                        "city": "New Delhi",
	                        "state": "Delhi",
	                        "country": "India",
	                        "zip_code": "110019"
	                    },
	                    "phone1": "+91 7838250407",
	                    "phone2": "",
	                    "phone3": "",
	                    "id": "2c9f8ff353af06840153af0a742c0000",
	                    "email_id": "xyz@gmail.com",
	                    "profile_pic": null,
	                    "cover_pic": null,
	                    "website": "http://www.test.com"
	                },
	                "booking_url": "https://in.bookmyshow.com"
	            },
	            "start_date": "Fri, 11 Mar 06:30 PM",
	            "end_date": "Fri, 11 Mar 10:30 PM",
	            "is_user_fav": true,
	            "display_image": {
	                "id": 2,
	                "name": "arjit.jpg",
	                "url": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/events/2c9f8ff353bd8bf50153bdab6f440001/arjit.jpg",
	                "displayOrder": 1,
	                "mimeType": null
	            },
	            "is_free": true
	        },
	        {
	            "title": "Songdew Media Nights - Live Music",
	            "description": "Songdew invites you to a night of rock music with some of the most sought after bands in the country. The energy and enthusiasm of the music is sure to make your heart pump. Dont miss the opportunity to be a part of the excitement.",
	            "tags": [
	                {
	                    "name": "rock",
	                    "description": "Rock"
	                },
	                {
	                    "name": "bollywood",
	                    "description": "Bollywood"
	                },
	                {
	                    "name": "family",
	                    "description": "Family"
	                },
	                {
	                    "name": "sufi",
	                    "description": "Sufi"
	                }
	            ],
	            "id": "2c9f8ff353bd8bf50153bdaf23470002",
	            "distance_from_src": "9 Kms",
	            "event_detail": {
	                "location": {
	                    "lattitude": 28.52721,
	                    "longitude": 77.2170872,
	                    "name": "Hard Rock Cafe, Saket, Delhi, India",
	                    "locality": "Hard Rock Cafe"
	                },
	                "organizer": {
	                    "name": "XYZ Entertainment",
	                    "address": {
	                        "street": "Kalkaji",
	                        "city": "New Delhi",
	                        "state": "Delhi",
	                        "country": "India",
	                        "zip_code": "110019"
	                    },
	                    "phone1": "+91 7838250407",
	                    "phone2": "",
	                    "phone3": "",
	                    "id": "2c9f8ff353af06840153af0a742c0000",
	                    "email_id": "xyz@gmail.com",
	                    "profile_pic": null,
	                    "cover_pic": null,
	                    "website": "http://www.test.com"
	                },
	                "booking_url": "https://in.bookmyshow.com"
	            },
	            "start_date": "Tue, 12 Apr 06:30 PM",
	            "end_date": "Tue, 12 Apr 10:30 PM",
	            "is_user_fav": false,
	            "display_image": {
	                "id": 3,
	                "name": "songdew.jpg",
	                "url": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/events/2c9f8ff353bd8bf50153bdaf23470002/songdew.jpg",
	                "displayOrder": 1,
	                "mimeType": null
	            },
	            "is_free": true
	        },
	        {
	            "title": "DISCOVERY One Month Weekend Theatre Workshop-Batch 1",
	            "description": "<p><b>Here is what they want you to know:</b><br/><br/>Essentially, through individual, team and cumulative exercises, activities and participation they intend to guide, mentor and extend a support mechanism to seekers and aspirants to know, realise and therefore, develop their acting potential and eventually hone their skills and talents in a valid direction. This means, in strict senses, 'how to go about it?' and of course, with 'where' and a little 'when'. The workshop, with months of perseverance and experience of people behind it, assures you a shred by shred detailing in improvisations, scenic studies and comprehension, actor's vision, blockages, characterization, the utility of observance and wait and myriad of inspiration which may provoke you to pull that 'string'..anywhere. This workshop, eclectic in method, like a beautiful bird flying in sky, is designed in a such a way that it becomes vast, enormous and gigantic as the 'greed for need' arises. And of course, aspirants are humbly expected to keep journals of their work. Plus, any experience or exposure in theatre, cinema or performing art or any related school is candidly acknowledged, however, it is not mandatory at all. This workshop is and shall always remain open for any/every/each one.<br/><br/></p>",
	            "tags": [
	                {
	                    "name": "rock",
	                    "description": "Rock"
	                },
	                {
	                    "name": "bollywood",
	                    "description": "Bollywood"
	                },
	                {
	                    "name": "family",
	                    "description": "Family"
	                },
	                {
	                    "name": "sufi",
	                    "description": "Sufi"
	                }
	            ],
	            "id": "2c9f8ff353bd8bf50153bdbcd0840003",
	            "distance_from_src": "9 Kms",
	            "event_detail": {
	                "location": {
	                    "lattitude": 28.52721,
	                    "longitude": 77.2170872,
	                    "name": "Hard Rock Cafe, Saket, Delhi, India",
	                    "locality": "Hard Rock Cafe"
	                },
	                "organizer": {
	                    "name": "XYZ Entertainment",
	                    "address": {
	                        "street": "Kalkaji",
	                        "city": "New Delhi",
	                        "state": "Delhi",
	                        "country": "India",
	                        "zip_code": "110019"
	                    },
	                    "phone1": "+91 7838250407",
	                    "phone2": "",
	                    "phone3": "",
	                    "id": "2c9f8ff353af06840153af0a742c0000",
	                    "email_id": "xyz@gmail.com",
	                    "profile_pic": null,
	                    "cover_pic": null,
	                    "website": "http://www.test.com"
	                },
	                "booking_url": "https://in.bookmyshow.com"
	            },
	            "start_date": "Tue, 12 Apr 06:30 PM",
	            "end_date": "Tue, 12 Apr 10:30 PM",
	            "is_user_fav": false,
	            "display_image": {
	                "id": 4,
	                "name": "disc.jpg",
	                "url": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/events/2c9f8ff353bd8bf50153bdbcd0840003/disc.jpg",
	                "displayOrder": 1,
	                "mimeType": null
	            },
	            "is_free": false
	        },
	        {
	            "title": "Worlds Of Wonder",
	            "description": "<p><span id="selectionBoundary_1459349793081_5889249769145433" class="rangySelectionBoundary">﻿</span>Spread over 20 acres, the WOW - Amusement park offers 20 world class attractions. It is segregated into the following entertainment zones:<br/>-The road show<br/>-The teen zone and la fiesta<br/>-The family zone<br/>The road show is meant for adventure lovers and consists of seven thrilling attractions. La Fiesta is a zone meant for family and kids. It comprises of 13 attractions. All rides at the WOW Amusement Park are certified for safety by Tuv Nord, a German certifying body. It also includes the largest man-made lake in the entire NCR.<br/>Come and have a memorable day with your loved ones filled with fun and adventure!<span id="selectionBoundary_1459349793081_02449337906340232" class="rangySelectionBoundary">﻿</span><br/></p>",
	            "tags": [
	                {
	                    "name": "hhour",
	                    "description": "HappyHours"
	                },
	                {
	                    "name": "family",
	                    "description": "Family"
	                }
	            ],
	            "id": "2c9f8ff353c815f60153c81a44e20000",
	            "distance_from_src": "14 Kms",
	            "event_detail": {
	                "location": {
	                    "lattitude": 28.483705,
	                    "longitude": 77.107372,
	                    "name": "No. 2, Behind TGIP Mall, Entertainment City, Delhi, NCR 201301, India",
	                    "locality": "Noida"
	                },
	                "organizer": {
	                    "name": "XYZ Entertainment",
	                    "address": {
	                        "street": "Kalkaji",
	                        "city": "New Delhi",
	                        "state": "Delhi",
	                        "country": "India",
	                        "zip_code": "110019"
	                    },
	                    "phone1": "+91 7838250407",
	                    "phone2": "",
	                    "phone3": "",
	                    "id": "2c9f8ff353af06840153af0a742c0000",
	                    "email_id": "xyz@gmail.com",
	                    "profile_pic": null,
	                    "cover_pic": null,
	                    "website": "http://www.test.com"
	                },
	                "booking_url": "https://in.bookmyshow.com/national-capital-region-ncr/events/wow-amusement-park/ET00035886"
	            },
	            "start_date": "Tue, 1 Mar 12:30 PM",
	            "end_date": "Tue, 2 May 08:30 PM",
	            "is_user_fav": false,
	            "display_image": {
	                "id": 5,
	                "name": "wow.jpg",
	                "url": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/events/2c9f8ff353c815f60153c81a44e20000/wow.jpg",
	                "displayOrder": 1,
	                "mimeType": null
	            },
	            "is_free": false
	        }
	    ],
	    "page": 1,
	    "nextPage": null,
	    "total_records": 5
	}
	*/
	@RequestMapping(value="/types/{eventType}",method=RequestMethod.GET,produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<EventResponse> getEventsOfType(
												@PathVariable String eventType,
												@RequestParam(required = true, value = "lat") String latitude,
												@RequestParam(required = true, value = "lon") String longitude,
												@RequestParam(required = false, value = "id") Long userId,
												@RequestParam(required = true, value = "city") String city,
												@RequestParam(required = true, value = "country") String country,
												@RequestParam(required=false,value="page") Integer page) {
		logRequestStart(GET_EVENTS_BY_TYPE_API, PUBLIC_REQUEST_START_LOG, GET_EVENTS_BY_TYPE_API);
		
		logInfo(GET_EVENTS_BY_TYPE_API,"Params [ Type :{} , City : {} , Country : {} ",eventType,city,country);
		if(page==null){
			page = new Integer(1);
		}
		String location = latitude +","+longitude;
		List<EventResponse> eventsList = this.eventService.getEventsByType(location,userId,eventType, city, country,page);
		
		EntityCollectionResponse<EventResponse> collectionResponse = new EntityCollectionResponse<>();
		collectionResponse.setData(eventsList);
		collectionResponse.setStatus("Success");
		collectionResponse.setPage(page);
		collectionResponse.setTotalRecords(eventsList.size());
		logRequestEnd(GET_EVENTS_BY_TYPE_API, GET_EVENTS_BY_TYPE_API);
		return collectionResponse;
	}
	
	@RequestMapping(value="/upcoming/organizer/{orgId}",method=RequestMethod.GET,produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<EventResponse> getUpcomingEventsByOrganizer(
			@PathVariable( value = "orgId") String organizerId,
			@RequestParam(required=false,value="filter") String filterEventId){
		logRequestStart(GET_UPCOMING_EVENTS_BY_ORG_API, PUBLIC_REQUEST_START_LOG, GET_UPCOMING_EVENTS_BY_ORG_API);
		logInfo(GET_UPCOMING_EVENTS_BY_ORG_API, "Organizer Id = {}", organizerId);
		List<EventResponse> events = this.eventService.getUpcomingEventsByOrg(organizerId,filterEventId);
		
		EntityCollectionResponse<EventResponse> collectionResponse = new EntityCollectionResponse<EventResponse>();
		collectionResponse.setData(events);
		collectionResponse.setPage(1);
		collectionResponse.setStatus(SUCCESS_STATUS);
		collectionResponse.setTotalRecords(events.size());
		return collectionResponse;
	}
}
