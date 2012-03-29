package org.brylex.reschedule.mule;

import org.mule.api.endpoint.MalformedEndpointException;
import org.mule.endpoint.ResourceNameEndpointURIBuilder;

import java.net.URI;
import java.util.Properties;

public class RescheduleEndpointURIBuilder extends ResourceNameEndpointURIBuilder {

    private String schedule;
    
    @Override
    protected void setEndpoint(URI uri, Properties properties) throws MalformedEndpointException {
        super.setEndpoint(uri, properties);

        System.err.println("URI: " + uri);

        if (uri.getScheme().equals("schedule")) {
            properties.setProperty(RESOURCE_INFO_PROPERTY, "schedule");
        }
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
