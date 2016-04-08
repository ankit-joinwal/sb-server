package com.bitlogic.sociallbox.service.controller.secured;

import java.util.List;

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
import com.bitlogic.sociallbox.data.model.UserSocialActivity;
import com.bitlogic.sociallbox.data.model.UserSocialActivity.ActivityTime;
import com.bitlogic.sociallbox.data.model.UserTypeBasedOnDevice;
import com.bitlogic.sociallbox.data.model.requests.MeetupResponse;
import com.bitlogic.sociallbox.data.model.response.EntityCollectionResponse;
import com.bitlogic.sociallbox.service.business.MeetupService;
import com.bitlogic.sociallbox.service.business.MySociallBoxService;
import com.bitlogic.sociallbox.service.controller.BaseController;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.utils.LoginUtil;

@RestController
@RequestMapping("/api/secured/users")
public class MySociallBoxController extends BaseController implements Constants {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MySociallBoxController.class);
	private static final String MY_SOCIALLBOX_API = "MySociallBox API";
	private static final String MY_PENDING_MEETUP_INVITE_API = "MyPendingMeetupInvites API";

	@Override
	public Logger getLogger() {
		return LOGGER;
	}

	@Autowired
	private MySociallBoxService mySociallBoxService;
	
	@Autowired
	private MeetupService meetupService;

	/**
	 *  @api {get} /api/secured/users/:userId/mybox?lat=:lattitude&lon=:longitude&type=:timeline Get User SociallBox Activities
	 *  @apiName Get User SociallBox activities
	 *  @apiGroup MySociallBox
	 *  @apiParam {Number} userId Mandatory User Id
	 *  @apiParam {Number} lattitude Mandatory User lattitude
	 *  @apiParam {Number} longitude Mandatory User longitude
	 *  @apiParam {Number} longitude Mandatory User longitude
	 *  @apiParam {String} timeline Mandatory Possible Values [ upcoming or past ]
	 *  @apiHeader {String} accept application/json
	 *  @apiHeader {String} Content-Type application/json
	 *  @apiHeader {Number} X-Auth-Date Current Epoch Date
	 *  @apiHeader {String} Authorization Authentication Token
	 *  @apiHeaderExample {json} Example Headers
	 *  accept: application/json
		Content-Type: application/json
		X-Auth-Date: 1455988523724
		Authorization: Basic U0R+U01BUlRfREVWSUNFXzI6NCtPU3JRN0tKMzZ2TW9iRmoxbmJEZG5ydVVJVTlwTWFVWmN1V0xxaUFaRT0=
	 *  @apiSuccess (Success - OK 200) {Object}  response  Response.
	 *  @apiSuccess (Success - OK 200) {String}  response.status   Eg.Success.
	 * 	@apiSuccess (Success - OK 200) {Object}  response.data User SociallBox Activities
	 *  @apiSuccessExample {json} Success-Response:
	 *  
		Sample Reponse for Upcoming Activities :
		{
			"status": "Success",
			"data": [
				{
					"detail": {
						"event": {
							"title": "Puma Flat 40% Off",
							"description": "<p>Puma is a global athletic brand which has enabled one to catch the attention of fine folks. Its athletes have been setting world records, making game-winning goals and just plain dominating – all while looking good doing it. And, wherever you may be, they have you covered. From football and running, to golf and sailing - they bring the most innovative technology and stylish designs to the field, the track, the course, and the deck. And thats just the tip of the iceberg. PUMA starts in Sport and ends in the Fashion. Its Sport performance and Lifestyle labels include categories such as Football, Running, Motorsports, Golf, and Sailing.<br/>More at : <a href="http://in.puma.com/sale.html" >http://in.puma.com/sale.html</a><br/></p><p><br/><br/></p>",
							"tags": [
								{
									"id": 3,
									"name": "fashion",
									"description": "Fashion"
								},
								{
									"id": 1,
									"name": "shopping",
									"description": "Shopping"
								},
								{
									"id": 2,
									"name": "sale",
									"description": "Sale"
								}
							],
							"id": "4028918853e672830153e6aec4230000",
							"distance_from_src": "14 Kms",
							"event_detail": {
								"location": {
									"lattitude": 28.483705,
									"longitude": 77.107372,
									"name": "Select Citywalk, New-Delhi, Delhi, India",
									"locality": "Saket"
								},
								"organizer": {
									"name": "Remix Entertainment",
									"address": {
										"street": "Mandakini Enclave",
										"city": "New Delhi",
										"state": "Delhi",
										"country": "India",
										"zip_code": "110019"
									},
									"phone1": "+91 7838250407",
									"phone2": "",
									"phone3": "",
									"id": "4028918b53adfe990153ae0134100000",
									"email_id": "harsh.singh@remixentertainments.com",
									"profile_pic": null,
									"cover_pic": null,
									"website": "http://www.remixentertainments.com"
								},
								"booking_url": null
							},
							"start_date": "Sun, 1 May 2016 12:30 PM",
							"end_date": "Sun, 2 Oct 2016 08:30 PM",
							"is_user_fav": false,
							"is_user_going": false,
							"display_image": {
								"id": 11,
								"name": "puma.jpg",
								"url": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/events/4028918853e672830153e6aec4230000/puma.jpg",
								"displayOrder": 1,
								"mimeType": null
							},
							"status": "LIVE",
							"is_free": false
						},
						"type": "GOING"
					},
					"type": "EVENT",
					"time": "Sun, 1 May 2016 12:30 PM"
				},
				{
					"detail": {
						"event": {
							"title": "Songdew Media Nights - Live Music",
							"description": "Songdew invites you to a night of rock music with some of the most sought after bands in the country. The energy and enthusiasm of the music is sure to make your heart pump. Dont miss the opportunity to be a part of the excitement.",
							"tags": [
								{
									"id": 5,
									"name": "rock",
									"description": "Rock"
								},
								{
									"id": 16,
									"name": "bollywood",
									"description": "Bollywood"
								},
								{
									"id": 22,
									"name": "family",
									"description": "Family"
								},
								{
									"id": 8,
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
									"name": "Remix Entertainment",
									"address": {
										"street": "Mandakini Enclave",
										"city": "New Delhi",
										"state": "Delhi",
										"country": "India",
										"zip_code": "110019"
									},
									"phone1": "+91 7838250407",
									"phone2": "",
									"phone3": "",
									"id": "4028918b53adfe990153ae0134100000",
									"email_id": "harsh.singh@remixentertainments.com",
									"profile_pic": null,
									"cover_pic": null,
									"website": "http://www.remixentertainments.com"
								},
								"booking_url": null
							},
							"start_date": "Tue, 12 Apr 2016 06:30 PM",
							"end_date": "Tue, 12 Apr 2016 10:30 PM",
							"is_user_fav": false,
							"is_user_going": false,
							"display_image": {
								"id": 3,
								"name": "songdew.jpg",
								"url": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/events/2c9f8ff353bd8bf50153bdaf23470002/songdew.jpg",
								"displayOrder": 1,
								"mimeType": null
							},
							"status": "LIVE",
							"is_free": false
						},
						"type": "GOING"
					},
					"type": "EVENT",
					"time": "Tue, 12 Apr 2016 06:30 PM"
				},
				{
					"detail": {
						"isOrganizer": true,
						"meetup": {
							"description": "Meetup at event desc",
							"id": "4028918a53f6b6c30153f6b705870000",
							"title": "Meetup at event",
							"location": {
								"lattitude": 77.06347870000002,
								"longitude": 28.4682917,
								"name": "Kalkaji, New Delhi, Delhi, India",
								"locality": "Kalkaji"
							},
							"start_date": "Fri, 1 Jul 2016 12:30 AM",
							"end_date": "Mon, 1 Aug 2016 12:30 PM",
							"organizer": {
								"id": 17,
								"name": "Ankit Joinwal",
								"social_details": [
									{
										"system": "FACEBOOK",
										"detail": "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xla1/v/t1.0-1/p200x200/12316467_10206731945876364_3008257792416820623_n.jpg?oh=cece300cd2db2d885c81f2c00b6a7d84&oe=578A9FB4&__gda__=1465178287_36f0dafbe70beb7506ebd22f8f089edf",
										"detailType": "USER_PROFILE_PIC"
									}
								]
							},
							"status": "CREATED",
							"meetup_access_url": null,
							"display_pic": null,
							"event_at_meetup": "2c9f8ff353bd8bf50153bd9ea0a10000",
							"user_actions": []
						}
					},
					"type": "MEETUP",
					"time": "Fri, 8 Apr 2016 10:01 PM"
				},
				{
					"detail": {
						"event": {
							"title": "Smaaash Cyberhub",
							"description": "<p><b>General Offer: Play 9 Games at Smaaash Gurgaon @ Rs 555 AI (Monday to Thursday) and @ Rs 888 AI (Friday to Sunday)11am to 11pm</b><br/>- Finger Coaster<br/>- Super Keeper (5 shots)<br/>- Vulcan Force<br/>- Harley Simulator<br/>- King of Hammer<br/>- Speed of Light<br/>- Nitro Wheelie<br/>- Pacman Basket<br/>- Over the Top<br/><br/><b>College Student Offer: Rs. 699 per student</b><br/>Valid from Monday to Thursday<br/>Not Valid Without College Student ID card<br/>Valid between 11 AM and 5 PM<br/>Which gives Single Access to College Students to all Games at Smaaash Gurgaon along with Bowling and Cricket.<br/><br/><b>Rs. 399- All days, Games combo. Any 3 Simulation Games or Any 3 Virtual Reality Games. Any 3 Arcade Games. 5 Shots at SupeerKeeper</b><br/></p>",
							"tags": [
								{
									"id": 10,
									"name": "hhour",
									"description": "HappyHours"
								},
								{
									"id": 22,
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
									"name": "Remix Entertainment",
									"address": {
										"street": "Mandakini Enclave",
										"city": "New Delhi",
										"state": "Delhi",
										"country": "India",
										"zip_code": "110019"
									},
									"phone1": "+91 7838250407",
									"phone2": "",
									"phone3": "",
									"id": "4028918b53adfe990153ae0134100000",
									"email_id": "harsh.singh@remixentertainments.com",
									"profile_pic": null,
									"cover_pic": null,
									"website": "http://www.remixentertainments.com"
								},
								"booking_url": null
							},
							"start_date": "Tue, 1 Mar 2016 12:30 PM",
							"end_date": "Mon, 2 May 2016 08:30 PM",
							"is_user_fav": false,
							"is_user_going": false,
							"display_image": {
								"id": 1,
								"name": "smash.jpg",
								"url": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/events/2c9f8ff353bd8bf50153bd9ea0a10000/smash.jpg",
								"displayOrder": 1,
								"mimeType": null
							},
							"status": "LIVE",
							"is_free": false
						},
						"type": "GOING"
					},
					"type": "EVENT",
					"time": "Tue, 1 Mar 2016 12:30 PM"
				}
			],
			"page": null,
			"nextPage": null,
			"total_records": null
		}
		Sample Response for Past Activities -
		{
			"status": "Success",
			"data": [
				{
					"detail": {
						"event": {
							"title": "Arijit Singh Live in Concert",
							"description": "<p>Carnival Media presents the nation's absolute heart-throb, Arijit Singh, with the Grand Symphony live in concert. <br/>The Grand Symphony Orchestra is a 45 -piece band of some of the most talented musicians from across the world. This large instrumental ensemblethat contains Drums, Violin, Grand Piano, Cello, Harp, Flute, French Horns, Trumpet, Viola and much more will be performing in sync with Arijit Singh.<br/>The scintillating and soulful voice behind the hits like Tum hi ho, Muskurane ki vajah and Sooraj Dooba Hai Yaaron will be singing his hits in ways like never before.<br/>This musical extravaganza will be held on four places, i.e. Singapore, Chandigarh, Indore and Srilanka. This will be a ticketed event and entry will be reserved only for Ticket holders.<br/></p>",
							"tags": [
								{
									"id": 5,
									"name": "rock",
									"description": "Rock"
								},
								{
									"id": 16,
									"name": "bollywood",
									"description": "Bollywood"
								},
								{
									"id": 22,
									"name": "family",
									"description": "Family"
								},
								{
									"id": 8,
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
									"name": "Remix Entertainment",
									"address": {
										"street": "Mandakini Enclave",
										"city": "New Delhi",
										"state": "Delhi",
										"country": "India",
										"zip_code": "110019"
									},
									"phone1": "+91 7838250407",
									"phone2": "",
									"phone3": "",
									"id": "4028918b53adfe990153ae0134100000",
									"email_id": "harsh.singh@remixentertainments.com",
									"profile_pic": null,
									"cover_pic": null,
									"website": "http://www.remixentertainments.com"
								},
								"booking_url": null
							},
							"start_date": "Fri, 11 Mar 2016 06:30 PM",
							"end_date": "Fri, 11 Mar 2016 10:30 PM",
							"is_user_fav": false,
							"is_user_going": false,
							"display_image": {
								"id": 2,
								"name": "arjit.jpg",
								"url": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/events/2c9f8ff353bd8bf50153bdab6f440001/arjit.jpg",
								"displayOrder": 1,
								"mimeType": null
							},
							"status": "LIVE",
							"is_free": true
						},
						"type": "INTERESTED"
					},
					"type": "EVENT",
					"time": "Fri, 11 Mar 2016 06:30 PM"
				}
			],
			"page": null,
			"nextPage": null,
			"total_records": null
		}
	 */
	@RequestMapping(value = "/{id}/mybox", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<UserSocialActivity<?>> getMySociallBox(
			@RequestHeader(value = Constants.AUTHORIZATION_HEADER) String auth,
			@PathVariable Long id,
			@RequestParam(value = "type") String type,
			@RequestParam(required = true, value = "lat") String latitude,
			@RequestParam(required = true, value = "lon") String longitude) {

		logRequestStart(MY_SOCIALLBOX_API, SECURED_REQUEST_START_LOG_MESSAGE,
				MY_SOCIALLBOX_API);

		ActivityTime activityTime = UserSocialActivity.ActivityTime
				.getActivityTime(type);
		if (activityTime == null) {
			throw new ClientException(RestErrorCodes.ERR_001,
					ERROR_INVALID_TIMELINE);
		}

		String userName = LoginUtil.getUserNameFromHeader(auth);
		UserTypeBasedOnDevice typeBasedOnDevice = LoginUtil
				.identifyUserType(userName);
		if (typeBasedOnDevice == UserTypeBasedOnDevice.MOBILE) {
			String deviceId = LoginUtil.getDeviceIdFromUserName(userName);
			String location = latitude +","+longitude;
			List<UserSocialActivity<?>> activities = this.mySociallBoxService.getMySociallBox(activityTime, deviceId,location);
			EntityCollectionResponse<UserSocialActivity<?>> collectionResponse = new EntityCollectionResponse<UserSocialActivity<?>>();
			collectionResponse.setStatus(SUCCESS_STATUS);
			collectionResponse.setData(activities);
			collectionResponse.setPage(1);
			collectionResponse.setTotalRecords(activities.size());
			logRequestEnd(MY_SOCIALLBOX_API, MY_SOCIALLBOX_API);
			return collectionResponse;
		} else {
			logRequestEnd(MY_SOCIALLBOX_API, MY_SOCIALLBOX_API);
			throw new ClientException(RestErrorCodes.ERR_003,
					ERROR_FEATURE_AVAILABLE_TO_MOBILE_ONLY);
		}

	}
	
	/**
	 *  @api {get} api/secured/users/:userId/meetups/invites/pending Get My Pending Meetup Invites
	 *  @apiName Get My Pending Meetup Invites
	 *  @apiGroup MySociallBox
	 *  @apiParam {Number} userId Mandatory User Id
	 *  @apiHeader {String} accept application/json
	 *  @apiHeader {String} Content-Type application/json
	 *  @apiHeader {Number} X-Auth-Date Current Epoch Date
	 *  @apiHeader {String} Authorization Authentication Token
	 *  @apiHeaderExample {json} Example Headers
	 *  accept: application/json
		Content-Type: application/json
		X-Auth-Date: 1455988523724
		Authorization: Basic U0R+U01BUlRfREVWSUNFXzI6NCtPU3JRN0tKMzZ2TW9iRmoxbmJEZG5ydVVJVTlwTWFVWmN1V0xxaUFaRT0=
	 *  @apiSuccess (Success - OK 200) {Object}  response  Response.
	 *  @apiSuccess (Success - OK 200) {String}  response.status   Eg.Success.
	 * 	@apiSuccess (Success - OK 200) {Object}  response.data Pending meetups
	 *  @apiSuccessExample {json} Success-Response:
	 *  
		{
			"status": "Success",
			"data": [
				{
					"description": "Meetup at event desc",
					"id": "4028918a53f6b6c30153f6b705870000",
					"title": "Meetup at event",
					"location": {
						"lattitude": 77.06347870000002,
						"longitude": 28.4682917,
						"name": "Kalkaji, New Delhi, Delhi, India",
						"locality": "Kalkaji"
					},
					"start_date": "Fri, 1 Jul 2016 12:30 AM",
					"end_date": "Mon, 1 Aug 2016 12:30 PM",
					"organizer": {
						"id": 17,
						"name": "Ankit Joinwal",
						"social_details": [
							{
								"system": "FACEBOOK",
								"detail": "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xla1/v/t1.0-1/p200x200/12316467_10206731945876364_3008257792416820623_n.jpg?oh=cece300cd2db2d885c81f2c00b6a7d84&oe=578A9FB4&__gda__=1465178287_36f0dafbe70beb7506ebd22f8f089edf",
								"detailType": "USER_PROFILE_PIC"
							}
						]
					},
					"status": "CREATED",
					"meetup_access_url": null,
					"display_pic": {
						"id": 7,
						"name": "disc.jpg",
						"url": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/meetups/4028918a53f6b6c30153f6b705870000/disc.jpg",
						"mimeType": null,
						"isDisplayImage": true,
						"uploadDt": 1460147708000
					},
					"event_at_meetup": "2c9f8ff353bd8bf50153bd9ea0a10000",
					"user_actions": []
				}
			],
			"page": 1,
			"nextPage": null,
			"total_records": null
		}
	 */
	@RequestMapping(value = "/{id}/meetups/invites/pending", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<MeetupResponse> getPendingMeetups(
			@RequestHeader(value = Constants.AUTHORIZATION_HEADER) String auth,
			@PathVariable Long id ){
		logRequestStart(MY_PENDING_MEETUP_INVITE_API, SECURED_REQUEST_START_LOG_MESSAGE, MY_PENDING_MEETUP_INVITE_API);
		
		String userName = LoginUtil.getUserNameFromHeader(auth);
		UserTypeBasedOnDevice typeBasedOnDevice = LoginUtil
				.identifyUserType(userName);
		if (typeBasedOnDevice == UserTypeBasedOnDevice.MOBILE) {
			String deviceId = LoginUtil.getDeviceIdFromUserName(userName);
			List<MeetupResponse> meetups = this.meetupService.getPendingMeetupInvites(deviceId);
			EntityCollectionResponse<MeetupResponse> collectionResponse = new EntityCollectionResponse<MeetupResponse>();
			collectionResponse.setPage(1);
			collectionResponse.setData(meetups);
			collectionResponse.setTotalRecords(meetups.size());
			collectionResponse.setStatus(SUCCESS_STATUS);
			return collectionResponse;
		} else {
			logRequestEnd(MY_PENDING_MEETUP_INVITE_API, MY_PENDING_MEETUP_INVITE_API);
			throw new ClientException(RestErrorCodes.ERR_003,
					ERROR_FEATURE_AVAILABLE_TO_MOBILE_ONLY);
		}
	}
}
