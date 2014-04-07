package org.brylex.mule.jetty.example;

import com.google.common.collect.Sets;
import org.brylex.mule.jetty.EmbeddedJettyConnector;
import org.mule.api.annotations.param.OutboundHeaders;
import org.mule.api.annotations.param.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class SoccerResultMulticaster {

    private final SubscriptionService subscriptionService;

    public SoccerResultMulticaster(final SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    public Set<SoccerResultWithReceiver> replicate(@Payload SoccerResult soccerResult) {

        final String league = soccerResult.getSoccerMatch().getLeague();

        Set<SoccerResultWithReceiver> subscribers = Sets.newHashSet();
        for (String subscriber : subscriptionService.getLeagueSubscribers(league)) {
            subscribers.add(new SoccerResultWithReceiver(subscriber, soccerResult));
        }

        return subscribers;
    }
}
