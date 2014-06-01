package com.secmonitor;

import com.secmonitor.service.SystemMonitor;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        String path = args[0];
        Path directory = Paths.get(path == null ? "./" : path);
        try {
            SystemMonitor monitor = new SystemMonitor(directory, null);
            System.out.println("Monitoring files created at: " + directory.toString());
            monitor.startMonitor();
        } catch (Exception e) {
            System.out.println("ERROR: Failed to initialize the SystemMonitor for " + directory.toString());
            e.printStackTrace();
        }
    }
}
