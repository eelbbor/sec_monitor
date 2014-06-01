package com.secmonitor.service;

import com.secmonitor.Output;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by rlee on 5/31/14.
 */
public class FileProcessor {
    private long fileCount = 0;
    private long processingTime = 0;
    private Output output;

    public FileProcessor() {
        output = new Output();
    }

    public void processFile(File file) throws IOException, ParseException {
        long startTime = Calendar.getInstance().getTimeInMillis();

        JSONParser parser = new JSONParser();
        JSONObject event = (JSONObject)parser.parse(new FileReader(file));

        processingTime += Calendar.getInstance().getTimeInMillis() - startTime;
    }
    public long getAvgProcessingTime() {
        return fileCount > 0 ? processingTime/fileCount : -1;
    }

    public String getStatusMessage() {
        return output.toString();
    }
}
