package com.secmonitor;

import com.secmonitor.service.SystemMonitor;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        Path directory = Paths.get(args.length > 0 ? args[0] : "./");
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
