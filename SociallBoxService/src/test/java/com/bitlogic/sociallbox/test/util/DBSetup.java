package com.bitlogic.sociallbox.test.util;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.transform.ResultTransformer;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.Category;
import com.bitlogic.sociallbox.data.model.Event;
import com.bitlogic.sociallbox.data.model.PushNotificationSettingMaster;
import com.bitlogic.sociallbox.data.model.Role;
import com.bitlogic.sociallbox.data.model.SourceSystemForPlaces;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.UserAndPlaceMapping;
import com.bitlogic.sociallbox.data.model.UserRoleType;
import com.bitlogic.sociallbox.data.model.UserSetting;

public class DBSetup {
	private static Session session = null;

	public static void main(String[] args) {
		Configuration configuration = new Configuration().configure();
		StandardServiceRegistryBuilder srb = new StandardServiceRegistryBuilder();
		srb.applySettings(configuration.getProperties());
		ServiceRegistry sr = srb.build();
		SessionFactory factory = configuration.buildSessionFactory(sr);

		session = factory.openSession();

		session.beginTransaction();
		// setupRoleData();
		// setupCategories();
		// setupPushSettingTypes();
		
		Criteria criteria = session.createCriteria(UserAndPlaceMapping.class)
				.add(Restrictions.eq("placeId", "ChIJq11ZZh3jDDkR6wx3ISmietM"))
				.setProjection(Projections.property("userId"));
		List<Long> usersIds = criteria.list();
		System.out.println(usersIds);
		
		Criteria criteria1 = session.createCriteria(User.class)
				.createAlias("friends", "friend")
				.setFetchMode("friend", FetchMode.JOIN)
				.add(Restrictions.eq("id", 5L))
				.add(Restrictions.in("friend.id", usersIds));
		User userWithFriends = (User)criteria1.uniqueResult();
		System.out.println(userWithFriends.getFriends());
		session.getTransaction().commit();
		session.close();
		factory.close();

	}

	private static void setupRoleData() {
		Role role = new Role();
		role.setUserRoleType(UserRoleType.APP_USER);

		Role role1 = new Role();
		role1.setUserRoleType(UserRoleType.EVENT_ORGANIZER);

		Role role2 = new Role();
		role2.setUserRoleType(UserRoleType.ADMIN);
		session.save(role);
		session.save(role1);
		session.save(role2);
	}

	private static void setupCategories() {
		Category category = new Category();
		category.setName("event");
		category.setDescription("Event-o-pedia");
		category.setDisplayOrder(1);
		category.setExtId("1");
		category.setSystemForPlaces(SourceSystemForPlaces.SOCIALLBOX);
		category.setCreateDt(new Date());

		Category categoryFood = new Category();
		categoryFood.setDisplayOrder(2);
		categoryFood.setName("restaurant");
		categoryFood.setDescription("Food Lust");
		categoryFood.setExtId("2");
		categoryFood.setSystemForPlaces(SourceSystemForPlaces.ZOMATO);
		categoryFood.setCreateDt(new Date());

		Category categoryCafe = new Category();
		categoryCafe.setDisplayOrder(3);
		categoryCafe.setName("cafe");
		categoryCafe.setDescription("Coffee Love");
		categoryCafe.setExtId("6");
		categoryCafe.setSystemForPlaces(SourceSystemForPlaces.ZOMATO);
		categoryCafe.setCreateDt(new Date());

		Category categoryClub = new Category();
		categoryClub.setDisplayOrder(4);
		categoryClub.setName("night_club");
		categoryClub.setDescription("NightLife");
		categoryClub.setExtId("3");
		categoryClub.setSystemForPlaces(SourceSystemForPlaces.ZOMATO);
		categoryClub.setCreateDt(new Date());

		Category categoryMovie = new Category();
		categoryMovie.setDisplayOrder(5);
		categoryMovie.setName("movie_theater");
		categoryMovie.setExtId("movie_theater");
		categoryMovie.setSystemForPlaces(SourceSystemForPlaces.GOOGLE);
		categoryMovie.setDescription("Movie-O-Logy");
		categoryMovie.setCreateDt(new Date());

		session.save(category);
		session.save(categoryFood);
		session.save(categoryCafe);
		session.save(categoryClub);
		session.save(categoryMovie);
	}

	private static void setupPushSettingTypes() {
		PushNotificationSettingMaster type = new PushNotificationSettingMaster();
		type.setName("newFriendNot");
		type.setDisplayName("Notify me when my friend joins SociallBox");
		type.setDisplayOrder(1);

		PushNotificationSettingMaster type1 = new PushNotificationSettingMaster();
		type1.setName("meetupInvite");
		type1.setDisplayName("Notify me when I'm invited for meetup");
		type1.setDisplayOrder(2);

		session.save(type);
		session.save(type1);

	}

}
