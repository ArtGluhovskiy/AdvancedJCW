package org.art.dao.util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class DateTimeUtil {

    /**
     * This method define user's age from his birth date
     *
     * @param birthDate user's birth date
     * @return user's age (in years)
     */
    public static int defineUserAge(Date birthDate) {
        int age;
        LocalDate localBirthDate = birthDate.toLocalDate();
        LocalDate today = LocalDate.now();
        Period period = Period.between(localBirthDate, today);
        age = period.getYears();
        return age;
    }

    /**
     * This method converts date from string to {@link Date}
     * in order to make it compatible with data base date type
     *
     * @param date date string in appropriate format
     * @return {@link Date}, converted from string
     */
    public static Date toSQLDate(String date) {
        LocalDate birthDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        int day = birthDate.getDayOfMonth();
        int year = birthDate.getYear();
        int month = birthDate.getMonthValue() - 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return new Date(calendar.getTimeInMillis());
    }
}
