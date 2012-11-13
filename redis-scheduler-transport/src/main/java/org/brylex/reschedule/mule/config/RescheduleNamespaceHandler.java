package org.brylex.reschedule.mule.config;

import org.jforce.mule.redis.RedisSchedulerConnector;
import org.mule.config.spring.handlers.AbstractMuleNamespaceHandler;

public class RescheduleNamespaceHandler extends AbstractMuleNamespaceHandler {
    @Override
    public void init() {
        registerStandardTransportEndpoints(RedisSchedulerConnector.RESCHEDULE, new String[]{"schedule"});
        registerConnectorDefinitionParser(RedisSchedulerConnector.class);
    }
}
