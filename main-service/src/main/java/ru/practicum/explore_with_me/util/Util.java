package ru.practicum.explore_with_me.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    public static final LocalDateTime DATE_TIME_MIN = LocalDateTime.of(1, 1, 1, 0, 0);
    public static final LocalDateTime DATE_TIME_MAX = LocalDateTime.of(294276, 12, 31, 23, 59, 59);
}
