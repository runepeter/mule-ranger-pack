package org.brylex.mule.jetty;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.lifecycle.CreateException;
import org.mule.api.transport.Connector;
import org.mule.transport.AbstractMessageReceiver;

import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkException;

public class EmbeddedJettyMessageReceiver extends AbstractMessageReceiver {

    private final EmbeddedJettyConnector connector;

    /**
     * Creates the Message Receiver
     *
     * @param connector     the endpoint that created this listener
     * @param flowConstruct the flow construct to associate with the receiver.
     * @param endpoint      the provider contains the endpointUri on which the receiver
     *                      will listen on. The endpointUri can be anything and is specific to
     *                      the receiver implementation i.e. an email address, a directory, a
     *                      jms destination or port address.
     * @see org.mule.api.construct.FlowConstruct
     * @see org.mule.api.endpoint.InboundEndpoint
     */
    public EmbeddedJettyMessageReceiver(final Connector connector,
                                        final FlowConstruct flowConstruct,
                                        final InboundEndpoint endpoint) throws CreateException {
        super(connector, flowConstruct, endpoint);
        this.connector = (EmbeddedJettyConnector) connector;
    }

    public EmbeddedJettyConnector getEmbeddedJettyConnector() {
        return connector;
    }

    public void routeMessageAsync(final MuleMessage message, final MuleWebSocketReplyTo replyTo)
    {
        try
        {
            getWorkManager().scheduleWork(new Work() {

                public void run()
                {
                    try
                    {
                        MuleMessage threadSafeMessage = new DefaultMuleMessage(message);
                        routeMessage(threadSafeMessage);
                    }
                    catch (MuleException e)
                    {
                        e.printStackTrace();
                        System.err.println("ERROR: should call onError method on WebSocket handler.");
                    }
                }

                public void release()
                {
                    // nothing to clean up
                }
            });
        }
        catch (WorkException e)
        {
            getConnector().getMuleContext().getExceptionListener().handleException(e);
        }
    }
}
