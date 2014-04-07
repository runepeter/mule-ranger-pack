package org.brylex.mule.jetty;

import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.endpoint.OutboundEndpoint;
import org.mule.transport.AbstractMessageDispatcher;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class EmbeddedJettyWebSocketMessageDispatcher extends AbstractMessageDispatcher {

    public EmbeddedJettyWebSocketMessageDispatcher(OutboundEndpoint endpoint) {
        super(endpoint);
    }

    @Override
    protected void doDispatch(MuleEvent event) throws Exception {

        final String webSocketPath = EmbeddedJettyConnector.toWebSocketPath(endpoint);

        final MuleMessage message = event.getMessage();
        final EmbeddedJettyConnector jettyConnector = (EmbeddedJettyConnector) getConnector();
        final String payload = message.getPayloadAsString();

        Set<Session> recipientSessions;

        final String receiver = (String) getEndpoint().getProperty("receiver");
        if (receiver != null) {
            recipientSessions = jettyConnector.getWebSocketSessionsForUser(webSocketPath, receiver);
        } else {
            recipientSessions = jettyConnector.getWebSocketSessions(webSocketPath);
        }

        for (Session session : recipientSessions) {

            final RemoteEndpoint.Async remote = session.getAsyncRemote();

            String json = "{\"data\":" + payload + "}";

            Future<Void> future = remote.sendText(json);
            future.get(10, TimeUnit.SECONDS);
        }
    }

    @Override
    protected MuleMessage doSend(MuleEvent event) throws Exception {
        throw new UnsupportedOperationException();
    }
}
