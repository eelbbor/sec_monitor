package com.secmonitor.domain;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by rlee on 6/1/14.
 */
@Test
public class DoorEventTest {
    public void shouldHaveOverriddenEventType() {
        DoorEvent event = new DoorEvent(null, false);
        Assert.assertEquals(event.getType(), "door");
    }
}
