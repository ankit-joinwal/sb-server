package com.bitlogic.sociallbox.data.model;

/**
 * Google API Configuration
 * @author ajoinwal
 *
 */
public class GAPIConfig extends APIConfig{

	private String nearBySearchURL;
	private String textSearchURL;
	private String placeDetailsURL;
	private String dataExchangeFormat;
	private String gapiKey;
	private String placePhotoGetAPI ;
	private String placePhotoGoogleAPI;
	private String gcmServerURL;
	
	
	public String getPlacePhotoGoogleAPI() {
		return placePhotoGoogleAPI;
	}

	public void setPlacePhotoGoogleAPI(String placePhotoGoogleAPI) {
		this.placePhotoGoogleAPI = placePhotoGoogleAPI;
	}

	public String getPlacePhotoGetAPI() {
		return placePhotoGetAPI;
	}

	public void setPlacePhotoGetAPI(String placePhotoGetAPI) {
		this.placePhotoGetAPI = placePhotoGetAPI;
	}

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

	public String getGcmServerURL() {
		return gcmServerURL;
	}

	public void setGcmServerURL(String gcmServerURL) {
		this.gcmServerURL = gcmServerURL;
	}
	
}
