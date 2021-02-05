package com.mohsenoid.androidutils.date;

import android.content.Context;

import com.mohsenoid.androidutils.R;

import java.util.Calendar;

public class DateCalculator {

    public static String pastDates(Context context, String date,
                                   String dateTimeSeperator, String dateSeperator, String timeSeperator) {

        try {
            String[] dateTimeParts = date.split(dateTimeSeperator);

            String[] dateParts = dateTimeParts[0].split(dateSeperator);
            String[] timeParts = dateTimeParts[1].split(timeSeperator);

            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int day = Integer.parseInt(dateParts[2]);
            int hour = Integer.parseInt(timeParts[0]);
            int min = Integer.parseInt(timeParts[1]);
            int sec = Integer.parseInt(timeParts[2]);

            return DateCalculator.pastDates(context, year, month, day, hour,
                    min, sec);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    public static String pastDates(Context context, int year, int month,
                                   int day, int hour, int min, int sec) {

        String result = null;

        Calendar currentCalendar = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        // calendar1.set(currYear, currMonth, currDay, currHour, currMin);
        calendar.set(year, month - 1, day, hour, min, sec);

        long currentMilliseconds = currentCalendar.getTimeInMillis();
        long milliseconds = calendar.getTimeInMillis();
        long diff = currentMilliseconds - milliseconds;

        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);
        long diffMonth = diff / (30l * 24l * 60l * 60l * 1000l);
        long diffYear = diff / (12l * 30l * 24l * 60l * 60l * 1000l);

        if (diffYear == 0) {
            if (diffMonth == 0) {
                if (diffDays == 0) {
                    if (diffHours == 0) {
                        if (diffMinutes == 0) {
                            result = diffSeconds
                                    + " "
                                    + context.getResources().getString(
                                    R.string.androidutils__ago_sec);

                        } else {
                            result = diffMinutes
                                    + " "
                                    + context.getResources().getString(
                                    R.string.androidutils__ago_min);
                        }
                    } else {
                        result = diffHours
                                + " "
                                + context.getResources().getString(
                                R.string.androidutils__ago_hour);
                    }
                } else {
                    result = diffDays
                            + " "
                            + context.getResources()
                            .getString(R.string.androidutils__ago_day);
                }
            } else {
                result = diffMonth + " "
                        + context.getResources().getString(R.string.androidutils__ago_month);
            }
        } else {
            result = diffYear + " "
                    + context.getResources().getString(R.string.androidutils__ago_year);
        }

        return result;
    }
}
