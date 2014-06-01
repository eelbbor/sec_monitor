package com.secmonitor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secmonitor.domain.AlarmEvent;
import com.secmonitor.domain.DoorEvent;
import com.secmonitor.domain.Event;
import com.secmonitor.domain.ImgEvent;
import com.secmonitor.factory.EventFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rlee on 5/31/14.
 */
public class EventFileProcessor {
    public static final String OUTPUT_FORMAT = "DoorCnt: %d, ImgCnt:%d, AlarmCnt:%d, avgProcessingTime: %dms";

    private long processingTime = 0;
    private EventFactory eventFactory;
    private Map<String, List<Event>> eventMap;

    public EventFileProcessor() {
        eventFactory = new EventFactory();
        eventMap = new HashMap<String, List<Event>>();
        eventMap.put(AlarmEvent.EVENT_TYPE, new ArrayList<Event>());
        eventMap.put(DoorEvent.EVENT_TYPE, new ArrayList<Event>());
        eventMap.put(ImgEvent.EVENT_TYPE, new ArrayList<Event>());
    }

    public Event processFile(File file) throws IOException {
        long startTime = Calendar.getInstance().getTimeInMillis();

        Map<String,Object> eventObjectMap = new ObjectMapper().readValue(file, Map.class);
        Event event = eventFactory.newEventObject(eventObjectMap);
        eventMap.get(event.getType()).add(event);

        processingTime += Calendar.getInstance().getTimeInMillis() - startTime;
        return event;
    }

    public String getStatusMessage() {
        int doorCount = eventMap.get(DoorEvent.EVENT_TYPE).size();
        int imgCount = eventMap.get(ImgEvent.EVENT_TYPE).size();
        int alarmCount = eventMap.get(AlarmEvent.EVENT_TYPE).size();
        long fileCount = doorCount + imgCount + alarmCount;
        long avgProcessingTime = fileCount > 0 ? processingTime / fileCount : 0;
        return String.format(OUTPUT_FORMAT,
                doorCount,
                imgCount,
                alarmCount,
                avgProcessingTime);
    }
}
