package no.jforce.mule.spring;

import org.mule.api.MuleContext;
import org.mule.api.client.LocalMuleClient;
import org.mule.api.config.ConfigurationException;
import org.mule.api.config.MuleProperties;
import org.mule.api.config.ThreadingProfile;
import org.mule.api.context.MuleContextFactory;
import org.mule.api.registry.MuleRegistry;
import org.mule.config.spring.SpringXmlConfigurationBuilder;
import org.mule.context.DefaultMuleContextFactory;
import org.mule.management.stats.AllStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class EmbeddedMuleServer implements InitializingBean, DisposableBean, ApplicationContextAware
{
    private final Logger logger = LoggerFactory.getLogger(EmbeddedMuleServer.class);

    private final Resource configResource;

    private MuleContext muleContext;

    private ApplicationContext applicationContext;

    public EmbeddedMuleServer(final Resource configResource)
    {
        this.configResource = configResource;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }

    @Override
    public void destroy() throws Exception
    {
        if (!(muleContext.isStopped() || muleContext.isStopping()))
        {
            logger.info("Stopping embedded Mule server...");
            muleContext.stop();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        final SpringXmlConfigurationBuilder configBuilder = new JallaConfigBuilder(configResource.getFilename());
        configBuilder.setParentContext(applicationContext);

        final MuleContextFactory contextFactory = new DefaultMuleContextFactory();

        this.muleContext = contextFactory.createMuleContext(configBuilder);

        muleContext.getStatistics().setEnabled(true);

        if (!(muleContext.isStarting() || muleContext.isStarted()))
        {
            logger.info("Starting embedded Mule server...");
            muleContext.start();
        }

    }

    @Bean
    public MuleContext getMuleContext()
    {
        return muleContext;
    }

    @Bean
    public MuleRegistry getMuleRegistry()
    {
        return muleContext.getRegistry();
    }

    @Bean
    public LocalMuleClient getLocalMuleClient()
    {
        return muleContext.getClient();
    }

    @Bean
    public AllStatistics getStatistics()
    {
        return muleContext.getStatistics();
    }

    public static class JallaConfigBuilder extends SpringXmlConfigurationBuilder
    {

        public JallaConfigBuilder(String configResources)
                throws ConfigurationException
        {
            super(configResources);
        }

        @Override
        protected void doConfigure(MuleContext muleContext) throws Exception
        {
            super.doConfigure(muleContext);

            //Object o = registry.lookupObject(MuleProperties.OBJECT_DEFAULT_MESSAGE_RECEIVER_THREADING_PROFILE);
            Object o = registry.lookupObject(MuleProperties.OBJECT_DEFAULT_SERVICE_THREADING_PROFILE);
            jalla((ThreadingProfile) registry.lookupObject(MuleProperties.OBJECT_DEFAULT_THREADING_PROFILE));
            jalla((ThreadingProfile) registry.lookupObject(MuleProperties.OBJECT_DEFAULT_SERVICE_THREADING_PROFILE));
            jalla((ThreadingProfile) registry.lookupObject(MuleProperties.OBJECT_DEFAULT_MESSAGE_DISPATCHER_THREADING_PROFILE));
            jalla((ThreadingProfile) registry.lookupObject(MuleProperties.OBJECT_DEFAULT_MESSAGE_RECEIVER_THREADING_PROFILE));
            jalla((ThreadingProfile) registry.lookupObject(MuleProperties.OBJECT_DEFAULT_MESSAGE_REQUESTER_THREADING_PROFILE));
        }

        private void jalla(final ThreadingProfile profile) throws Exception
        {
            profile.setRejectedExecutionHandler(new RejectedExecutionHandler()
            {
                @Override
                public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor)
                {
                    System.err.println("JALLA, BALLA - " + profile);
                }
            });
        }

    }

}
