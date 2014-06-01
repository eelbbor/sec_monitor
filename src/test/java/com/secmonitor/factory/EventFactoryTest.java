package com.secmonitor.factory;

import com.secmonitor.TestUtil;
import com.secmonitor.domain.AlarmEvent;
import com.secmonitor.domain.DoorEvent;
import com.secmonitor.domain.ImgEvent;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Test
public class EventFactoryTest {
    private EventFactory factory = new EventFactory();

    public void shouldCreateAlarmEvent() {
        String name = TestUtil.randomName();
        Map<String, Object> eventObjectMap = createBaseEventObjectMap(AlarmEvent.EVENT_TYPE);
        eventObjectMap.put("name", name);
        eventObjectMap.put("floor", "1");
        eventObjectMap.put("room", "101");

        AlarmEvent event = (AlarmEvent) factory.newEventObject(eventObjectMap);
        Assert.assertEquals(event.getName(), name);
        Assert.assertEquals(event.getFloor(), "1");
        Assert.assertEquals(event.getRoom(), "101");
    }

    public void shouldCreateDoorEvent() {
        Map<String, Object> eventObjectMap = createBaseEventObjectMap(DoorEvent.EVENT_TYPE);
        eventObjectMap.put("open", true);
        DoorEvent event = (DoorEvent) factory.newEventObject(eventObjectMap);
        Assert.assertTrue(event.isOpen());
    }

    public void shouldCreateImgEvent() {
        Map<String, Object> eventObjectMap = createBaseEventObjectMap(ImgEvent.EVENT_TYPE);
        String bytes = "0123456789abcdef";
        eventObjectMap.put("bytes", bytes);
        eventObjectMap.put("size", bytes.length());

        ImgEvent event = (ImgEvent) factory.newEventObject(eventObjectMap);
        Assert.assertEquals(event.getBytes(), bytes);
        Assert.assertEquals(event.getSize(), bytes.length());
    }

    public void shouldIgnoreCaseOnParameters() {
        Map<String, Object> eventObjectMap = new HashMap<String, Object>() {
            {
                put(EventFactory.TYPE_KEY.toUpperCase(), DoorEvent.EVENT_TYPE);
                put(EventFactory.DATE_KEY.toUpperCase(), Calendar.getInstance().getTime().toString());
                put("oPeN", true);
            }
        };
        DoorEvent event = (DoorEvent) factory.newEventObject(eventObjectMap);
        Assert.assertTrue(event.isOpen());
    }

    public void shouldIgnoreExtraParameters() {
        Map<String, Object> eventObjectMap = new HashMap<String, Object>() {
            {
                put(EventFactory.TYPE_KEY.toUpperCase(), DoorEvent.EVENT_TYPE);
                put(EventFactory.DATE_KEY.toUpperCase(), Calendar.getInstance().getTime().toString());
                put("oPeN", true);
                put("foo", "man");
            }
        };
        DoorEvent event = (DoorEvent) factory.newEventObject(eventObjectMap);
        Assert.assertTrue(event.isOpen());
    }

    private Map<String,Object> createBaseEventObjectMap(String type) {
        return new HashMap<String, Object>() {
            {
                put(EventFactory.TYPE_KEY, type);
                put(EventFactory.DATE_KEY, Calendar.getInstance().getTime().toString());

            }
        };
    }
}
