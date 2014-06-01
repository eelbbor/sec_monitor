package com.secmonitor.domain;

public class DoorEvent extends Event {
    public static final String EVENT_TYPE = "door";
    private boolean open;

    public DoorEvent(){
        super();
    }

    public DoorEvent(String date, boolean open) {
        super(date);
        this.setOpen(open);
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    @Override
    public String getType() {
        return EVENT_TYPE;
    }
}
