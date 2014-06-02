package com.secmonitor.factory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secmonitor.domain.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The EventFactory allows for creating an Event object based off of a File containing a JSON definition
 * of an Event.
 */
public class EventFactory {
    public static final String TYPE_KEY = "type";
    public static final String DATE_KEY = "date";

    private final ObjectMapper mapper = new ObjectMapper();
    public static final Map<String, Class> EVENT_TYPE_CLASS_MAP = new HashMap<String, Class>() {
        {
            put(AlarmEvent.EVENT_TYPE, AlarmEvent.class);
            put(DoorEvent.EVENT_TYPE, DoorEvent.class);
            put(ImgEvent.EVENT_TYPE, ImgEvent.class);
        }
    };

    /**
     * Creates an Event object based on an event file
     * @param file File that contains JSON defining and event
     * @return Event object that correlates with the event type
     * @throws IOException
     */
    public Event newEventObject(File file) throws IOException {
        Map<String,Object> eventObjectMap = mapper.readValue(file, Map.class);
        return newEventObject(eventObjectMap);
    }

    /**
     * Creates an Event object based on a map of parameters derived from a JSON object
     * @param eventObjectMap Map of properties defining an Event
     * @return
     */
    protected Event newEventObject(Map<String, Object> eventObjectMap) {
        //decorate the keys so they are all lower case to enable Domain POJO serialization with jackson
        Map<String,Object> decoratedMap = decorateEventObjectMap(eventObjectMap);
        String type = (String) decoratedMap.get(TYPE_KEY);
        Class typeClass = EVENT_TYPE_CLASS_MAP.get(type);

        //ignore extra fields, refactoring discussion on logging unknown fields
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return (Event) mapper.convertValue(decoratedMap, typeClass == null ? UnknownEvent.class : typeClass);
    }

    private Map<String, Object> decorateEventObjectMap(Map<String,Object> eventObjectMap) {
        Map<String, Object> decoratedMap = new HashMap<String, Object>();
        for(String key : eventObjectMap.keySet()) {
            decoratedMap.put(key.toLowerCase(), eventObjectMap.get(key));
        }
        return decoratedMap;
    }
}
