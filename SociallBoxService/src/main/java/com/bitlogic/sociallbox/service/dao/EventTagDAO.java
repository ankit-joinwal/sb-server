package com.bitlogic.sociallbox.service.dao;

import java.util.List;

import com.bitlogic.sociallbox.data.model.EventTag;

public interface EventTagDAO {

	public EventTag create(EventTag eventTag);
	
	public List<EventTag> getAll();
	
	public List<EventTag> getTagsByNames(List<String> names);
	
	public List<EventTag> getUserTags(Long userId);
	
	public List<Long> getUserTagIds(Long userId);
	
	public List<Long> getRetailTagIdsForUser(Long userId);
	 
	public List<Long> getAllTagIds();
	
	public List<Long> getAllRetailTagIds();
	
	public List<EventTag> getAllRetailTag();
	
	public List<EventTag> getRetailTagsForUser(Long userId);
}
