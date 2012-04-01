package org.jforce.mule.redis;

import org.jforce.reschedule.Job;
import org.mule.DefaultMuleMessage;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.lifecycle.CreateException;
import org.mule.api.transport.Connector;
import org.mule.transport.AbstractPollingMessageReceiver;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.data.redis.support.collections.DefaultRedisZSet;
import org.springframework.data.redis.support.collections.RedisMap;
import org.springframework.data.redis.support.collections.RedisZSet;

import java.nio.charset.Charset;
import java.util.Set;

public class RedisSchedulerMessageReceiver extends AbstractPollingMessageReceiver {

    private final RedisSchedulerConnector rescheduleConnector;
    private final InboundEndpoint endpoint;
    private RedisMap<String, Job> jobMap;
    private RedisZSet<String> triggerSet;
    private final String scheduleId;

    public RedisSchedulerMessageReceiver(Connector connector, FlowConstruct flowConstruct, InboundEndpoint endpoint) throws CreateException {
        super(connector, flowConstruct, endpoint);
        this.rescheduleConnector = (RedisSchedulerConnector) connector;
        this.endpoint = endpoint;
        this.scheduleId = endpoint.getEndpointURI().getAddress();
    }

    @Override
    protected void doConnect() throws Exception {

        this.triggerSet = new DefaultRedisZSet<String>(scheduleId, new StringRedisTemplate(rescheduleConnector.getConnectionFactory()));
        this.jobMap = new DefaultRedisMap<String, Job>("jobs", createRedisTemplate());

        doStart();
    }

    @Override
    protected void poll() throws Exception {

        Set<String> executableJobs = triggerSet.rangeByScore(0, System.currentTimeMillis());

        logger.info("Jobs #: " + executableJobs.size());
        for (String jobRef : executableJobs) {

            if (getLifecycleState().isStopping()) {
                break;
            }

            DefaultMuleMessage muleMessage = new DefaultMuleMessage(jobMap.get(jobRef), connector.getMuleContext());
            routeMessage(muleMessage);

            boolean removed = triggerSet.remove(jobRef);
            if (!removed) {
                logger.error("Trigger [" + jobRef + "] not removed after processing.");
            }
        }

    }

    private RedisTemplate<String, Job> createRedisTemplate() {

        StringRedisSerializer keySerializer = new StringRedisSerializer(Charset.forName("UTF-8"));
        GenericToStringSerializer<Job> valueSerializer = new GenericToStringSerializer<Job>(Job.class, Charset.forName("UTF-8"));

        RedisTemplate<String, Job> redisTemplate = new RedisTemplate<String, Job>();
        redisTemplate.setConnectionFactory(rescheduleConnector.getConnectionFactory());
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);

        return redisTemplate;
    }
}
