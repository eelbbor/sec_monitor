package com.secmonitor.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

/**
 * Monitors a directory based on the provided Path using a WatchService.  The monitor only keys
 * off of newly created files in the directory.  The system status is also presented based on the
 * defined interval or a default of 1 second.
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
        //spawn a thread to ensure status is reported on the interval and is tolerant to event processing over time boundaries
        new Thread(new StatusUpdateRunnable(processor, msOutputInterval)).start();
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
                processor.processFile(newFile);
            }
        }
        //return the result of a reset on the key
        return key.reset();
    }

    protected long getMsOutputInterval() {
        return msOutputInterval;
    }

    private class StatusUpdateRunnable implements Runnable {
        private final long msOutputInterval;
        private EventFileProcessor processor;

        protected StatusUpdateRunnable(EventFileProcessor processor, long msOutputInterval) {
            this.processor = processor;
            this.msOutputInterval = msOutputInterval;
        }

        @Override
        public void run() {
            System.out.println(processor.getStatusMessage());
            try {
                Thread.sleep(msOutputInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            run();
        }
    }
}
