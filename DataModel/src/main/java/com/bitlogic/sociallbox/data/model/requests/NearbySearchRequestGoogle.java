package com.bitlogic.sociallbox.data.model.requests;

import java.util.ArrayList;
import java.util.List;

public class NearbySearchRequestGoogle extends NearbySearchRequest {
	private static List<SortOption> sortOptions = new ArrayList<>();
	static {
		SortBy[] values = SortBy.values();
		for (SortBy sortBy : values) {
			if (sortBy != SortBy.DEFAULT) {
				sortOptions.add(new SortOption(sortBy.getValue(), sortBy
						.getDescription()));
			}
		}

	}

	public enum SortBy {
		DEFAULT("prominence", "Rating"),
		RATING("prominence", "Rating"), 
		DISTANCE("real_distance", "Distance");

		private String value;
		private String description;

		private SortBy(String value, String description) {
			this.value = value;
			this.description = description;
		}

		public String getValue() {
			return this.value;
		}

		public String getDescription() {
			return description;
		}

	}

	public static List<SortOption> getSortOptions() {
		return sortOptions;
	}

	public static SortBy getSortByUsingCode(String code){
		for(SortBy sortBy : SortBy.values()){
			if(sortBy.getValue().equals(code)){
				return sortBy;
			}
		}
		return SortBy.DEFAULT;
	}
	
	public enum RequestParamNames {
		LOCATION("location"), 
		RADIUS("radius"),
		NAME("name"),
		TYPES("type"),
		RANKBY("rankby"), 
		KEY("key"), 
		PAGE_TOKEN("pagetoken"),
		OPEN_NOW("opennow"),
		PHOTO_REFERENCE("photoreference");
		String name;

		RequestParamNames(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}

	private String location;

	private String types;
	private String rankBy;
	private String pageToken;

	public String getPageToken() {
		return pageToken;
	}

	public void setPageToken(String pageToken) {
		this.pageToken = pageToken;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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
		return "NearbySearchRequest = [location = " + this.location
				+ " , radius = " + this.getRadius() 
				+ " , categoryId = " + this.getCategoryId() + " , types = "
				+ this.types + " , rankBy = " + this.rankBy + " ,pagetoken="
				+ this.pageToken + " ]";
	}

}
