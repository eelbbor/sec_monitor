package com.secmonitor.service;

import com.secmonitor.domain.AlarmEvent;
import com.secmonitor.domain.DoorEvent;
import com.secmonitor.domain.Event;
import com.secmonitor.domain.ImgEvent;
import com.secmonitor.factory.EventFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * The EventFileProcessor creates events based off of files containing JSON data defining an Event.  The processor
 * tracks the amount of processing time and stores the events in a synchronized map to enable thread safety for
 * a single instance.  This class also keeps track of the amount of time spent processing the events.
 */
public class EventFileProcessor {
    public static final String OUTPUT_FORMAT = "DoorCnt: %d, ImgCnt:%d, AlarmCnt:%d, avgProcessingTime: %dms";

    private long processingTime = 0;
    private EventFactory eventFactory;
    public static final Set<String> TRACKABLE_EVENT_TYPES = new HashSet<String>(){
        {
            add(AlarmEvent.EVENT_TYPE);
            add(DoorEvent.EVENT_TYPE);
            add(ImgEvent.EVENT_TYPE);
        }
    };

    private Map<String, List<Event>> eventMap;
    public EventFileProcessor() {
        eventFactory = new EventFactory();
        //create a synchronized map to handle access from multiple threads
        eventMap = Collections.synchronizedMap(new HashMap<String, List<Event>>());
        for(String eventType : TRACKABLE_EVENT_TYPES) {
            eventMap.put(eventType, new ArrayList<Event>());
        }
    }

    /**
     * Processes an Event file and updates the status of the system with the data if it is a trackable event
     * @param file File containing JSON data defining and Event
     * @return Event that is defined in the JSON data in the file
     * @throws IOException
     */
    public Event processFile(File file) throws IOException {
        long startTime = Calendar.getInstance().getTimeInMillis();
        Event event = eventFactory.newEventObject(file);
        //
        if(TRACKABLE_EVENT_TYPES.contains(event.getType())) {
            eventMap.get(event.getType()).add(event);
            long elapsedTime = Calendar.getInstance().getTimeInMillis() - startTime;
            setProcessingTime(getProcessingTime() + elapsedTime);
        }
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

    protected synchronized long getProcessingTime() {
        return processingTime;
    }

    private synchronized void setProcessingTime(long processingTime) {
        this.processingTime = processingTime;
    }
}
