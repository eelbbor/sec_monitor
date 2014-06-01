package com.secmonitor.factory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secmonitor.domain.AlarmEvent;
import com.secmonitor.domain.DoorEvent;
import com.secmonitor.domain.Event;
import com.secmonitor.domain.ImgEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rlee on 6/1/14.
 */
public class EventFactory {
    public static final String TYPE_KEY = "type";
    public static final String DATE_KEY = "date";

    public static final Map<String, Class> EVENT_TYPE_CLASS_MAP = new HashMap<String, Class>() {
        {
            put(AlarmEvent.EVENT_TYPE, AlarmEvent.class);
            put(DoorEvent.EVENT_TYPE, DoorEvent.class);
            put(ImgEvent.EVENT_TYPE, ImgEvent.class);
        }
    };

    public Event newEventObject(Map<String, Object> eventObjectMap) {
        Map<String,Object> decoratedMap = decorateEventObjectMap(eventObjectMap);
        String type = (String) decoratedMap.get(TYPE_KEY);
        Class typeClass = EVENT_TYPE_CLASS_MAP.get(type);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return (Event)mapper.convertValue(decoratedMap, typeClass);
    }

    private Map<String, Object> decorateEventObjectMap(Map<String,Object> eventObjectMap) {
        Map<String, Object> decoratedMap = new HashMap<String, Object>();
        for(String key : eventObjectMap.keySet()) {
            decoratedMap.put(key.toLowerCase(), eventObjectMap.get(key));
        }
        return decoratedMap;
    }
}
