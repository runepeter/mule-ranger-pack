package org.brylex.mule.jetty.example;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class WebSocketRequest implements Serializable {

    private String type;
    private String callback_id;
    private Map<String, String> arguments;

    public WebSocketRequest(String type) {
        this.type = type;
        this.arguments = new HashMap<>();
    }

    public WebSocketRequest() {
    }

    public String getType() {
        return type;
    }

    public String getCallback_id() {
        return callback_id;
    }

    public Map<String, String> getArguments() {
        return arguments;
    }
}
