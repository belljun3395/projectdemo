package com.example.demo.infra.flyway;

import static com.example.demo.config.AppConfig.BASE_PACKAGE;

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
	private static final String SERVICE_NAME = "demo";
	private static final String MODULE_NAME = "infra";
	public static final String MODULE_BASE_PACKAGE = BASE_PACKAGE + "." + MODULE_NAME;

	// base property prefix for flyway
	private static final String BASE_PROPERTY_PREFIX = SERVICE_NAME + "." + MODULE_NAME + ".flyway";

	// bean name for flyway configuration
	private static final String FLYWAY = SERVICE_NAME + "Flyway";
	private static final String FLYWAY_PROPERTIES = SERVICE_NAME + "FlywayProperties";
	private static final String FLYWAY_MIGRATION_INITIALIZER =
			SERVICE_NAME + "FlywayMigrationInitializer";
	private static final String FLYWAY_VALIDATE_INITIALIZER =
			SERVICE_NAME + "FlywayValidateInitializer";
	private static final String FLYWAY_CONFIGURATION = SERVICE_NAME + "FlywayConfiguration";

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
			@Qualifier(value = FLYWAY_PROPERTIES) FlywayProperties flywayProperties) {
		FluentConfiguration configuration = new FluentConfiguration();
		configuration.dataSource(
				flywayProperties.getUrl(), flywayProperties.getUser(), flywayProperties.getPassword());
		flywayProperties.getLocations().forEach(configuration::locations);
		return configuration;
	}
}
