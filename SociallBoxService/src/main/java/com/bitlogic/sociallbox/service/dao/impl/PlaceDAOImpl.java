package com.bitlogic.sociallbox.service.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bitlogic.sociallbox.data.model.UserAndPlaceMapping;
import com.bitlogic.sociallbox.service.dao.AbstractDAO;
import com.bitlogic.sociallbox.service.dao.PlaceDAO;

@Repository("placeDAO")
public class PlaceDAOImpl extends AbstractDAO implements PlaceDAO {

	@Override
	public void saveUserLikeForPlace(UserAndPlaceMapping mapping) {
		Boolean isUserPlaceMappingExist = checkIfUserLikesPlace(mapping);
		if(!isUserPlaceMappingExist){
			this.getSession().save(mapping);
		}
	}

	@Override
	public Boolean checkIfUserLikesPlace(UserAndPlaceMapping mapping) {
		Criteria criteria = getSession().createCriteria(UserAndPlaceMapping.class)
				.add(Restrictions.eq("userId", mapping.getUserId()))
				.add(Restrictions.eq("placeId", mapping.getPlaceId()));
		UserAndPlaceMapping placeMapping = (UserAndPlaceMapping) criteria.uniqueResult();
		if(placeMapping!=null){
			return Boolean.TRUE;
		}else{
			return Boolean.FALSE;
		}
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
