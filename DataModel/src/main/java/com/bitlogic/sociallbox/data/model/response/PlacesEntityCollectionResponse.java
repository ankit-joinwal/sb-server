package com.bitlogic.sociallbox.data.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlacesEntityCollectionResponse<T> extends EntityCollectionResponse<T> {

	private static final long serialVersionUID = 1L;
	@JsonProperty("source")
	private String sourceSystem;
	public String getSourceSystem() {
		return sourceSystem;
	}
	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}
	
	
}
