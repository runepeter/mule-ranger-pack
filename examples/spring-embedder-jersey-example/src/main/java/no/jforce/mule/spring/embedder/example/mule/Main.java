package no.jforce.mule.spring.embedder.example.mule;

import no.jforce.mule.spring.config.ApplicationConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
    }
}
