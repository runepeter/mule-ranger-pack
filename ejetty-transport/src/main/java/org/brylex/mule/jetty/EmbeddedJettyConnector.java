package org.brylex.mule.jetty;

import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.MuleRuntimeException;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.context.notification.MuleContextNotificationListener;
import org.mule.api.endpoint.ImmutableEndpoint;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.transport.MessageReceiver;
import org.mule.api.transport.ReplyToHandler;
import org.mule.config.i18n.CoreMessages;
import org.mule.config.i18n.MessageFactory;
import org.mule.context.notification.MuleContextNotification;
import org.mule.context.notification.NotificationException;
import org.mule.transport.AbstractConnector;

import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.Extension;
import javax.websocket.Session;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EmbeddedJettyConnector extends AbstractConnector {

    public static final String EJETTY = "ejetty";

    private ServletContextHandler.Context jettyContext;

    private final ConcurrentMap<String, ConcurrentMap<String, Set<Session>>> webSocketSessionMap = new ConcurrentHashMap<>();

    public EmbeddedJettyConnector(MuleContext context) {
        super(context);
        registerSupportedProtocol("http");
        registerSupportedProtocol("websocket");
        registerSupportedProtocol(EJETTY);
        setInitialStateStopped(true);
    }

    @Override
    public String getProtocol() {
        return EJETTY;
    }

    @Override
    protected void doInitialise() throws InitialisationException {

        this.jettyContext = (ServletContextHandler.Context) ContextHandler.getCurrentContext();
        if (jettyContext == null) {
            throw new InitialisationException(MessageFactory.createStaticMessage("Connector is not run inside a Jetty instance."), this);
        }

        try {
            muleContext.registerListener(new MuleContextNotificationListener<MuleContextNotification>() {
                @Override
                public void onNotification(MuleContextNotification notification) {

                    if (notification.getAction() == MuleContextNotification.CONTEXT_STARTED) {

                        setInitialStateStopped(false);
                        try {
                            start();
                        } catch (MuleException e) {
                            throw new MuleRuntimeException(CoreMessages.failedToStart(getName()), e);
                        }
                    }
                }
            });
        } catch (NotificationException e) {
            throw new InitialisationException(e, this);
        }
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
    }

    @Override
    protected void doDisconnect() throws Exception {
    }

    @Override
    protected MessageReceiver createReceiver(final FlowConstruct flowConstruct, final InboundEndpoint endpoint) throws Exception {

        final EmbeddedJettyMessageReceiver embeddedJettyMessageReceiver = (EmbeddedJettyMessageReceiver) super.createReceiver(flowConstruct, endpoint);

        final ServletContextHandler servletContextHandler = (ServletContextHandler) jettyContext.getContextHandler();
        final ContextHandler.Context servletContext = servletContextHandler.getServletContext();

        ServerContainer websocketContainer = (ServerContainer) servletContext.getAttribute("javax.websocket.server.ServerContainer");
        if (websocketContainer == null) {
            WebSocketServerContainerInitializer.configureContext(servletContextHandler);
            websocketContainer = (ServerContainer) servletContext.getAttribute(ServerContainer.class.getName());
            logger.debug("Initialized WebSocket container.");
        }

        final String webSocketPath = toWebSocketPath(endpoint);
        webSocketSessionMap.put(webSocketPath, new ConcurrentHashMap<String, Set<Session>>());

        websocketContainer.addEndpoint(
                new ServerEndpointConfig() {
                    @Override
                    public Class<?> getEndpointClass() {
                        return MuleWebSocketServerEndpoint.class; // configurable through indirection
                    }

                    @Override
                    public String getPath() {
                        return webSocketPath;
                    }

                    @Override
                    public List<String> getSubprotocols() {
                        return Collections.emptyList();
                    }

                    @Override
                    public List<Extension> getExtensions() {
                        return Collections.emptyList();
                    }

                    @Override
                    public Configurator getConfigurator() {
                        return new MuleWebSocketConfigurator();
                    }

                    @Override
                    public List<Class<? extends Encoder>> getEncoders() {
                        return Collections.emptyList();
                    }

                    @Override
                    public List<Class<? extends Decoder>> getDecoders() {
                        return Collections.emptyList();
                    }

                    @Override
                    public Map<String, Object> getUserProperties() {

                        Map<String, Object> properties = new HashMap<String, Object>();
                        properties.put("mule.ejetty.message_receiver", embeddedJettyMessageReceiver);

                        return properties;
                    }
                }
        );

        return embeddedJettyMessageReceiver;
    }

    public static String toWebSocketPath(final ImmutableEndpoint endpoint) {
        String socketPath = "/" + endpoint.getEndpointURI().getHost();
        if (!"".equals(endpoint.getEndpointURI().getPath())) {
            socketPath += endpoint.getEndpointURI().getPath();
        }
        return socketPath;
    }

    @Override
    public ReplyToHandler getReplyToHandler(ImmutableEndpoint endpoint) {
        return new EmbeddedJettyWebSocketReplyToHandler(muleContext);
    }

    public void registerWebSocketSession(final Session session) {

        final String requestPath = session.getRequestURI().getPath();

        ConcurrentMap<String, Set<Session>> sessionMap = webSocketSessionMap.get(requestPath);
        if (sessionMap == null) {
            for (String path : webSocketSessionMap.keySet()) {
                if (requestPath.endsWith(path)) {
                    webSocketSessionMap.put(requestPath, sessionMap = webSocketSessionMap.get(path));
                }
            }
        }

        if (sessionMap == null) {
            throw new IllegalStateException("There's no WebSocket configured for path [" + requestPath + "].");
        }

        String user = session.getUserPrincipal() != null ? session.getUserPrincipal().getName() : null;
        sessionMap.putIfAbsent(user, new HashSet<Session>());
        sessionMap.get(user).add(session);
        logger.info("Registered WebSocket session [" + session + "] for path [" + requestPath + "].");
    }

    public void unregisterWebSocketSession(final Session session) {

        final String requestPath = session.getRequestURI().getPath();

        final Map<String, Set<Session>> sessionMap = webSocketSessionMap.get(requestPath);
        if (sessionMap == null) {
            throw new IllegalStateException("There's no WebSocket configured for path [" + requestPath + "].");
        }

        String user = session.getUserPrincipal() != null ? session.getUserPrincipal().getName() : null;
        sessionMap.get(user).remove(session);
        logger.info("Un-registered WebSocket session [" + session + "] for path [" + requestPath + "].");
    }

    public Set<Session> getWebSocketSessionsForUser(final String path, final String user) {

        final Set<Session> sessions = webSocketSessionMap.get(path).get(user);

        return Collections.unmodifiableSet(sessions != null ? sessions : Collections.<Session>emptySet());
    }

    public Set<Session> getWebSocketSessions(final String path) {

        // yes I know - this doesn't scale.

        final HashSet<Session> set = new HashSet<>();

        final ConcurrentMap<String, Set<Session>> sessionMap = webSocketSessionMap.get(path);
        for (String user : sessionMap.keySet()) {
            set.addAll(sessionMap.get(user));
        }

        return set;
    }
}
