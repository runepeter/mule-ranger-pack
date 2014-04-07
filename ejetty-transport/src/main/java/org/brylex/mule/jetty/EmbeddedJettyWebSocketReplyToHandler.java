package org.brylex.mule.jetty;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.transport.DefaultReplyToHandler;

public class EmbeddedJettyWebSocketReplyToHandler extends DefaultReplyToHandler {

    public EmbeddedJettyWebSocketReplyToHandler(MuleContext muleContext) {
        super(muleContext);
    }

    @Override
    public void processReplyTo(MuleEvent event, MuleMessage returnMessage, Object replyTo) throws MuleException {

        MuleWebSocketReplyTo reply = (MuleWebSocketReplyTo) replyTo;
        DefaultMuleMessage message = new DefaultMuleMessage(returnMessage);
        reply.reply(message);
    }
}
