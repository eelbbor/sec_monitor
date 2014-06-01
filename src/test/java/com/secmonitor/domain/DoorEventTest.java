package com.secmonitor.domain;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class DoorEventTest {
    public void shouldHaveOverriddenEventType() {
        DoorEvent event = new DoorEvent(null, false);
        Assert.assertEquals(event.getType(), "door");
    }
}
