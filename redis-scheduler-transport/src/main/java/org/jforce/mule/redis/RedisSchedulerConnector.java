package org.jforce.mule.redis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.transport.AbstractConnector;
import redis.clients.jedis.Jedis;

public class RedisSchedulerConnector extends AbstractConnector {

    private static Log logger = LogFactory.getLog(RedisSchedulerConnector.class);

    public static final String RESCHEDULE = "reschedule";
    
    private Jedis jedis;

    private String redisHost;
    private String schedule;
    private long pollingFrequency;

    public RedisSchedulerConnector(final MuleContext context) {
        super(context);
    }

    Jedis getJedis() {
        return jedis;
    }

    public void setPollingFrequency(long pollingFrequency) {
        this.pollingFrequency = pollingFrequency;
    }

    long getPollingFrequency() {
        return pollingFrequency;
    }

    String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(final String redisHost) {
        this.redisHost = redisHost;
    }

    String getSchedule() {
        return schedule;
    }

    public void setSchedule(final String schedule) {
        this.schedule = schedule;
    }

    @Override
    protected void doInitialise() throws InitialisationException {
    }

    @Override
    protected void doDispose() {
    }

    @Override
    protected void doStart() throws MuleException {
    }

    @Override
    protected void doStop() throws MuleException {
    }

    @Override
    protected void doConnect() throws Exception {
        if (jedis == null) {
            jedis = new Jedis(redisHost);
        }
        //jedis.connect();
        logger.info("Successfully connected to REDIS server at [" + redisHost + "].");
    }

    @Override
    protected void doDisconnect() throws Exception {
        if (jedis != null) {
            jedis.disconnect();
            jedis = null;
            logger.info("Disconnected from REDIS server.");
        }
    }

    @Override
    public String getProtocol() {
        return RESCHEDULE;
    }
}
