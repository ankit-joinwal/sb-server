package com.bitlogic.sociallbox.data.model.ext.zomato;

import java.util.List;

import com.bitlogic.sociallbox.data.model.ext.Place;
import com.bitlogic.sociallbox.data.model.ext.Places;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings(value = "unused")
public class ZomatoPlaces extends Places {

	@JsonProperty("results_found")
	private Integer resultsFound;

	@JsonProperty("results_start")
	private Integer resultsStart;

	@JsonProperty("results_shown")
	private Integer resultsShown;

	private List<Restaurant> restaurants;

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Restaurant extends Place {

		@JsonProperty("restaurant")
		private RestaurantData restaurantData;

		public RestaurantData getRestaurantData() {
			if(restaurantData == null){
				return new RestaurantData();
			}
			return restaurantData;
		}

		public void setRestaurantData(RestaurantData restaurantData) {
			this.restaurantData = restaurantData;
		}

		@JsonIgnoreProperties(ignoreUnknown = true)
		public static final class RestaurantData {
			private String id;
			private String name;
			private String url;
			private String cuisines;
			@JsonProperty("average_cost_for_two")
			private Integer averageCostForTwo;
			private Location location;
			@JsonProperty("price_range")
			private Integer priceRange;
			private String currency;
			private Integer costPerPerson;
			@JsonProperty("featured_image")
			private String featuredImage;
			@JsonProperty("phone_numbers")
			private String phoneNumbers;
			@JsonProperty("all_reviews")
			private Reviews allReviews;
			@JsonProperty("user_rating")
			private UserRating userRating;
			private List<Photo> photos;
			@JsonProperty("distance_from_you")
			private String distanceFromYou;
			
			
			@JsonIgnoreProperties(ignoreUnknown = true)
			public static class Location {
				private String address;
				private String locality;
				private String city;
				private String latitude;
				private String longitude;

				public String getAddress() {
					return address;
				}

				public void setAddress(String address) {
					this.address = address;
				}

				public String getLocality() {
					return locality;
				}

				public void setLocality(String locality) {
					this.locality = locality;
				}

				public String getCity() {
					return city;
				}

				public void setCity(String city) {
					this.city = city;
				}

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

			}

			@JsonIgnoreProperties(ignoreUnknown = true)
			private static final class UserRating {

				@JsonProperty("aggregate_rating")
				private String aggregateRating;
				@JsonProperty("rating_text")
				private String ratingText;
				private String votes;

				public String getAggregateRating() {
					return aggregateRating;
				}

				public void setAggregateRating(String aggregateRating) {
					this.aggregateRating = aggregateRating;
				}

				public String getRatingText() {
					return ratingText;
				}

				public void setRatingText(String ratingText) {
					this.ratingText = ratingText;
				}

				public String getVotes() {
					return votes;
				}

				public void setVotes(String votes) {
					this.votes = votes;
				}

			}

			@JsonIgnoreProperties(ignoreUnknown = true)
			private static final class Photo {

				@JsonProperty("photo")
				private PhotoData photoData;

				public PhotoData getPhotoData() {
					return photoData;
				}

				public void setPhotoData(PhotoData photoData) {
					this.photoData = photoData;
				}
				
				@JsonIgnoreProperties(ignoreUnknown=true)
				private static final class PhotoData {
					private String id;
					private String url;
					@JsonProperty("thumb_url")
					private String thumbUrl;
					private Integer width;
					private Integer height;

					public String getId() {
						return id;
					}

					public void setId(String id) {
						this.id = id;
					}

					public String getUrl() {
						return url;
					}

					public void setUrl(String url) {
						this.url = url;
					}

					public String getThumbUrl() {
						return thumbUrl;
					}

					public void setThumbUrl(String thumbUrl) {
						this.thumbUrl = thumbUrl;
					}

					public Integer getWidth() {
						return width;
					}

					public void setWidth(Integer width) {
						this.width = width;
					}

					public Integer getHeight() {
						return height;
					}

					public void setHeight(Integer height) {
						this.height = height;
					}

				}
			}

			@JsonIgnoreProperties(ignoreUnknown = true)
			private static final class Reviews {

				private List<Review> reviews;

				public List<Review> getReviews() {
					return reviews;
				}

				public void setReviews(List<Review> reviews) {
					this.reviews = reviews;
				}

				@JsonIgnoreProperties(ignoreUnknown = true)
				private static final class Review {

					@JsonProperty("review")
					private ReviewData reviewData;
					
					public ReviewData getReviewData() {
						return reviewData;
					}

					public void setReviewData(ReviewData reviewData) {
						this.reviewData = reviewData;
					}

					@JsonIgnoreProperties(ignoreUnknown=true)
					private static final class ReviewData {
						private Integer rating;
						@JsonProperty("rating_text")
						private String ratingText;
						@JsonProperty("review_text")
						private String reviewText;

						@JsonIgnoreProperties(ignoreUnknown = true)
						private static final class User {
							private String name;
							@JsonProperty("profile_image")
							private String profileImage;

							public String getName() {
								return name;
							}

							public void setName(String name) {
								this.name = name;
							}

							public String getProfileImage() {
								return profileImage;
							}

							public void setProfileImage(String profileImage) {
								this.profileImage = profileImage;
							}

						}

						public Integer getRating() {
							return rating;
						}

						public void setRating(Integer rating) {
							this.rating = rating;
						}

						public String getRatingText() {
							return ratingText;
						}

						public void setRatingText(String ratingText) {
							this.ratingText = ratingText;
						}

						public String getReviewText() {
							return reviewText;
						}

						public void setReviewText(String reviewText) {
							this.reviewText = reviewText;
						}
					}
				}
			}
			
			
			public String getDistanceFromYou() {
				return distanceFromYou;
			}

			public void setDistanceFromYou(String distanceFromYou) {
				this.distanceFromYou = distanceFromYou;
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

			public String getUrl() {
				return url;
			}

			public void setUrl(String url) {
				this.url = url;
			}

			public String getCuisines() {
				return cuisines;
			}

			public void setCuisines(String cuisines) {
				this.cuisines = cuisines;
			}

			public Integer getAverageCostForTwo() {
				return averageCostForTwo;
			}

			public void setAverageCostForTwo(Integer averageCostForTwo) {
				this.averageCostForTwo = averageCostForTwo;
			}

			public Integer getCostPerPerson() {

				return costPerPerson;
			}

			public void setCostPerPerson(Integer costPerPerson) {
				if (averageCostForTwo != null) {
					this.costPerPerson = averageCostForTwo / 2;
				}
			}

			public Location getLocation() {
				if(location==null){
					location = new Location();
				}
				return location;
			}

			public void setLocation(Location location) {
				this.location = location;
			}

			public Integer getPriceRange() {
				return priceRange;
			}

			public void setPriceRange(Integer priceRange) {
				this.priceRange = priceRange;
			}

			public String getCurrency() {
				return currency;
			}

			public void setCurrency(String currency) {
				this.currency = currency;
			}

			public String getFeaturedImage() {
				return featuredImage;
			}

			public void setFeaturedImage(String featuredImage) {
				this.featuredImage = featuredImage;
			}

			public String getPhoneNumbers() {
				return phoneNumbers;
			}

			public void setPhoneNumbers(String phoneNumbers) {
				this.phoneNumbers = phoneNumbers;
			}

			public Reviews getAllReviews() {
				return allReviews;
			}

			public void setAllReviews(Reviews allReviews) {
				this.allReviews = allReviews;
			}

			public UserRating getUserRating() {
				return userRating;
			}

			public void setUserRating(UserRating userRating) {
				this.userRating = userRating;
			}

			public List<Photo> getPhotos() {
				return photos;
			}

			public void setPhotos(List<Photo> photos) {
				this.photos = photos;
			}
		}
	}

	public Integer getResultsFound() {
		return resultsFound;
	}

	public void setResultsFound(Integer resultsFound) {
		this.resultsFound = resultsFound;
	}

	public Integer getResultsStart() {
		return resultsStart;
	}

	public void setResultsStart(Integer resultsStart) {
		this.resultsStart = resultsStart;
	}

	public Integer getResultsShown() {
		return resultsShown;
	}

	public void setResultsShown(Integer resultsShown) {
		this.resultsShown = resultsShown;
	}

	public List<Restaurant> getRestaurants() {
		return restaurants;
	}

	public void setRestaurants(List<Restaurant> restaurants) {
		this.restaurants = restaurants;
	}

}
