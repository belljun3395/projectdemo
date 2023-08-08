package com.example.demo.infra.flyway.config;

import static com.example.demo.infra.flyway.FlywayConfig.BEAN_NAME_PREFIX;
import static com.example.demo.infra.flyway.FlywayConfig.PROPERTY_PREFIX;

import com.example.demo.data.mysql.config.EntityJpaDataSourceConfig;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class EntityFlywayConfig {

	// base property prefix for flyway
	private static final String BASE_PROPERTY_PREFIX = PROPERTY_PREFIX;

	// bean name for flyway configuration
	private static final String FLYWAY = BEAN_NAME_PREFIX;
	private static final String FLYWAY_PROPERTIES = BEAN_NAME_PREFIX + "Properties";
	private static final String FLYWAY_MIGRATION_INITIALIZER =
			BEAN_NAME_PREFIX + "MigrationInitializer";
	private static final String FLYWAY_VALIDATE_INITIALIZER =
			BEAN_NAME_PREFIX + "ValidateInitializer";
	private static final String FLYWAY_CONFIGURATION = BEAN_NAME_PREFIX + "Configuration";

	@Bean(name = FLYWAY)
	public Flyway flyway(
			@Qualifier(value = FLYWAY_CONFIGURATION)
					org.flywaydb.core.api.configuration.Configuration configuration) {
		return new Flyway(configuration);
	}

	@Profile({"!local"})
	@Bean(name = FLYWAY_VALIDATE_INITIALIZER)
	public FlywayMigrationInitializer flywayValidateInitializer(
			@Qualifier(value = FLYWAY) Flyway flyway) {
		return new FlywayMigrationInitializer(flyway, Flyway::validate);
	}

	@Profile({"!local"})
	@Bean(name = FLYWAY_MIGRATION_INITIALIZER)
	public FlywayMigrationInitializer flywayMigrationInitializer(
			@Qualifier(value = FLYWAY) Flyway flyway) {
		return new FlywayMigrationInitializer(flyway, Flyway::migrate);
	}

	@Bean(name = FLYWAY_PROPERTIES)
	@ConfigurationProperties(prefix = BASE_PROPERTY_PREFIX)
	public FlywayProperties flywayProperties() {
		return new FlywayProperties();
	}

	@Bean(name = FLYWAY_CONFIGURATION)
	public org.flywaydb.core.api.configuration.Configuration configuration(
			@Qualifier(value = FLYWAY_PROPERTIES) FlywayProperties flywayProperties,
			@Qualifier(value = EntityJpaDataSourceConfig.DATASOURCE_NAME) DataSource dataSource) {
		FluentConfiguration configuration = new FluentConfiguration();
		configuration.dataSource(dataSource);
		flywayProperties.getLocations().forEach(configuration::locations);
		return configuration;
	}
}
