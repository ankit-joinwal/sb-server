package com.bitlogic.sociallbox.data.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name="response")
public class SingleEntityResponse<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty
	private String status;
	
	@JsonProperty
	private T data;

	public String getStatus() {
		return status;
	}

	public void setStatus(String result) {
		this.status = result;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	
	
	
}
