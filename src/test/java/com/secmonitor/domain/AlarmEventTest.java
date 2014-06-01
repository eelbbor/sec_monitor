package com.secmonitor.domain;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class AlarmEventTest {
    public void shouldHaveOverriddenEventType() {
        AlarmEvent event = new AlarmEvent(null, null, null, null);
        Assert.assertEquals(event.getType(), "alarm");
    }
}
