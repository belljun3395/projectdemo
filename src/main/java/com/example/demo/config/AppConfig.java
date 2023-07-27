package com.example.demo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = AppConfig.BASE_PACKAGE)
public class AppConfig {

	public static final String BASE_PACKAGE = "com.example.demo";
}
