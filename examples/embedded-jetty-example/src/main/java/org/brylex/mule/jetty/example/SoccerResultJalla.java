package org.brylex.mule.jetty.example;

import org.mule.api.annotations.param.OutboundHeaders;
import org.mule.api.annotations.param.Payload;

import java.util.Map;

public class SoccerResultJalla {

    public SoccerResult jalla(@Payload SoccerResultWithReceiver soccerResultWithReceiver,
                              @OutboundHeaders Map<String, Object> headers
    ) {
        headers.put("jalla", "balla");

        return soccerResultWithReceiver.getSoccerResult();
    }
}
