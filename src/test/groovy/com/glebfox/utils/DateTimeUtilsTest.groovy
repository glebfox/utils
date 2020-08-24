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

package com.glebfox.utils

import spock.lang.Specification

import java.sql.Time
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.TextStyle

import static com.glebfox.utils.DateTimeUtils.*

class DateTimeUtilsTest extends Specification {

    private static final int[] DAYS_PER_MONTH = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
    private static final int[] DAYS_PER_MONTH_LEAP_YEAR = [31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
    private static final String[] MONTH_NAMES_FULL_EN = ["January", "February", "March", "April", "May", "June", "July",
                                                         "August", "September", "October", "November", "December"]

    private static Locale RU = Locale.forLanguageTag("ru-RU")

    def "convert date to local time"() {
        when:
        def localTime = asLocalTime(date as Date)

        then:
        localTime.getHour() == hour
        localTime.getMinute() == minute
        localTime.getSecond() == second

        where:
        [date, hour, minute, second] << generateDateToLocalTimeTestData()
    }

    private List<Object[]> generateDateToLocalTimeTestData() {
        List<Object[]> testData = new ArrayList<>()
        for (int hour = 0; hour <= 23; hour++) {
            for (int minute = 0; minute < 60; minute++) {
                for (int second = 0; second < 60; second++) {
                    Object[] item = new Object[4]
                    item[0] = getDate(2020, 1, 1, hour, minute, second)
                    item[1] = hour
                    item[2] = minute
                    item[3] = second
                    testData.add(item)
                }
            }
        }

        return testData
    }

    def "convert time to local time"() {
        when:
        def localTime = asLocalTime(time as Time)

        then:
        localTime.getHour() == hour
        localTime.getMinute() == minute
        localTime.getSecond() == second

        where:
        [time, hour, minute, second] << generateTimeToLocalTimeTestData()
    }

    private List<Object[]> generateTimeToLocalTimeTestData() {
        List<Object[]> testData = new ArrayList<>()
        for (int hour = 0; hour <= 23; hour++) {
            for (int minute = 0; minute < 60; minute++) {
                for (int second = 0; second < 60; second++) {
                    Object[] item = new Object[4]
                    item[0] = new Time(hour, minute, second)
                    item[1] = hour
                    item[2] = minute
                    item[3] = second
                    testData.add(item)
                }
            }
        }

        return testData
    }

    def "convert date to local date"() {
        when:
        def localDate = asLocalDate(date as Date)

        then:
        localDate.getYear() == year
        localDate.getMonthValue() == month
        localDate.getDayOfMonth() == day

        where:
        [date, year, month, day] << (generateDateToLocalDateTestData(2020, DAYS_PER_MONTH_LEAP_YEAR)
                + generateDateToLocalDateTestData(2021, DAYS_PER_MONTH))
    }

    private List<Object[]> generateDateToLocalDateTestData(int year, int[] daysPerMonth) {
        List<Object[]> testData = new ArrayList<>()
        for (int month = 0; month < 12; month++) {
            for (int day = 1; day <= daysPerMonth[month]; day++) {
                Object[] item = new Object[4]
                item[0] = getDate(year, month + 1, day)
                item[1] = year
                item[2] = month + 1
                item[3] = day
                testData.add(item)
            }
        }

        return testData
    }

    def "convert sql date to local date"() {
        when:
        def localDate = asLocalDate(date as java.sql.Date)

        then:
        localDate.getYear() == year
        localDate.getMonthValue() == month
        localDate.getDayOfMonth() == day

        where:
        [date, year, month, day] << (generateSqlDateToLocalDateTestData(2020, DAYS_PER_MONTH_LEAP_YEAR)
                + generateSqlDateToLocalDateTestData(2021, DAYS_PER_MONTH))
    }

    private List<Object[]> generateSqlDateToLocalDateTestData(int year, int[] daysPerMonth) {
        List<Object[]> testData = new ArrayList<>()
        for (int month = 0; month < 12; month++) {
            for (int day = 1; day <= daysPerMonth[month]; day++) {
                Object[] item = new Object[4]
                item[0] = new java.sql.Date(year - 1900, month, day)
                item[1] = year
                item[2] = month + 1
                item[3] = day
                testData.add(item)
            }
        }

        return testData
    }

    def "convert date to local date-time"() {
        when:
        def localDateTime = asLocalDateTime(date as Date)

        then:
        localDateTime.getYear() == year
        localDateTime.getMonthValue() == month
        localDateTime.getDayOfMonth() == day
        localDateTime.getHour() == hour
        localDateTime.getMinute() == minute
        localDateTime.getSecond() == second

        where:
        [date, year, month, day, hour, minute, second] << (
                generateDateToLocalDateTimeTestData(2020) + generateDateToLocalDateTimeTestData(2021)
        )
    }

    private List<Object[]> generateDateToLocalDateTimeTestData(int year) {
        List<Object[]> testData = new ArrayList<>()
        int day = 1
        for (int month = 0; month < 12; month++) {
            for (int hour = 0; hour <= 23; hour++) {
                for (int minute = 0; minute < 60; minute++) {
                    for (int second = 0; second < 60; second++) {
                        Object[] item = new Object[7]
                        item[0] = getDate(year, month + 1, day, hour, minute, second)
                        item[1] = year
                        item[2] = month + 1
                        item[3] = day
                        item[4] = hour
                        item[5] = minute
                        item[6] = second
                        testData.add(item)
                    }
                }
            }
        }

        return testData
    }

    def "convert local time to date"() {
        expect:
        asDate(localTime as LocalTime) == date

        where:
        [localTime, date] << generateLocalTimeToDateTestData()
    }

    private List<Object[]> generateLocalTimeToDateTestData() {
        List<Object[]> testData = new ArrayList<>()
        Date now = new Date()
        for (int hour = 0; hour <= 23; hour++) {
            for (int minute = 0; minute < 60; minute++) {
                for (int second = 0; second < 60; second++) {
                    Object[] item = new Object[2]
                    item[0] = LocalTime.of(hour, minute, second)
                    item[1] = getDate(now, hour, minute, second)
                    testData.add(item)
                }
            }
        }

        return testData
    }

    def "convert local date to date"() {
        expect:
        asDate(localDate as LocalDate) == date

        where:
        [localDate, date] << (generateLocalDateToDateTestData(2020, DAYS_PER_MONTH_LEAP_YEAR)
                + generateLocalDateToDateTestData(2021, DAYS_PER_MONTH))
    }

    private List<Object[]> generateLocalDateToDateTestData(int year, int[] daysPerMonth) {
        List<Object[]> testData = new ArrayList<>()
        for (int month = 0; month < 12; month++) {
            for (int day = 1; day <= daysPerMonth[month]; day++) {
                Object[] item = new Object[2]
                item[0] = LocalDate.of(year, month + 1, day)
                item[1] = getDate(year, month + 1, day)
                testData.add(item)
            }
        }

        return testData
    }

    def "convert local date and local time to date"() {
        expect:
        asDate(localDate as LocalDate, localTime as LocalTime) == date

        where:
        [localDate, localTime, date] << (generateLocalDateLocalTimeToDateTestData())
    }

    private List<Object[]> generateLocalDateLocalTimeToDateTestData() {
        List<Object[]> testData = new ArrayList<>()
        int year = 2020
        int month = 1
        int day = 1
        for (int hour = 0; hour <= 23; hour++) {
            for (int minute = 0; minute < 60; minute++) {
                for (int second = 0; second < 60; second++) {
                    Object[] item = new Object[3]
                    item[0] = LocalDate.of(year, month, day)
                    item[1] = LocalTime.of(hour, minute, second)
                    item[2] = getDate(year, month, day, hour, minute, second)
                    testData.add(item)
                }
            }
        }

        return testData
    }

    def "convert local date-time to date"() {
        expect:
        asDate(localDateTime as LocalDateTime) == date

        where:
        [localDateTime, date] << (generateLocalDateTimeToDateTestData())
    }

    private List<Object[]> generateLocalDateTimeToDateTestData() {
        List<Object[]> testData = new ArrayList<>()
        int year = 2020
        int month = 1
        int day = 1
        for (int hour = 0; hour <= 23; hour++) {
            for (int minute = 0; minute < 60; minute++) {
                for (int second = 0; second < 60; second++) {
                    Object[] item = new Object[2]
                    item[0] = LocalDateTime.of(year, month, day, hour, minute, second)
                    item[1] = getDate(year, month, day, hour, minute, second)
                    testData.add(item)
                }
            }
        }

        return testData
    }

    def "get date without time"() {
        def now = new Date()
        Date expected = getDate(now, 0, 0, 0)

        expect:
        getDateWithoutTime(now) == expected
    }

    def "format date"() {
        def date = getDate(2001, 7, 4, 12, 8, 56)

        expect:
        format(date, formatString, Locale.ENGLISH) == formattedValue
        format(date, formatString) == formattedValue

        where:
        formatString                || formattedValue
        "yyyy-MM-dd"                || "2001-07-04"
        "EEE, MMM d, ''yy"          || "Wed, Jul 4, '01"
        "h:mm a"                    || "12:08 PM"
        "yyyy-MM-dd'T'HH:mm:ss.SSS" || "2001-07-04T12:08:56.000"
        "EEE, d MMM yyyy HH:mm:ss"  || "Wed, 4 Jul 2001 12:08:56"
    }

    def "get first date of week"() {
        expect:
        getFirstDateOfWeek(date, locale) == firstDate

        where:
        date                | locale    || firstDate
        getDate(2020, 1, 9) | RU        || getDate(2020, 1, 6)
        getDate(2020, 1, 9) | Locale.UK || getDate(2020, 1, 6)
        getDate(2020, 1, 9) | Locale.US || getDate(2020, 1, 5)
    }

    def "get first date of week with default locale"() {
        expect:
        getFirstDateOfWeek(date) == firstDate

        where:
        date                 || firstDate
        getDate(2020, 1, 9)  || getDate(2020, 1, 5)
        getDate(2020, 8, 24) || getDate(2020, 8, 23)
        getDate(2020, 8, 23) || getDate(2020, 8, 23)
    }

    def "get last date of week"() {
        expect:
        getLastDateOfWeek(date, locale) == lastDate

        where:
        date                | locale    || lastDate
        getDate(2020, 1, 9) | RU        || getDate(2020, 1, 12)
        getDate(2020, 1, 9) | Locale.UK || getDate(2020, 1, 12)
        getDate(2020, 1, 9) | Locale.US || getDate(2020, 1, 11)
    }

    def "get last date of week with default locale"() {
        expect:
        getLastDateOfWeek(date) == lastDate

        where:
        date                 || lastDate
        getDate(2020, 1, 9)  || getDate(2020, 1, 11)
        getDate(2020, 8, 24) || getDate(2020, 8, 29)
        getDate(2020, 8, 29) || getDate(2020, 8, 29)
    }

    def "get first date of month"() {
        expect:
        getFirstDateOfMonth(date as Date) == firstDate

        where:
        [date, firstDate] << generateFirstDateOfMonthTestData()
    }

    private List<Object[]> generateFirstDateOfMonthTestData() {
        List<Object[]> testData = new ArrayList<>()
        def year = 2020
        for (int month = 0; month < 12; month++) {
            Object[] item = new Object[2]
            item[0] = getDate(year, month + 1, 10)
            item[1] = getDate(year, month + 1, 1)
            testData.add(item)
        }

        return testData
    }

    def "get last date of month"() {
        expect:
        getLastDateOfMonth(date as Date) == lastDate

        where:
        [date, lastDate] << (generateLastDateOfMonthTestData(2020, DAYS_PER_MONTH_LEAP_YEAR)
                + generateLastDateOfMonthTestData(2021, DAYS_PER_MONTH))
    }

    private List<Object[]> generateLastDateOfMonthTestData(int year, int[] daysPerMonth) {
        List<Object[]> testData = new ArrayList<>()
        for (int month = 0; month < 12; month++) {
            Object[] item = new Object[2]
            item[0] = getDate(year, month + 1, 1)
            item[1] = getDate(year, month + 1, daysPerMonth[month])
            testData.add(item)
        }

        return testData
    }

    def "get month name"() {
        expect:
        getMonthName(date as Date, TextStyle.FULL, Locale.ENGLISH) == monthName
        getMonthName(date as Date) == monthName

        where:
        [date, monthName] << generateMonthNameTestData()
    }

    private List<Object[]> generateMonthNameTestData() {
        List<Object[]> testData = new ArrayList<>()
        for (int month = 0; month < 12; month++) {
            Object[] item = new Object[2]
            item[0] = getDate(2020, month + 1, 1)
            item[1] = MONTH_NAMES_FULL_EN[month]
            testData.add(item)
        }

        return testData
    }

    def "get day of month"() {
        expect:
        getDayOfMonth(date as Date) == day

        where:
        [date, day] << (generateDayOfMonthTestData(2020, DAYS_PER_MONTH_LEAP_YEAR)
                + generateDayOfMonthTestData(2021, DAYS_PER_MONTH))
    }

    private List<Object[]> generateDayOfMonthTestData(int year, int[] daysPerMonth) {
        List<Object[]> testData = new ArrayList<>()
        for (int month = 0; month < 12; month++) {
            for (int day = 1; day <= daysPerMonth[month]; day++) {
                Object[] item = new Object[2]
                item[0] = getDate(year, month + 1, day)
                item[1] = day
                testData.add(item)
            }
        }

        return testData
    }

    def "get year"() {
        when: "date is the first day of the year"
        def date = getDate(year, 1, 1)

        then:
        getYear(date) == year

        when: "date is the last day of the year"
        date = getDate(year, 1, 1)

        then:
        getYear(date) == year

        where:
        year << (2000..2020)
    }

    def "get day of week"() {
        when:
        def monday = getDate(2020, 8, 24)

        then:
        getDayOfWeek(monday) == 1

        when:
        def tuesday = getDate(2020, 8, 25)

        then:
        getDayOfWeek(tuesday) == 2

        when:
        def wednesday = getDate(2020, 8, 26)

        then:
        getDayOfWeek(wednesday) == 3

        when:
        def thursday = getDate(2020, 8, 27)

        then:
        getDayOfWeek(thursday) == 4

        when:
        def friday = getDate(2020, 8, 28)

        then:
        getDayOfWeek(friday) == 5

        when:
        def saturday = getDate(2020, 8, 29)

        then:
        getDayOfWeek(saturday) == 6

        when:
        def sunday = getDate(2020, 8, 30)

        then:
        getDayOfWeek(sunday) == 7
    }

    def "date within range"() {
        def startDate = getDate(2020, 1, 2)
        def endDate = getDate(2020, 1, 10)

        when: "date within range"
        def date = getDate(2020, 1, 5)

        then:
        isWithinRange(date, startDate, endDate)

        and: "start date within range"
        isWithinRange(startDate, startDate, endDate)

        and: "end date within range"
        isWithinRange(endDate, startDate, endDate)

        when: "start date after end date"
        isWithinRange(date, endDate, startDate)

        then: "exception is thrown"
        thrown(IllegalArgumentException)

        when: "date out of range"
        date = getDate(2020, 2, 5)

        then:
        !isWithinRange(date, startDate, endDate)

        when: "date out of range"
        date = getDate(2020, 1, 11)

        then:
        !isWithinRange(date, startDate, endDate)

        when: "date out of range"
        date = getDate(2020, 1, 1)

        then:
        !isWithinRange(date, startDate, endDate)
    }

    def "date is out of range"() {
        def startDate = getDate(2020, 1, 2)
        def endDate = getDate(2020, 1, 10)

        when: "date within range"
        def date = getDate(2020, 1, 5)

        then:
        !isNotWithinRange(date, startDate, endDate)

        and: "start date out of range"
        !isNotWithinRange(startDate, startDate, endDate)

        and: "end date out of range"
        !isNotWithinRange(endDate, startDate, endDate)

        when: "start date after end date"
        isWithinRange(date, endDate, startDate)

        then: "exception is thrown"
        thrown(IllegalArgumentException)

        when: "date out of range"
        date = getDate(2020, 2, 5)

        then:
        isNotWithinRange(date, startDate, endDate)

        when: "date out of range"
        date = getDate(2020, 1, 11)

        then:
        isNotWithinRange(date, startDate, endDate)

        when: "date out of range"
        date = getDate(2020, 1, 1)

        then:
        isNotWithinRange(date, startDate, endDate)
    }

    private Date getDate(int year, int month, int day) {
        return new GregorianCalendar(year, month - 1, day).getTime()
    }

    private Date getDate(int year, int month, int day, int hour, int minute, int second) {
        return new GregorianCalendar(year, month - 1, day, hour, minute, second).getTime()
    }

    private Date getDate(Date date, int hour, int minute, int second) {
        def calendar = Calendar.getInstance()
        calendar.setTime(date)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, second)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.getTime()
    }
}
