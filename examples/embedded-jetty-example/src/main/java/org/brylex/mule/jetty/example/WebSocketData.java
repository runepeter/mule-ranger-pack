package org.brylex.mule.jetty.example;

import java.io.Serializable;

public class WebSocketData implements Serializable {

    private final Object data;

    public WebSocketData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
