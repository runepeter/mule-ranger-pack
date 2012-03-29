package org.jforce.mule.redis;

import org.jforce.reschedule.Job;
import org.mule.DefaultMuleMessage;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.lifecycle.CreateException;
import org.mule.api.transport.Connector;
import org.mule.transport.AbstractPollingMessageReceiver;
import redis.clients.jedis.Jedis;

import java.util.Set;

public class RedisSchedulerMessageReceiver extends AbstractPollingMessageReceiver {

    private final RedisSchedulerConnector rescheduleConnector;

    public RedisSchedulerMessageReceiver(Connector connector, FlowConstruct flowConstruct, InboundEndpoint endpoint) throws CreateException {
        super(connector, flowConstruct, endpoint);
        this.rescheduleConnector = (RedisSchedulerConnector) connector;
    }

    @Override
    protected void poll() throws Exception {

        Jedis jedis = rescheduleConnector.getJedis();

        String schedule = rescheduleConnector.getSchedule();

        Set<String> hashKeys = jedis.zrangeByScore(schedule, 0, System.currentTimeMillis());
        for (String hashKey : hashKeys) {

            if (getLifecycleState().isStopping()) {
                break;
            }

            DefaultMuleMessage muleMessage = new DefaultMuleMessage(new Job(hashKey), connector.getMuleContext());
            routeMessage(muleMessage);
        }
    }

}
