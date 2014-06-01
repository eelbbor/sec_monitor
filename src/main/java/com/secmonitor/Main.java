package com.secmonitor;

import com.secmonitor.service.SystemMonitor;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        //TODO: need to parse args for directory and output timing
        SystemMonitor monitor = new SystemMonitor(Paths.get("./"), null);
        monitor.startMonitor();
    }
}
