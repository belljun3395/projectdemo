package com.example.demo.repository.config;

import static com.example.demo.config.AppConfig.BASE_PACKAGE;
import static com.example.demo.repository.config.JpaDataSourceConfig.MODULE_BASE_PACKAGE;

import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages = MODULE_BASE_PACKAGE,
		transactionManagerRef = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME,
		entityManagerFactoryRef = JpaDataSourceConfig.ENTITY_MANAGER_FACTORY_NAME)
public class JpaDataSourceConfig {

	private static final String MODULE_NAME = "repository";
	private static final String SERVICE_NAME = "demo";
	public static final String MODULE_BASE_PACKAGE = BASE_PACKAGE + "." + MODULE_NAME;
	private static final String REPOSITORY_DRIVER = "mysql";

	// base property prefix for jpa datasource
	private static final String BASE_PROPERTY_PREFIX =
			SERVICE_NAME + "." + MODULE_NAME + "." + REPOSITORY_DRIVER + ".entity";

	// bean name for jpa datasource configuration
	public static final String ENTITY_MANAGER_FACTORY_NAME =
			SERVICE_NAME + "DomainEntityManagerFactory";
	public static final String TRANSACTION_MANAGER_NAME = SERVICE_NAME + "DomainTransactionManager";
	public static final String DATASOURCE_NAME = SERVICE_NAME + "DataSource";
	private static final String JPA_PROPERTIES_NAME = SERVICE_NAME + "JpaProperties";
	private static final String HIBERNATE_PROPERTIES_NAME = SERVICE_NAME + "HibernateProperties";
	private static final String JPA_VENDOR_ADAPTER_NAME = SERVICE_NAME + "JpaVendorAdapter";
	private static final String PERSIST_UNIT = SERVICE_NAME + "Domain";

	@Bean(name = DATASOURCE_NAME)
	@ConfigurationProperties(prefix = BASE_PROPERTY_PREFIX + ".datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = JPA_PROPERTIES_NAME)
	@ConfigurationProperties(prefix = BASE_PROPERTY_PREFIX + ".jpa")
	public JpaProperties jpaProperties() {
		return new JpaProperties();
	}

	@Bean(name = HIBERNATE_PROPERTIES_NAME)
	@ConfigurationProperties(prefix = BASE_PROPERTY_PREFIX + ".jpa.hibernate")
	public HibernateProperties hibernateProperties() {
		return new HibernateProperties();
	}

	@Bean(name = JPA_VENDOR_ADAPTER_NAME)
	public JpaVendorAdapter jpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}

	@Bean(name = ENTITY_MANAGER_FACTORY_NAME)
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
			@Qualifier(value = DATASOURCE_NAME) DataSource dataSource) {
		Map<String, String> jpaPropertyMap = jpaProperties().getProperties();
		Map<String, Object> hibernatePropertyMap =
				hibernateProperties().determineHibernateProperties(jpaPropertyMap, new HibernateSettings());
		return new EntityManagerFactoryBuilder(jpaVendorAdapter(), jpaPropertyMap, null)
				.dataSource(dataSource)
				.properties(hibernatePropertyMap)
				.persistenceUnit(PERSIST_UNIT)
				.packages(MODULE_BASE_PACKAGE)
				.build();
	}

	@Bean(name = TRANSACTION_MANAGER_NAME)
	public PlatformTransactionManager transactionManager(
			@Qualifier(ENTITY_MANAGER_FACTORY_NAME) EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}
}
