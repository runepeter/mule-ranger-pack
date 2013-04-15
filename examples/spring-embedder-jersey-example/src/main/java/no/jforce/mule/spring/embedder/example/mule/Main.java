package no.jforce.mule.spring.embedder.example.mule;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA. User: rpbjo Date: 13.11.12 Time: 10:11 To change this template use File | Settings | File
 * Templates.
 */
public class Main {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classpath:/applicationContext.xml");
    }
}
