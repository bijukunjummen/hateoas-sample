package univ.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import java.net.URI;

@Configuration
public class CustomizedRestMvcConfiguration extends RepositoryRestMvcConfiguration {

	@Override
	public RepositoryRestConfiguration config() {
		RepositoryRestConfiguration config = super.config();
		config.setBaseUri(URI.create("/api"));
		return config;
	}
}