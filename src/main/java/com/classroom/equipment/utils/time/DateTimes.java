package com.classroom.equipment.utils.time;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateTimes {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static LocalDateTime from(String strTime, String format){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(strTime, formatter);
    }

    public static LocalDateTime from(String strTime){
        return from(strTime, DATE_TIME_FORMAT);
    }

    public static String toString(LocalDateTime time, String format){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return time.format(formatter);
    }

    public static String toString(LocalDateTime time){
        return toString(time, DATE_TIME_FORMAT);
    }
}
