package com.secmonitor.service;

import com.secmonitor.domain.DoorEvent;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.secmonitor.TestUtil.createFile;
import static com.secmonitor.TestUtil.makeTestDirectory;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Test
public class SystemMonitorTest {
    private SystemMonitor monitor;
    private File testDir;

    @BeforeClass
    protected void classSetup() throws Exception {
        testDir = makeTestDirectory();
    }

    @BeforeMethod
    protected void setUp() throws Exception {
        createMonitor(null);
    }

    @AfterClass
    protected void classTearDown() {
        testDir.delete();
    }

    public void shouldDefaultOutputIntervalToOneSecondForNullInterval() throws Exception {
        createMonitor(null);
        assertEquals(monitor.getMsOutputInterval(), 1000);
    }

    public void shouldDefaultOutputIntervalToOneSecondForZeroInterval() throws Exception {
        createMonitor(0L);
        assertEquals(monitor.getMsOutputInterval(), 1000);
    }

    public void shouldDefaultOutputIntervalToOneSecondForNegativeInterval() throws Exception {
        createMonitor(-10L);
        assertEquals(monitor.getMsOutputInterval(), 1000);
    }

    public void shouldIndicateValidWithANullKey() throws Exception {
        assertTrue(monitor.handleWatchEvents(null));
    }

    public void shouldIndicateInvalidOnKeyReset() throws Exception {
        File file = null;
        try {
            file = createFile(testDir.getPath(), new DoorEvent(Calendar.getInstance().getTime().toString(), true));
            WatchEvent event = mockWatchEventForFile(file, ENTRY_CREATE);
            WatchKey key = mockWatchKeyForEvent(event);
            when(key.reset()).thenReturn(false);
            assertFalse(monitor.handleWatchEvents(key));
            verify(key,times(1)).reset();
        } finally {
            file.delete();
        }
    }

    public void shouldProcessCreateEvent() throws Exception {
        File file = null;
        try {
            file = createFile(testDir.getPath(), new DoorEvent(Calendar.getInstance().getTime().toString(), true));
            WatchEvent event = mockWatchEventForFile(file, ENTRY_CREATE);
            monitor.handleWatchEvents(mockWatchKeyForEvent(event));
            verify(event,times(1)).context();
        } finally {
            file.delete();
        }
    }

    public void shouldNotProcessNonCreateEvents() throws Exception {
        File file = null;
        try {
            file = createFile(testDir.getPath(), new DoorEvent(Calendar.getInstance().getTime().toString(), true));
            WatchEvent overflow = mockWatchEventForFile(file, OVERFLOW);
            WatchEvent delete = mockWatchEventForFile(file, ENTRY_DELETE);
            WatchEvent modify = mockWatchEventForFile(file, ENTRY_MODIFY);
            monitor.handleWatchEvents(mockWatchKeyForEvent(overflow, delete, modify));
            verify(overflow,never()).context();
        } finally {
            file.delete();
        }
    }

    private WatchEvent mockWatchEventForFile(File file, WatchEvent.Kind<?> eventKind) {
        WatchEvent event = mock(WatchEvent.class);
        when(event.kind()).thenReturn(eventKind);
        when(event.context()).thenReturn(Paths.get(file.getName()));
        return event;
    }

    private WatchKey mockWatchKeyForEvent(WatchEvent... events) {
        List<WatchEvent<?>> pollEvents = new ArrayList<WatchEvent<?>>();
        for(WatchEvent event : events) {
            pollEvents.add(event);
        }
        WatchKey key = mock(WatchKey.class);
        when(key.pollEvents()).thenReturn(pollEvents);
        when(key.reset()).thenReturn(true);
        return key;
    }

    private void createMonitor(Long msOutputInterval) throws Exception {
        monitor = new SystemMonitor(testDir.toPath(), msOutputInterval);
    }
}
