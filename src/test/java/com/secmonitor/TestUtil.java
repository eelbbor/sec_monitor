package com.secmonitor;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by rlee on 5/31/14.
 */
public class TestUtil {
    private static SecureRandom random = new SecureRandom();

    public static String randomName() {
        return new BigInteger(130, random).toString(32);
    }

    public static File makeTestDirectory() throws Exception {
        File testDir = new File("src/test/dir_" + randomName());
        try {
            if (!testDir.exists()) {
                testDir.mkdir();
            }
            return testDir;
        } catch (Exception e) {
            testDir.delete();
            throw e;
        }
    }

    public static File createFile(String directoryPath, Object object) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(directoryPath + "/file_" + randomName() + ".json");
        mapper.writeValue(file, object);
        file.createNewFile();
        return file;
    }
}
