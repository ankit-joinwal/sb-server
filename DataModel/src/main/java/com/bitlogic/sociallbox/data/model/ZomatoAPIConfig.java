package com.bitlogic.sociallbox.data.model;

/**
 * Zomato API Configuration
 * @author ajoinwal
 *
 */
public class ZomatoAPIConfig extends APIConfig{

	private String nearBySearchURL;
	private String placeDetailsURL;
	private String dataExchangeFormat;
	private String apiKey;
	
	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getPlaceDetailsURL() {
		return placeDetailsURL;
	}

	public void setPlaceDetailsURL(String placeDetailsURL) {
		this.placeDetailsURL = placeDetailsURL;
	}

	public String getDataExchangeFormat() {
		return dataExchangeFormat;
	}

	public void setDataExchangeFormat(String dataExchangeFormat) {
		this.dataExchangeFormat = dataExchangeFormat;
	}

	public String getNearBySearchURL() {
		return nearBySearchURL;
	}

	public void setNearBySearchURL(String nearBySearchURL) {
		this.nearBySearchURL = nearBySearchURL;
	}
	
}
