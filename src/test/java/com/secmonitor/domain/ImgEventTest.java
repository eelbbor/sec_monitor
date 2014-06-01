package com.secmonitor.domain;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class ImgEventTest {
    public void shouldHaveOverriddenEventType() {
        ImgEvent event = new ImgEvent(null, null, 0);
        Assert.assertEquals(event.getType(), "img");
    }
}
