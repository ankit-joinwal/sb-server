package com.bitlogic.sociallbox.service.business.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitlogic.sociallbox.data.model.Category;
import com.bitlogic.sociallbox.data.model.SourceSystemForPlaces;
import com.bitlogic.sociallbox.data.model.requests.NearbySearchRequestGoogle;
import com.bitlogic.sociallbox.data.model.requests.NearbySearchRequestZomato;
import com.bitlogic.sociallbox.data.model.requests.SortOption;
import com.bitlogic.sociallbox.data.model.requests.SortOrderOption;
import com.bitlogic.sociallbox.service.business.CategoryService;
import com.bitlogic.sociallbox.service.dao.CategoryDAO;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryDAO categoryDAO;
	
	
	public void setCategoryDAO(CategoryDAO categoryDAO) {
		this.categoryDAO = categoryDAO;
	}

	@Override
	public Category create(Category category) {
		Date now = new Date();
		category.setCreateDt(now);
		return categoryDAO.create(category);
	}

	@Override
	public List<Category> getAll() {
		List<Category> categories = this.categoryDAO.getAllCategories();
		if(categories!=null && !categories.isEmpty()){
			for(Category category : categories){
				SourceSystemForPlaces sourceSystem = category.getSystemForPlaces();
				if(sourceSystem == SourceSystemForPlaces.ZOMATO){
					category.setSortByOptions(NearbySearchRequestZomato.getSortOptions());
					category.setSortOrderOptions(NearbySearchRequestZomato.getSortOrderOptions());
				}else if(sourceSystem == SourceSystemForPlaces.GOOGLE){
					category.setSortByOptions(NearbySearchRequestGoogle.getSortOptions());
					category.setSortOrderOptions(new ArrayList<SortOrderOption>());
				}else{
					category.setSortByOptions(new ArrayList<SortOption>(1));
					category.setSortOrderOptions(new ArrayList<SortOrderOption>(1));
				}
			}
		}
		return categoryDAO.getAllCategories();
	}
	
	
}
