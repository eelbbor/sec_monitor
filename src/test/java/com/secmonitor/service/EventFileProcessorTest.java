package com.secmonitor.service;

import com.secmonitor.TestUtil;
import com.secmonitor.domain.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import static com.secmonitor.TestUtil.createFile;
import static com.secmonitor.TestUtil.randomName;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Test
public class EventFileProcessorTest {
    private EventFileProcessor fileProcessor;
    private File testDirectory;
    private Set<File> testFiles = new HashSet<File>();

    @BeforeClass
    protected void classSetup() throws Exception {
        testDirectory = TestUtil.makeTestDirectory();
    }

    @AfterClass
    protected void classTearDown() {
        testDirectory.delete();
    }

    @BeforeMethod
    protected void setUp() {
        fileProcessor = new EventFileProcessor();
    }

    @AfterMethod
    protected void tearDown() {
        for (File file : testFiles) {
            file.delete();
        }
    }

    public void shouldInitializeAllCountsToZero() {
        assertEquals(fileProcessor.getStatusMessage(), String.format(EventFileProcessor.OUTPUT_FORMAT, 0, 0, 0, 0));
    }

    public void shouldParseAlarmEvent() throws Exception {
        AlarmEvent alarmEvent = getAlarmEvent();
        File file = createEventFile(alarmEvent);
        AlarmEvent event = (AlarmEvent) fileProcessor.processFile(file);
        assertEquals(event.getDate(), alarmEvent.getDate());
        assertEquals(event.getName(), alarmEvent.getName());
        assertEquals(event.getRoom(), alarmEvent.getRoom());
        assertEquals(event.getFloor(), alarmEvent.getFloor());
        validateStatus(0, 0, 1);
    }

    public void shouldParseDoorEvent() throws Exception {
        DoorEvent doorEvent = getDoorEvent();
        File file = createEventFile(doorEvent);
        DoorEvent event = (DoorEvent) fileProcessor.processFile(file);
        assertEquals(event.getDate(), doorEvent.getDate());
        assertEquals(event.isOpen(), doorEvent.isOpen());
        validateStatus(1, 0, 0);
    }

    public void shouldParseImgEvent() throws Exception {
        ImgEvent imgEvent = getImgEvent();
        File file = createEventFile(imgEvent);
        ImgEvent event = (ImgEvent) fileProcessor.processFile(file);
        assertEquals(event.getDate(), imgEvent.getDate());
        assertEquals(event.getBytes(), imgEvent.getBytes());
        assertEquals(event.getSize(), imgEvent.getSize());
        validateStatus(0, 1, 0);
    }

    public void shouldUpdateCountsBasedOnFilesProcessed() throws Exception {
        fileProcessor.processFile(createEventFile(getAlarmEvent()));
        fileProcessor.processFile(createEventFile(getAlarmEvent()));
        fileProcessor.processFile(createEventFile(getAlarmEvent()));
        fileProcessor.processFile(createEventFile(getDoorEvent()));
        fileProcessor.processFile(createEventFile(getDoorEvent()));
        fileProcessor.processFile(createEventFile(getImgEvent()));
        validateStatus(2, 1, 3);
    }

    public void shouldNotUpdateStatusValuesBasedOnEventWithoutType() throws Exception {
        Event event = new Event() {
            @Override
            public String getType() {
                return null;
            }
        };
        fileProcessor.processFile(createEventFile(event));
        fileProcessor.processFile(createEventFile(event));
        fileProcessor.processFile(createEventFile(event));
        validateStatus(0, 0, 0);
    }

    public void shouldNotUpdateStatusValuesBasedOnEventOfUnknownType() throws Exception {
        UnknownEvent event = new UnknownEvent();
        fileProcessor.processFile(createEventFile(event));
        fileProcessor.processFile(createEventFile(event));
        fileProcessor.processFile(createEventFile(event));
        validateStatus(0, 0, 0);
    }

    public void shouldNotUpdateStatusValuesBasedOnEventOfUntrackableType() throws Exception {
        UnknownEvent event = new UnknownEvent("someothertype",null);
        fileProcessor.processFile(createEventFile(event));
        fileProcessor.processFile(createEventFile(event));
        fileProcessor.processFile(createEventFile(event));
        validateStatus(0, 0, 0);
    }

    private File createEventFile(Event event) throws Exception {
        File file = createFile(testDirectory.getPath(), event);
        testFiles.add(file);
        return file;
    }

    private AlarmEvent getAlarmEvent() {
        return new AlarmEvent(Calendar.getInstance().getTime().toString(), randomName(), randomName(), randomName());
    }

    private DoorEvent getDoorEvent() {
        return new DoorEvent(Calendar.getInstance().getTime().toString(), true);
    }

    private ImgEvent getImgEvent() {
        return new ImgEvent(Calendar.getInstance().getTime().toString(), randomName(), 32);
    }

    private void validateStatus(int doorCnt, int imgCnt, int alarmCnt) {
        int totalEventCnt = doorCnt + imgCnt + alarmCnt;
        long avgTime = totalEventCnt > 0 ? fileProcessor.getProcessingTime() / totalEventCnt : 0;
        String expectedStatus = String.format(EventFileProcessor.OUTPUT_FORMAT, doorCnt, imgCnt, alarmCnt, avgTime);
        assertEquals(fileProcessor.getStatusMessage(), expectedStatus);
    }
}
