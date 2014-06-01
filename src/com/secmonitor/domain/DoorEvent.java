package com.secmonitor.domain;

/**
 * Created by rlee on 5/31/14.
 */
public class DoorEvent extends Event {
    public static final String EVENT_TYPE = "door";

    protected DoorEvent(String date) {
        super(date);
    }
}
