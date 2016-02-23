package com.bitlogic.sociallbox.data.model;

/**
 * Google API Configuration
 * @author ajoinwal
 *
 */
public class GAPIConfig {

	private String nearBySearchURL;
	private String textSearchURL;
	private String placeDetailsURL;
	private String dataExchangeFormat;
	private String gapiKey;
	
	
	public String getPlaceDetailsURL() {
		return placeDetailsURL;
	}

	public void setPlaceDetailsURL(String placeDetailsURL) {
		this.placeDetailsURL = placeDetailsURL;
	}

	public String getTextSearchURL() {
		return textSearchURL;
	}

	public void setTextSearchURL(String textSearchURL) {
		this.textSearchURL = textSearchURL;
	}

	public String getGapiKey() {
		return gapiKey;
	}

	public void setGapiKey(String gapiKey) {
		this.gapiKey = gapiKey;
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
