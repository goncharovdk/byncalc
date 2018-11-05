package com.rydlyouka;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NBRBRate {
    // {"Cur_ID":292,"Date":"2018-03-17T00:00:00","Cur_Abbreviation":"EUR","Cur_Scale":1,"Cur_Name":"Евро","Cur_OfficialRate":2.4171}

    @SerializedName("Cur_ID")
    String currencyId;

    @SerializedName("Date")
    @JsonAdapter(NBRBDateJsonAdapter.class)
    Date date;

    @SerializedName("Cur_Abbreviation")
    String abbreviation;

    @SerializedName("Cur_Scale")
    BigDecimal scale;

    @SerializedName("Cur_Name")
    String name;

    @SerializedName("Cur_OfficialRate")
    BigDecimal officialRate;

    private static final String urlTemplate = "http://www.nbrb.by/API/ExRates/Rates/%s?onDate=%s&ParamMode=2";

    public static NBRBRate getRate(Date date, String currency) {
        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        URL url;
        try {
            url = new URL(String.format(urlTemplate, currency, df.format(date)));
        } catch (MalformedURLException e) {
            // e.printStackTrace();
            System.err.println(e.getMessage());
            return null;
        }

        try (InputStreamReader in = new InputStreamReader(url.openStream())) {
            return new Gson().fromJson(in, NBRBRate.class);
        } catch (IOException e) {
            // e.printStackTrace();
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        return String.format("Rate of %s on %s is %s BYN for %s units.", abbreviation, df.format(date), officialRate.toString(), scale.toString());
    }

}
