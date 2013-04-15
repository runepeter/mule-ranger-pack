package org.jforce.mule.redis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.config.ThreadingProfile;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.transport.MessageReceiver;
import org.mule.transport.AbstractConnector;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisShardInfo;

public class RedisSchedulerConnector extends AbstractConnector {

    private static Log logger = LogFactory.getLog(RedisSchedulerConnector.class);

    public static final String RESCHEDULE = "reschedule";

    private RedisConnection connection;
    private RedisConnectionFactory connectionFactory;

    private String redisHost;
    private Integer redisPort;

    private String schedule;
    private long pollingFrequency;

    public RedisSchedulerConnector(final MuleContext context) {
        super(context);
    }

    /*@Override
    public ThreadingProfile getReceiverThreadingProfile() {
        ThreadingProfile threadingProfile = super.getReceiverThreadingProfile();
        threadingProfile.setDoThreading(false);
        threadingProfile.setMuleContext(getMuleContext());
        threadingProfile.setMaxThreadsActive(1);
        return threadingProfile;
    }*/

    RedisConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    RedisConnection getConnection() {
        return connection;
    }

    public void setPollingFrequency(long pollingFrequency) {
        this.pollingFrequency = pollingFrequency;
    }

    long getPollingFrequency() {
        return pollingFrequency;
    }

    public void setRedisHost(final String redisHost) {
        this.redisHost = redisHost;
    }

    public void setRedisPort(int redisPort) {
        this.redisPort = redisPort;
    }

    String getSchedule() {
        return schedule;
    }

    public void setSchedule(final String schedule) {
        this.schedule = schedule;
    }

    @Override
    protected void doInitialise() throws InitialisationException {

        if (connectionFactory == null) {
            this.connectionFactory = createConnectionFactory();
        }

    }

    @Override
    protected void doDispose() {
        closeRedisConnection();
    }

    @Override
    protected void doStart() throws MuleException {
    }

    @Override
    protected void doStop() throws MuleException {
        closeRedisConnection();
    }

    @Override
    protected void doConnect() throws Exception {
        this.connection = connectionFactory.getConnection();
    }

    @Override
    protected void doDisconnect() throws Exception {
        closeRedisConnection();
    }

    private void closeRedisConnection() {
        try {

            if (connection != null) {
                try {
                    connection.close();
                } catch (DataAccessException e) {
                    logger.error("Connection failed to close properly.", e);
                }
            }

        } finally {
            this.connection = null;
        }
    }

    @Override
    public String getProtocol() {
        return RESCHEDULE;
    }

    private RedisConnectionFactory createConnectionFactory() {

        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setUsePool(false);
        factory.setHostName(redisHost);

        if (redisPort != null) {
            factory.setPort(redisPort);
            factory.setShardInfo(new JedisShardInfo(redisHost, redisPort));
        } else {
            factory.setShardInfo(new JedisShardInfo(redisHost));
        }

        return factory;
    }
}
