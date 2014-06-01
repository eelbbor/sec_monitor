package com.secmonitor.domain;

public abstract class Event {
    private String date;

    public abstract String getType();

    public Event() {
    }

    protected Event(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
