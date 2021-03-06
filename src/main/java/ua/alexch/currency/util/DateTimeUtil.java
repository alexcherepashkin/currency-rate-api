package ua.alexch.currency.util;

import static ua.alexch.currency.exception.InvalidParameterException.DATE_FROM_PARAM;
import static ua.alexch.currency.exception.InvalidParameterException.DATE_TO_PARAM;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import java.util.function.Function;

import ua.alexch.currency.exception.InvalidParameterException;

public final class DateTimeUtil {
    public static final Function<String, DateTimeFormatter> FORMATTER = DateTimeFormatter::ofPattern;
    public static final String 
            DATE_FORMAT = "dd-MM-yyyy",
            DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss",
            DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";

    public static final int 
            HOUR = 23,
            MINUTE = 59,
            SECOND = 59;

    private DateTimeUtil() {}

    public static void validateDatesOrder(LocalDateTime from, LocalDateTime to) {
        if (from.isAfter(to)) {
            throw new InvalidParameterException(DATE_FROM_PARAM, "later than dateTo");
        } else if (to.isAfter(LocalDateTime.MAX)) {
            throw new InvalidParameterException(DATE_TO_PARAM, "exceeds supported range");
        } else if (from.isBefore(LocalDateTime.MIN)) {
            throw new InvalidParameterException(DATE_FROM_PARAM, "exceeds supported range");
        }
    }

    public static LocalDateTime parseDateStartDay(String str) {
        try {
            return parseDate(str).atStartOfDay();
        } catch (DateTimeException e) {
            throw new InvalidParameterException(DATE_FROM_PARAM, str, e);
        }
    }

    public static LocalDateTime parseDateEndDay(String str) {
        try {
            return parseDate(str).atTime(HOUR, MINUTE, SECOND);
        } catch (DateTimeException e) {
            throw new InvalidParameterException(DATE_TO_PARAM, str, e);
        }
    }

    public static LocalDate parseDate(String str) {
        return LocalDate.parse(str, FORMATTER.apply(DATE_FORMAT));
    }

    public static LocalDateTime parseDateTime(String str) {
        return LocalDateTime.parse(str, FORMATTER.apply(DATE_TIME_PATTERN));
    }

    public static LocalDateTime parseDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), TimeZone.getDefault().toZoneId());
    }

    public static String printDateTime(LocalDateTime dateTime) {
        return FORMATTER.apply(DATE_TIME_FORMAT).format(dateTime);
    }
}
