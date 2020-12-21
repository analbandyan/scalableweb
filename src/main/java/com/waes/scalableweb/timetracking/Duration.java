package com.waes.scalableweb.timetracking;

import lombok.Data;

@Data
public class Duration {

    private final long minutes;
    private final long seconds;
    private final long milliseconds;

    @Override
    public String toString() {
        return String.format("%d mins, %d secs, %d millis", minutes, seconds, milliseconds);
    }

}
