package no.jforce.mule.spring;

import org.mule.api.MuleContext;
import org.mule.api.client.LocalMuleClient;
import org.mule.api.context.MuleContextFactory;
import org.mule.config.spring.SpringXmlConfigurationBuilder;
import org.mule.context.DefaultMuleContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

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
        final SpringXmlConfigurationBuilder configBuilder = new SpringXmlConfigurationBuilder(configResource.getFilename());
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
    public LocalMuleClient getLocalMuleClient()
    {
        return muleContext.getClient();
    }

    @Bean
    public Object getMuleConfiguration()
    {
        return muleContext.getConfiguration();
    }

}
