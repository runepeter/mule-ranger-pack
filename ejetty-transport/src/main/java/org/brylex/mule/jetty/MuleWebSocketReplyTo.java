package org.brylex.mule.jetty;

import org.mule.api.MuleMessage;

import javax.websocket.Session;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class MuleWebSocketReplyTo {

    private final Session session;

    public MuleWebSocketReplyTo(final Session session) {
        this.session = session;
    }

    public void reply(MuleMessage message) {
        try {

            String json = "{\"data\":" + message.getPayloadAsString() + "}";

            Future<Void> future = session.getAsyncRemote().sendText(json);
            future.get(10, TimeUnit.SECONDS);

        } catch (Exception e) {
            throw new RuntimeException("Unable to send reply message.", e);
        }
    }
}
