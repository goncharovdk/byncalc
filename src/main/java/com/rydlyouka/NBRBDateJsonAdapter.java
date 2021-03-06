package com.rydlyouka;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
* To be used as GSON JsonAdapter.
*/

class NBRBDateJsonAdapter extends TypeAdapter<Date> {

    private final SimpleDateFormat df;

    public NBRBDateJsonAdapter() {
        df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("Europe/Minsk"));
    }

    @Override
    public void write(JsonWriter out, Date date) throws IOException {
        out.value(df.format(date));
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        try {
            return df.parse(in.nextString());
        } catch (ParseException e) {
            // e.printStackTrace();
            System.err.println(e.getMessage());
            return null;
        }
    }
}
