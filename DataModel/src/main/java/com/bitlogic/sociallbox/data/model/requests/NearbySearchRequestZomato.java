package com.bitlogic.sociallbox.data.model.requests;

import java.util.ArrayList;
import java.util.List;

public class NearbySearchRequestZomato extends NearbySearchRequest {
	private static List<SortOption> sortOptions = new ArrayList<>();
	public static final String API_KEY_HEADER_NAME = "user-key";
	private static List<SortOrderOption> sortOrderOptions = new ArrayList<>();

	static {
		SortBy[] values = SortBy.values();
		for (SortBy sortBy : values) {
			if (sortBy != SortBy.DEFAULT) {
				sortOptions.add(new SortOption(sortBy.getValue(), sortBy
						.getDescription()));
			}
		}
		SortOrder[] orderValues = SortOrder.values();
		for (SortOrder sortOrder : orderValues) {
			if (sortOrder != SortOrder.DEFAULT) {
				sortOrderOptions.add(new SortOrderOption(sortOrder.getValue(),
						sortOrder.getDescription()));
			}
		}
	}

	public static List<SortOption> getSortOptions() {
		return sortOptions;
	}

	public static List<SortOrderOption> getSortOrderOptions() {
		return sortOrderOptions;
	}

	public enum SortBy {
		DEFAULT("rating", "Rating"),
		COST("cost", "Price"),
		RATING("rating","Rating"),
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

	public enum SortOrder {
		DEFAULT("desc", "High To Low"),
		HIGH_TO_LOW("desc", "High To Low"), 
		LOW_TO_HIGH("asc", "Low to High");

		private String value, description;

		private SortOrder(String value, String description) {
			this.value = value;
			this.description = description;
		}

		public SortOrder getValueOf(String code){
			for(SortOrder orderBy : SortOrder.values()){
				if(orderBy.getValue().equals(code)){
					return orderBy;
				}
			}
			return DEFAULT;
		}
		public String getValue() {
			return this.value;
		}

		public String getDescription() {
			return description;
		}

	
	}
	
	public static SortBy getSortByUsingCode(String code){
		for(SortBy sortBy : SortBy.values()){
			if(sortBy.getValue().equals(code)){
				return sortBy;
			}
		}
		return SortBy.DEFAULT;
	}
	
	public static SortOrder getSortOrderUsingCode(String code){
		for(SortOrder sortOrder : SortOrder.values()){
			if(sortOrder.getValue().equals(code)){
				return sortOrder;
			}
		}
		return SortOrder.DEFAULT;
	}

	public enum RequestParamNames {
		START("start"),
		COUNT("count"), 
		LATITUDE("lat"), 
		LONGITUDE("lon"), 
		RADIUS("radius"),
		CATEGORY("category"),
		SORT("sort"),
		ORDER("ord");

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

	private String start;
	private String count;
	private String radius;
	private String category;

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getRadius() {
		return radius;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	@Override
	public String toString() {
		return "NearbySearchRequestZomato = [category="+this.getCategory()+
				" , radius="+this.getRadius()+
				" , latitude="+this.getLatitude()+
				" , longitude="+this.getLongitude()+
				" , start="+this.getStart()+
				" , count="+this.getCount()+
				" , sort="+this.getSort()+
				" , order="+this.getOrder()+ " ]";
	}
}
