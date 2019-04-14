package com.nrdc.policeHamrah.helper;

import java.util.UUID;

/**
 * Created by mhd.zerehpoosh on 4/14/2019.
 */
public class IDBuilder {
    public static String generateNewID() {
        return UUID.randomUUID().toString().substring(0, 10) + String.valueOf(System.currentTimeMillis());
    }
}
