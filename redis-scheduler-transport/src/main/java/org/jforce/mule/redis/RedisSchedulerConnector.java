package org.jforce.mule.redis;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.transport.AbstractConnector;

public class RedisSchedulerConnector extends AbstractConnector {

    public RedisSchedulerConnector(final MuleContext context) {
        super(context);
    }

    @Override
    protected void doInitialise() throws InitialisationException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void doDispose() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void doStart() throws MuleException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void doStop() throws MuleException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void doConnect() throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void doDisconnect() throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getProtocol() {
        return "redis-scheduler";
    }
}
