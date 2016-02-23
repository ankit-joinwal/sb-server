package com.bitlogic.sociallbox.service.business;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitlogic.sociallbox.data.model.Category;
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
		return categoryDAO.getAllCategories();
	}
	
	
}
