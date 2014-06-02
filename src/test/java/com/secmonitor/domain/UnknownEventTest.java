package com.secmonitor.domain;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test
public class UnknownEventTest {
    public void shouldDefaultEventType() {
        Event event = new UnknownEvent();
        assertEquals(event.getType(), UnknownEvent.DEFAULT_EVENT_TYPE);
    }

    public void shouldSetDefaultEventTypeIfTryingToSetTypeToNull() {
        UnknownEvent event = new UnknownEvent("mynewevent", null);
        event.setType(null);
        assertEquals(event.getType(), UnknownEvent.DEFAULT_EVENT_TYPE);
    }

    public void shouldSetEventType() {
        String type = "mynewevent";
        Event event = new UnknownEvent(type, null);
        assertEquals(event.getType(), type);
    }

    public void shouldAllowSettingTheEventType() {
        UnknownEvent event = new UnknownEvent();
        String newType = "anewtype";
        event.setType(newType);
        assertEquals(event.getType(), newType);
    }
}