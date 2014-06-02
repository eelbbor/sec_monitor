package com.secmonitor.domain;

/**
 * Generic event structure to allow for an event that is not of a predefined type to be created.  When created it
 * will specify the type as __unknown__ if none is provided.
 */
public class UnknownEvent extends Event {
    public static final String DEFAULT_EVENT_TYPE = "__unknown__";
    private String type;

    public UnknownEvent() {
        this(DEFAULT_EVENT_TYPE, null);
    }

    public UnknownEvent(String type, String date) {
        super(date);
        setType(type);
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? DEFAULT_EVENT_TYPE : type;
    }
}
