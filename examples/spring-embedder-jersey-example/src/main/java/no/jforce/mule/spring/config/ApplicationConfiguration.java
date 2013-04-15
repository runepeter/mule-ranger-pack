package no.jforce.mule.spring.config;

import no.jforce.mule.spring.EmbeddedMuleServer;
import org.codehaus.jackson.map.ObjectMapper;
import org.mule.api.MuleContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@ComponentScan({"no.jforce.mule.spring.embedder.example.jersey"})
public class ApplicationConfiguration {

    @Bean
    public EmbeddedMuleServer embeddedMuleServer() {
        return new EmbeddedMuleServer(new ClassPathResource("/mule-context.xml"));
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public MuleContext muleContext() {
        return embeddedMuleServer().getMuleContext();
    }

}
