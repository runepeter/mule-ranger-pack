package org.brylex.mule.jetty;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.mule.api.MuleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class EmbeddedJettyWebSocketServlet extends WebSocketServlet implements WebSocketCreator {

    private final Logger logger = LoggerFactory.getLogger(EmbeddedJettyWebSocketServlet.class);

    private final EmbeddedJettyMessageReceiver messageReceiver;

    private final Map<String, Set<Session>> sessionMap = new HashMap<>();

    public EmbeddedJettyWebSocketServlet(final EmbeddedJettyMessageReceiver messageReceiver) {
        this.messageReceiver = messageReceiver;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(10000);
        factory.setCreator(this);
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        return null;//new MuleWebSocketServerEndpoint(messageReceiver, this);
    }

    public Set<Session> getSessions(final String clientId) {

        if (sessionMap.containsKey(clientId)) {
            return new HashSet<>(sessionMap.get(clientId));
        }

        return Collections.emptySet();
    }

    public void registerSession(final String username, final Session session) {
        Set<Session> sessions = sessionMap.get(username);
        if (sessions == null) {
            sessionMap.put(username, sessions = new HashSet<>());
        }
        sessions.add(session);
        logger.info("Registered WebSocket session for user {}.", username);
    }

    public void unregisterSession(final String username, final Session session) {
        Set<Session> sessions = sessionMap.get(username);
        if (sessions != null) {
            for (Iterator<Session> it = sessions.iterator(); it.hasNext(); ) {
                if (it.next().equals(session)) {
                    it.remove();
                    logger.info("Unregistered session for user {}.", username);
                    break;
                }
            }
        }
    }

    public static class JallaConfigurator extends ServerEndpointConfig.Configurator {

        @Override
        public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
            System.err.println("HANDSHAKE");
            super.modifyHandshake(sec, request, response);
        }
    }

}
