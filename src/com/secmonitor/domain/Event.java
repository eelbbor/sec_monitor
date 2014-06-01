package com.secmonitor.domain;

import java.util.Date;

/**
 * Created by rlee on 5/31/14.
 */
public abstract class Event {
    public static final String EVENT_TYPE = "event";
    private Date eventDate;

    protected Event(String date) {

    }

    public String getEventType() {
      return EVENT_TYPE;
    }
}
