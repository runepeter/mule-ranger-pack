package org.brylex.mule.jetty;

import org.mule.api.MuleException;
import org.mule.api.endpoint.OutboundEndpoint;
import org.mule.api.transport.MessageDispatcher;
import org.mule.transport.AbstractMessageDispatcherFactory;

public class EmbeddedJettyWebSocketMessageDispatcherFactory extends AbstractMessageDispatcherFactory {
    @Override
    public MessageDispatcher create(OutboundEndpoint endpoint) throws MuleException {
        return new EmbeddedJettyWebSocketMessageDispatcher(endpoint);
    }
}
