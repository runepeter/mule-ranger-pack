package org.jforce.mule.redis;

import org.jforce.reschedule.Job;
import org.mule.api.MuleContext;
import org.mule.transport.AbstractMuleMessageFactory;

public class RedisSchedulerMuleMessageFactory extends AbstractMuleMessageFactory {

    public RedisSchedulerMuleMessageFactory(MuleContext context) {
        super(context);
    }

    @Override
    protected Class<?>[] getSupportedTransportMessageTypes() {
        return new Class<?>[]{Job.class};
    }

    @Override
    protected Object extractPayload(Object transportMessage, String encoding) throws Exception {
        return transportMessage;
    }
}
