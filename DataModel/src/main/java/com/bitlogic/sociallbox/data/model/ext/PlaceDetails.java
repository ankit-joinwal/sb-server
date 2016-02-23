package com.bitlogic.sociallbox.data.model.ext;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings(value = "unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	private String status;
	private Result result;
	
	
	
	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public static final class Result {
		@JsonProperty("formatted_address")
		private String formattedAddress;

		@JsonProperty("formatted_phone_number")
		private String formattedPhoneNumber;

		private String icon;

		private String id;

		@JsonProperty("international_phone_number")
		private String interNationalPhone;

		private String name;

		@JsonProperty("place_id")
		private String placeId;

		private Long rating;

		private Geometry geometry;

		@JsonProperty("address_components")
		private List<AddressComponent> addressComponents;
		
		public Geometry getGeometry() {
			return geometry;
		}

		public void setGeometry(Geometry geometry) {
			this.geometry = geometry;
		}
		
		@JsonIgnoreProperties(ignoreUnknown = true)
		public static final class AddressComponent{
			@JsonProperty("short_name")
			private String shortName;
			@JsonProperty("long_name")
			private String longName;
			@JsonProperty("types")
			private List<String> types;
			public String getShortName() {
				return shortName;
			}
			public void setShortName(String shortName) {
				this.shortName = shortName;
			}
			public String getLongName() {
				return longName;
			}
			public void setLongName(String longName) {
				this.longName = longName;
			}
			public List<String> getTypes() {
				return types;
			}
			public void setTypes(List<String> types) {
				this.types = types;
			}
			
			
		}

		// "geometry" : {
		@JsonIgnoreProperties(ignoreUnknown = true)
		private static final class Geometry {
			private Location location;

			public Location getLocation() {
				return location;
			}

			public void setLocation(Location location) {
				this.location = location;
			}

			// "location" :{
			@JsonIgnoreProperties(ignoreUnknown = true)
			private static final class Location {
				@JsonProperty(value = "lat")
				private double lattitude;
				@JsonProperty(value = "lng")
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

			}// End of "location"
		}// End of "geometry"

		private List<Review> reviews;
		private String[] types;
		private String url;
		private String vicinity;
		private String website;
		@JsonProperty("opening_hours")
		private OpeningHours openingHours;
		
		
		public OpeningHours getOpeningHours() {
			return openingHours;
		}

		public void setOpeningHours(OpeningHours openingHours) {
			this.openingHours = openingHours;
		}

		@JsonIgnoreProperties(ignoreUnknown=true)
		private static final class OpeningHours{
			@JsonProperty("open_now")
			private Boolean openNow;
			@JsonProperty("weekday_text")
			private String[] weekdayText;
			public Boolean getOpenNow() {
				return openNow;
			}
			public void setOpenNow(Boolean openNow) {
				this.openNow = openNow;
			}
			public String[] getWeekdayText() {
				return weekdayText;
			}
			public void setWeekdayText(String[] weekdayText) {
				this.weekdayText = weekdayText;
			}
			
		}

		// "reviews"
		@JsonIgnoreProperties(ignoreUnknown = true)
		private static final class Review {
			private List<Aspect> aspects;
			@JsonProperty("author_name")
			private String authorName;
			@JsonProperty("author_url")
			private String authorUrl;
			private Long rating;
			@JsonProperty("time")
			private Long submittedTime;
			private String text;

			public String getText() {
				return text;
			}

			public void setText(String text) {
				this.text = text;
			}

			public List<Aspect> getAspects() {
				return aspects;
			}

			public void setAspects(List<Aspect> aspects) {
				this.aspects = aspects;
			}

			public String getAuthorName() {
				return authorName;
			}

			public void setAuthorName(String authorName) {
				this.authorName = authorName;
			}

			public String getAuthorUrl() {
				return authorUrl;
			}

			public void setAuthorUrl(String authorUrl) {
				this.authorUrl = authorUrl;
			}

			public Long getRating() {
				return rating==null ? 0L : rating;
			}

			public void setRating(Long rating) {
				this.rating = rating;
			}

			public Long getSubmittedTime() {
				return submittedTime;
			}

			public void setSubmittedTime(Long submittedTime) {
				this.submittedTime = submittedTime;
			}

			@JsonIgnoreProperties(ignoreUnknown = true)
			private static final class Aspect {
				private String type;
				private String rating;

				public String getType() {
					return type;
				}

				public void setType(String type) {
					this.type = type;
				}

				public String getRating() {
					return rating;
				}

				public void setRating(String rating) {
					this.rating = rating;
				}

			}
		}

		public String getFormattedAddress() {
			return formattedAddress;
		}

		public void setFormattedAddress(String formattedAddress) {
			this.formattedAddress = formattedAddress;
		}

		public String getFormattedPhoneNumber() {
			return formattedPhoneNumber;
		}

		public void setFormattedPhoneNumber(String formattedPhoneNumber) {
			this.formattedPhoneNumber = formattedPhoneNumber;
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

		public String getInterNationalPhone() {
			return interNationalPhone;
		}

		public void setInterNationalPhone(String interNationalPhone) {
			this.interNationalPhone = interNationalPhone;
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

		public Long getRating() {
			return rating;
		}

		public void setRating(Long rating) {
			this.rating = rating;
		}

		public List<Review> getReviews() {
			return reviews;
		}

		public void setReviews(List<Review> reviews) {
			this.reviews = reviews;
		}

		public String[] getTypes() {
			return types;
		}

		public void setTypes(String[] types) {
			this.types = types;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getVicinity() {
			return vicinity;
		}

		public void setVicinity(String vicinity) {
			this.vicinity = vicinity;
		}

		public String getWebsite() {
			return website;
		}

		public void setWebsite(String website) {
			this.website = website;
		}
	}
}
