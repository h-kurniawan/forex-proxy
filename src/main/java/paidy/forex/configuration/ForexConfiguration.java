package paidy.forex.configuration;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "service.forex")
@Configuration 
public class ForexConfiguration {
    private URI baseUri;
    private String accessToken;
}