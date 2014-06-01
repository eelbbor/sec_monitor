package com.secmonitor.service;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.fail;

/**
 * Created by rlee on 5/31/14.
 */
@Test
public class FileProcessorTest {
    private FileProcessor fileProcessor;

    @BeforeMethod
    protected void setUp() {
        fileProcessor = new FileProcessor();
    }

    public void shouldParseAlarmEvent() throws Exception {
        File file = new File("./coding_exercise/input/alarm.json");
        fileProcessor.processFile(file);
        fail("Need to implement");
    }

    public void shouldParseDoorEvent() {
        fail("Need to implement");
    }

    public void shouldParseImgEvent() {
        fail("Need to implement");
    }
}
