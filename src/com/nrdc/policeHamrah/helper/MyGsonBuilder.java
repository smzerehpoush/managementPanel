package com.nrdc.policeHamrah.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

public class MyGsonBuilder {
    public static Gson build() {
        return new GsonBuilder().registerTypeAdapter(Date.class, new DateLongFormatTypeAdapter()).create();

    }
}
