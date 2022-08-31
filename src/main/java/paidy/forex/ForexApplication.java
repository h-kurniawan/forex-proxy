package paidy.forex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;

@SpringBootApplication
public class ForexApplication {
	public static void main(String[] args) {
		SpringApplication.run(ForexApplication.class, args);
	}

	@Bean
	public ObjectMapper objectMapper() {
		var om = new ObjectMapper();
		om.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
		om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		return om;
	}	
}
