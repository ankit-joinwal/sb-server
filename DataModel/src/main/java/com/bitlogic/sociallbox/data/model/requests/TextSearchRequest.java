package com.bitlogic.sociallbox.data.model.requests;


public class TextSearchRequest {
	public enum TextSearchRequestParamNames{
		QUERY("query"),LOCATION("location"),RADIUS("radius"),NAME("name"),TYPES("types"),RANKBY("rankby"),KEY("key");
		String name;
		TextSearchRequestParamNames(String name){
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
	private String query;
	private String location;
	private String radius;
	private String name;
	private String types;
	private String rankBy;
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
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	@Override
	public String toString() {
		return "TextSearchRequest = [query = "+this.query+" , location = "+ this.location+ " , radius = " + this.radius +
				" , name = "+ this.name + " , types = "+this.types + " , rankBy = "+ this.rankBy + " ]";
	}
	
}
