package com.secmonitor;

import com.secmonitor.service.SystemMonitor;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        Path directory = Paths.get(args.length > 0 ? args[0] : "./");
        SystemMonitor monitor = null;
        try {
            monitor = new SystemMonitor(directory, null);
        } catch (Exception e) {
            System.out.println("ERROR: Failed to initialize the SystemMonitor for " + directory.toString());
            e.printStackTrace();
        }

        try {
            monitor.startMonitor();
            System.out.println("Monitoring files created at: " + directory.toString());
        } catch (Exception e) {
            System.out.println("ERROR: Failure processing events");
            e.printStackTrace();
        }
    }
}
