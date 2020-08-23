/*
 * Copyright (c) 2016-2020 Gleb Gorelov.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.glebfox.utils;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.Locale;

/**
 * @author glebfox
 */
public class DateTimeUtils {

    private DateTimeUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Obtains an instance of {@code LocalTime} from a date object.
     * <p>
     * If the date object is instance of {@link java.sql.Time}, then {@link java.sql.Time#toLocalTime()} is used.
     * Otherwise {@code LocalTime} is obtained by converting {@code Instant} at {@link ZoneId#systemDefault()}
     * to {@code LocalTime}.
     *
     * @param date the date object to convert, not null
     * @return the local time, not null
     */
    public static LocalTime asLocalTime(Date date) {
        return asLocalTime(date, getDefaultTimeZone());
    }

    /**
     * Obtains an instance of {@code LocalTime} from a date object.
     * <p>
     * If the date object is instance of {@link java.sql.Time}, then {@link java.sql.Time#toLocalTime()} is used.
     * Otherwise {@code LocalTime} is obtained by converting {@code Instant} at {@link ZoneId#systemDefault()}
     * to {@code LocalTime}.
     *
     * @param date   the date object to convert, not null
     * @param zoneId the time zone id, not null
     * @return the local time, not null
     */
    public static LocalTime asLocalTime(Date date, ZoneId zoneId) {
        return date instanceof Time ? ((Time) date).toLocalTime()
                : date.toInstant().atZone(zoneId).toLocalTime();
    }

    /**
     * Obtains an instance of {@code LocalDate} from a date object.
     * <p>
     * If the date object is instance of {@link java.sql.Date}, then {@link java.sql.Date#toLocalDate()} is used.
     * Otherwise {@code LocalDate} is obtained by converting {@code Instant} at {@link ZoneId#systemDefault()}
     * to {@code LocalDate}.
     *
     * @param date the date object to convert, not null
     * @return the local date, not null
     */
    public static LocalDate asLocalDate(Date date) {
        return asLocalDate(date, getDefaultTimeZone());
    }

    /**
     * Obtains an instance of {@code LocalDate} from a date object.
     * <p>
     * If the date object is instance of {@link java.sql.Date}, then {@link java.sql.Date#toLocalDate()} is used.
     * Otherwise {@code LocalDate} is obtained by converting {@code Instant} at {@link ZoneId#systemDefault()}
     * to {@code LocalDate}.
     *
     * @param date   the date object to convert, not null
     * @param zoneId the time zone id, not null
     * @return the local date, not null
     */
    public static LocalDate asLocalDate(Date date, ZoneId zoneId) {
        return date instanceof java.sql.Date ? ((java.sql.Date) date).toLocalDate()
                : date.toInstant().atZone(zoneId).toLocalDate();
    }

    /**
     * Obtains an instance of {@code LocalDateTime} from a date object.
     * <p>
     * {@code LocalDate} is obtained by converting {@code Instant} at {@link ZoneId#systemDefault()}
     * to {@code LocalDateTime}.
     *
     * @param date the date object to convert, not null
     * @return the local date-time, not null
     */
    public static LocalDateTime asLocalDateTime(Date date) {
        return asLocalDateTime(date, getDefaultTimeZone());
    }

    /**
     * Obtains an instance of {@code LocalDateTime} from a date object.
     * <p>
     * {@code LocalDate} is obtained by converting {@code Instant} at {@link ZoneId#systemDefault()}
     * to {@code LocalDateTime}.
     *
     * @param date   the date object to convert, not null
     * @param zoneId the time zone id, not null
     * @return the local date-time, not null
     */
    public static LocalDateTime asLocalDateTime(Date date, ZoneId zoneId) {
        return date.toInstant().atZone(zoneId).toLocalDateTime();
    }

    /**
     * Obtains an instance of {@code Date} from a local time object.
     * <p>
     * A {@code Date} object represents a current date with time represented by {@code LocalTime}.
     *
     * @param localTime the local time object, not null
     * @return the date, not null
     */
    public static Date asDate(LocalTime localTime) {
        return asDate(localTime, getDefaultTimeZone());
    }

    /**
     * Obtains an instance of {@code Date} from a local time object.
     * <p>
     * A {@code Date} object represents date represented by {@code LocalDate}
     * with time represented by {@code LocalTime}.
     *
     * @param localTime the local time object, not null
     * @param localDate the local date object, not null
     * @return the date, not null
     */
    public static Date asDate(LocalTime localTime, LocalDate localDate) {
        return asDate(localTime, localDate, getDefaultTimeZone());
    }

    /**
     * Obtains an instance of {@code Date} from a local time object.
     * <p>
     * A {@code Date} object represents a current date with time represented by {@code LocalTime}.
     *
     * @param localTime the local time object, not null
     * @param zoneId    the time zone id, not null
     * @return the date, not null
     */
    public static Date asDate(LocalTime localTime, ZoneId zoneId) {
        return asDate(localTime, LocalDate.now(), zoneId);
    }

    /**
     * Obtains an instance of {@code Date} from a local time object.
     * <p>
     * A {@code Date} object represents date represented by {@code LocalDate}
     * with time represented by {@code LocalTime}.
     *
     * @param localTime the local time object, not null
     * @param localDate the local date object, not null
     * @param zoneId    the time zone id, not null
     * @return the date, not null
     */
    public static Date asDate(LocalTime localTime, LocalDate localDate, ZoneId zoneId) {
        return Date.from(localTime.atDate(localDate).atZone(zoneId).toInstant());
    }

    /**
     * Obtains an instance of {@code Date} from a local date object.
     *
     * @param localDate the local date object, not null
     * @return the date, not null
     */
    public static Date asDate(LocalDate localDate) {
        return asDate(localDate, getDefaultTimeZone());
    }

    /**
     * Obtains an instance of {@code Date} from a local date object.
     *
     * @param localDate the local date object, not null
     * @param zoneId    the time zone id, not null
     * @return the date, not null
     */
    public static Date asDate(LocalDate localDate, ZoneId zoneId) {
        return Date.from(localDate.atStartOfDay(zoneId).toInstant());
    }

    /**
     * Obtains an instance of {@code Date} from a local date-time object.
     *
     * @param localDateTime the local date-time object, not null
     * @return the date, not null
     */
    public static Date asDate(LocalDateTime localDateTime) {
        return asDate(localDateTime, getDefaultTimeZone());
    }

    /**
     * Obtains an instance of {@code Date} from a local date-time object.
     *
     * @param localDateTime the local date-time object, not null
     * @param zoneId        the time zone id, not null
     * @return the date, not null
     */
    public static Date asDate(LocalDateTime localDateTime, ZoneId zoneId) {
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

    /**
     * Returns a date with zero time.
     *
     * @param date the date object, not null
     * @return the date, not null
     */
    public static Date getDateWithoutTime(Date date) {
        return asDate(asLocalDate(date));
    }

    /**
     * Formats a {@code Date} into a date/time string.
     *
     * @param date   a date to be formatted into a date/time string
     * @param format the pattern describing the date and time format
     * @return the formatted date/time string
     */
    public static String format(Date date, String format) {
        return format(date, format, getDefaultLocale());
    }

    /**
     * Formats a {@code Date} into a date/time string.
     *
     * @param date   a date to be formatted into a date/time string
     * @param format the pattern describing the date and time format
     * @param locale the locale to use, not null
     * @return the formatted date/time string
     */
    public static String format(Date date, String format, Locale locale) {
        return new SimpleDateFormat(format, locale).format(date);
    }

    /**
     * Returns the date corresponding to the first day of the week based on a specified date.
     *
     * @param date the date object, not null
     * @return the date that corresponds to the first day of the week, not null
     */
    public static Date getFirstDateOfWeek(Date date) {
        return getFirstDateOfWeek(date, getDefaultLocale());
    }

    /**
     * Returns the date corresponding to the first day of the week based on a specified date.
     *
     * @param date   the date object, not null
     * @param locale the locale to use, not null
     * @return the date that corresponds to the first day of the week, not null
     */
    public static Date getFirstDateOfWeek(Date date, Locale locale) {
        return asDate(__getFirstDateOfWeek(date, locale));
    }

    private static LocalDate __getFirstDateOfWeek(Date date, Locale locale) {
        TemporalField fieldISO = WeekFields.of(locale).dayOfWeek();
        return asLocalDate(date).with(fieldISO, 1);
    }

    /**
     * Returns the date corresponding to the last day of the week based on a specified date.
     *
     * @param date the date object, not null
     * @return the date that corresponds to the last day of the week, not null
     */
    public static Date getLastDateOfWeek(Date date) {
        return getLastDateOfWeek(date, getDefaultLocale());
    }

    /**
     * Returns the date corresponding to the last day of the week based on a specified date.
     *
     * @param date   the date object, not null
     * @param locale the locale to use, not null
     * @return the date that corresponds to the last day of the week, not null
     */
    public static Date getLastDateOfWeek(Date date, Locale locale) {
        return asDate(__getFirstDateOfWeek(date, locale).plusDays(6));
    }

    /**
     * Returns the date corresponding to the first day of the month based on a specified date.
     * <p>
     * The ISO calendar system behaves as follows:<br>
     * The input 2011-01-15 will return 2011-01-01.<br>
     * The input 2011-02-15 will return 2011-02-01.
     *
     * @param date the date object, not null
     * @return the first day-of-month, not null
     */
    public static Date getFirstDateOfMonth(Date date) {
        return asDate(asLocalDate(date).with(TemporalAdjusters.firstDayOfMonth()));
    }

    /**
     * Returns the date corresponding to the last day of the month based on a specified date.
     * <p>
     * The ISO calendar system behaves as follows:<br>
     * The input 2011-01-15 will return 2011-01-31.<br>
     * The input 2011-02-15 will return 2011-02-28.<br>
     * The input 2012-02-15 will return 2012-02-29 (leap year).<br>
     * The input 2011-04-15 will return 2011-04-30.
     *
     * @param date the date object, not null
     * @return the last day-of-month, not null
     */
    public static Date getLastDateOfMonth(Date date) {
        return asDate(asLocalDate(date).with(TemporalAdjusters.lastDayOfMonth()));
    }

    /**
     * Gets the textual representation, such as 'January' or 'December' for passed date object.
     * <p>
     * Returns the textual name used to identify the month-of-year
     * with {@link TextStyle#FULL} style and default locale.
     * <p>
     * If no textual mapping is found then the {@link Month#getValue() numeric value} is returned.
     *
     * @param date the date to get the text value of the month-of-year
     * @return the text value of the month-of-year, not null
     */
    public static String getMonthName(Date date) {
        return getMonthName(date, TextStyle.FULL, getDefaultLocale());
    }

    /**
     * Gets the textual representation, such as 'Jan' or 'December' for passed date object.
     * <p>
     * Returns the textual name used to identify the month-of-year,
     * suitable for presentation to the user.
     * <p>
     * If no textual mapping is found then the {@link Month#getValue() numeric value} is returned.
     *
     * @param date   the date to get the text value of the month-of-year
     * @param style  the length of the text required, not null
     * @param locale the locale to use, not null
     * @return the text value of the month-of-year, not null
     */
    public static String getMonthName(Date date, TextStyle style, Locale locale) {
        return asLocalDate(date).getMonth().getDisplayName(style, locale);
    }

    /**
     * Gets the day-of-month field.
     * <p>
     * This method returns the primitive {@code int} value for the day-of-month.
     *
     * @param date the date object, not null
     * @return the day-of-month, from 1 to 31
     */
    public static int getDayOfMonth(Date date) {
        return asLocalDate(date).getDayOfMonth();
    }

    /**
     * Gets the year field.
     * <p>
     * This method returns the primitive {@code int} value for the year.
     *
     * @param date the date object, not null
     * @return the year
     */
    public static int getYear(Date date) {
        return asLocalDate(date).getYear();
    }

    /**
     * Gets the day-of-week {@code int} value.
     * <p>
     * The values are numbered following the ISO-8601 standard, from 1 (Monday) to 7 (Sunday).
     *
     * @param date the date object, not null
     * @return the day-of-week, from 1 (Monday) to 7 (Sunday)
     */
    public static int getDayOfWeek(Date date) {
        return asLocalDate(date).getDayOfWeek().getValue();
    }

    /**
     * Checks if the date object represented by {@code input} is within the range
     * represented by {@code startDate} and {@code endDate} inclusive.
     *
     * @param input     a date object to test, not null
     * @param startDate a date object corresponding to the lower boundary, not null
     * @param endDate   a date object corresponding to the upper boundary, not null
     * @return {@code true} if the date object represented by {@code input} is within
     * the range represented by {@code startDate} and {@code endDate} inclusive
     */
    public static boolean isWithinRange(Date input, Date startDate, Date endDate) {
        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("The start date must be earlier or equals to the end date");
        }
        return !(input.before(startDate) || input.after(endDate));
    }

    /**
     * Checks if a {@code input} is <i>not</i> within the range
     * represented by {@code startDate} and {@code endDate}.
     *
     * @param input     a date object to test, not null
     * @param startDate a date object corresponding to the lower boundary, not null
     * @param endDate   a date object corresponding to the upper boundary, not null
     * @return {@code true} if the date object represented by {@code input} is <i>not</i>
     * within the range represented by {@code startDate} and {@code endDate} inclusive
     */
    public static boolean isNotWithinRange(Date input, Date startDate, Date endDate) {
        return !isWithinRange(input, startDate, endDate);
    }

    private static Locale getDefaultLocale() {
        return Locale.getDefault();
    }

    private static ZoneId getDefaultTimeZone() {
        return ZoneId.systemDefault();
    }
}
