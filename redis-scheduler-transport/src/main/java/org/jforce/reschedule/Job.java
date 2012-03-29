package org.jforce.reschedule;

import java.io.Serializable;
import java.util.Date;

public class Job implements Serializable {

    private Date triggerDateTime;
    private final byte[] hashKey;

    public Job(String hashKey) {
        this(hashKey.getBytes());
    }

    public Job(byte[] hashKey) {
        this.hashKey = hashKey;
    }
}
