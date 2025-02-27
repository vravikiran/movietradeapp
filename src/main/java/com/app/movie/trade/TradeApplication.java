package com.app.movie.trade;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;
import org.springframework.scheduling.annotation.EnableAsync;

import com.app.movie.trade.security.AppConfigPropertySource;

@SpringBootApplication
@EnableAsync
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
public class TradeApplication {
	private static final String DYNAMIC_PROPERTIES_SOURCE_NAME = "dynamicProperties";
	
	@Autowired
	AppConfigPropertySource appConfigPropertySource;

	public static void main(String[] args) {
		SpringApplication.run(TradeApplication.class, args);
	}

	@Autowired
	void contributeToTheEnvironment(ConfigurableEnvironment environment) {
		appConfigPropertySource.init();
		MutablePropertySources propertySources = environment.getPropertySources();
		if (!propertySources.contains(DYNAMIC_PROPERTIES_SOURCE_NAME)) {
			Map<String, Object> dynamicProperties = new HashMap<>();
			Object[] names = appConfigPropertySource.getPropertyNames();
			for (Object name : names) {
				dynamicProperties.put(name.toString(), appConfigPropertySource.getProperty(name.toString()));
			}
			propertySources.addFirst(new MapPropertySource(DYNAMIC_PROPERTIES_SOURCE_NAME, dynamicProperties));
		}
	}
}
