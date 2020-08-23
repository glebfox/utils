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

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static java.util.Locale.*;

/**
 * @author glebfox
 */
public class DateTimeUtilsTest extends Assert {

    private int[] daysPerMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private int[] daysPerMonthLeapYear = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private String[] monthNamesFullEn = {"January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};

    private Locale ruRu = forLanguageTag("ru");

    @Test
    public void testDateToLocalTimeConverting() {
        for (int expectedHour = 0; expectedHour <= 23; expectedHour++) {
            for (int expectedMinute = 0; expectedMinute < 60; expectedMinute++) {
                for (int expectedSecond = 0; expectedSecond < 60; expectedSecond++) {
                    Date date = getCalendar(2016, 0, 1, expectedHour, expectedMinute, expectedSecond).getTime();
                    LocalTime localTime = DateTimeUtils.asLocalTime(date);

                    assertEquals("hour", expectedHour, localTime.getHour());
                    assertEquals("minute", expectedMinute, localTime.getMinute());
                    assertEquals("second", expectedSecond, localTime.getSecond());
                }
            }
        }
    }

    @Test
    public void testDateToLocalDateConverting() {
        testDateToLocalDateConverting(2015, daysPerMonth);
        testDateToLocalDateConverting(2016, daysPerMonthLeapYear);
    }

    private void testDateToLocalDateConverting(int expectedYear, int[] daysPerMonth) {
        for (int month = 0; month < 12; month++) {
            for (int day = 1; day <= daysPerMonth[month]; day++) {
                Date date = getCalendar(expectedYear, month, day).getTime();
                LocalDate localDate = DateTimeUtils.asLocalDate(date);

                assertEquals("year", expectedYear, localDate.getYear());
                assertEquals("month", month + 1, localDate.getMonthValue());
                assertEquals("day", day, localDate.getDayOfMonth());
            }
        }
    }

    @Test
    public void testDateToLocalDateTimeConverting() {
        testDateToLocalDateTimeConverting(2015, 15);
        testDateToLocalDateTimeConverting(2016, 15);
    }

    private void testDateToLocalDateTimeConverting(int expectedYear, int expectedDay) {
        for (int month = 0; month < 12; month++) {
            for (int expectedHour = 0; expectedHour <= 23; expectedHour++) {
                for (int expectedMinute = 0; expectedMinute < 60; expectedMinute++) {
                    for (int expectedSecond = 0; expectedSecond < 60; expectedSecond++) {
                        Date date = getCalendar(expectedYear, month, expectedDay,
                                expectedHour, expectedMinute, expectedSecond).getTime();
                        LocalDateTime localDateTime = DateTimeUtils.asLocalDateTime(date);

                        assertEquals("year", expectedYear, localDateTime.getYear());
                        assertEquals("month", month + 1, localDateTime.getMonthValue());
                        assertEquals("day", expectedDay, localDateTime.getDayOfMonth());
                        assertEquals("hour", expectedHour, localDateTime.getHour());
                        assertEquals("minute", expectedMinute, localDateTime.getMinute());
                        assertEquals("second", expectedSecond, localDateTime.getSecond());
                    }
                }
            }
        }
    }

    @Test
    public void testLocalTimeToDateConverting() {
        for (int hour = 0; hour <= 23; hour++) {
            for (int minute = 0; minute < 60; minute++) {
                for (int second = 0; second < 60; second++) {
                    Date expectedDate = getCalendar(2016, 0, 1,
                            hour, minute, second).getTime();
                    Date actualDate = DateTimeUtils.asDate(LocalTime.of(hour, minute, second),
                            LocalDate.of(2016, 1, 1));

                    assertEquals(expectedDate, actualDate);
                }
            }
        }
    }

    @Test
    public void testLocalDateToDateConverting() {
        testLocalDateToDateConverting(2015, daysPerMonth);
        testLocalDateToDateConverting(2016, daysPerMonthLeapYear);
    }

    private void testLocalDateToDateConverting(int expectedYear, int[] daysPerMonth) {
        for (int month = 0; month < 12; month++) {
            for (int day = 1; day <= daysPerMonth[month]; day++) {
                Date expectedDate = getCalendar(expectedYear, month, day).getTime();
                Date actualDate = DateTimeUtils.asDate(LocalDate.of(expectedYear, month + 1, day));
                assertEquals(expectedDate, actualDate);
            }
        }
    }

    @Test
    public void testLocalDateTimeToDateConverting() {
        testLocalDateTimeToDateConverting(2015, 15);
        testLocalDateTimeToDateConverting(2016, 15);
    }

    private void testLocalDateTimeToDateConverting(int year, int day) {
        for (int month = 0; month < 12; month++) {
            for (int hour = 0; hour <= 23; hour++) {
                for (int minute = 0; minute < 60; minute++) {
                    for (int second = 0; second < 60; second++) {
                        Date expectedDate = getCalendar(year, month, day,
                                hour, minute, second).getTime();
                        Date actualDate = DateTimeUtils.asDate(LocalDateTime.of(year, month + 1, day,
                                hour, minute, second));

                        assertEquals(expectedDate, actualDate);
                    }
                }
            }
        }
    }

    @Test
    public void testDateWithoutTime() {
        Date now = new Date();
        Date expected = getDateWithoutTime(now);
        Date actual = DateTimeUtils.getDateWithoutTime(now);

        assertEquals(expected, actual);
    }

    @Test
    public void testFormat() {
        Date date = getCalendar(2001, 6, 4, 12, 8, 56).getTime();
        assertEquals("2001-07-04", DateTimeUtils.format(date, "yyyy-MM-dd", ENGLISH));
        assertEquals("Wed, Jul 4, '01", DateTimeUtils.format(date, "EEE, MMM d, ''yy", ENGLISH));
        assertEquals("12:08 PM", DateTimeUtils.format(date, "h:mm a", ENGLISH));
        assertEquals("2001-07-04T12:08:56.000", DateTimeUtils.format(date, "yyyy-MM-dd'T'HH:mm:ss.SSS", ENGLISH));
        assertEquals("Wed, 4 Jul 2001 12:08:56", DateTimeUtils.format(date, "EEE, d MMM yyyy HH:mm:ss", ENGLISH));
    }

    @Test
    public void testFirstDateOfWeek() {
        Date date = getCalendar(2016, 0, 4, 10, 42, 37).getTime();
        testFirstDateOfWeak(date, ruRu);
        testFirstDateOfWeak(date, UK);
        testFirstDateOfWeak(date, US);

        date = getCalendar(2016, 0, 15, 10, 42, 37).getTime();
        testFirstDateOfWeak(date, ruRu);
        testFirstDateOfWeak(date, UK);
        testFirstDateOfWeak(date, US);
    }

    private void testFirstDateOfWeak(Date date, Locale locale) {
        Date expected = getFirstDayOfWeek(date, locale);
        Date actual = DateTimeUtils.getFirstDateOfWeek(date, locale);
        assertEquals(expected, actual);
    }

    @Test
    public void testLastDateOfWeek() {
        Date date = getCalendar(2016, 0, 10, 10, 42, 37).getTime();
        testLastDateOfWeek(date, ruRu);
        testLastDateOfWeek(date, UK);
        testLastDateOfWeek(date, US);

        date = getCalendar(2016, 0, 15, 10, 42, 37).getTime();
        testLastDateOfWeek(date, ruRu);
        testLastDateOfWeek(date, UK);
        testLastDateOfWeek(date, US);
    }

    private void testLastDateOfWeek(Date date, Locale locale) {
        Date expected = getLastDayOfWeek(date, locale);
        Date actual = DateTimeUtils.getLastDateOfWeek(date, locale);
        assertEquals(expected, actual);
    }

    @Test
    public void testFirstDateOfMonth() {
        int year = 2016;
        for (int month = 0; month < 12; month++) {
            Date date = getCalendar(year, month, 15).getTime();
            Date expectedDate = getCalendar(year, month, 1).getTime();
            assertEquals(expectedDate, DateTimeUtils.getFirstDateOfMonth(date));
        }
    }

    @Test
    public void testLastDateOfMonth() {
        testLastDateOfMonth(2015, daysPerMonth);
        testLastDateOfMonth(2016, daysPerMonthLeapYear);
    }

    private void testLastDateOfMonth(int expectedYear, int[] daysPerMonth) {
        for (int month = 0; month < 12; month++) {
            int expectedDay = daysPerMonth[month];
            Date date = getCalendar(expectedYear, month, 15).getTime();
            Date expectedDate = getCalendar(expectedYear, month, expectedDay).getTime();
            assertEquals(expectedDate, DateTimeUtils.getLastDateOfMonth(date));
        }
    }

    @Test
    public void testMonthName() {
        for (int month = 0; month < 12; month++) {
            Date date = getCalendar(2016, month, 1).getTime();
            assertEquals(monthNamesFullEn[month], DateTimeUtils.getMonthName(date,
                    TextStyle.FULL, ENGLISH));
        }
    }

    @Test
    public void testDayOfMonth() {
        testDayOfMonth(2015, daysPerMonth);
        testDayOfMonth(2016, daysPerMonthLeapYear);
    }

    private void testDayOfMonth(int expectedYear, int[] daysPerMonth) {
        for (int month = 0; month < 12; month++) {
            for (int day = 1; day <= daysPerMonth[month]; day++) {
                Date date = getCalendar(expectedYear, month, day).getTime();
                assertEquals(day, DateTimeUtils.getDayOfMonth(date));
            }
        }
    }

    @Test
    public void testYear() {
        for (int expectedYear = 2000; expectedYear <= 2020; expectedYear++) {
            Date date = getCalendar(expectedYear, 0, 1).getTime();
            int actualYear = DateTimeUtils.getYear(date);
            assertEquals(expectedYear, actualYear);

            date = getCalendar(expectedYear, 11, 31).getTime();
            actualYear = DateTimeUtils.getYear(date);
            assertEquals(expectedYear, actualYear);
        }
    }

    @Test
    public void testDayOfWeek() {
        for (int day = 4; day < 10; day++) {
            Date date = getCalendar(2016, 0, day).getTime();
            assertEquals(day - 3, DateTimeUtils.getDayOfWeek(date));
        }
    }

    @Test
    public void testWithinRange() {
        Date startDate = getCalendar(2016, 0, 2).getTime();
        Date endDate = getCalendar(2016, 0, 10).getTime();

        Date date = getCalendar(2016, 0, 5).getTime();
        assertTrue(DateTimeUtils.isWithinRange(date, startDate, endDate));
        assertTrue(DateTimeUtils.isWithinRange(startDate, startDate, endDate));
        assertTrue(DateTimeUtils.isWithinRange(endDate, startDate, endDate));

        date = getCalendar(2016, 0, 11).getTime();
        assertFalse(DateTimeUtils.isWithinRange(date, startDate, endDate));
        date = getCalendar(2016, 0, 1).getTime();
        assertFalse(DateTimeUtils.isWithinRange(date, startDate, endDate));
        date = getCalendar(2016, 1, 5).getTime();
        assertFalse(DateTimeUtils.isWithinRange(date, startDate, endDate));
    }

    @Test
    public void testNotWithinRange() {
        Date startDate = getCalendar(2016, 0, 2).getTime();
        Date endDate = getCalendar(2016, 0, 10).getTime();

        Date date = getCalendar(2016, 0, 5).getTime();
        assertFalse(DateTimeUtils.isNotWithinRange(date, startDate, endDate));
        assertFalse(DateTimeUtils.isNotWithinRange(startDate, startDate, endDate));
        assertFalse(DateTimeUtils.isNotWithinRange(endDate, startDate, endDate));

        date = getCalendar(2016, 0, 11).getTime();
        assertTrue(DateTimeUtils.isNotWithinRange(date, startDate, endDate));
        date = getCalendar(2016, 0, 1).getTime();
        assertTrue(DateTimeUtils.isNotWithinRange(date, startDate, endDate));
        date = getCalendar(2016, 1, 5).getTime();
        assertTrue(DateTimeUtils.isNotWithinRange(date, startDate, endDate));
    }

    private Calendar getCalendar(Date date, Locale locale) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(date);

        return calendar;
    }

    private Calendar getCalendar(int year, int month, int day) {
        return new GregorianCalendar(year, month, day);
    }

    private Calendar getCalendar(int year, int month, int day, int hour, int minute, int second) {
        return new GregorianCalendar(year, month, day, hour, minute, second);
    }

    private Calendar getCalendarWithoutTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    private Date getDateWithoutTime(Date date) {
        return getCalendarWithoutTime(date).getTime();
    }

    private Date getFirstDayOfWeek(Date date, Locale locale) {
        Calendar calendar = getCalendar(date, locale);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return getDateWithoutTime(calendar.getTime());
    }

    private Date getLastDayOfWeek(Date date, Locale locale) {
        Calendar calendar = getCalendar(date, locale);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6);
        return getDateWithoutTime(calendar.getTime());
    }
}
