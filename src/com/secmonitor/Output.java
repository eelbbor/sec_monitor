package com.secmonitor;

/**
 * Created by rlee on 5/31/14.
 */

public class Output {
    public static final String OUTPUT_FORMAT = "DoorCnt: %d, ImgCnt:%d, AlarmCnt:%d, avgProcessingTime: %dms";
    private long doorCount = 0;
    private long imgCount = 0;
    private long alarmCount = 0;
    private double avgProcessingTime = 0;

    @Override
    public String toString() {
        return String.format(OUTPUT_FORMAT,doorCount,imgCount,alarmCount,avgProcessingTime);
    }

}
