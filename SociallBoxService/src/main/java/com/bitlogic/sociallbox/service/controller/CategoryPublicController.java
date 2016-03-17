package com.bitlogic.sociallbox.service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.Category;
import com.bitlogic.sociallbox.data.model.response.EntityCollectionResponse;
import com.bitlogic.sociallbox.service.business.CategoryService;

@RestController
@RequestMapping("/api/public/categories")
public class CategoryPublicController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(CategoryPublicController.class);
	private static final String GET_CATEGORIES_API = "GetCategories API";
	@Autowired
	private CategoryService categoryService;
	
	@Override
	public Logger getLogger() {
		return logger;
	}
	
	/**
	 *  @api {get} /api/public/categories Get All Categories
	 *  @apiName Get Categories
	 *  @apiGroup Categories
	 *  @apiHeader {String} accept application/json
	 *  @apiSuccess (Success - OK 200) {Object}  response  Response.
	 *  @apiSuccess (Success - OK 200) {String}  response.status   Eg.Success.
	 * 	@apiSuccess (Success - OK 200) {Object}  response.data Categories
	 *  @apiSuccessExample {json} Success-Response: 
	 * 
	 {
		  "status": "Success",
		  "data": [
		    {
		      "id": 1,
		      "name": "event",
		      "description": "Event-o-pedia",
		      "display_order": 1,
		      "sortOptions": [],
		      "sortOrderOptions": []
		    },
		    {
		      "id": 2,
		      "name": "restaurant",
		      "description": "Food Lust",
		      "display_order": 2,
		      "sortOptions": [
		        {
		          "code": "prominence",
		          "description": "Rating"
		        },
		        {
		          "code": "real_distance",
		          "description": "Distance"
		        }
		      ],
		      "sortOrderOptions": []
		    },
		    {
		      "id": 5,
		      "name": "bar",
		      "description": "Bar-O-Bar",
		      "display_order": 3,
		      "sortOptions": [
		        {
		          "code": "prominence",
		          "description": "Rating"
		        },
		        {
		          "code": "real_distance",
		          "description": "Distance"
		        }
		      ],
		      "sortOrderOptions": []
		    },
		    {
		      "id": 3,
		      "name": "cafe",
		      "description": "Coffee Love",
		      "display_order": 4,
		      "sortOptions": [
		        {
		          "code": "prominence",
		          "description": "Rating"
		        },
		        {
		          "code": "real_distance",
		          "description": "Distance"
		        }
		      ],
		      "sortOrderOptions": []
		    },
		    {
		      "id": 4,
		      "name": "night_club",
		      "description": "NightLife",
		      "display_order": 5,
		      "sortOptions": [
		        {
		          "code": "prominence",
		          "description": "Rating"
		        },
		        {
		          "code": "real_distance",
		          "description": "Distance"
		        }
		      ],
		      "sortOrderOptions": []
		    }
		  ],
		  "page": 1,
		  "nextPage": null,
		  "total_records": 5
		}
	 *	
	 */
	@RequestMapping(method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<Category> get(){

		logRequestStart(GET_CATEGORIES_API, Constants.PUBLIC_REQUEST_START_LOG, GET_CATEGORIES_API);
		
		List<Category> categories = categoryService.getAll();
		EntityCollectionResponse<Category> collectionResponse = new EntityCollectionResponse<>();
		collectionResponse.setData(categories);
		collectionResponse.setTotalRecords(categories.size());
		collectionResponse.setPage(1);
		collectionResponse.setStatus("Success");
		logRequestEnd(GET_CATEGORIES_API, GET_CATEGORIES_API);
		return collectionResponse;
	}
	
}
