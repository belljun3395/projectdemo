package com.example.demo.app;

import com.example.demo.data.mysql.MysqlConfig;
import com.example.demo.infra.flyway.FlywayConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = AppConfig.BASE_PACKAGE)
@Import(value = {MysqlConfig.class, FlywayConfig.class})
public class AppConfig {

	public static final String BASE_PACKAGE = "com.example.demo.data.mysql";
	public static final String SERVICE_NAME = "demo";
	public static final String MODULE_NAME = "app";
	public static final String BEAN_NAME_PREFIX = SERVICE_NAME + MODULE_NAME;
	public static final String PROPERTY_PREFIX = SERVICE_NAME + "." + MODULE_NAME;
}
