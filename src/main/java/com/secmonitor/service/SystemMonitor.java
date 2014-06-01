package com.secmonitor.service;

import com.secmonitor.domain.Event;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Calendar;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

/**
 * Created by rlee on 5/31/14.
 * Monitors a directory based on the provided Path using a WatchService.  The monitor only keys
 * off of newly created files in the directory.
 */
public class SystemMonitor {
    private WatchService watcher;
    private EventFileProcessor processor;

    private Path directory;
    private long msOutputInterval = 1000;

    /**
     * Constructor to create the SystemMonitor
     *
     * @param directory        - directory to monitor for new file creation
     * @param msOutputInterval - interval in milliseconds to output the system status
     * @throws IOException
     */
    public SystemMonitor(Path directory, Long msOutputInterval) throws IOException {
        watcher = FileSystems.getDefault().newWatchService();
        this.directory = directory;
        this.directory.register(watcher, ENTRY_CREATE);

        processor = new EventFileProcessor();
        if (msOutputInterval != null && msOutputInterval > 0) {
            this.msOutputInterval = msOutputInterval;
        }
    }

    /**
     * Starts the monitor for detecting file creation and will output the system status when based on
     * the defined interval.  The polling will process a file as soon as an event is detected.
     *
     * @throws IOException
     */
    public void startMonitor() {
        /*timing for status update is a bit of a hack, should spawn a separate thread with timeout in case
          of processing across the update interval, need to sync on the output object to avoid contention*/
        long lastStatusUpdateTime = Calendar.getInstance().getTimeInMillis();
        for (; ; ) {
            WatchKey key = watcher.poll();
            //handle any events that have occurred in the directory
            try {
                if (!handleWatchEvents(key)) {
                    watcher.close();
                    break;
                }
            } catch (IOException e) {
                //NOTE: real development should be logging as opposed to dumping to command line
                System.out.println("ERROR: Failed to handle WatchEvents");
                e.printStackTrace();
            }

            //determine whether to send status output
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastStatusUpdateTime >= msOutputInterval) {
                outputStatus();
                lastStatusUpdateTime = currentTime;
            }
        }
    }

    protected boolean handleWatchEvents(WatchKey key) throws IOException {
        //if key is null there are no events so simply return
        if (key == null) {
            return true;
        }
        //process the events that have occurred
        for (WatchEvent<?> watchEvent : key.pollEvents()) {
            WatchEvent.Kind<?> kind = watchEvent.kind();
            //only process create events
            if (kind == ENTRY_CREATE) {
                WatchEvent<Path> ev = (WatchEvent<Path>) watchEvent;
                File newFile = directory.resolve(ev.context()).toFile();
                Event event = processor.processFile(newFile);
            }
        }
        //return the result of a reset on the key
        return key.reset();
    }

    protected void outputStatus() {
        System.out.println(processor.getStatusMessage());
    }

    protected long getMsOutputInterval() {
        return msOutputInterval;
    }
}
