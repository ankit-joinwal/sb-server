package com.bitlogic.sociallbox.service.test.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.bitlogic.Constants;

@Configuration
@EnableTransactionManagement
@PropertySource(value = { "classpath:application.properties" })
public class MockHibernateConfiguration {
	@Autowired
	private Environment environment;
	
	  @Bean
	    public LocalSessionFactoryBean sessionFactory() {
	        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
	        sessionFactory.setDataSource(dataSource());
	        sessionFactory.setPackagesToScan(new String[] { "com.bitlogic.sociallbox.data.model" });
	        sessionFactory.setHibernateProperties(hibernateProperties());
	        return sessionFactory;
	     }
	     
	    @Bean
	    public DataSource dataSource() {
	        DriverManagerDataSource dataSource = new DriverManagerDataSource();
	        dataSource.setDriverClassName(environment.getRequiredProperty(Constants.JDBC_DRIVER_PROPERTY));
	        dataSource.setUrl(environment.getRequiredProperty(Constants.JDBC_URL_PROPERTY));
	        dataSource.setUsername(environment.getRequiredProperty(Constants.JDBC_USERNAME_PROPERTY));
	        dataSource.setPassword(environment.getRequiredProperty(Constants.JDBC_PASSWORD_PROPERTY));
	        return dataSource;
	    }
	     
	    private Properties hibernateProperties() {
	        Properties properties = new Properties();
	        properties.put(Constants.HIBERNATE_DIALECT_PROPERTY, environment.getRequiredProperty(Constants.HIBERNATE_DIALECT_PROPERTY));
	        properties.put(Constants.HIBERNATE_SHOW_SQL_PROPERTY, environment.getRequiredProperty(Constants.HIBERNATE_SHOW_SQL_PROPERTY));
	        properties.put(Constants.HIBERNATE_FORMAT_SQL_PROPERTY, environment.getRequiredProperty(Constants.HIBERNATE_FORMAT_SQL_PROPERTY));
	        //TODO: REMOVE THIS PROPERTY IN PRODUCTION TO AVOID ACCIDENTAL DAMAGE TO SCHEMA
	        properties.put(Constants.HIBERNATE_HBM_DDL_PROPERTY, environment.getRequiredProperty(Constants.HIBERNATE_HBM_DDL_PROPERTY));
	        return properties;        
	    }
	     
	    @Bean
	    @Autowired
	    public HibernateTransactionManager transactionManager(SessionFactory s) {
	       HibernateTransactionManager txManager = new HibernateTransactionManager();
	       txManager.setSessionFactory(s);
	       return txManager;
	    }

}
