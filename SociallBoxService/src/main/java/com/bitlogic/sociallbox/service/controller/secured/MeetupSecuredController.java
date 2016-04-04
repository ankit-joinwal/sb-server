package com.bitlogic.sociallbox.service.controller.secured;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.Meetup;
import com.bitlogic.sociallbox.data.model.MeetupMessage;
import com.bitlogic.sociallbox.data.model.UserTypeBasedOnDevice;
import com.bitlogic.sociallbox.data.model.requests.CreateMeetupRequest;
import com.bitlogic.sociallbox.data.model.requests.AddMeetupAttendeesRequest;
import com.bitlogic.sociallbox.data.model.requests.MeetupResponse;
import com.bitlogic.sociallbox.data.model.requests.SaveAttendeeResponse;
import com.bitlogic.sociallbox.data.model.response.EntityCollectionResponse;
import com.bitlogic.sociallbox.data.model.response.SingleEntityResponse;
import com.bitlogic.sociallbox.service.business.MeetupService;
import com.bitlogic.sociallbox.service.controller.BaseController;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.exception.ServiceException;
import com.bitlogic.sociallbox.service.exception.UnauthorizedException;
import com.bitlogic.sociallbox.service.transformers.Transformer;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory.Transformer_Types;
import com.bitlogic.sociallbox.service.utils.LoginUtil;

@RestController
@RequestMapping("/api/secured/meetups")
public class MeetupSecuredController extends BaseController implements Constants{

	private static final Logger logger = LoggerFactory.getLogger(MeetupSecuredController.class);
	private static final String CREATE_MEETUP_API = "CreateMeetup API";
	private static final String UPLOAD_MEETUP_IMAGE_API = "UploadMeetupImage API";
	private static final String GET_MEETUP_API = "GetMeetup API";
	private static final String ADD_ATTENDEES_API = "AddAttendees API";
	private static final String SAVE_ATTENDEE_RESPONSE_API = "SaveResponse API";
	private static final String POST_MESSAGE_TO_MEETUP_API = "PostMessageToMeetup API";
	private static final String GET_MEETUP_MESSAGES_API = "GetMeetupMessages API";
	
	@Override
	public Logger getLogger() {
		return logger;
	}
	
	@Autowired
	private MeetupService meetupService;
	
	@RequestMapping(method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public SingleEntityResponse<MeetupResponse> create(@RequestHeader(value=Constants.AUTHORIZATION_HEADER)String auth,@Valid @RequestBody CreateMeetupRequest createMeetupRequest,HttpServletRequest  httpRequest) throws ServiceException{
		logRequestStart(CREATE_MEETUP_API, SECURED_REQUEST_START_LOG_MESSAGE, CREATE_MEETUP_API);
		logInfo(CREATE_MEETUP_API, "Auth header = {}", auth);
		logInfo(CREATE_MEETUP_API, "Request = {}", createMeetupRequest);
		String userName = LoginUtil.getUserNameFromHeader(auth);
		UserTypeBasedOnDevice typeBasedOnDevice = LoginUtil.identifyUserType(userName);
		if(typeBasedOnDevice==UserTypeBasedOnDevice.MOBILE){
			String deviceId = LoginUtil.getDeviceIdFromUserName(userName);
			logInfo(CREATE_MEETUP_API, " Device Id {} ", deviceId);
			createMeetupRequest.setDeviceId(deviceId);
			Transformer<MeetupResponse, Meetup> transformer = (Transformer<MeetupResponse, Meetup>) TransformerFactory.getTransformer(Transformer_Types.MEETUP_TRANS);
			MeetupResponse createMeetupResponse = transformer.transform(meetupService.createMetup(createMeetupRequest));
			createMeetupResponse.setUrl(httpRequest.getRequestURL()+"/"+createMeetupResponse.getUuid());
			SingleEntityResponse<MeetupResponse> entityResponse = new SingleEntityResponse<>();
			entityResponse.setData(createMeetupResponse);
			entityResponse.setStatus(SUCCESS_STATUS);
			logRequestEnd(CREATE_MEETUP_API, CREATE_MEETUP_API);
			return entityResponse;
			
		}else{
			
			throw new ClientException(RestErrorCodes.ERR_003,ERROR_FEATURE_AVAILABLE_TO_MOBILE_ONLY);
		}
		
		
	}
	
	@RequestMapping(value = "/{meetupId}/images", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
    public void upload(@PathVariable String meetupId,
    				@RequestHeader(value=Constants.AUTHORIZATION_HEADER) String authorization,
    				MultipartHttpServletRequest request,
    				HttpServletResponse response){
		
		logRequestStart(UPLOAD_MEETUP_IMAGE_API, SECURED_REQUEST_START_LOG_MESSAGE, UPLOAD_MEETUP_IMAGE_API);
		logInfo(UPLOAD_MEETUP_IMAGE_API, "Auth header = {}", authorization);
		if(authorization==null){
			throw new UnauthorizedException(RestErrorCodes.ERR_002,ERROR_USER_INVALID);
		}
		logInfo(UPLOAD_MEETUP_IMAGE_API, "Meetup id = {}", meetupId);
		String userName = LoginUtil.getUserNameFromHeader(authorization);
		UserTypeBasedOnDevice typeBasedOnDevice = LoginUtil.identifyUserType(userName);
		if(typeBasedOnDevice==UserTypeBasedOnDevice.MOBILE){
			String deviceId = LoginUtil.getDeviceIdFromUserName(userName);
			String imagesURL = request.getRequestURL()+"";
			Map<String, MultipartFile> fileMap = request.getFileMap();
	        if(fileMap.values()!=null && !fileMap.values().isEmpty()){
	      	  List<MultipartFile> files = new ArrayList<MultipartFile>(fileMap.values());
	      	  this.meetupService.uploadImageToMeetup(deviceId,imagesURL,files, meetupId);
	        }
		}else{
			throw new ClientException(RestErrorCodes.ERR_003,ERROR_FEATURE_AVAILABLE_TO_MOBILE_ONLY);
		}
		
		logRequestEnd(UPLOAD_MEETUP_IMAGE_API, UPLOAD_MEETUP_IMAGE_API);
	}
	
	
	
	@RequestMapping(value="/{meetupId}",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<MeetupResponse> getMeetup(@RequestHeader(value=Constants.AUTHORIZATION_HEADER)String auth,
			@PathVariable String meetupId){
		logRequestStart(GET_MEETUP_API, SECURED_REQUEST_START_LOG_MESSAGE, GET_MEETUP_API);
		logInfo(GET_MEETUP_API, "Auth header = {}", auth);
		logInfo(GET_MEETUP_API, "Meetup id = {}", meetupId);
		Transformer<MeetupResponse, Meetup> transformer = (Transformer<MeetupResponse, Meetup>) TransformerFactory.getTransformer(Transformer_Types.MEETUP_TRANS);
		MeetupResponse createMeetupResponse = transformer.transform(meetupService.getMeetup(meetupId));
		SingleEntityResponse<MeetupResponse> entityResponse = new SingleEntityResponse<>();
		entityResponse.setData(createMeetupResponse);
		entityResponse.setStatus(SUCCESS_STATUS);
		logRequestEnd(GET_MEETUP_API, GET_MEETUP_API);
		return entityResponse;
		
	}
	
	@RequestMapping(value="/{meetupId}/attendees",method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public SingleEntityResponse<MeetupResponse> addAttendees(
			@RequestHeader(value=Constants.AUTHORIZATION_HEADER)String auth,
			@Valid @RequestBody AddMeetupAttendeesRequest editMeetupRequest,
			@PathVariable String meetupId) {
		logRequestStart(ADD_ATTENDEES_API, SECURED_REQUEST_START_LOG_MESSAGE, ADD_ATTENDEES_API);
		logInfo(ADD_ATTENDEES_API, "Auth header = {}", auth);
		logInfo(ADD_ATTENDEES_API, "Request = {}", editMeetupRequest);
		
		editMeetupRequest.setMeetupId(meetupId);
		meetupService.addAttendees(editMeetupRequest);

		SingleEntityResponse<MeetupResponse> entityResponse = new SingleEntityResponse<>();
		entityResponse.setStatus(SUCCESS_STATUS);
		logRequestEnd(ADD_ATTENDEES_API, ADD_ATTENDEES_API);
		return entityResponse;
	}
	
	@RequestMapping(value="/{meetupId}/attendees/{attendeeId}/response",method = RequestMethod.POST,  consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public void saveResponse(@RequestHeader(value=Constants.AUTHORIZATION_HEADER)String auth,
							@Valid @RequestBody SaveAttendeeResponse saveAttendeeResponse,
							@PathVariable String meetupId,
							@PathVariable Long attendeeId){
		logRequestStart(SAVE_ATTENDEE_RESPONSE_API, SECURED_REQUEST_START_LOG_MESSAGE, SAVE_ATTENDEE_RESPONSE_API);
		logInfo(SAVE_ATTENDEE_RESPONSE_API, "Auth header = {}", auth);
		logInfo(SAVE_ATTENDEE_RESPONSE_API, "Meetup id = {}, attendee = {} , response = {} ", meetupId,attendeeId,saveAttendeeResponse);
		saveAttendeeResponse.setAttendeeId(attendeeId);
		saveAttendeeResponse.setMeetupId(meetupId);

		this.meetupService.saveAttendeeResponse(saveAttendeeResponse);
		logRequestEnd(SAVE_ATTENDEE_RESPONSE_API, SAVE_ATTENDEE_RESPONSE_API);
	}
	
	
	@RequestMapping(value="/{meetupId}/attendees/{userSocialId}/message",method = RequestMethod.POST,  consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public void postMessageToMeetup(@RequestHeader(value=Constants.AUTHORIZATION_HEADER)String auth,
									@Valid @RequestBody MeetupMessage meetupMessage,
									@PathVariable String meetupId,
									@PathVariable String userSocialId){
		logRequestStart(POST_MESSAGE_TO_MEETUP_API, SECURED_REQUEST_START_LOG_MESSAGE, POST_MESSAGE_TO_MEETUP_API);
		logInfo(POST_MESSAGE_TO_MEETUP_API, "Auth header = {}", auth);
		logInfo(POST_MESSAGE_TO_MEETUP_API, "Meetup = {} , User id = {} ",meetupId,userSocialId  );
		this.meetupService.sendMessageInMeetup(meetupMessage, meetupId, userSocialId);
		logRequestEnd(POST_MESSAGE_TO_MEETUP_API, POST_MESSAGE_TO_MEETUP_API);
	}
	
	@RequestMapping(value="/{meetupId}/messages",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<MeetupMessage> getMeetupMessages(@RequestHeader(value=Constants.AUTHORIZATION_HEADER)String auth,
												@PathVariable String meetupId,
												@RequestParam(required=false,value="page") Integer page) throws ServiceException{
		logRequestStart(GET_MEETUP_MESSAGES_API, SECURED_REQUEST_START_LOG_MESSAGE, GET_MEETUP_MESSAGES_API);
		logInfo(GET_MEETUP_MESSAGES_API, "Auth header = {} ", auth);
		logInfo(GET_MEETUP_MESSAGES_API, "Meetup id = {}", meetupId);
		if(page==null){
			page = new Integer(1);
		}
		List<MeetupMessage> messages = this.meetupService.getMeetupMessages(meetupId, page);
		
		EntityCollectionResponse<MeetupMessage> collectionResponse = new EntityCollectionResponse<MeetupMessage>();
		collectionResponse.setData(messages);
		collectionResponse.setStatus("Success");
		collectionResponse.setPage(page);
		collectionResponse.setTotalRecords(messages == null ? 0 : messages.size());
		logRequestEnd(GET_MEETUP_MESSAGES_API, GET_MEETUP_MESSAGES_API);
		return collectionResponse;
		
	}
	
}
