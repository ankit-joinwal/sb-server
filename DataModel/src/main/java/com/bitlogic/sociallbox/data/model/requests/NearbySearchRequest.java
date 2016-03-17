package com.bitlogic.sociallbox.data.model.requests;


public class NearbySearchRequest {
	
	
	private Long categoryId;
	private String pageNumber;
	private String radius;
	private String latitude;
	private String longitude;
	private String externalId;
	private String sort;
	private String order;
	
	
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getRadius() {
		return radius;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}

	public String getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
	@Override
	public String toString() {
		return "NearbySearchRequest [categoryId="+this.getCategoryId()+
				" , radius="+this.getRadius()+
				" , pageNum="+this.getPageNumber()+
				" , externalId="+this.getExternalId()+
				" , latitude="+this.getLatitude()+
				" , longitude="+this.getLongitude()+
				" , sort="+this.getSort()+
				" , order="+this.getOrder()+ " ]";
	}
}
