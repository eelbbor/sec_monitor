package com.secmonitor.domain;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by rlee on 6/1/14.
 */
@Test
public class ImgEventTest {
    public void shouldHaveOverriddenEventType() {
        ImgEvent event = new ImgEvent(null, null, 0);
        Assert.assertEquals(event.getType(), "img");
    }
}
