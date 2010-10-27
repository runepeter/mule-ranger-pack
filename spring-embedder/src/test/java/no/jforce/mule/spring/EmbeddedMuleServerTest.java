package no.jforce.mule.spring;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

public class EmbeddedMuleServerTest
{

    @Test
    public void jalla() throws Exception
    {
        ApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);

        Thread.sleep(5000);
    }

    @Configuration
    public static class TestConfig
    {
        @Bean
        public EmbeddedMuleServer embeddedMuleServer()
        {
            return new EmbeddedMuleServer(new ClassPathResource("."));
        }

    }

}
