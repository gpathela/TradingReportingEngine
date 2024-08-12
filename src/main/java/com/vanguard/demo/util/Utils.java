package com.vanguard.demo.util;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Utils {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    public static final String ERROR_LOADING_DATA = "Error loading data";

    public static Date getDateFromString(String date, String patternString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(patternString);
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + date, e);
        }
    }

    public static Date getDateFromString(String date) {
        return getDateFromString(date, DEFAULT_DATE_FORMAT);
    }

    public static LocalDateTime getDateTimeFromString(String creationTimestamp, DateTimeFormatter formatter) {
        try {
            return LocalDateTime.parse(creationTimestamp, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date time format: " + creationTimestamp, e);
        }
    }

    public static LocalDateTime getDateTimeFromString(String creationTimestamp) {
        return getDateTimeFromString(creationTimestamp, DEFAULT_DATE_TIME_FORMATTER);
    }
    
}
