package org.brylex.mule.jetty.example;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.mule.api.MuleMessage;
import org.mule.api.annotations.ContainsTransformerMethods;
import org.mule.api.annotations.Transformer;
import org.mule.api.annotations.param.OutboundHeaders;
import org.mule.api.annotations.param.Payload;
import org.mule.api.transformer.TransformerException;
import org.mule.transport.DefaultMuleMessageFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@ContainsTransformerMethods
public class SubscriptionService {

    private final Map<String, Set<String>> leagueMap = Maps.newConcurrentMap();

    public void subscribeToLeague(final String user, final String league) {
        System.err.println(user + " is now subscribing to league -> " + league);
        Set<String> subscribers = leagueMap.get(league);
        if (subscribers == null) {
            leagueMap.put(league, subscribers = Sets.newHashSet());
        }
        subscribers.add(user);
    }

    @Transformer
    public Set<SoccerResult> replicateForAllSubscribers(
            @Payload SoccerResult soccerResult,
            @OutboundHeaders Map<String, Object> headers) {

        final String league = soccerResult.getSoccerMatch().getLeague();

        if (leagueMap.containsKey(league)) {
            return Sets.newHashSet(soccerResult);
        }

        return Sets.newHashSet();
    }

    public Set<String> getLeagueSubscribers(final String league) {
        final Set<String> subscribers = leagueMap.get(league);
        return Collections.unmodifiableSet(subscribers != null ? subscribers : Collections.<String>emptySet());
    }

    public boolean subscribesToLeague(final String username, final String league) {

        if (!leagueMap.containsKey(league)) {
            return false;
        }

        return leagueMap.get(league).contains(username);
    }
}
