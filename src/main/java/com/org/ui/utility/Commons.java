package com.org.ui.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This class contains common utility functions available for the project.
 */

public class Commons {

    private static final Logger logger = LoggerFactory.getLogger(Commons.class);

    public synchronized static long getCurrentTimeStamp() {
        // System.out.println(System.nanoTime());
        return System.nanoTime();
    }

    public static Date getTime() {
        Calendar calendar = Calendar.getInstance();
        // System.out.println(calendar.getTime());
        return calendar.getTime();
    }

    public static String getDateTime(String dateTimeFormat) {
        DateFormat dateFormat = new SimpleDateFormat(dateTimeFormat);
        Date date = new Date();
        String dateTime = dateFormat.format(date);
        // System.out.println(dateTime);
        return dateTime;
    }

    public static void generateFile(String filePath) {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        try {
            FileWriter writer = new FileWriter(file);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getRelativePath(String basePath, String otherPath) {
        Path basePathObj = Paths.get(basePath);
        Path otherPathObj = Paths.get(otherPath);
        Path relativePath = basePathObj.relativize(otherPathObj);
        return relativePath.toString();
    }

    public static String getRandomNumber(int length) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (i < length) {
            builder.append(getRandomInt());
            i++;
        }
        return builder.toString();
    }

    private static int getRandomInt() {
        return (int) (Math.random() * 9);
    }

    /**
     * Generate Edit string by appending Edit or by removing Edit from the string.
     */
    public static String generateEditInput(String input) {
        if (input.contains("Edit")) {
            return input.replace("Edit", "");
        } else {
            return input + "Edit";
        }
    }

    /**
     * Generates random number with in given range.
     */
    public static int getRandomNumber(int min, int max) {
        return min + (int) (Math.random() * max);
    }

}
