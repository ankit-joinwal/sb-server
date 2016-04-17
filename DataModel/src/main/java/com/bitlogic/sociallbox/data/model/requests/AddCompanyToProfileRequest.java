package com.bitlogic.sociallbox.data.model.requests;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="add_company_request")
public class AddCompanyToProfileRequest{

	@JsonIgnore
	private Long userId;
	
	@NotNull(message="error.company.mandatory")
	@JsonProperty("event_organizer")
	private CreateEventOrganizerRequest createEventOrganizerRequest;

	@NotNull(message="error.is.existing.mandatory")
	@JsonProperty("is_existing_company")
	private Boolean isExistingCompany;

	
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public CreateEventOrganizerRequest getCreateEventOrganizerRequest() {
		return createEventOrganizerRequest;
	}

	public void setCreateEventOrganizerRequest(
			CreateEventOrganizerRequest createEventOrganizerRequest) {
		this.createEventOrganizerRequest = createEventOrganizerRequest;
	}

	public Boolean getIsExistingCompany() {
		return isExistingCompany;
	}

	public void setIsExistingCompany(Boolean existingCompany) {
		this.isExistingCompany = existingCompany;
	}
	
	@Override
	public String toString() {
		return "AddCompanyToProfileRequest = [userId = "+this.userId + 
				" , event_organizer = [ " + this.createEventOrganizerRequest +
				"] , existingCompany = "+this.isExistingCompany + " ]";
	}
}
