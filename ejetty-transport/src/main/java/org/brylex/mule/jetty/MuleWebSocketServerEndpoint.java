package org.brylex.mule.jetty;

import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.transport.PropertyScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.websocket.*;

public class MuleWebSocketServerEndpoint extends Endpoint {

    private final Logger logger = LoggerFactory.getLogger(MuleWebSocketServerEndpoint.class);

    private EmbeddedJettyMessageReceiver messageReceiver;

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

        this.messageReceiver = (EmbeddedJettyMessageReceiver) endpointConfig.getUserProperties().get("mule.ejetty.message_receiver");

        session.addMessageHandler(new EmbeddedJettyMessageHandler(session, messageReceiver));
        logger.info("Received connection from {}.", session.getUserPrincipal());

        messageReceiver.getEmbeddedJettyConnector().registerWebSocketSession(session);
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        super.onClose(session, closeReason);

        messageReceiver.getEmbeddedJettyConnector().unregisterWebSocketSession(session);
    }

    class EmbeddedJettyMessageHandler implements MessageHandler.Whole<String> {

        private final Session session;
        private final EmbeddedJettyMessageReceiver messageReceiver;

        EmbeddedJettyMessageHandler(final Session session, final EmbeddedJettyMessageReceiver messageReceiver) {
            this.session = session;
            this.messageReceiver = messageReceiver;
        }

        @Override
        public void onMessage(String message) {
            try {
                MuleMessage muleMessage = messageReceiver.createMuleMessage(message);

                if (session.getUserPrincipal() != null) {
                    muleMessage.setProperty("ejetty.ws.session.user_principal", session.getUserPrincipal().getName(), PropertyScope.INBOUND);
                }

                muleMessage.setProperty("ejetty.ws.session.id", session.getId(), PropertyScope.INBOUND);

                MuleWebSocketReplyTo replyTo = new MuleWebSocketReplyTo(session);
                muleMessage.setReplyTo(replyTo);

                messageReceiver.routeMessageAsync(muleMessage, replyTo);

            } catch (MuleException e) {
                throw new RuntimeException("Unable to handle message from client.", e);
            }
        }
    }
}
