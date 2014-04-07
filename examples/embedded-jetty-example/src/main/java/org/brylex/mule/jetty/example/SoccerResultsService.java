package org.brylex.mule.jetty.example;

import com.google.common.collect.Sets;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.impl.ReaderBasedParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.mule.api.annotations.param.InboundHeaders;
import org.mule.api.annotations.param.Payload;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SoccerResultsService {

    private final SoccerResultsCache cache;
    private final SubscriptionService subscriptionService;

    private final ObjectMapper mapper;

    public SoccerResultsService(SoccerResultsCache cache, SubscriptionService subscriptionService) {
        this.cache = cache;
        this.subscriptionService = subscriptionService;
        this.mapper = new ObjectMapper();
    }

    public Object handle(
            @Payload String request,
            @InboundHeaders("*") Map<String, Object> headers
    ) {
        boolean isPersonalFeed = headers.get("MULE_ORIGINATING_ENDPOINT").equals("endpoint.ejetty.personal");
        String username = (String) headers.get("ejetty.ws.session.user_principal");

        try {

            WebSocketRequest webSocketRequest = mapper.readValue(request, WebSocketRequest.class);

            switch (webSocketRequest.getType()) {

                case "get_results":

                    if (isPersonalFeed) {

                        final Set<SoccerResult> results = Sets.newHashSet();

                        for (SoccerResult soccerResult : cache.all()) {
                            final String league = soccerResult.getSoccerMatch().getLeague();
                            if (subscriptionService.subscribesToLeague(username, league)) {
                                results.add(soccerResult);
                            }
                        }

                        return results;
                    } else {
                        return cache.all();
                    }

                case "subscribe_league":

                    subscriptionService.subscribeToLeague(username, webSocketRequest.getArguments().get("league"));

                    break;
                default:
                    System.err.println("Unknown request type: [" + webSocketRequest.getType() + "]");
            }

        } catch (IOException e) {
            throw new RuntimeException("Unable to parse WebSocket request.", e);
        }

        return Sets.newHashSet();
    }
}
