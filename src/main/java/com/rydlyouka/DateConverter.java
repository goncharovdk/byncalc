package com.rydlyouka;

import com.beust.jcommander.IStringConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateConverter implements IStringConverter<Date> {

    private static final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
    private static final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Minsk"));

    @Override
    public Date convert(String value) {
        try {
            return df.parse(value);
        } catch (ParseException e) {
            try {
                return df.parse(
                        String.format("%s.%s", value,calendar.get(Calendar.YEAR)));
            } catch (ParseException e1) {
                // e1.printStackTrace();
                System.err.println(e1.getMessage());
                return null;
            }
        }
    }

    public static String format(Date date) {
        return df.format(date);
    }

    public static Date parse(String string) throws ParseException {
        return df.parse(string);
    }
}
