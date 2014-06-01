package com.secmonitor.domain;

public class AlarmEvent extends Event {
    public static final String EVENT_TYPE = "alarm";
    private String name;
    private String floor;
    private String room;

    public AlarmEvent() {}

    public AlarmEvent(String date, String name, String floor, String room) {
        super(date);
        this.setName(name);
        this.setFloor(floor);
        this.setRoom(room);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @Override
    public String getType() {
        return EVENT_TYPE;
    }
}
