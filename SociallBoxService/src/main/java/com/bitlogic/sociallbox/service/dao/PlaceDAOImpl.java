package com.bitlogic.sociallbox.service.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bitlogic.sociallbox.data.model.UserAndPlaceMapping;

@Repository("placeDAO")
public class PlaceDAOImpl extends AbstractDAO implements PlaceDAO {

	@Override
	public void saveUserLikeForPlace(UserAndPlaceMapping mapping) {

		this.getSession().save(mapping);
	}

	@Override
	public List<Long> getUsersWhoLikePlace(String placeId) {
		Criteria criteria = getSession().createCriteria(UserAndPlaceMapping.class)
							.add(Restrictions.eq("placeId", placeId))
							.setProjection(Projections.property("userId"));
		List<Long> usersIds = criteria.list();
		
		return usersIds;
	}
}
