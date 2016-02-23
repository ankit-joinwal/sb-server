package com.bitlogic.sociallbox.service.business;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.EventTag;

public interface EventTagService {

	@PreAuthorize("hasRole('"+Constants.ROLE_TYPE_ADMIN+"')")
	public EventTag create(EventTag eventTag);
	
	public List<EventTag> getAll();
}
