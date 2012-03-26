package org.jforce.mule.redis;

import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.endpoint.OutboundEndpoint;
import org.mule.transport.AbstractMessageDispatcher;

public class RedisSchedulerMessageDispatcher extends AbstractMessageDispatcher {
    public RedisSchedulerMessageDispatcher(OutboundEndpoint endpoint) {
        super(endpoint);
    }

    @Override
    protected void doDispatch(MuleEvent event) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected MuleMessage doSend(MuleEvent event) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
