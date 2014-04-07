package org.brylex.mule.jetty.example;

import eu.nets.oss.jetty.ClasspathResourceHandler;
import eu.nets.oss.jetty.EmbeddedJettyBuilder;
import eu.nets.oss.jetty.EmbeddedSpringBuilder;
import eu.nets.oss.jetty.StaticConfig;
import org.brylex.mule.jetty.example.config.ApplicationConfiguration;
import org.eclipse.jetty.security.*;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.UserIdentity;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {

        final EmbeddedJettyBuilder builder = new EmbeddedJettyBuilder(new StaticConfig("/mule", 8080), false);
        final WebApplicationContext context = EmbeddedSpringBuilder.createApplicationContext("default", ApplicationConfiguration.class);
        final ContextLoaderListener springContextLoader = EmbeddedSpringBuilder.createSpringContextLoader(context);

        ClasspathResourceHandler resourceHandler = builder.createWebAppClasspathResourceHandler();
        resourceHandler.setResourceBase("/webapp");

        EmbeddedJettyBuilder.ServletContextHandlerBuilder contextHandler = builder.createRootServletContextHandler("");
        contextHandler.getHandler().setSecurityHandler(createSecurityHandler());
        contextHandler.addEventListener(springContextLoader);
        contextHandler.setResourceHandler(resourceHandler);

        try {

            builder.startJetty();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Thread.sleep(Long.MAX_VALUE);
    }

    private static SecurityHandler createSecurityHandler() {

        HashLoginService loginService = new HashLoginService();
        loginService.putUser("runepeter", Credential.getCredential("password"), new String[]{"user"});
        loginService.setName("PERSONAL");

        // add authentication
        Constraint constraint = new Constraint(Constraint.__BASIC_AUTH,"user");
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{"user"});

        // map the security constraint to the root path.
        ConstraintMapping constraintMapping = new ConstraintMapping();
        constraintMapping.setConstraint(constraint);
        constraintMapping.setPathSpec("/*");

        // create the security handler, set the authentication to Basic
        // and assign the realm.
        ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();
        securityHandler.setAuthenticator(new BasicAuthenticator());
        securityHandler.setRealmName("PERSONAL");
        securityHandler.addConstraintMapping(constraintMapping);

        securityHandler.setLoginService(loginService);

        return securityHandler;
    }

}
