package com.secmonitor.domain;

/**
 * Created by rlee on 5/31/14.
 */
public class AlarmEvent extends Event {
    public static final String EVENT_TYPE = "alarm";

    protected AlarmEvent(String date) {
        super(date);
    }

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }
}
