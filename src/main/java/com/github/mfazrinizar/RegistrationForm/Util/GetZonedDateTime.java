package com.github.mfazrinizar.RegistrationForm.Util;

/*
 * Author       : M. Fazri Nizar
 * Institution  : Sriwijaya University
 * GitHub       : github.com/mfazrinizar
 * File Name    : GetZonedDateTime.java
 */

import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class GetZonedDateTime {

    public String getCurrentZonedTime() {
        String currentZoneId = "Asia/Jakarta";
        ZonedDateTime nowWIB = ZonedDateTime.now(ZoneId.of(currentZoneId));
        return nowWIB.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public String getZonedTime(String zoneId) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(zoneId));
        return now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
