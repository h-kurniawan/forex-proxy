package paidy.forex.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Getter
@Setter
@ConfigurationProperties(prefix = "service.forex")
@Configuration
public class ForexConfiguration {
    private URI baseUri;
    private String accessToken;
}