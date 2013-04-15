package org.jforce.reschedule;

import java.io.Serializable;
import java.util.Date;

public class Job implements Serializable {

    private final String reference;
    private final Date triggerTime;

    public Job(final String reference, final long triggerTime) {
        this(reference, new Date(triggerTime));
    }

    public Job(final String reference, final Date triggerTime) {
        this.reference = reference;
        this.triggerTime = triggerTime;
    }

    public String getReference() {
        return reference;
    }

    public Date getTriggerTime() {
        return triggerTime;
    }

    @Override
    public String toString() {
        return "Job(reference='" + reference + "' time='" + triggerTime + "')";
    }
}
