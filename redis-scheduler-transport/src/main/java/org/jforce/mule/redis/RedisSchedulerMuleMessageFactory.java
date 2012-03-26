package org.jforce.mule.redis;

import org.mule.api.MuleContext;
import org.mule.transport.AbstractMuleMessageFactory;

public class RedisSchedulerMuleMessageFactory extends AbstractMuleMessageFactory {

    public RedisSchedulerMuleMessageFactory(MuleContext context) {
        super(context);
    }

    @Override
    protected Class<?>[] getSupportedTransportMessageTypes() {
        return new Class<?>[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected Object extractPayload(Object transportMessage, String encoding) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
