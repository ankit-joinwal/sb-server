package com.bitlogic.sociallbox.data.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("sortOption")
public class SortOption {

	public SortOption() {
		super();
	}
	
	public SortOption(String code,String description){
		this.code = code;
		this.description = description;
	}
	
	@JsonProperty("code")
	private String code;
	@JsonProperty("description")
	private String description;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
