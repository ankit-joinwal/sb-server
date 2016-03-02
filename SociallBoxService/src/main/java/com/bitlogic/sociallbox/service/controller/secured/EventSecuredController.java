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
import com.bitlogic.sociallbox.service.exception.ServiceException;
import com.bitlogic.sociallbox.service.transformers.Transformer;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory.Transformer_Types;

@RestController
@RequestMapping("/api/secured/events")
public class EventSecuredController implements Constants{

	private static final Logger logger = LoggerFactory.getLogger(EventSecuredController.class);
	
	@Autowired
	private EventService eventService;
	
	@RequestMapping(method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public SingleEntityResponse<EventResponse> create(@Valid @RequestBody CreateEventRequest createEventRequest) throws ServiceException{
		logger.info("### Request recieved - create event {} ",createEventRequest) ;
		Transformer<EventResponse, Event> transformer = (Transformer<EventResponse, Event>) TransformerFactory.getTransformer(Transformer_Types.EVENT_TRANS);
		EventResponse createEventResponse = transformer.transform(eventService.create(createEventRequest));
		SingleEntityResponse<EventResponse> entityResponse = new SingleEntityResponse<>();
		entityResponse.setData(createEventResponse);
		entityResponse.setStatus(SUCCESS_STATUS);
		return entityResponse;
	}
	
	@RequestMapping(value = "/{eventId}/images", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
    public void upload(@PathVariable String eventId,MultipartHttpServletRequest request,
                  HttpServletResponse response)  {

		logger.info("Request recieved to upload event images for event {} ",eventId);
		String imagesURL = request.getRequestURL()+"";
		Map<String, MultipartFile> fileMap = request.getFileMap();
          if(fileMap.values()!=null && !fileMap.values().isEmpty()){
        	  List<MultipartFile> files = new ArrayList<MultipartFile>(fileMap.values());
        	  this.eventService.storeEventImages(imagesURL,files, eventId);
          }
          
    }
	
	@RequestMapping(value="/{eventId}",method = RequestMethod.PUT, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public void makeEventLive(@PathVariable String eventId){
		logger.info("### Request Recieved - make Event Live ###");
		this.eventService.makeEventLive(eventId);
	}
	
}
