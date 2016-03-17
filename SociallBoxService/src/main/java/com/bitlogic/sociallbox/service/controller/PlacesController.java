package com.bitlogic.sociallbox.service.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.GAPIConfig;
import com.bitlogic.sociallbox.data.model.UserTypeBasedOnDevice;
import com.bitlogic.sociallbox.data.model.ext.Place;
import com.bitlogic.sociallbox.data.model.ext.Places;
import com.bitlogic.sociallbox.data.model.ext.google.GooglePlaces;
import com.bitlogic.sociallbox.data.model.ext.google.GooglePlaces.Result;
import com.bitlogic.sociallbox.data.model.ext.zomato.ZomatoPlaces;
import com.bitlogic.sociallbox.data.model.ext.zomato.ZomatoPlaces.Restaurant;
import com.bitlogic.sociallbox.data.model.requests.NearbySearchRequest;
import com.bitlogic.sociallbox.data.model.requests.NearbySearchRequestGoogle;
import com.bitlogic.sociallbox.data.model.requests.PlaceDetailsRequest;
import com.bitlogic.sociallbox.data.model.response.EntityCollectionResponse;
import com.bitlogic.sociallbox.data.model.response.PlacesEntityCollectionResponse;
import com.bitlogic.sociallbox.data.model.response.SingleEntityResponse;
import com.bitlogic.sociallbox.data.model.response.UserFriend;
import com.bitlogic.sociallbox.service.business.PlacesService;
import com.bitlogic.sociallbox.service.business.UserService;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.utils.LoginUtil;

@RestController
@RequestMapping("/api/public/places")
public class PlacesController extends BaseController implements Constants{

	private static final Logger LOGGER = LoggerFactory.getLogger(PlacesController.class);
	private static final String NEARBY_PLACES_REQUEST = "NearbyPlacesRequest API";
	private static final String PLACE_DETAILS_REQUEST_API = "PlaceDetailsRequest API";
	private static final String LIKE_PLACE_REQUEST = "LikePlace API";
	private static final String GET_FRIENDS_WHO_LIKE_PLACE_API = "GetFriendsWhoLikePlace API";
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public Logger getLogger() {
		return LOGGER;
	}
	
	@Autowired
	private GAPIConfig gapiConfig;
	
	@Autowired
	private PlacesService placesService;
	
	/**
	*  @api {get} /api/public/places/nearby?cid=:cid&radius=:radius&page=:page&lat=:lat&lon=:lon&sort=:sort&order=:order Get Nearby Places
	*  @apiName Get Nearby Places
	*  @apiGroup Places
	*  @apiHeader {String} accept application/json
	*  @apiParam {Number} cid Mandatory Category Id to search for
	*  @apiParam {String} lat Mandatory Lattitude of User
	*  @apiParam {String} lon Mandatory Longitude of User
	*  @apiParam {String} radius Optional Radius of search in metres (Default to 5000)
	*  @apiParam {String} page Optional Page Token To search[Will be blank in intial request, then pass previous search response's nextPage token]
	*  @apiParam {String} sort Optional Sort By [Values for Sort by are returned in Get Categories response for each category]
	*  @apiParam {String} order Optional Ignore this for now
	*  @apiSuccess (Success - OK 200) {Object}  response  Response.
	*  @apiSuccess (Success - OK 200) {String}  response.status   OK.
	*  @apiSuccess (Success - OK 200) {String}  response.nextPage This is next page token. Pass this in next request to get next 20 records.
	*  @apiSuccess (Success - OK 200) {String}  response.total_records   Eg. 20
	*  @apiSuccess (Success - OK 200) {String}  response.source Source of Data eg.GOOGLE
	*  @apiSuccess (Success - OK 200) {Object}  response.results Places Results
	*  @apiSuccessExample {json} Success-Response:
	* 
	{
	    "status": "Success",
	    "data": [
	        {
	            "geometry": {
	                "location": {
	                    "lat": 28.5834989,
	                    "lng": 77.22301709999999
	                }
	            },
	            "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png",
	            "id": "49223a21f97edf98b5b5f57ae1af6816b159988c",
	            "name": "Ploof",
	            "rating": "4.2",
	            "reference": "CmRZAAAAD1PzoERVskoSsnTWXQZ89yza_gtZt2W8u_yRnSyLWugn3ArifZqhJGFW0u5bu2wCoaQhi7u8xnBlRgvU6aIyS-Vxk-HoF1Vv3vghli4O0v36_nR_s509-kARUWJ7lIkyEhBevNNcu64KBVQo7McwytrSGhTroTznlFm-5-wLGRxnwZ6lv2qldA",
	            "scope": "GOOGLE",
	            "types": [
	                "restaurant",
	                "food",
	                "point_of_interest",
	                "establishment"
	            ],
	            "vicinity": "13, Lodhi Colony Market, Next to Khubsoorat Salon & Lodhi Sports, New Delhi",
	            "photos": [
	                {
	                    "height": 814,
	                    "width": 545,
	                    "url": "http://localhost:8080/SociallBox/api/public/places/photos/CmRdAAAA2RQBRKJpJLuG1HZsZLjB3Eh9gTyNRhyUzrNm8hVn-tfh-bzXzqmCO9p1NrKTuCCN7XHR15gV3HtSOgauWI1zccPnv2uvIxxELd-VagpYT9aBcIKIstFkMqe6qLAWWMa7EhC5-7C-GDqayi2gcv6acT49GhS3l9ogYS8uZfv8Yg9qarGtENTyTw",
	                    "photo_reference": "CmRdAAAA2RQBRKJpJLuG1HZsZLjB3Eh9gTyNRhyUzrNm8hVn-tfh-bzXzqmCO9p1NrKTuCCN7XHR15gV3HtSOgauWI1zccPnv2uvIxxELd-VagpYT9aBcIKIstFkMqe6qLAWWMa7EhC5-7C-GDqayi2gcv6acT49GhS3l9ogYS8uZfv8Yg9qarGtENTyTw"
	                }
	            ],
	            "place_id": "ChIJq11ZZh3jDDkR6wx3ISmietM",
	            "distance_from_you": "4 Kms",
	            "opening_hours": {
	                "open_now": true
	            }
	        },
	        {
	            "geometry": {
	                "location": {
	                    "lat": 28.5557469,
	                    "lng": 77.1953958
	                }
	            },
	            "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png",
	            "id": "be03b7631af32b2dfdcaa35d707a06071f0e91c5",
	            "name": "The Project At Park Balluchi",
	            "rating": "3.9",
	            "reference": "CnRvAAAAyzkD_BZfJYMiwDX4MipsSiaH6iKmZ8KP12lG4Q5a4UtKNE02fackTqbnFsvNWI8KMTplY2NaTHgKLKb9DxPTuEUSCOArfxg1XDXZ89ckp8PZbnL4sBocSq9zwJ2E7UnlUjNis1wFVAPa635x5G0PsRIQFiy7gpcXQqZ93ZH18ZnIKxoUtm0QRuJKg_V5IQJ3rnUkBDIf2eA",
	            "scope": "GOOGLE",
	            "types": [
	                "restaurant",
	                "food",
	                "point_of_interest",
	                "establishment"
	            ],
	            "vicinity": "Inside Deer Park, Hauz Khas Village, New Delhi",
	            "photos": [
	                {
	                    "height": 768,
	                    "width": 1024,
	                    "url": "http://localhost:8080/SociallBox/api/public/places/photos/CmRdAAAAmz3tWmggfKgg7q-qH8_LHxvH1JimGi6ZOXhMa6zIG1483LV6RXEeB3Ypbz4Nk_AOkH-YPRNO5FkuaefkXNFLMBD_iQWYCNeagkleisfBP0-OT1h-7wacCc9uNuTsF9y3EhA2b2z8LOPgWliOhPXZB-uPGhQQOG5JqR7vUxixEBycyKlonSvECQ",
	                    "photo_reference": "CmRdAAAAmz3tWmggfKgg7q-qH8_LHxvH1JimGi6ZOXhMa6zIG1483LV6RXEeB3Ypbz4Nk_AOkH-YPRNO5FkuaefkXNFLMBD_iQWYCNeagkleisfBP0-OT1h-7wacCc9uNuTsF9y3EhA2b2z8LOPgWliOhPXZB-uPGhQQOG5JqR7vUxixEBycyKlonSvECQ"
	                }
	            ],
	            "place_id": "ChIJVVVVnoodDTkR_KNlS1XDIA4",
	            "distance_from_you": "1 Kms",
	            "opening_hours": {
	                "open_now": true
	            }
	        },
	        {
	            "geometry": {
	                "location": {
	                    "lat": 28.5427287,
	                    "lng": 77.1563585
	                }
	            },
	            "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png",
	            "id": "ae74ce53f5d5491eb605e2ee7fad9ff0f6cabf34",
	            "name": "Smoke House Deli",
	            "rating": "3.7",
	            "reference": "CnRkAAAANZnVL2fQPeY-H-QJ-wfqGySlobs23XeTKy0D7-BQNDqmvmoafAFzVc7DCRzdIsWRPV_sgjoNXCl84tNKBwBXuDFOF04DkGSSgabGLLzlTAguXbOWbWFOPHp5vExjlPXf-uLQJAnrVdadZQy8SAFfdBIQFpnXRuz3q0x-FHHGLzRfYRoUxjQwN915OtZdimUZQH8DD0MBRAE",
	            "scope": "GOOGLE",
	            "types": [
	                "bar",
	                "restaurant",
	                "food",
	                "point_of_interest",
	                "establishment"
	            ],
	            "vicinity": "Shop No 125 GF DLF Promenade Mall, Vasant Kunj",
	            "photos": [
	                {
	                    "height": 900,
	                    "width": 1600,
	                    "url": "http://localhost:8080/SociallBox/api/public/places/photos/CmRdAAAAA0EuWhRvB-Ka8OD8bQw58MkGl8kK4M8OXEh_bRMQd_2kqjC7keTPECh1fReOAvay1S1PFp_oqYG5XcfcJd72uGCXCO-VQG6Q5GF7Luh2c-Hg7dUBYBGZuD1wlECLBBm4EhC6qz_dih8NgmgOkxYsK5atGhQnkj6E2lezEdXRSFRWrMJd7JzL4Q",
	                    "photo_reference": "CmRdAAAAA0EuWhRvB-Ka8OD8bQw58MkGl8kK4M8OXEh_bRMQd_2kqjC7keTPECh1fReOAvay1S1PFp_oqYG5XcfcJd72uGCXCO-VQG6Q5GF7Luh2c-Hg7dUBYBGZuD1wlECLBBm4EhC6qz_dih8NgmgOkxYsK5atGhQnkj6E2lezEdXRSFRWrMJd7JzL4Q"
	                }
	            ],
	            "place_id": "ChIJ9-ciyc4dDTkRK3M4GGPoeOc",
	            "distance_from_you": "0 Kms",
	            "opening_hours": {
	                "open_now": true
	            }
	        },
	        {
	            "geometry": {
	                "location": {
	                    "lat": 28.536649,
	                    "lng": 77.1480311
	                }
	            },
	            "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png",
	            "id": "c2da9be40de99bcb7e6427938836296dca8a3ece",
	            "name": "NANKING",
	            "rating": "4.2",
	            "reference": "CmRbAAAA79cbeCna0kM1AVctayu-yPCpwQ0bzNxTz3UaYOtv5dwxfcb3e2BidTGDY05b9R1yPZA8IFqU-2Od6skIQghnoiDHtzAvc7CAGbQK9Kz-AJlVnj3tvF4fJCBMAjEoXRPEEhDzCuFfLxgXDJ-EeQxPU6sNGhTawGfQHYbnpDfCc49YeNGyiiiKjg",
	            "scope": "GOOGLE",
	            "types": [
	                "restaurant",
	                "food",
	                "point_of_interest",
	                "establishment"
	            ],
	            "vicinity": "Opp Delhi Public School, Sector- C, Vasant Kunj, New Delhi",
	            "photos": [
	                {
	                    "height": 768,
	                    "width": 1024,
	                    "url": "http://localhost:8080/SociallBox/api/public/places/photos/CmRdAAAADJYRUudeA6J5GygCdF1QWNP0kAd680xwKLj4RtZ-r3GH3AruAHHFxhjTMONmXSCf89PSOP6WCRm5ZL1DTLK9Uluzj-Uzau23HjWUOuU-G8z57QSRuJtW_lFequrjqjbAEhA3iXmk-OEQFwFXVp0MN2gGGhTWjaI-5IMJgLNXTyvHUm6j6KrrkQ",
	                    "photo_reference": "CmRdAAAADJYRUudeA6J5GygCdF1QWNP0kAd680xwKLj4RtZ-r3GH3AruAHHFxhjTMONmXSCf89PSOP6WCRm5ZL1DTLK9Uluzj-Uzau23HjWUOuU-G8z57QSRuJtW_lFequrjqjbAEhA3iXmk-OEQFwFXVp0MN2gGGhTWjaI-5IMJgLNXTyvHUm6j6KrrkQ"
	                }
	            ],
	            "place_id": "ChIJCdZIqTIcDTkRuojNsdjMkdA",
	            "distance_from_you": "0 Kms",
	            "opening_hours": {
	                "open_now": true
	            }
	        },
	        {
	            "geometry": {
	                "location": {
	                    "lat": 28.5221749,
	                    "lng": 77.181641
	                }
	            },
	            "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png",
	            "id": "d0869bc142e6a7319be7305b353d66cecfedfe4f",
	            "name": "Thai High",
	            "rating": "3.8",
	            "reference": "CmRdAAAA_m-AZWg4M0y_rq73ILmS1t_MHpDre7KVO1nwR2hl8--x0g2Gw5_sWvrB4aF5AdliUv4Cph36V94X6nSMzAnh53XuBP9LAIB1PA2JyJm3tM0liB_RQm-hCLFoJv6_j_WcEhCvI4ECcG2EQBJ9jfLbGbOmGhTD0YDyLkEM7-JB1Bz6x64t8oAUig",
	            "scope": "GOOGLE",
	            "types": [
	                "bar",
	                "restaurant",
	                "food",
	                "point_of_interest",
	                "establishment"
	            ],
	            "vicinity": "1091/1, Ambawatta Complex, Kalka Das Marg, Mehrauli, New Delhi",
	            "photos": [
	                {
	                    "height": 1190,
	                    "width": 2048,
	                    "url": "http://localhost:8080/SociallBox/api/public/places/photos/CmRdAAAA1BBCIkesx6csIBC51Tbrigd-GAfnXvc63Amrd5WMV6xLrrkL8DKzxh4C2WfNvD6ssiYt8opri4fv3Yruen1l9wQ0QAR_mQ2QQ15kKYHlvMZCYTAch5RmAWQvTPbrjUccEhAZseGW0DhtoMyRWHmZEaDoGhS80BNT94BaZsqsPFB4QqKAvRgqwg",
	                    "photo_reference": "CmRdAAAA1BBCIkesx6csIBC51Tbrigd-GAfnXvc63Amrd5WMV6xLrrkL8DKzxh4C2WfNvD6ssiYt8opri4fv3Yruen1l9wQ0QAR_mQ2QQ15kKYHlvMZCYTAch5RmAWQvTPbrjUccEhAZseGW0DhtoMyRWHmZEaDoGhS80BNT94BaZsqsPFB4QqKAvRgqwg"
	                }
	            ],
	            "place_id": "ChIJJWjGrxEeDTkR5gOdf_tx2s4",
	            "distance_from_you": "2 Kms",
	            "opening_hours": {
	                "open_now": true
	            }
	        },
	        {
	            "geometry": {
	                "location": {
	                    "lat": 28.558374,
	                    "lng": 77.16414
	                }
	            },
	            "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png",
	            "id": "3f7e72654c1955131fcfb5704ef5544c94907a01",
	            "name": "Punjabi By Nature",
	            "rating": "3.6",
	            "reference": "CnRlAAAAsXu64cXyd5a8iyWxdchKULmKRWnjE5ujNuXqxeVUrUl64-XU1imYPdZ9fqizcwXrpwlgYIrj-_0aYOdrDrJHqaxaMzm9aSRJ1u8FOsw3sPX2eNIKK892dCgFhXzmC501cG_HKvzwqoM_tMI0wAL8fRIQqulgqF15z4kk58RaVaWEShoUBRiNJ7VDqF_QfaoIHPVQMLEBaCI",
	            "scope": "GOOGLE",
	            "types": [
	                "restaurant",
	                "food",
	                "point_of_interest",
	                "establishment"
	            ],
	            "vicinity": "T-305, 3rd Floor, Ambience Mall, Nelson Mandela Marg, Vasant Kunj, New Delhi",
	            "photos": [
	                {
	                    "height": 1034,
	                    "width": 1500,
	                    "url": "http://localhost:8080/SociallBox/api/public/places/photos/CmRdAAAAnhX_rMUouXhwcVEbPVWDoY670HAcgP4Zycwbid8LAQOoqMBtKHbFjHgCyVdCIB-k7FjunnCho4Ka5vgUR9igSc4qojeN2WBJ27HjTkYv6uQ5IETb6dsIhwrNv3KL0Lg8EhDWsWEz3bM9y_eUCPVjOmUlGhTFSTaqv1RgtFzxi6SXdvT2KasPgQ",
	                    "photo_reference": "CmRdAAAAnhX_rMUouXhwcVEbPVWDoY670HAcgP4Zycwbid8LAQOoqMBtKHbFjHgCyVdCIB-k7FjunnCho4Ka5vgUR9igSc4qojeN2WBJ27HjTkYv6uQ5IETb6dsIhwrNv3KL0Lg8EhDWsWEz3bM9y_eUCPVjOmUlGhTFSTaqv1RgtFzxi6SXdvT2KasPgQ"
	                }
	            ],
	            "place_id": "ChIJfW7_oLsdDTkRFPEDVG7M5sY",
	            "distance_from_you": "2 Kms",
	            "opening_hours": {
	                "open_now": true
	            }
	        },
	        {
	            "geometry": {
	                "location": {
	                    "lat": 28.5275923,
	                    "lng": 77.2191562
	                }
	            },
	            "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png",
	            "id": "c51fe251d9a7a55711a090cde437afec68ff8eda",
	            "name": "Gulati Spice Market",
	            "rating": "4.1",
	            "reference": "CnRnAAAAuhjd3drerqNRSoVYomDfpEMOKthSvxKIQt_bnW_ctoj3bWwlRPfaXWS5eB7g32FGC7MdDPcSHn8qTw3HSJavQOm0yM4nUlWqXVK6ZNitmG2_8TMZB79OUHCHC5zaTqVbnSeg9gJGcCp5cKdmeNJKyhIQW6NJ7IXelAyB5GicUR9xnBoUaPiWc4kmbWkGnMQQjE8bUDCbWE4",
	            "scope": "GOOGLE",
	            "types": [
	                "meal_takeaway",
	                "restaurant",
	                "food",
	                "point_of_interest",
	                "establishment"
	            ],
	            "vicinity": "Shop No. 18, Ground Floor, Plot No. D2, Southern Park Mall, Behind Select City Walk Mall, Saket, New Delhi",
	            "photos": [
	                {
	                    "height": 1536,
	                    "width": 2048,
	                    "url": "http://localhost:8080/SociallBox/api/public/places/photos/CmRdAAAA3SNBB43i9bSA86Po0En7CE8zNx5g1HoQvq_M7GfITuHeDnSiprCbs2T-okpCz7PqvYKz1UkIG4Ay9kOT-XbR3WL6kQstXajNBXQtNo5WP4nrbFx5-xB9ein4I6G3bnyZEhB9ABi-JGXm1FmfbAtFGcf4GhQBcE8tfF48zS6zvkWYZw_GVt528A",
	                    "photo_reference": "CmRdAAAA3SNBB43i9bSA86Po0En7CE8zNx5g1HoQvq_M7GfITuHeDnSiprCbs2T-okpCz7PqvYKz1UkIG4Ay9kOT-XbR3WL6kQstXajNBXQtNo5WP4nrbFx5-xB9ein4I6G3bnyZEhB9ABi-JGXm1FmfbAtFGcf4GhQBcE8tfF48zS6zvkWYZw_GVt528A"
	                }
	            ],
	            "place_id": "ChIJGSI1H4vhDDkRHbBTrkKE1eI",
	            "distance_from_you": "1 Kms",
	            "opening_hours": {
	                "open_now": true
	            }
	        },
	        {
	            "geometry": {
	                "location": {
	                    "lat": 28.5567227,
	                    "lng": 77.2053088
	                }
	            },
	            "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png",
	            "id": "f1d106a425ae00796db32d9a888c338ddff29619",
	            "name": "Gung The Palace",
	            "rating": "4.2",
	            "reference": "CnRjAAAA4xZHY0jtghZv_XLx4czUdPD7iQMxctvkvSzJLv7bJxC5oW9OyHl0c5Lt6lecK4dEIqSI0y48hTFhe0nbtzlrNLHD-6vt-rlcN7huUZ9aY-2TNoREhfbfCL647VI9oGDZqXmjBlKfqYMwIIvXWJqkdBIQ4kH4v869EXDfbx9fl2__cxoUDfqoyVJK5U3YTFYVLpwFgvEZxHM",
	            "scope": "GOOGLE",
	            "types": [
	                "restaurant",
	                "food",
	                "point_of_interest",
	                "establishment"
	            ],
	            "vicinity": "D 1B, Near Ashirwad Complex, Block D, Green Park, New Delhi",
	            "photos": [
	                {
	                    "height": 3120,
	                    "width": 4160,
	                    "url": "http://localhost:8080/SociallBox/api/public/places/photos/CoQBcwAAAP1nryyZ37Z-X_PvSvE2bQXWTVd-B4VBi-H-wU2Myg5Q1U2cyBwvVIWjeK-NbWF9A5U__QmBOUcBpTe-bRs9ORqv825lYdtQCCrwJMl07UkfN1fs-OfWkefOx_9cQKYMUPexwwsMm_Z2Dli0j4YqVQxtanu211jFyAK33NaoF71lEhC1Lh-Tz7yJxTONkQzb4zAvGhSWaHdakDqCfqqUgomDzlHSELYdug",
	                    "photo_reference": "CoQBcwAAAP1nryyZ37Z-X_PvSvE2bQXWTVd-B4VBi-H-wU2Myg5Q1U2cyBwvVIWjeK-NbWF9A5U__QmBOUcBpTe-bRs9ORqv825lYdtQCCrwJMl07UkfN1fs-OfWkefOx_9cQKYMUPexwwsMm_Z2Dli0j4YqVQxtanu211jFyAK33NaoF71lEhC1Lh-Tz7yJxTONkQzb4zAvGhSWaHdakDqCfqqUgomDzlHSELYdug"
	                }
	            ],
	            "place_id": "ChIJC-xqj23iDDkRW01pk9HAc-A",
	            "distance_from_you": "1 Kms",
	            "opening_hours": {
	                "open_now": true
	            }
	        },
	        {
	            "geometry": {
	                "location": {
	                    "lat": 28.5536047,
	                    "lng": 77.194194
	                }
	            },
	            "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png",
	            "id": "73ebd6bf93031ece4af57e76270ed7e64b5830df",
	            "name": "Yeti",
	            "rating": "4.1",
	            "reference": "CmRXAAAA4sAv4h8BIPvwKtTUDVPkmISKkruVXzJE9CzpUW0RuLJTIhzbuv2qBCJs54oV1NeHnhfY8RioSzuKSshGRnx_mxPNCZbAuIOu1bAMXSMqWjgnFUPOVt0fB64glPUBN_w_EhAW_tcAuwQ7_yPQd9WQ5vxwGhQpCCifN6yEbrKJWeHmu31sm8qr9Q",
	            "scope": "GOOGLE",
	            "types": [
	                "restaurant",
	                "food",
	                "point_of_interest",
	                "establishment"
	            ],
	            "vicinity": "50 A, 2nd Floor, Hauz Khas Village, New Delhi",
	            "photos": [
	                {
	                    "height": 612,
	                    "width": 816,
	                    "url": "http://localhost:8080/SociallBox/api/public/places/photos/CmRdAAAAGOCZfRkNuXpE2L4Y5yUBJS_QDCUZ_bwBdDKWJL8VakZIIAEp87I8wbgKJP63EJzBm3umJuWMq0bj0ui4yC0Nf_AvmehBkUy41I5IqxYA9Yga-OCgDZ3O__pwIeaUwfazEhCmfA7Xs73V1lS3h-lSqKT7GhRbBxoWpIB8MD1cYVQKD2z36TLATg",
	                    "photo_reference": "CmRdAAAAGOCZfRkNuXpE2L4Y5yUBJS_QDCUZ_bwBdDKWJL8VakZIIAEp87I8wbgKJP63EJzBm3umJuWMq0bj0ui4yC0Nf_AvmehBkUy41I5IqxYA9Yga-OCgDZ3O__pwIeaUwfazEhCmfA7Xs73V1lS3h-lSqKT7GhRbBxoWpIB8MD1cYVQKD2z36TLATg"
	                }
	            ],
	            "place_id": "ChIJYcd-oYodDTkRzfhWhXhDtiA",
	            "distance_from_you": "1 Kms",
	            "opening_hours": {
	                "open_now": true
	            }
	        },
	        {
	            "geometry": {
	                "location": {
	                    "lat": 28.5411537,
	                    "lng": 77.15540519999999
	                }
	            },
	            "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png",
	            "id": "953f5482da70c3dcb33a85006d879c09e0287733",
	            "name": "Kylin Premier",
	            "rating": "3.8",
	            "reference": "CmRgAAAANZB09J9FMCbtV_FVyr4o7ulGAkSGTvQMmC1_R260pZj_EXEYtWuDNECqEe62av-nleB85W2LdFM2YkcNH325iAAlzmQoifa5CsXBKmGHYqIOMnZNRn79qGCmyJVJWWRiEhCEVas0llvc5OCrdNtqraybGhSWSDpcpeoLFAsLVDTxnUQE83X0vQ",
	            "scope": "GOOGLE",
	            "types": [
	                "restaurant",
	                "food",
	                "point_of_interest",
	                "establishment"
	            ],
	            "vicinity": "T-302, 3rd Floor, Ambience Mall, Vasant Kunj, Vasant Kunj II, Vasant Kunj, New Delhi",
	            "photos": [
	                {
	                    "height": 2698,
	                    "width": 4888,
	                    "url": "http://localhost:8080/SociallBox/api/public/places/photos/CmRdAAAAFDXcoaUO8m7Bh_5Hpsn1N6zAOSvQRbbLa0aXt3JziW7yv-Ha0y2iCjzU38jET29iT_XdAvfqY2ZzhXwDi7XLcibI6UO1pFLvZjUF0IrHxi6agfopzKlrFpiMAFG1o9xIEhB68RjlzcY9QTl3gC9Hdvp1GhRtoadsPbbs4Ooy7BFeRVnWCSO3Ng",
	                    "photo_reference": "CmRdAAAAFDXcoaUO8m7Bh_5Hpsn1N6zAOSvQRbbLa0aXt3JziW7yv-Ha0y2iCjzU38jET29iT_XdAvfqY2ZzhXwDi7XLcibI6UO1pFLvZjUF0IrHxi6agfopzKlrFpiMAFG1o9xIEhB68RjlzcY9QTl3gC9Hdvp1GhRtoadsPbbs4Ooy7BFeRVnWCSO3Ng"
	                }
	            ],
	            "place_id": "ChIJv8aUm84dDTkRlOs3CN8e3R8",
	            "distance_from_you": "0 Kms",
	            "opening_hours": {
	                "open_now": true
	            }
	        },
	        {
	            "geometry": {
	                "location": {
	                    "lat": 28.5426173,
	                    "lng": 77.15677869999999
	                }
	            },
	            "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png",
	            "id": "98394ca2917f1e03167a9d6b2207cb1b90e865c7",
	            "name": "Kainoosh",
	            "rating": "3.6",
	            "reference": "CmRcAAAAOJfCz-UjaSjBqnRNr_plUIf_302lFQdyILdJ-xDRSquaWL9p-YRzJIAxzrcd7_qMI0yVRg3CCyRdn8a0mj9lvzcp46XlIA0Ak2_JSkxZgEMv_yUZdLQbOTQ_Q_BPN5o_EhDN5i1jNYX953Y4ZUE8-mmrGhS7cKW2qr2FWfuXLwK_njiQoBjU1w",
	            "scope": "GOOGLE",
	            "types": [
	                "restaurant",
	                "food",
	                "point_of_interest",
	                "establishment"
	            ],
	            "vicinity": "122-124, DLF Promenade, Nelson Mandela Marg, Vasant Kunj, New Delhi",
	            "photos": [
	                {
	                    "height": 4160,
	                    "width": 3120,
	                    "url": "http://localhost:8080/SociallBox/api/public/places/photos/CmRdAAAAgCG1VTjVLZEv-JEo-ti223KW0Sr0I3wA8akBHYj8PrBkHKOqHbn3-FWm0Ppk0pWSebGVF1xZtwOTczH2oaGvd9MBiagRKFQ2dV4f-W7bU1yluwMxMTA1ZVRzTK_gGnzpEhDInusy0QsVxIm5yecXLkLGGhSwLlMckIQRYenyLiHcHHdm2uQVXw",
	                    "photo_reference": "CmRdAAAAgCG1VTjVLZEv-JEo-ti223KW0Sr0I3wA8akBHYj8PrBkHKOqHbn3-FWm0Ppk0pWSebGVF1xZtwOTczH2oaGvd9MBiagRKFQ2dV4f-W7bU1yluwMxMTA1ZVRzTK_gGnzpEhDInusy0QsVxIm5yecXLkLGGhSwLlMckIQRYenyLiHcHHdm2uQVXw"
	                }
	            ],
	            "place_id": "ChIJ-Rpu1M4dDTkR5Po1wOFuN-Q",
	            "distance_from_you": "0 Kms",
	            "opening_hours": {
	                "open_now": true
	            }
	        },
	        {
	            "geometry": {
	                "location": {
	                    "lat": 28.5236697,
	                    "lng": 77.20645990000001
	                }
	            },
	            "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/bar-71.png",
	            "id": "eb81ac9ff4c47b4beba8e7b7112579066ca776f7",
	            "name": "Buzz",
	            "rating": "4.6",
	            "reference": "CmRYAAAAe8wxbizsfcIwHfIMxuOSAC29g-gmFE06ZSo6MVjWzu6XhU2IP11Nw2X7EPGBlO6gJZ_MT8SsI_OFtife2NmDV_Mdstbtu4jWIh51PMQ5RvZ2Ca_hoNf028uIJB12rA7FEhA8JMnhW3SGcOxyzu_SRFfhGhT8sjfo97j3_DTVJ2ncPbQ8UjvjCA",
	            "scope": "GOOGLE",
	            "types": [
	                "bar",
	                "restaurant",
	                "food",
	                "point_of_interest",
	                "establishment"
	            ],
	            "vicinity": "17, Commercial Centre, Ist Floor, PVR Complex, Saket, New Delhi",
	            "photos": null,
	            "place_id": "ChIJAQBAk_DhDDkRya0EzOMHEOE",
	            "distance_from_you": "1 Kms",
	            "opening_hours": null
	        },
	        {
	            "geometry": {
	                "location": {
	                    "lat": 28.52721,
	                    "lng": 77.2170872
	                }
	            },
	            "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/cafe-71.png",
	            "id": "5a44fed12849cc3077e218cc32484a8b9afc07a1",
	            "name": "Hard Rock Cafe",
	            "rating": "4.3",
	            "reference": "CnRiAAAASoQUxOGFaD9RybyfrB2-SOMJqTnQVdidXkcoITgP9pglu1-ikcbtZU50I5qHV2Ypl2IKT4rUxJTPdf0i7bXQ7KZz4ylz-iOc98LkH5VGjFzhfvRIFkxei7f29pWti5EMfZzaGCR7brKelwKvxrk1vxIQorr7nuoWvuFxCzFDW4mALBoUZLH4sc05D9YcGThrcUV1TV_1NY4",
	            "scope": "GOOGLE",
	            "types": [
	                "cafe",
	                "bar",
	                "restaurant",
	                "food",
	                "point_of_interest",
	                "establishment"
	            ],
	            "vicinity": "M 110, 1st Floor, DLF Place, Saket, New Delhi",
	            "photos": [
	                {
	                    "height": 2448,
	                    "width": 3264,
	                    "url": "http://localhost:8080/SociallBox/api/public/places/photos/CmRdAAAAsindUXuxDZkSQug3d-OAN98bLoXeiFbZasmRsR5jTVlFc2c2yKtmk3wc-NeHYi2FhdAiEleTHvoYllR3g0ki0tiAwVC9zfU-p-KRYvZg3pAX6wcZ5ONZR_NBsf6PJXA2EhDHl_WWk0SImaBC1cQ0k0SPGhTyNyA3mhtKr35RzTevue2pqjBoMQ",
	                    "photo_reference": "CmRdAAAAsindUXuxDZkSQug3d-OAN98bLoXeiFbZasmRsR5jTVlFc2c2yKtmk3wc-NeHYi2FhdAiEleTHvoYllR3g0ki0tiAwVC9zfU-p-KRYvZg3pAX6wcZ5ONZR_NBsf6PJXA2EhDHl_WWk0SImaBC1cQ0k0SPGhTyNyA3mhtKr35RzTevue2pqjBoMQ"
	                }
	            ],
	            "place_id": "ChIJbQ3c0fThDDkRC7IAMdZhv4w",
	            "distance_from_you": "1 Kms",
	            "opening_hours": {
	                "open_now": true
	            }
	        },
	        {
	            "geometry": {
	                "location": {
	                    "lat": 28.538261,
	                    "lng": 77.2148132
	                }
	            },
	            "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png",
	            "id": "47368a3289956d0dac7a18ce6bc3c9a966116b59",
	            "name": "Karim's Food Corner",
	            "rating": "4",
	            "reference": "CnRnAAAAc3NXGorltHGonE2AY5P_D5Ne6YkK5DLeSMKDsjUBKJMk2kerKHru47p62dDhTElmWN4PiDPnW9C-oa2NTTDoelIQ_rAMb3USOQflBgImnjl1qHRoRZoWGX1sNFVy7JLO6TAGnSRnEH1PoEb1hpKp-xIQOw2EpG49YehCeTSVu15zoBoUI-vN5BhPUT-2YbGv3lJNNugufm4",
	            "scope": "GOOGLE",
	            "types": [
	                "restaurant",
	                "food",
	                "point_of_interest",
	                "establishment"
	            ],
	            "vicinity": "# 40-B,Corner Maket,Malviya Nagar, New Delhi",
	            "photos": [
	                {
	                    "height": 1280,
	                    "width": 960,
	                    "url": "http://localhost:8080/SociallBox/api/public/places/photos/CmRdAAAAw6jmsKwGASeWIuJs89ClW_d0ogo-sevLucdMk4ZEa2-0grftoJ79J97RbqMjMT09VVY-z-IToxQMsMRuAbfj_eubqCN2T8E7AsT0nCtVpNlzOMAGfCtQtTZqjJc9sjQZEhDeB4xa97kl0tpnJy7exR-UGhStlgIAzb50w5ze7BtPkrMKSKBM8w",
	                    "photo_reference": "CmRdAAAAw6jmsKwGASeWIuJs89ClW_d0ogo-sevLucdMk4ZEa2-0grftoJ79J97RbqMjMT09VVY-z-IToxQMsMRuAbfj_eubqCN2T8E7AsT0nCtVpNlzOMAGfCtQtTZqjJc9sjQZEhDeB4xa97kl0tpnJy7exR-UGhStlgIAzb50w5ze7BtPkrMKSKBM8w"
	                }
	            ],
	            "place_id": "ChIJy9Yh3BjiDDkRbmP44cEYNcI",
	            "distance_from_you": "0 Kms",
	            "opening_hours": {
	                "open_now": true
	            }
	        },
	        {
	            "geometry": {
	                "location": {
	                    "lat": 28.5574867,
	                    "lng": 77.1635656
	                }
	            },
	            "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png",
	            "id": "2b1da3d2c2c1dc5d0e2911469659b71369aee4a7",
	            "name": "McDonald's",
	            "rating": "4.1",
	            "reference": "CmRcAAAAixCheOUE_49EyEX9JB6AiUcCSJlpWRVdK78KIQjZNFztxZsQyOlZ2lcUxT9TTqwH4smeyR9fp2Xnyx3LjB6Dvq-iIPsJkjDDi53cufDXT1qLTD9iGIdeQ0P9OI8BRQIxEhDW2tm9G_FQLEQBMr1A73_SGhSq5-M_TNzgHEApgm2Y2UNdfOpE5w",
	            "scope": "GOOGLE",
	            "types": [
	                "restaurant",
	                "food",
	                "point_of_interest",
	                "establishment"
	            ],
	            "vicinity": "Shop No. 47, Basant Lok, Community Centre, Vasant Vihar, New Delhi",
	            "photos": [
	                {
	                    "height": 683,
	                    "width": 1024,
	                    "url": "http://localhost:8080/SociallBox/api/public/places/photos/CmRdAAAAHl-uYM1LzzuY98dKbXQpThjI26B7k4wwtHUa6Z_pQWKPeAjqplz7ZSv4T8f3b7JCRKnjbfRKN283EaO8jDiNejHiKlZhFXos_dygYbEzrsMMjJdrUmyoWzShlAE1FiZbEhB7Od0gmyF-0vqw4RqEwpqvGhROycdC3kZdTEe_W_yW0HdK9mUUEg",
	                    "photo_reference": "CmRdAAAAHl-uYM1LzzuY98dKbXQpThjI26B7k4wwtHUa6Z_pQWKPeAjqplz7ZSv4T8f3b7JCRKnjbfRKN283EaO8jDiNejHiKlZhFXos_dygYbEzrsMMjJdrUmyoWzShlAE1FiZbEhB7Od0gmyF-0vqw4RqEwpqvGhROycdC3kZdTEe_W_yW0HdK9mUUEg"
	                }
	            ],
	            "place_id": "ChIJjRJ0r7sdDTkRQguVsAVKNAA",
	            "distance_from_you": "1 Kms",
	            "opening_hours": {
	                "open_now": true
	            }
	        },
	        {
	            "geometry": {
	                "location": {
	                    "lat": 28.5464051,
	                    "lng": 77.1968469
	                }
	            },
	            "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png",
	            "id": "5e46695256392d9b7899bcab55474f4ba4a3666d",
	            "name": "Pizza Square",
	            "rating": "3.6",
	            "reference": "CmRgAAAAdUwM4gcqM07h55HniMkAC07tj2Dn8nTxdshmhRaFy-EO67BaOV3qSa1e9Fz4EIUOGCQNhlE4BRZN4c4MF01KKpXUB2Vr92RDgvftU8QF-Hcul2AOS5LKlye3ELcPVuENEhDrK9JvY8uT68HH42uoBzelGhQKJ54yZuZLHEk394iOFdmBr15KzA",
	            "scope": "GOOGLE",
	            "types": [
	                "restaurant",
	                "food",
	                "point_of_interest",
	                "establishment"
	            ],
	            "vicinity": "C-14, Community Center, Opp IIT Main Gate, Safdarjung Development Area, New Delhi",
	            "photos": [
	                {
	                    "height": 3264,
	                    "width": 2448,
	                    "url": "http://localhost:8080/SociallBox/api/public/places/photos/CmRdAAAAnNHMpgfZX_carQHm89FnM-4m-cFVNRvgSyHoFcQcJZwVwv2RJXrx_4shKreqHpezKO2NM96hY4jrxThhuILO1_BxTADsasyXD4qhv8RztJWH6uR503SsN3cpCia1MiMpEhDFHU7WkffZ0QbPZWrGq-gwGhSD03oht5KlMivXFFB0UElrVU9q1g",
	                    "photo_reference": "CmRdAAAAnNHMpgfZX_carQHm89FnM-4m-cFVNRvgSyHoFcQcJZwVwv2RJXrx_4shKreqHpezKO2NM96hY4jrxThhuILO1_BxTADsasyXD4qhv8RztJWH6uR503SsN3cpCia1MiMpEhDFHU7WkffZ0QbPZWrGq-gwGhSD03oht5KlMivXFFB0UElrVU9q1g"
	                }
	            ],
	            "place_id": "ChIJ1YB9hgniDDkRRyunEy2RX4k",
	            "distance_from_you": "0 Kms",
	            "opening_hours": {
	                "open_now": true
	            }
	        },
	        {
	            "geometry": {
	                "location": {
	                    "lat": 28.514074,
	                    "lng": 77.19710110000001
	                }
	            },
	            "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png",
	            "id": "72c2a3756d9eb0d2bc956bb1160f357330b4ee76",
	            "name": "Fio",
	            "rating": "3.9",
	            "reference": "CmRXAAAAw_TN5_3yI-pZqTszmWejCCyOMrG-xJ6BQrMEUQsT6D3zvkPB0Ocseylc_1ZL7nTu9czIitaAC9cu1Atv8QBEVPDQePKLjZAsLDD3GtLF_XsyQWCCD_uFiwSMGSKl9rJ2EhCm33Zam0L4sFt99_F2hRDXGhTXkutouqTelb4KVrLC6SfyJ6a0Iw",
	            "scope": "GOOGLE",
	            "types": [
	                "restaurant",
	                "food",
	                "point_of_interest",
	                "establishment"
	            ],
	            "vicinity": "Gate Number 1, Garden of 5 Senses, Near Saket Metro Station, Saidulajab, Mehrauli, New Delhi",
	            "photos": [
	                {
	                    "height": 798,
	                    "width": 1200,
	                    "url": "http://localhost:8080/SociallBox/api/public/places/photos/CmRdAAAAB18cdxOgcc4d_WyPQRCIkYmVNEBjqJ2B2V1Drm92WD0U22dWuVQRJrg8Rsh-Q1-tZycOZuaKLCygEK19YUTEHktCiiiQbJ_56AKhIEGjWye5Z8o8ww919x30LvM_3TxwEhBL1SnK4OjEBkTt9kTxKHzAGhQg8MwOC8huEsAyfRV7isUFFhRLgQ",
	                    "photo_reference": "CmRdAAAAB18cdxOgcc4d_WyPQRCIkYmVNEBjqJ2B2V1Drm92WD0U22dWuVQRJrg8Rsh-Q1-tZycOZuaKLCygEK19YUTEHktCiiiQbJ_56AKhIEGjWye5Z8o8ww919x30LvM_3TxwEhBL1SnK4OjEBkTt9kTxKHzAGhQg8MwOC8huEsAyfRV7isUFFhRLgQ"
	                }
	            ],
	            "place_id": "ChIJBe5iSeLhDDkR_J3vpw0dJ4U",
	            "distance_from_you": "2 Kms",
	            "opening_hours": {
	                "open_now": true
	            }
	        },
	        {
	            "geometry": {
	                "location": {
	                    "lat": 28.5572835,
	                    "lng": 77.164221
	                }
	            },
	            "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png",
	            "id": "baeb57f93b1c7aa7f8663c28263e69f7501d790a",
	            "name": "Arabian Nites",
	            "rating": "4.6",
	            "reference": "CmRgAAAAIfMzUqcOB6Eesv5wicWzoAJMSaUdPD26EDSu8RCeOl4nsgh_f677ONW_uej_Vcnvyb1IHAhgAaQWXB5i8wbQsztAJQ0UTQSNMRC5WGB_JX0w7Mz_EzMmVD1MeEhADQmWEhCEBUq7sYBRoGqIfDRGKh5YGhQNIBZFmKGJcpxnCtz70Q679RP8OQ",
	            "scope": "GOOGLE",
	            "types": [
	                "restaurant",
	                "food",
	                "point_of_interest",
	                "establishment"
	            ],
	            "vicinity": "59, Pvr Priya Complex, Basant Lok, Vasant Vihar, New Delhi",
	            "photos": null,
	            "place_id": "ChIJ____CrAdDTkRrrGaFGFqWHQ",
	            "distance_from_you": "1 Kms",
	            "opening_hours": {
	                "open_now": true
	            }
	        },
	        {
	            "geometry": {
	                "location": {
	                    "lat": 28.5248555,
	                    "lng": 77.1569016
	                }
	            },
	            "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png",
	            "id": "45d0a11c61a6da40b42d9c97719716e0d65e4528",
	            "name": "Sagar Ratna",
	            "rating": "4",
	            "reference": "CmRfAAAA18RbRGpoOtCQ7uIUVO-Qhizo2WQUrELQ6UOHuyvRv8FkkghXo4JQvdT90wJvSnhH5LP_UdMKJsYGGFFgmq3nzd2QEMG7CHNSgWuK2sC0xzTET-tJu_steLJ6Mmeo_3XpEhBCaD02D0E1i4IvR3GsrjuYGhT2Cc8zvLYimpNfo8V11l6qXs6TQA",
	            "scope": "GOOGLE",
	            "types": [
	                "restaurant",
	                "food",
	                "point_of_interest",
	                "establishment"
	            ],
	            "vicinity": "No. 5/5A, Central Market, Masoodpur, Vasant Kunj, New Delhi",
	            "photos": [
	                {
	                    "height": 2988,
	                    "width": 5312,
	                    "url": "http://localhost:8080/SociallBox/api/public/places/photos/CmRdAAAAvVAL8Mysa3hqmnJYqX7g6_ffHFgplhZYhYggR0_BZxt2nMROosaplBxnRsJnX_KvnCqgThI1qMD4dvy9oBnuC6nycJu90uvMfpdB5TsbWfegNVWwIWeEDTc7KM3vTK_tEhDo2EdbIbFTf3Tt-4VQin98GhSeUwxl0FNBp4zlFKNbBNWsbOk5og",
	                    "photo_reference": "CmRdAAAAvVAL8Mysa3hqmnJYqX7g6_ffHFgplhZYhYggR0_BZxt2nMROosaplBxnRsJnX_KvnCqgThI1qMD4dvy9oBnuC6nycJu90uvMfpdB5TsbWfegNVWwIWeEDTc7KM3vTK_tEhDo2EdbIbFTf3Tt-4VQin98GhSeUwxl0FNBp4zlFKNbBNWsbOk5og"
	                }
	            ],
	            "place_id": "ChIJ____v9cdDTkRBN-M7xfVPo0",
	            "distance_from_you": "1 Kms",
	            "opening_hours": {
	                "open_now": true
	            }
	        },
	        {
	            "geometry": {
	                "location": {
	                    "lat": 28.5684387,
	                    "lng": 77.21921909999999
	                }
	            },
	            "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png",
	            "id": "ae2d55a26752672fc3a9c4ae97f6d440a4114e99",
	            "name": "McDonald's",
	            "rating": "3.4",
	            "reference": "CmRdAAAAeqd-83PpC1DVHwytD9n0iedGXLz0IJx-9Tqu9rMtM5656SxL_a7BQoDhxLbEk3sJBJk69KSyyUCD8Z6Hxl9Sr__-p2sdkNwiCkmSuvNKVWoRj98psqhhEv6X1qa7b9OiEhB6Ot4iWLe8SHP3UAAiyN1yGhRH-hVyao3y3tcvTRa09qFTcoflfg",
	            "scope": "GOOGLE",
	            "types": [
	                "restaurant",
	                "food",
	                "point_of_interest",
	                "establishment"
	            ],
	            "vicinity": "E-31& 32, South Extension 2, New Delhi",
	            "photos": [
	                {
	                    "height": 3264,
	                    "width": 2448,
	                    "url": "http://localhost:8080/SociallBox/api/public/places/photos/CmRdAAAABXX27wV7ZuRS-euaENl0U1Y7-QbSY9R5yAfySCYXSBx78hBzgivxnb1M_rDZ4RHvZ56JvQ1m0GnbI2A493eIOEozOOae93aHzmFWbaAWuIyoVq0EIfZ8wHlkYmek5YNDEhBTFR4PYSwV0ks1fTFZDvcHGhQEq9Np6tRlTmdsxbLKhcsjm_Qzkg",
	                    "photo_reference": "CmRdAAAABXX27wV7ZuRS-euaENl0U1Y7-QbSY9R5yAfySCYXSBx78hBzgivxnb1M_rDZ4RHvZ56JvQ1m0GnbI2A493eIOEozOOae93aHzmFWbaAWuIyoVq0EIfZ8wHlkYmek5YNDEhBTFR4PYSwV0ks1fTFZDvcHGhQEq9Np6tRlTmdsxbLKhcsjm_Qzkg"
	                }
	            ],
	            "place_id": "ChIJaehoqF3iDDkRxpdWYyvP1yQ",
	            "distance_from_you": "3 Kms",
	            "opening_hours": {
	                "open_now": true
	            }
	        }
	    ],
	    "page": null,
	    "nextPage": "CpQCBgEAAOZ4CY-7BTh8cg2sSfCSarU7DO-BPO9HQ3A2_1vUhIyX2qXeKPEMp449qIKqiWdpzZyb4AXa0WgET8bab-u7gdr_Smbd5g9v0A3o2mreGE5c-cEsHcHy91NXpRosmLzZTTzU9odRuPjP_aFAvYa_FRvxKTkFFWS0R9OmoEIh8IbpGyKKH9QDNnHbLQPnkmNV013AeV5WuPKE-avGU6QCH5VZ8ucZoNgiVn5KysNTVlDPYO7lirXQ298MDVFVVr1892dA1rhxcBcsEKAWD68-2FyIjdgfvNwJ3gqDkq5JGSS2jwW0M1C3LMwWQymq9qNOOyDsfzuWLe9R1HufFLyfNUAV8JiNlB4ETRw2BTFXu_C_EhDTr_zwJKwcLcKQZpHe3DwIGhRxHP0dbbDz69eBslTihQ8BFKoSVA",
	    "total_records": null,
	    "source": "GOOGLE"
	}
	*/
	@RequestMapping(value = "/nearby", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<?> searchNearbyPlaces(
			@RequestParam(required = false, value = "radius", defaultValue = Constants.DEFAULT_RADIUS) String radius,
			@RequestParam(required = true, value = "cid") Long categoryId,
			@RequestParam(required = false, value = "page") String page,
			@RequestParam(required = true, value = "lat") String latitude,
			@RequestParam(required = true, value = "lon") String longitude,
			@RequestParam(required = false, value = "sort") String sort,
			@RequestParam(required = false, value = "order") String order,
			HttpServletRequest request
			){
		String LOG_PREFIX = NEARBY_PLACES_REQUEST;
		logRequestStart(LOG_PREFIX, Constants.PUBLIC_REQUEST_START_LOG, NEARBY_PLACES_REQUEST);
		logInfo(LOG_PREFIX, "Validating Request ");
		NearbySearchRequest searchRequest = validateNearbyRequest(radius, categoryId, page, latitude, longitude, sort, order);
		
		logInfo(LOG_PREFIX, "NearbySearchRequest = {}", searchRequest);
		Places places = this.placesService.searchNearby(searchRequest);
		
		if(places instanceof ZomatoPlaces){
			ZomatoPlaces zomatoPlaces = (ZomatoPlaces) places;
			EntityCollectionResponse<Restaurant> collectionResponse = new EntityCollectionResponse<Restaurant>();
			collectionResponse.setData(zomatoPlaces.getRestaurants());
			collectionResponse.setPage(Integer.parseInt(searchRequest.getPageNumber()));
			collectionResponse.setTotalRecords(zomatoPlaces.getResultsFound());
			collectionResponse.setStatus(SUCCESS_STATUS);

			logInfo(LOG_PREFIX, "Returning response from Zomato");
			logRequestEnd(LOG_PREFIX, NEARBY_PLACES_REQUEST);
			return collectionResponse;
		}else if(places instanceof GooglePlaces){
			GooglePlaces googlePlaces = (GooglePlaces) places;
			PlacesEntityCollectionResponse<Result> collectionResponse = new PlacesEntityCollectionResponse<Result>();
			collectionResponse.setData(googlePlaces.getResults());
			collectionResponse.setNextPage(googlePlaces.getNexPageToken());
			collectionResponse.setSourceSystem(googlePlaces.getSourceSystem());
			collectionResponse.setStatus(SUCCESS_STATUS);

			logInfo(LOG_PREFIX, "Returning response from Google");
			logRequestEnd(LOG_PREFIX, NEARBY_PLACES_REQUEST);
			return collectionResponse;
		}else{
			logError(LOG_PREFIX, "Response from PlacesService is not of type {}", ZomatoPlaces.class.getName());
			logRequestEnd(LOG_PREFIX, NEARBY_PLACES_REQUEST);
			return new EntityCollectionResponse<>();
		}
		
	}
	
	@RequestMapping(value = "/place/{placeId}/detail", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<Place> getPlaceDetails(@PathVariable(value = "placeId") String placeId,
			@RequestParam(required = true, value = "lat") String latitude,
			@RequestParam(required = true, value = "lon") String longitude,
			@RequestParam(required = true, value = "source") String sourceSystem
			)
			{
		String LOG_PREFIX = PLACE_DETAILS_REQUEST_API;
		
		logRequestStart(LOG_PREFIX, PUBLIC_REQUEST_START_LOG, PLACE_DETAILS_REQUEST_API);
		logInfo(LOG_PREFIX, "Place Id = {} , User Lat = {} ,Lon = {}", placeId,latitude,longitude);
		PlaceDetailsRequest placeDetailsRequest = new PlaceDetailsRequest();
		placeDetailsRequest.setSourceSystem(sourceSystem);
		placeDetailsRequest.setUserLatitude(latitude);
		placeDetailsRequest.setUserLongitude(longitude);
		placeDetailsRequest.setPlaceId(placeId);
		Place place = 	placesService.getPlaceDetails(placeDetailsRequest);
		SingleEntityResponse<Place> entityResponse = new SingleEntityResponse<Place>();
		entityResponse.setData(place);
		entityResponse.setStatus(SUCCESS_STATUS);
		return entityResponse;
	}
	
	@RequestMapping(value = "/photos/{reference}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public void downloadPlaceImage(HttpServletResponse response, @PathVariable("reference") String reference) throws IOException {
		String LOG_PREFIX = "downloadPlaceImage";
		logRequestStart(LOG_PREFIX, PUBLIC_REQUEST_START_LOG, "Get Place Image API");
		 HttpHeaders headers = new HttpHeaders();
		    headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));

		    HttpEntity<String> entity = new HttpEntity<String>(headers);
		    StringBuilder urlBuilder = new StringBuilder(gapiConfig.getPlacePhotoGoogleAPI());
		    urlBuilder.append(Constants.AMP)
		    	.append(NearbySearchRequestGoogle.RequestParamNames.KEY)
		    	.append(Constants.EQUAL)
		    	.append(gapiConfig.getGapiKey())
		    	.append(Constants.AMP)
		    	.append(NearbySearchRequestGoogle.RequestParamNames.PHOTO_REFERENCE)
		    	.append(Constants.EQUAL)
		    	.append(reference);
		    
		    
		    logInfo(LOG_PREFIX, "URL to Get image : {}", urlBuilder.toString());
		    ResponseEntity<byte[]> imageResponse = restTemplate.exchange(
		            urlBuilder.toString(),
		            HttpMethod.GET, entity, byte[].class);

		    if (imageResponse.getStatusCode() == HttpStatus.OK) {
		    	logInfo(LOG_PREFIX, "Recieved 200 Ok from Google");
		    	HttpHeaders httpHeaders = imageResponse.getHeaders();
		    	MediaType mediaType = httpHeaders.getContentType();
		         byte[] imageArr = imageResponse.getBody();
		         logInfo(LOG_PREFIX, "Mime Type {}", mediaType.getType());
		         logInfo(LOG_PREFIX, "Image Size {}", httpHeaders.getContentLength());
		         response.setContentType(mediaType.getType());
		         response.setHeader("Content-Disposition", String.format("inline; filename=\"" + reference +"\""));
		         InputStream inputStream = new ByteArrayInputStream(imageArr);
		         FileCopyUtils.copy(inputStream, response.getOutputStream());
		    }else{
		    	logError(LOG_PREFIX, "Google Returned status : {}", imageResponse.getStatusCode());
		    }
	}
	
	@RequestMapping(value="/place/{placeId}/like",method=RequestMethod.PUT,produces = { MediaType.APPLICATION_JSON_VALUE },consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public void saveLikeActionForUser(@PathVariable("placeId") String placeId,@RequestHeader(value=Constants.AUTHORIZATION_HEADER) String authHeader){
		final String LOG_PREFIX = LIKE_PLACE_REQUEST;

		logRequestStart(LOG_PREFIX, SECURED_REQUEST_START_LOG_MESSAGE, LIKE_PLACE_REQUEST );
		logInfo(LOG_PREFIX, "Authorization {}", authHeader);
		String userName = LoginUtil.getUserNameFromHeader(authHeader);
		UserTypeBasedOnDevice typeBasedOnDevice = LoginUtil.identifyUserType(userName);
		if(typeBasedOnDevice==UserTypeBasedOnDevice.MOBILE){
			String deviceId = LoginUtil.getDeviceIdFromUserName(userName);
			logInfo(LOG_PREFIX, " Device Id {} ", deviceId);
			this.placesService.saveUserPlaceLike(deviceId, placeId);
			logInfo(LOG_PREFIX,"User place like mapping saved successfully");
		}else{
			
			throw new ClientException(RestErrorCodes.ERR_003,ERROR_FEATURE_AVAILABLE_TO_MOBILE_ONLY);
		}
		
		logRequestEnd(LOG_PREFIX,LIKE_PLACE_REQUEST);
	}
	
	@RequestMapping(value="/place/{placeId}/likes",method=RequestMethod.GET,produces = { MediaType.APPLICATION_JSON_VALUE },consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<UserFriend> getFriendsWhoLikePlace(@PathVariable("placeId") String placeId,
								@RequestHeader(value=Constants.AUTHORIZATION_HEADER) String authHeader){
		String LOG_PREFIX = GET_FRIENDS_WHO_LIKE_PLACE_API;
		logRequestStart(LOG_PREFIX, SECURED_REQUEST_START_LOG_MESSAGE, GET_FRIENDS_WHO_LIKE_PLACE_API);
		logInfo(LOG_PREFIX, "Auth header = {}", authHeader);
		logInfo(LOG_PREFIX, "Place Id = {} ", placeId);
		String userName = LoginUtil.getUserNameFromHeader(authHeader);
		UserTypeBasedOnDevice typeBasedOnDevice = LoginUtil.identifyUserType(userName);
		List<UserFriend> friendsWhoLikeThisPlace = null;
		if(typeBasedOnDevice==UserTypeBasedOnDevice.MOBILE){
			String deviceId = LoginUtil.getDeviceIdFromUserName(userName);
			logInfo(LOG_PREFIX, " Device Id {} ", deviceId);
			friendsWhoLikeThisPlace = this.placesService.getFriendsWhoLikePlace(deviceId, placeId);
			
		}else{
			throw new ClientException(RestErrorCodes.ERR_003,ERROR_FEATURE_AVAILABLE_TO_MOBILE_ONLY);
		}
		
		EntityCollectionResponse<UserFriend> collectionResponse = new EntityCollectionResponse<UserFriend>();
		collectionResponse.setData(friendsWhoLikeThisPlace);
		collectionResponse.setPage(1);
		collectionResponse.setTotalRecords(friendsWhoLikeThisPlace.size());
		collectionResponse.setStatus(SUCCESS_STATUS);
		
		logRequestEnd(LOG_PREFIX, GET_FRIENDS_WHO_LIKE_PLACE_API);
		return collectionResponse;
	}
	
	private NearbySearchRequest validateNearbyRequest(String radius,Long categoryId,
														String page,
														String latitude,
														String longitude,
														String sort,
														String order){
		String LOG_PREFIX = "validateNearbyRequest";
		NearbySearchRequest nearbySearchRequest = new NearbySearchRequest();
		if(radius!=null){
			try{
				Integer.parseInt(radius);
			}catch(Exception exception){
				logError(LOG_PREFIX, "Invalid radius {}", radius);
				throw new ClientException(RestErrorCodes.ERR_001, ERROR_INVALID_INPUT_RADIUS);
			}
		}
		
		//TODO
		/*if(page!=null){
			try{
				Integer.parseInt(page);
			}catch(Exception exception){
				logError(LOG_PREFIX, "Invalid parameter page {}", page);
				throw new ClientException(RestErrorCodes.ERR_001, ERROR_INVALID_INPUT_PAGE);
			}
		}*/
		
		//TODO:Validate lattitude and longitude
		
		nearbySearchRequest.setCategoryId(categoryId);
		nearbySearchRequest.setLatitude(latitude);
		nearbySearchRequest.setLongitude(longitude);
		nearbySearchRequest.setOrder(order);
		nearbySearchRequest.setSort(sort);
		nearbySearchRequest.setRadius(radius);
		if(page!=null){
			nearbySearchRequest.setPageNumber(page);
		}else{
			nearbySearchRequest.setPageNumber(null);
		}
		
		return nearbySearchRequest;
	}
}
