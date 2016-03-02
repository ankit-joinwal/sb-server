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
