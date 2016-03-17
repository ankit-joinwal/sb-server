package com.bitlogic.sociallbox.data.model.requests;

public class PlaceDetailsRequestGoogle extends PlaceDetailsRequest{

	public enum PlaceDetailsRequestParams{
		PLACEID("placeid");
		private String name;
		
		PlaceDetailsRequestParams(String name){
			this.name = name;
		}
		public String getName() {
			return name;
		}
	}
	private String placeId;
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	
}
