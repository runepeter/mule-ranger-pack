package org.brylex.mule.jetty;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.security.Principal;

public class MuleWebSocketConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(final ServerEndpointConfig sec,
                                final HandshakeRequest request,
                                final HandshakeResponse response) {

        // Is Authenticated?
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            throw new RuntimeException("WTF!? Not authenticated");
        }

        // Is Authorized?
        if (!request.isUserInRole("user")) {
            throw new RuntimeException("Dang! Not authorized");
        }

        // normal operation
        super.modifyHandshake(sec, request, response);
    }
}
