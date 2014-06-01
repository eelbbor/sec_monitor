package com.secmonitor.domain;

/**
 * Created by rlee on 5/31/14.
 */
public class ImgEvent extends Event {
    public static final String EVENT_TYPE = "img";
    private String bytes;
    private long size;

    private ImgEvent(String date, String bytes, long size) {
        super(date);
        this.bytes = bytes;
        this.size = size;
    }

    public static ImgEvent getEvent(String json) {
        return new ImgEvent(null, null, -1);
    }

    public String getBytes() {
        return bytes;
    }

    public byte[] getByteArray() {
        return bytes.getBytes();
    }

    public long getSize() {
        return size;
    }
}
