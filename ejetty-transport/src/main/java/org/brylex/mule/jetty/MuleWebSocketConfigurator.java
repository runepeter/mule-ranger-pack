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

        System.err.println("HANDSHAKE");

        // Is Authenticated?
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            System.err.println("No principal.");
            throw new RuntimeException("WTF!? Not authenticated");
        }

        System.err.println("User: " + principal);

        // Is Authorized?
        if (!request.isUserInRole("user")) {
            System.err.println("User not in role.");
            throw new RuntimeException("Dang! Not authorized");
        }

        System.err.println(principal + " in role 'user'.");

        // normal operation
        super.modifyHandshake(sec, request, response);
    }
}
