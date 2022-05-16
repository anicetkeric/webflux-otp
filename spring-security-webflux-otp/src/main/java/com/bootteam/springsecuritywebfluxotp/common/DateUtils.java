package com.bootteam.springsecuritywebfluxotp.common;

import lombok.experimental.UtilityClass;

import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@UtilityClass
public class DateUtils {


    /**
     * The default time zone identifier.
     */
    public final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    /**
     * The time zone to use by default.
     */
    public final ZoneId zoneId = DEFAULT_ZONE_ID;

    /**
     * Converts an object of type LocalDateTime to an object of type Calendar.
     *
     * @param localDateTime the object of type LocalDateTime to convert.
     * @return the Calendar object instance converted from LocalDateTime.
     */
    public Calendar convertFromLocalDateTimeToCalendar(LocalDateTime localDateTime) {
        return GregorianCalendar.from(localDateTime.atZone(zoneId));
    }

    /**
     * Converts an object of type Calendar to an object of type LocalDateTime.
     *
     * @param calendar the instance of the Calendar object to convert.
     * @return the instance of the LocalDateTime object converted from Calendar.
     */
    public LocalDateTime convertFromCalendarToLocalDate(Calendar calendar) {
        return calendar.toInstant().atZone(zoneId).toLocalDateTime();
    }

    /**
     * Converts an object of type LocalDateTime to an object of type Date.
     *
     * @param localDateTime the LocalDateTime object to convert.
     * @return the converted Date object.
     */
    public Date convertFromLocalDateToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

    public Date convertFromLocalDateToDate() {
        return Date.from(LocalDateTime.now().atZone(zoneId).toInstant());
    }

    /**
     * Converts an object of type Date to an object of type LocalDateTime.
     *
     * @param date the object of type Date to convert.
     * @return the object of type LocalDateTime converted.
     */
    public LocalDateTime convertFromDateToLocalDate(Date date) {
        return date.toInstant().atZone(zoneId).toLocalDateTime();
    }

    public LocalDate convertToLocalDate(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate();
    }

    public LocalDateTime convertToLocalDateTime(LocalDate localDate) {
        return localDate.atStartOfDay();
    }

    public LocalDateTime convertToLocalDateTime(LocalDate localDate, LocalTime localTime) {
        return localDate.atTime(localTime);
    }

    public Instant convertFromLocalDateToInstant(LocalDateTime localDateTime) {
        return localDateTime.atZone(zoneId).toInstant();
    }
    public LocalDateTime getLocalDateTimeNow() {
        return LocalDateTime.now(zoneId);
    }
}
