package com.secmonitor.service;

import com.secmonitor.domain.AlarmEvent;
import com.secmonitor.domain.DoorEvent;
import com.secmonitor.domain.Event;
import com.secmonitor.domain.ImgEvent;
import com.secmonitor.factory.EventFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The EventFileProcessor creates events based off of files containing JSON data defining an Event.  The processor
 * tracks the amount of processing time and stores the events in a synchronized map to enable thread safety for
 * a single instance.  This class also keeps track of the amount of time spent processing the events.
 */
public class EventFileProcessor {
    public static final String OUTPUT_FORMAT = "DoorCnt: %d, ImgCnt:%d, AlarmCnt:%d, avgProcessingTime: %dms";

    private long processingTime = 0;
    private EventFactory eventFactory;
    private Map<String, List<Event>> eventMap;

    public EventFileProcessor() {
        eventFactory = new EventFactory();
        //create a synchronized map to handle access from multiple threads
        eventMap = Collections.synchronizedMap(new HashMap<String, List<Event>>());
        eventMap.put(AlarmEvent.EVENT_TYPE, new ArrayList<Event>());
        eventMap.put(DoorEvent.EVENT_TYPE, new ArrayList<Event>());
        eventMap.put(ImgEvent.EVENT_TYPE, new ArrayList<Event>());
    }

    /**
     * Processes and Event file and updates the status of the system with the data
     * @param file File containing JSON data defining and Event
     * @return Event that is defined in the JSON data in the file
     * @throws IOException
     */
    public Event processFile(File file) throws IOException {
        long startTime = Calendar.getInstance().getTimeInMillis();
        Event event = eventFactory.newEventObject(file);
        eventMap.get(event.getType()).add(event);

        long elapsedTime = Calendar.getInstance().getTimeInMillis() - startTime;
        setProcessingTime(getProcessingTime() + elapsedTime);
        return event;
    }

    /**
     * Generates a string representation of the status of the system based off the events that have been processed.
     * @return String
     */
    public String getStatusMessage() {
        int doorCount = eventMap.get(DoorEvent.EVENT_TYPE).size();
        int imgCount = eventMap.get(ImgEvent.EVENT_TYPE).size();
        int alarmCount = eventMap.get(AlarmEvent.EVENT_TYPE).size();
        long fileCount = doorCount + imgCount + alarmCount;
        long avgProcessingTime = fileCount > 0 ? getProcessingTime() / fileCount : 0;
        return String.format(OUTPUT_FORMAT,
                doorCount,
                imgCount,
                alarmCount,
                avgProcessingTime);
    }

    private synchronized long getProcessingTime() {
        return processingTime;
    }

    private synchronized void setProcessingTime(long processingTime) {
        this.processingTime = processingTime;
    }
}
