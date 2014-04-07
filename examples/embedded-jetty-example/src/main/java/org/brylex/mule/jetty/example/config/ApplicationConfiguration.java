package org.brylex.mule.jetty.example.config;

import no.jforce.mule.spring.EmbeddedMuleServer;
import org.brylex.mule.jetty.example.*;
import org.mule.config.endpoint.RegistryBackedAnnotationsParserFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public EmbeddedMuleServer embeddedMuleServer() throws Exception {
        return new EmbeddedMuleServer(new ClassPathResource("/mule-context.xml"));
    }

    @Bean
    public SoccerResultsCache soccerResultsCache() {
        return new SoccerResultsCache();
    }

    @Bean
    public SubscriptionService subscriptionService() {
        return new SubscriptionService();
    }

    @Bean
    public SoccerResultJalla soccerResultJalla() {
        return new SoccerResultJalla();
    }

    @Bean
    public SoccerResultMulticaster soccerResultMulticaster(SubscriptionService subscriptionService) {
        return new SoccerResultMulticaster(subscriptionService);
    }

    @Bean
    public EsperEventToWebSocketTransformer esper2WebSocket(SoccerResultsCache cache) {
        return new EsperEventToWebSocketTransformer(cache);
    }

    @Bean
    public EsperEventToSoccerResult esper2result() {
        return new EsperEventToSoccerResult();
    }

    @Bean
    public SoccerResultsService soccerResultsService(final SoccerResultsCache cache, final SubscriptionService subscriptionService) {
        return new SoccerResultsService(cache, subscriptionService);
    }

}
