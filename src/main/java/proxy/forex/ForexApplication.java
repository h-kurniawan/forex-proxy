package proxy.forex;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootApplication
public class ForexApplication {
	@Value("${cache.name}")
	private String cacheName;
	
	public static void main(String[] args) {
		SpringApplication.run(ForexApplication.class, args);
	}

	@Bean
	public ObjectMapper objectMapper() {
		var om = new ObjectMapper();
		om.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
		om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		om.registerModule(new JavaTimeModule());

		return om;
	}

	@Bean
	CacheManager cacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();
		cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache(cacheName)));
		return cacheManager;
	}
}
