package org.jforce.reschedule;

import java.io.Serializable;
import java.util.Date;

public class Job implements Serializable {

    private final Date triggerTime;
    private final String key;

    public Job(String key) {
        this.key = key;
        this.triggerTime = new Date(System.currentTimeMillis() - 5000);
    }

    public String getKey() {
        return key;
    }

    public Date getTriggerTime() {
        return triggerTime;
    }
}
