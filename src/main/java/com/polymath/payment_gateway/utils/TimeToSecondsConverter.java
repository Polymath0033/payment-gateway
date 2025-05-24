package com.polymath.payment_gateway.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimeToSecondsConverter {
    public static long convertDateTimeToSeconds(LocalDateTime dateTime) {
        return Math.max(0, dateTime.toEpochSecond(ZoneOffset.UTC) - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    }
}
