package com.waes.scalableweb.timetracking;

import java.util.concurrent.TimeUnit;

public class ConversionUtils {

    public static String millisecondsToReadableDuration(long milliseconds) {
        return millisecondsToDuration(milliseconds).toString();
    }

    static Duration millisecondsToDuration(long milliseconds) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(minutes);
        long millis = milliseconds - TimeUnit.MINUTES.toMillis(minutes) - TimeUnit.SECONDS.toMillis(seconds);

        return new Duration(minutes, seconds, millis);
    }

}
