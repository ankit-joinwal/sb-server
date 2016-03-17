package com.bitlogic.sociallbox.data.model.ext.google;

import java.io.Serializable;
import java.util.List;

import com.bitlogic.sociallbox.data.model.ext.Places;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings(value="unused")
@JsonIgnoreProperties(ignoreUnknown=true)
public class GooglePlaces extends Places {

	private List<Result> results;
	private String status;
	@JsonProperty("next_page_token")
	private String nexPageToken;
	private Integer totalRecords;
	
	public String getNexPageToken() {
		return nexPageToken;
	}

	public void setNexPageToken(String nexPageToken) {
		this.nexPageToken = nexPageToken;
	}

	//{
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static final class Result{
		private Geometry geometry;
		public Geometry getGeometry() {
			return geometry;
		}
		public void setGeometry(Geometry geometry) {
			this.geometry = geometry;
		}
		//"geometry" : {
		@JsonIgnoreProperties(ignoreUnknown=true)
		public static final class Geometry{
			private Location location;
			public Location getLocation() {
				return location;
			}
			public void setLocation(Location location) {
				this.location = location;
			}
			//"location" :{
			@JsonIgnoreProperties(ignoreUnknown=true)
			public static final class Location{
				@JsonProperty(value="lat")
				private double lattitude;
				@JsonProperty(value="lng")
				private double longitude;
				
				public double getLattitude() {
					return lattitude;
				}
				public void setLattitude(double lattitude) {
					this.lattitude = lattitude;
				}
				public double getLongitude() {
					return longitude;
				}
				public void setLongitude(double longitude) {
					this.longitude = longitude;
				}
				
				
			}//End of "location"
		}//End of "geometry"
		//"opening hours"
		@JsonIgnoreProperties(ignoreUnknown=true)
		private static final class OpeningHours{
			@JsonProperty("open_now")
			private Boolean openNow;

			public Boolean getOpenNow() {
				return openNow;
			}

			public void setOpenNow(Boolean openNow) {
				this.openNow = openNow;
			}
			
			
		}
		private String icon;
		private String id;
		private String name;
		@JsonProperty("place_id")
		private String placeId;
		private String rating;
		private String reference;
		private String scope;
		private String[] types;
		private String vicinity;
		private Photo[] photos;
		@JsonProperty("distance_from_you")
		private String distanceFromYou;
		@JsonProperty("opening_hours")
		private OpeningHours openingHours;
		
		public OpeningHours getOpeningHours() {
			return openingHours;
		}
		public void setOpeningHours(OpeningHours openingHours) {
			this.openingHours = openingHours;
		}
		public String getRating() {
			return rating;
		}
		public void setRating(String rating) {
			this.rating = rating;
		}
		@JsonIgnoreProperties(ignoreUnknown=true)
		public static final class Photo{
			@JsonProperty("photo_reference")
			private String photoReference;
			private Integer height;
			private Integer width;
			private String url;
			
			public String getUrl() {
				return url;
			}
			public void setUrl(String url) {
				this.url = url;
			}
			public String getPhotoReference() {
				return photoReference;
			}
			public void setPhotoReference(String photoReference) {
				this.photoReference = photoReference;
			}
			public Integer getHeight() {
				return height;
			}
			public void setHeight(Integer height) {
				this.height = height;
			}
			public Integer getWidth() {
				return width;
			}
			public void setWidth(Integer width) {
				this.width = width;
			}
			
		}
		
		
		public String getDistanceFromYou() {
			return distanceFromYou;
		}
		public void setDistanceFromYou(String distanceFromYou) {
			this.distanceFromYou = distanceFromYou;
		}
		public Photo[] getPhotos() {
			return photos;
		}
		public void setPhotos(Photo[] photos) {
			this.photos = photos;
		}
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPlaceId() {
			return placeId;
		}
		public void setPlaceId(String placeId) {
			this.placeId = placeId;
		}
		public String getReference() {
			return reference;
		}
		public void setReference(String reference) {
			this.reference = reference;
		}
		public String getScope() {
			return scope;
		}
		public void setScope(String scope) {
			this.scope = scope;
		}
		public String[] getTypes() {
			return types;
		}
		public void setTypes(String[] types) {
			this.types = types;
		}
		public String getVicinity() {
			return vicinity;
		}
		public void setVicinity(String vicinity) {
			this.vicinity = vicinity;
		}
		@Override
		public String toString() {
			return "PlaceSearchResult [name ="+this.name+ " , placeId = "+this.placeId+" ]";
		}
	}
	
	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

	
}
