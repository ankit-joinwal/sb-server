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
import com.jolbox.bonecp.BoneCPDataSource;

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
	       /* DriverManagerDataSource dataSource = new DriverManagerDataSource();
	        dataSource.setDriverClassName(environment.getRequiredProperty(Constants.JDBC_DRIVER_PROPERTY));
	        dataSource.setUrl(environment.getRequiredProperty(Constants.JDBC_URL_PROPERTY));
	        dataSource.setUsername(environment.getRequiredProperty(Constants.JDBC_USERNAME_PROPERTY));
	        dataSource.setPassword(environment.getRequiredProperty(Constants.JDBC_PASSWORD_PROPERTY));*/
	    	 BoneCPDataSource dataSource = new BoneCPDataSource();
	         dataSource.setDriverClass(environment.getRequiredProperty(Constants.JDBC_DRIVER_PROPERTY));
	         dataSource.setJdbcUrl(environment.getRequiredProperty(Constants.BONECP_URL));
	         dataSource.setUsername(environment.getRequiredProperty(Constants.BONECP_USERNAME));
	         dataSource.setPassword(environment.getRequiredProperty(Constants.BONECP_PASSWORD));
	         dataSource.setIdleConnectionTestPeriodInMinutes(Integer.parseInt(environment.getRequiredProperty(Constants.BONECP_IDLE_CONNECTION_TEST_PERIOD_IN_MINUTES)));
	         dataSource.setIdleMaxAgeInMinutes(Integer.parseInt(environment.getRequiredProperty(Constants.BONECP_IDLE_MAX_AGE_IN_MINUTES)));
	         dataSource.setMaxConnectionsPerPartition(Integer.parseInt(environment.getRequiredProperty(Constants.BONECP_MAX_CONNECTIONS_PER_PARTITION)));
	         dataSource.setMinConnectionsPerPartition(Integer.parseInt(environment.getRequiredProperty(Constants.BONECP_MIN_CONNECTIONS_PER_PARTITION)));
	         dataSource.setPartitionCount(Integer.parseInt(environment.getRequiredProperty(Constants.BONECP_PARTITION_COUNT)));
	         dataSource.setAcquireIncrement(Integer.parseInt(environment.getRequiredProperty(Constants.BONECP_ACQUIRE_INCREMENT)));
	         dataSource.setStatementsCacheSize(Integer.parseInt(environment.getRequiredProperty(Constants.BONECP_STATEMENTS_CACHE_SIZE)));

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
