package com.secmonitor.domain;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by rlee on 6/1/14.
 */
@Test
public class AlarmEventTest {
    public void shouldHaveOverriddenEventType() {
        AlarmEvent event = new AlarmEvent(null, null, null, null);
        Assert.assertEquals(event.getType(), "alarm");
    }
}
