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

import com.bitlogic.sociallbox.data.model.Category;
import com.bitlogic.sociallbox.data.model.response.EntityCollectionResponse;
import com.bitlogic.sociallbox.service.business.CategoryService;

@RestController
@RequestMapping("/api/public/categories")
public class CategoryPublicController {

	private static final Logger logger = LoggerFactory.getLogger(CategoryPublicController.class);
	@Autowired
	private CategoryService categoryService;
	
	/**
	 *  @api {get} /api/public/categories Get All Categories
	 *  @apiName Get Categories
	 *  @apiGroup Categories
	 *  @apiHeader {String} accept application/json
	 *  @apiSuccess (Success - OK 200) {Object}  response  Response.
	 *  @apiSuccess (Success - OK 200) {String}  response.status   Eg.Success.
	 * 	@apiSuccess (Success - OK 200) {Object}  response.data Categories
	 *  @apiSuccessExample {json} Success-Response: 
	 *  {
		  "status": "Success",
		  "data": [
		    {
		      "id": 1,
		      "name": "event",
		      "description": "Event-o-pedia",
		      "createDt": 1456599044000,
		      "displayOrder": 1,
		      "navURL": "#/categories/events"
		    },
		    {
		      "id": 2,
		      "name": "restaurant",
		      "description": "Food Lust",
		      "createDt": 1456599044000,
		      "displayOrder": 2,
		      "navURL": "#/categories/2/Food+Lust/places"
		    },
		    {
		      "id": 3,
		      "name": "cafe",
		      "description": "Coffee Love",
		      "createDt": 1456599044000,
		      "displayOrder": 3,
		      "navURL": "#/categories/3/Coffe+Love/places"
		    },
		    {
		      "id": 4,
		      "name": "night_club",
		      "description": "NightLife",
		      "createDt": 1456599044000,
		      "displayOrder": 4,
		      "navURL": "#/categories/4/NightLife+Karma/places"
		    },
		    {
		      "id": 5,
		      "name": "bar",
		      "description": "Bar-O-Bar",
		      "createDt": 1456599044000,
		      "displayOrder": 5,
		      "navURL": "#/categories/5/Bar-O-Bar/places"
		    },
		    {
		      "id": 6,
		      "name": "movie_theater",
		      "description": "Movie-O-Logy",
		      "createDt": 1456599044000,
		      "displayOrder": 5,
		      "navURL": "#/categories/5/Movie-O-Logy/places"
		    }
		  ],
		  "page": 1,
		  "nextPage": null
		}
	 *	
	 */
	@RequestMapping(method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<Category> get(){
		logger.info("### Request recieved- Get All Categories. ###");
		List<Category> categories = categoryService.getAll();
		EntityCollectionResponse<Category> collectionResponse = new EntityCollectionResponse<>();
		collectionResponse.setData(categories);
		collectionResponse.setPage(1);
		collectionResponse.setStatus("Success");
		return collectionResponse;
	}
	
}
