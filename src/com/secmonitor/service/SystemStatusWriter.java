package com.secmonitor.service;

/**
 * Created by rlee on 5/31/14.
 * This class will maintain the running status of the system and output the state at a fixed interval.  Note
 * this class is a singleton intended to run on a separate thread due to its synchronous nature.
 */
public class SystemStatusWriter {
    private int outputInterval = 1000;
    private SystemStatus systemStatus;

    private SystemStatusWriter(int outputInterval) {

    }

    private class SystemStatus {
        private long alarmCnt = 0;
        private long doorCnt = 0;
        private long imgCnt = 0;
        private long avgProcessingTime = 0;
    }
}
