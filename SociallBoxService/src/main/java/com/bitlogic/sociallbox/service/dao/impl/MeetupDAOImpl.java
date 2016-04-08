package com.bitlogic.sociallbox.service.dao.impl;


import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.AddressComponentType;
import com.bitlogic.sociallbox.data.model.Meetup;
import com.bitlogic.sociallbox.data.model.MeetupAttendee;
import com.bitlogic.sociallbox.data.model.MeetupAttendeeEntity;
import com.bitlogic.sociallbox.data.model.MeetupImage;
import com.bitlogic.sociallbox.data.model.MeetupMessage;
import com.bitlogic.sociallbox.data.model.SocialDetailType;
import com.bitlogic.sociallbox.data.model.UserSocialDetail;
import com.bitlogic.sociallbox.data.model.requests.SaveAttendeeResponse;
import com.bitlogic.sociallbox.service.dao.AbstractDAO;
import com.bitlogic.sociallbox.service.dao.MeetupDAO;

@Repository("meetupDAO")
public class MeetupDAOImpl extends AbstractDAO implements MeetupDAO{
	private static final Logger logger = LoggerFactory.getLogger(MeetupDAOImpl.class);
	
	
	@Override
	public Meetup createMeetup(Meetup meetup) {
		logger.info("### Inside MeetupDAOImpl.createMeetup ");
		String meetupId = (String) getSession().save(meetup);
		return getMeetup(meetupId);
	}
	
	@Override
	public Meetup getMeetup(String id) {
		logger.info("### Inside MeetupDAOImpl.getMeetup ");
		Criteria criteria = getSession().createCriteria(Meetup.class).
				add(Restrictions.eq("uuid", id)).
				setFetchMode("attendees", FetchMode.JOIN)
				.setFetchMode("eventAtMeetup",FetchMode.JOIN)
				.setFetchMode("images", FetchMode.JOIN);
		return (Meetup) criteria.uniqueResult();
	}
	
	@Override
	public Meetup saveMeetup(Meetup meetup) {
		logger.info("### Inside MeetupDAOImpl.saveMeetup ");
		saveOrUpdate(meetup);
		return meetup;
	}
	
	@Override
	public MeetupAttendeeEntity addAttendee(MeetupAttendeeEntity meetupAttendee) {
		Long id = (Long) save(meetupAttendee);
		return getAttendeeById(id);
	}
	
	@Override
	public MeetupAttendeeEntity getAttendeeById(Long id) {
	
		Criteria criteria = getSession().createCriteria(MeetupAttendeeEntity.class)
							.setFetchMode("user", FetchMode.JOIN)
							.add(Restrictions.eq("id", id));
		MeetupAttendeeEntity meetupAttendee = (MeetupAttendeeEntity) criteria.uniqueResult();
		return meetupAttendee;
	}
	
	@Override
	public MeetupAttendeeEntity getMeetupAttendee(Long userId, String meetupId) {
		
		String sql = "SELECT * FROM MEETUP_ATTENDEES WHERE USER_ID = :userId AND MEETUP_ID = :meetupId";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.addEntity(MeetupAttendeeEntity.class);
		sqlQuery.setParameter("userId", userId);
		sqlQuery.setParameter("meetupId", meetupId);
		
		Object result = sqlQuery.uniqueResult();
		if(result!=null){
			return (MeetupAttendeeEntity) result;
		}
		return null;
	}
	
	@Override
	public MeetupAttendeeEntity getMeetupAttendeeByAttendeeId(Long attendeeId) {
		Criteria criteria = getSession().createCriteria(MeetupAttendeeEntity.class)
								.add(Restrictions.eq("attendeeId", attendeeId));
		return (MeetupAttendeeEntity)criteria.uniqueResult();
	}
	
	@Override
	public List<MeetupImage> getMeetupImages(String meetupId) {
		Criteria criteria = getSession().createCriteria(MeetupImage.class,"image")
							.createAlias("image.meetup", "meetup")
							.setFetchMode("meetup", FetchMode.JOIN)
							.add(Restrictions.eq("meetup.uuid", meetupId))
							.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();
	}
	
	@Override
	public void saveAttendeeResponse(SaveAttendeeResponse attendeeResponse) {
		MeetupAttendeeEntity meetupAttendee = (MeetupAttendeeEntity) getSession().get(MeetupAttendeeEntity.class, attendeeResponse.getAttendeeId());
		if(meetupAttendee!=null){
			logger.info("Storing attendee response for meetup {} for attendee {} ",attendeeResponse.getMeetupId(),attendeeResponse.getAttendeeId());
			meetupAttendee.setAttendeeResponse(attendeeResponse.getAttendeeResponse());
			getSession().saveOrUpdate(meetupAttendee);
		}
	}
	
	@Override
	public void sendMessageInMeetup(MeetupMessage meetupMessage,
			Meetup meetup , MeetupAttendeeEntity attendeeEntity) {
		
		Date now = new Date();
		if(meetup != null && attendeeEntity != null){
			meetupMessage.setMeetup(meetup);
			meetupMessage.setMeetupAttendee(attendeeEntity);
			meetupMessage.setCreateDt(now);
			saveOrUpdate(meetupMessage);
		}
	}
	
	@Override
	public List<MeetupMessage> getMeetupMessages(Meetup meetup, Integer page) {
		int startIdx = (page - 1) * Constants.RECORDS_PER_PAGE;
		String sql = "SELECT "+
				"	MESSAGES.ID, "+
				"	MESSAGES.MESSAGE, "+
				"	DATE_FORMAT(MESSAGES.CREATE_DT,'%d-%m-%Y %H:%i:%S'),	"+
				"	USR.NAME ,	"+
				"	USD.SOCIAL_DETAIL	"+
				"	FROM MEETUP_MESSAGES MESSAGES	"+
				"	INNER JOIN MEETUP_ATTENDEES ATTENDEES ON MESSAGES.SENDER_ID = ATTENDEES.ID	"+
				"	INNER JOIN MEETUP MEETUP ON MEETUP.ID = MESSAGES.MEETUP_ID	"+
				"	INNER JOIN USER USR ON ATTENDEES.USER_ID = USR.ID	"+
				"	LEFT OUTER JOIN USER_SOCIAL_DETAILS USD ON USR.ID = USD.USER_ID	"+
				"	WHERE	"+
				"	MEETUP.ID = :meetupId	"+ 
				"	AND USD.DETAIL_TYPE = :profilePicDetail	"+
				"	ORDER BY MESSAGES.CREATE_DT DESC LIMIT :startIdx,:endIdx ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("meetupId",meetup.getUuid());
		query.setParameter("profilePicDetail", SocialDetailType.USER_PROFILE_PIC.name());
		query.setParameter("startIdx", startIdx);
		query.setParameter("endIdx", Constants.RECORDS_PER_PAGE);
		List results = query.list();
		Iterator it = results.iterator();
		List<MeetupMessage> meetupMessages = new ArrayList<MeetupMessage>();
		while(it.hasNext())
		{
			MeetupMessage meetupMessage = new MeetupMessage();
			Object[] rows = (Object[])it.next();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			try{
				Date createDt = formatter.parse((String) rows[2]);
				meetupMessage.setCreateDt(createDt);
			}catch(Exception ex){
				logger.error("Error while parsing meetup message date {}",(String) rows[2]);
				
			}
			meetupMessage.setId(((BigInteger)rows[0]).longValue());
			meetupMessage.setMessage((String)rows[1]);
			meetupMessage.setUserName((String)rows[3]);
			meetupMessage.setProfilePic((String)rows[4]);
			meetupMessages.add(meetupMessage);
		}
		
		return meetupMessages;
	}
	
	@Override
	public List<AddressComponentType> getAddressTypes() {
		Criteria criteria = getSession().createCriteria(AddressComponentType.class);
		
		return criteria.list();
	}
	
	@Override
	public void saveMeetupImages(List<MeetupImage> images) {
		for(MeetupImage meetupImage : images){
			saveOrUpdate(meetupImage);
		}
	}
	
	@Override
	public List<MeetupAttendeeEntity> getAttendees(Meetup meetup) {
		Criteria criteria = getSession().createCriteria(MeetupAttendeeEntity.class,"attendee")
							.createAlias("attendee.meetup", "meetup")
							.createAlias("attendee.user", "user")
							.setFetchMode("user", FetchMode.JOIN)
							.add(Restrictions.eq("meetup.uuid", meetup.getUuid()))
							.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();
	}
}
