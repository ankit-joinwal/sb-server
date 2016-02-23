package com.bitlogic.sociallbox.data.model.requests;


public class NearbySearchRequest {
	public enum NearbySearchRequestParamNames{
		LOCATION("location"),RADIUS("radius"),NAME("name"),TYPES("types"),RANKBY("rankby"),KEY("key");
		String name;
		
		NearbySearchRequestParamNames(String name){
			this.name = name;
		}
		public String getName(){
			return this.name;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
	}
	private String location;
	private String radius;
	private String name;
	private Long categoryId;
	private String types;
	private String rankBy;
	
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getRadius() {
		return radius;
	}
	public void setRadius(String radius) {
		this.radius = radius;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTypes() {
		return types;
	}
	public void setTypes(String types) {
		this.types = types;
	}
	public String getRankBy() {
		return rankBy;
	}
	public void setRankBy(String rankBy) {
		this.rankBy = rankBy;
	}
	
	@Override
	public String toString() {
		return "NearbySearchRequest = [location = "+ this.location+ " , radius = " + this.radius +
				" , name = "+ this.name + " , categoryId = "+this.categoryId+" , types = "+this.types + " , rankBy = "+ this.rankBy + " ]";
	}
	
}
