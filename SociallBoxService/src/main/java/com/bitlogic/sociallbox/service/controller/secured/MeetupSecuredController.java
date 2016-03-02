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
import com.bitlogic.sociallbox.data.model.response.SingleEntityResponse;
import com.bitlogic.sociallbox.service.business.MeetupService;
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
public class MeetupSecuredController implements Constants{

	private static final Logger logger = LoggerFactory.getLogger(MeetupSecuredController.class);
	
	@Autowired
	private MeetupService meetupService;
	
	@RequestMapping(method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public SingleEntityResponse<MeetupResponse> create(@Valid @RequestBody CreateMeetupRequest createMeetupRequest,HttpServletRequest  httpRequest) throws ServiceException{
		logger.info("### Request recieved- CreateMeetupRequest:  {} ###"+createMeetupRequest);
		logger.info("Request URL : {} ",httpRequest.getRequestURL());
		logger.info("Context Path : {} ",httpRequest.getContextPath());
		Transformer<MeetupResponse, Meetup> transformer = (Transformer<MeetupResponse, Meetup>) TransformerFactory.getTransformer(Transformer_Types.MEETUP_TRANS);
		MeetupResponse createMeetupResponse = transformer.transform(meetupService.createMetup(createMeetupRequest));
		createMeetupResponse.setUrl(httpRequest.getRequestURL()+"/"+createMeetupResponse.getUuid());
		SingleEntityResponse<MeetupResponse> entityResponse = new SingleEntityResponse<>();
		entityResponse.setData(createMeetupResponse);
		entityResponse.setStatus(SUCCESS_STATUS);
				
		return entityResponse;
		
	}
	
	@RequestMapping(value = "/{meetupId}/images", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
    public void upload(@PathVariable String meetupId,
    				@RequestHeader("Authorization") String authorization,
    				MultipartHttpServletRequest request,
    				HttpServletResponse response){
		logger.info("Request recieved to upload images for meetup {} ",meetupId);
		if(authorization==null){
			throw new UnauthorizedException(RestErrorCodes.ERR_002,ERROR_USER_INVALID);
		}
		
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
		
		
	}
	
	
	
	@RequestMapping(value="/{meetupId}",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<MeetupResponse> getMeetup(@PathVariable String meetupId) throws ServiceException{
		logger.info("### Request recieved- getMeetup : {} ###",meetupId);
		Transformer<MeetupResponse, Meetup> transformer = (Transformer<MeetupResponse, Meetup>) TransformerFactory.getTransformer(Transformer_Types.MEETUP_TRANS);
		MeetupResponse createMeetupResponse = transformer.transform(meetupService.getMeetup(meetupId));
		SingleEntityResponse<MeetupResponse> entityResponse = new SingleEntityResponse<>();
		entityResponse.setData(createMeetupResponse);
		entityResponse.setStatus(SUCCESS_STATUS);
		return entityResponse;
		
	}
	
	@RequestMapping(value="/{meetupId}/attendees",method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public SingleEntityResponse<MeetupResponse> addAttendees(@Valid @RequestBody AddMeetupAttendeesRequest editMeetupRequest,@PathVariable String meetupId) throws ServiceException{
		logger.info("### Request recieved- editMeetup : {} ###",editMeetupRequest) ;
		Transformer<MeetupResponse, Meetup> transformer = (Transformer<MeetupResponse, Meetup>) TransformerFactory.getTransformer(Transformer_Types.MEETUP_TRANS);
		
		editMeetupRequest.setMeetupId(meetupId);
		meetupService.addAttendees(editMeetupRequest);

		SingleEntityResponse<MeetupResponse> entityResponse = new SingleEntityResponse<>();
		entityResponse.setStatus(SUCCESS_STATUS);
		return entityResponse;
	}
	
	@RequestMapping(value="/{meetupId}/attendees/{attendeeId}/response",method = RequestMethod.POST,  consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public void saveResponse(@Valid @RequestBody SaveAttendeeResponse saveAttendeeResponse,@PathVariable String meetupId,@PathVariable Long attendeeId){
		logger.info("### Request recieved- SaveAttendeeResponse : {} for meetup {} , attendee {} ###",saveAttendeeResponse,meetupId,attendeeId);
		saveAttendeeResponse.setAttendeeId(attendeeId);
		saveAttendeeResponse.setMeetupId(meetupId);
		this.meetupService.saveAttendeeResponse(saveAttendeeResponse);
	}
	
	
	@RequestMapping(value="/{meetupId}/attendees/{userSocialId}/message",method = RequestMethod.POST,  consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public void createMesssage(@Valid @RequestBody MeetupMessage meetupMessage,@PathVariable String meetupId,@PathVariable String userSocialId){
		logger.info("### Request recieved- SendMessage : {} for meetup {} , user social id {} ###",meetupMessage,meetupId,userSocialId);
		this.meetupService.sendMessageInMeetup(meetupMessage, meetupId, userSocialId);
	}
	
	@RequestMapping(value="/{meetupId}/messages",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public Set<MeetupMessage> getMeetupMessages(@PathVariable String meetupId) throws ServiceException{
		Transformer<MeetupResponse, Meetup> transformer = (Transformer<MeetupResponse, Meetup>) TransformerFactory.getTransformer(Transformer_Types.MEETUP_TRANS);
		MeetupResponse createMeetupResponse = transformer.transform(meetupService.getMeetup(meetupId));
		return createMeetupResponse.getMessages();
	}
	
}
