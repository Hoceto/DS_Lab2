package com.example.demo.date_src;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class UserDate {
    private int day;
    private int month;
    private int year;
    private int thisYear = Calendar.getInstance().get(1);

    public UserDate(int day, int month, int year) {
        this.validateDate(String.format("%d-%d-%d", day, month, year));
        if (year >= 1900 && year <= this.thisYear) {
            this.day = day;
            this.month = month;
            this.year = year;
        } else {
            throw new IllegalArgumentException("Incorrect year number (must be within 1900 or " + this.thisYear + ")");
        }
    }

    private void validateDate(String date) {
        try {
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            df.setLenient(false);
            df.parse(date);
        } catch (ParseException var3) {
            System.out.println("Wrong date format");
            System.exit(1);
        }

    }
}