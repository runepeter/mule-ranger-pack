package org.jforce.mule.redis;

import org.mule.api.construct.FlowConstruct;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.lifecycle.CreateException;
import org.mule.api.transport.Connector;
import org.mule.transport.AbstractPollingMessageReceiver;

public class RedisSchedulerMessageReceiver extends AbstractPollingMessageReceiver {

    public RedisSchedulerMessageReceiver(Connector connector, FlowConstruct flowConstruct, InboundEndpoint endpoint) throws CreateException {
        super(connector, flowConstruct, endpoint);
    }

    @Override
    protected void poll() throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
