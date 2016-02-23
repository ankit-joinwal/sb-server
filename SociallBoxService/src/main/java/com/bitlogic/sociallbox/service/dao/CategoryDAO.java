package com.bitlogic.sociallbox.service.dao;

import java.util.List;

import com.bitlogic.sociallbox.data.model.Category;

public interface CategoryDAO{

	public Category create(Category category);
	
	public List<Category> getAllCategories();
	
	public Category getCategoryById(Long id);
	
	public List<Category> getCategoriesByName(List<String> categoryNames);
}
