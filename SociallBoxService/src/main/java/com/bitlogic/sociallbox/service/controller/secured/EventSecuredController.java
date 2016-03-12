package com.bitlogic.sociallbox.service.controller.secured;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.bitlogic.sociallbox.data.model.Event;
import com.bitlogic.sociallbox.data.model.requests.CreateEventRequest;
import com.bitlogic.sociallbox.data.model.response.EventResponse;
import com.bitlogic.sociallbox.data.model.response.SingleEntityResponse;
import com.bitlogic.sociallbox.service.business.EventService;
import com.bitlogic.sociallbox.service.controller.BaseController;
import com.bitlogic.sociallbox.service.exception.ServiceException;
import com.bitlogic.sociallbox.service.transformers.Transformer;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory.Transformer_Types;

@RestController
@RequestMapping("/api/secured/events")
public class EventSecuredController extends BaseController implements Constants{

	private static final Logger logger = LoggerFactory.getLogger(EventSecuredController.class);
	private static final String CREATE_EVENT_API = "CreateEvent API";
	private static final String UPLOAD_IMAGE_TO_EVENT_API = "UploadImageToEvent API";
	private static final String MAKE_EVENT_LIVE_API = "MakeEventLive API";
	
	@Override
	public Logger getLogger() {
		return logger;
	}
	
	@Autowired
	private EventService eventService;
	
	@RequestMapping(method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public SingleEntityResponse<EventResponse> create(@RequestHeader(value=Constants.AUTHORIZATION_HEADER)String auth,@Valid @RequestBody CreateEventRequest createEventRequest) throws ServiceException{
		logRequestStart(CREATE_EVENT_API, SECURED_REQUEST_START_LOG_MESSAGE, CREATE_EVENT_API);
		
		logInfo(CREATE_EVENT_API, "Auth header = {}", auth);
		logInfo(CREATE_EVENT_API, "Request = {}", createEventRequest);
		Transformer<EventResponse, Event> transformer = (Transformer<EventResponse, Event>) TransformerFactory.getTransformer(Transformer_Types.EVENT_TRANS);
		EventResponse createEventResponse = transformer.transform(eventService.create(createEventRequest));
		SingleEntityResponse<EventResponse> entityResponse = new SingleEntityResponse<>();
		entityResponse.setData(createEventResponse);
		entityResponse.setStatus(SUCCESS_STATUS);
		logRequestEnd(CREATE_EVENT_API, CREATE_EVENT_API);
		return entityResponse;
	}
	
	@RequestMapping(value = "/{eventId}/images", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
    public void upload(@RequestHeader(value=Constants.AUTHORIZATION_HEADER) String auth,@PathVariable String eventId,MultipartHttpServletRequest request,
                  HttpServletResponse response)  {
		logRequestStart(UPLOAD_IMAGE_TO_EVENT_API, SECURED_REQUEST_START_LOG_MESSAGE, UPLOAD_IMAGE_TO_EVENT_API);
		logInfo(UPLOAD_IMAGE_TO_EVENT_API, "Auth header = {}", auth);
		logInfo(UPLOAD_IMAGE_TO_EVENT_API, "Event id = {}", eventId);
		logger.info("Request recieved to upload event images for event {} ",eventId);
		String imagesURL = request.getRequestURL()+"";
		logInfo(UPLOAD_IMAGE_TO_EVENT_API, "Image url = {}", imagesURL);
		Map<String, MultipartFile> fileMap = request.getFileMap();
          if(fileMap.values()!=null && !fileMap.values().isEmpty()){
        	  List<MultipartFile> files = new ArrayList<MultipartFile>(fileMap.values());
        	  this.eventService.storeEventImages(imagesURL,files, eventId);
          }
         logRequestEnd(UPLOAD_IMAGE_TO_EVENT_API, UPLOAD_IMAGE_TO_EVENT_API);
    }
	
	@RequestMapping(value="/{eventId}",method = RequestMethod.PUT, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public void makeEventLive(@RequestHeader(value=Constants.AUTHORIZATION_HEADER) String auth,@PathVariable String eventId){
		logRequestStart(MAKE_EVENT_LIVE_API, SECURED_REQUEST_START_LOG_MESSAGE, MAKE_EVENT_LIVE_API);
		logInfo(MAKE_EVENT_LIVE_API, "Auth header = {}", auth);
		logInfo(MAKE_EVENT_LIVE_API, "Event id ", eventId);
		logger.info("### Request Recieved - make Event Live ###");
		this.eventService.makeEventLive(eventId);
		logRequestEnd(MAKE_EVENT_LIVE_API, MAKE_EVENT_LIVE_API);
	}
	
}
