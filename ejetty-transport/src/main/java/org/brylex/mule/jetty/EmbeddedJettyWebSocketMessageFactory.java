package org.brylex.mule.jetty;

import org.mule.api.MuleContext;
import org.mule.transport.AbstractMuleMessageFactory;

public class EmbeddedJettyWebSocketMessageFactory extends AbstractMuleMessageFactory {

    public EmbeddedJettyWebSocketMessageFactory(MuleContext context) {
        super(context);
    }

    @Override
    protected Class<?>[] getSupportedTransportMessageTypes() {
        return new Class<?>[]{String.class};
    }

    @Override
    protected Object extractPayload(Object transportMessage, String encoding) throws Exception {
        return "" + transportMessage;
    }
}
