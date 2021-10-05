package com.fatec.havingorder.others;

import android.annotation.SuppressLint;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTextFormatter {

    private static String current = "";
    private static final String ddmmyyyy = "        ";
    private static final Calendar cal = Calendar.getInstance();

    @SuppressLint("DefaultLocale")
    public static void format(EditText dateInput, CharSequence s) {
        if (!s.toString().equals(current)) {
            String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
            String cleanC = current.replaceAll("[^\\d.]|\\.", "");

            int cl = clean.length();
            int sel = cl;
            for (int i = 2; i <= cl && i < 6; i += 2) {
                sel++;
            }
            //Fix for pressing delete next to a forward slash
            if (clean.equals(cleanC)) sel--;

            if (clean.length() < 8){
                clean = clean + ddmmyyyy.substring(clean.length());
            }else{
                //This part makes sure that when we finish entering numbers
                //the date is correct, fixing it otherwise
                int day  = Integer.parseInt(clean.substring(0,2));
                int mon  = Integer.parseInt(clean.substring(2,4));
                int year = Integer.parseInt(clean.substring(4,8));

                mon = mon < 1 ? 1 : Math.min(mon, 12);
                cal.set(Calendar.MONTH, mon-1);
                year = (year<1900)?1900: Math.min(year, 2100);
                cal.set(Calendar.YEAR, year);
                // ^ first set year for the line below to work correctly
                //with leap years - otherwise, date e.g. 29/02/2012
                //would be automatically corrected to 28/02/2012

                day = Math.min(day, cal.getActualMaximum(Calendar.DATE));
                clean = String.format("%02d%02d%02d",day, mon, year);
            }

            clean = String.format("%s/%s/%s", clean.substring(0, 2),
                    clean.substring(2, 4),
                    clean.substring(4, 8));

            sel = Math.max(sel, 0);
            current = clean;
            dateInput.setText(current);
            dateInput.setSelection(Math.min(sel, current.length()));
        }
    }

    public static Calendar stringToCalendar(String stringDate) throws ParseException {
        return stringToCalendar(stringDate, "dd/MM/yyyy");
    }

    public static Calendar stringToCalendar(String stringDate, String format) throws ParseException {
        if (stringDate != null && !stringDate.isEmpty() && format != null && !format.isEmpty()) {
            Calendar calendarDate = Calendar.getInstance();

            calendarDate.setTime((new SimpleDateFormat(format)).parse(stringDate));

            return calendarDate;

        } else return null;
    }
}
