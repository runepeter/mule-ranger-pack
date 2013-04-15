package no.jforce.mule.spring;

import org.mule.api.MuleContext;
import org.mule.api.client.LocalMuleClient;
import org.mule.api.context.MuleContextBuilder;
import org.mule.api.registry.MuleRegistry;
import org.mule.config.ConfigResource;
import org.mule.config.spring.SpringXmlConfigurationBuilder;
import org.mule.context.DefaultMuleContextBuilder;
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

import java.net.URL;

public class EmbeddedMuleServer implements InitializingBean, DisposableBean, ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger(EmbeddedMuleServer.class);

    private final Resource configResource;

    private MuleContext muleContext;

    private ApplicationContext applicationContext;

    private boolean enableStatistics = true;

    public EmbeddedMuleServer(final Resource configResource) {
        this.configResource = configResource;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        if (!(muleContext.isStopped() || muleContext.isStopping())) {
            logger.info("Stopping embedded Mule server...");
            muleContext.stop();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        URL url = configResource.getURL();
        logger.info("Mule Configuration url: [" + url + "]");

        final SpringXmlConfigurationBuilder configBuilder = new SpringXmlConfigurationBuilder(new ConfigResource[]{new ConfigResource(url)});
        configBuilder.setParentContext(applicationContext);

        MuleContextBuilder contextBuilder = new DefaultMuleContextBuilder();
        this.muleContext = new DefaultMuleContextFactory().createMuleContext(configBuilder, contextBuilder);

        muleContext.getStatistics().setEnabled(isEnableStatistics());

        if (!(muleContext.isStarting() || muleContext.isStarted())) {
            logger.info("Starting embedded Mule server...");
            muleContext.start();
        }

    }

    @Bean
    public MuleContext getMuleContext() {
        return muleContext;
    }

    @Bean
    public MuleRegistry getMuleRegistry() {
        return muleContext.getRegistry();
    }

    @Bean
    public LocalMuleClient getLocalMuleClient() {
        return muleContext.getClient();
    }

    @Bean
    public AllStatistics getStatistics() {
        return muleContext.getStatistics();
    }

    public boolean isEnableStatistics() {
        return enableStatistics;
    }

    public void setEnableStatistics(boolean enableStatistics) {
        this.enableStatistics = enableStatistics;
    }
}
