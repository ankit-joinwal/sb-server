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
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.Category;
import com.bitlogic.sociallbox.data.model.Event;
import com.bitlogic.sociallbox.data.model.PushNotificationSettingMaster;
import com.bitlogic.sociallbox.data.model.Role;
import com.bitlogic.sociallbox.data.model.UserRoleType;

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
		setupRoleData();
		setupCategories();
		setupPushSettingTypes();
			
		session.getTransaction().commit();
		session.close();
		factory.close();

	}

	private static void setupRoleData() {
		Role role = new Role();
		role.setUserRoleType(UserRoleType.APP_USER.getRoleType());
		
		Role role1 = new Role();
		role1.setUserRoleType(UserRoleType.EVENT_ORGANIZER.getRoleType());

		Role role2 = new Role();
		role2.setUserRoleType(UserRoleType.ADMIN.getRoleType());
		session.save(role);
		session.save(role1);
		session.save(role2);
	}
	
	private static void setupCategories(){
		Category category = new Category();
		category.setName("event");
		category.setDescription("Event-o-pedia");
		category.setDisplayOrder(1);
		category.setCreateDt(new Date());
		category.setNavURL("#/categories/events");

		Category categoryFood = new Category();
		categoryFood.setDisplayOrder(2);
		categoryFood.setName("restaurant");
		categoryFood.setDescription("Food Lust");
		categoryFood.setNavURL("#/categories/2/Food+Lust/places");
		categoryFood.setCreateDt(new Date());
		
		Category categoryCafe = new Category();
		categoryCafe.setDisplayOrder(3);
		categoryCafe.setName("cafe");
		categoryCafe.setDescription("Coffee Love");
		categoryCafe.setNavURL("#/categories/3/Coffe+Love/places");
		categoryCafe.setCreateDt(new Date());
		
		Category categoryClub = new Category();
		categoryClub.setDisplayOrder(4);
		categoryClub.setName("night_club");
		categoryClub.setDescription("NightLife Karma");
		categoryClub.setNavURL("#/categories/4/NightLife+Karma/places");
		categoryClub.setCreateDt(new Date());
		
		Category categoryBar = new Category();
		categoryBar.setDisplayOrder(5);
		categoryBar.setName("bar");
		categoryBar.setDescription("Bar-O-Bar");
		categoryBar.setNavURL("#/categories/5/Bar-O-Bar/places");
		categoryBar.setCreateDt(new Date());
		
		Category categoryMovie = new Category();
		categoryMovie.setDisplayOrder(5);
		categoryMovie.setName("movie_theater");
		categoryMovie.setDescription("Movie-O-Logy");
		categoryMovie.setNavURL("#/categories/5/Movie-O-Logy/places");
		categoryMovie.setCreateDt(new Date());
		
		
		session.save(category);
		session.save(categoryFood);
		session.save(categoryCafe);
		session.save(categoryClub);
		session.save(categoryBar);
		session.save(categoryMovie);
	}
	
	private static void setupPushSettingTypes(){
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
