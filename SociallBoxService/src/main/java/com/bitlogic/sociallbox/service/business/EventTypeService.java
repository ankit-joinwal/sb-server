package com.bitlogic.sociallbox.service.business;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.EventType;

public interface EventTypeService {

	@PreAuthorize("hasRole('"+Constants.ROLE_TYPE_ADMIN+"')")
	public EventType createEventType(EventType eventType);
	
	public List<EventType> getAllEventTypes();
	
	public List<EventType> getAllEventTypesExceptShopping();
}
