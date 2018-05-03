package com.datastax.vehicle.webservice.resources;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Period of time.
 *
 * Start date is mandatory, end date is optional.
 * Period can be defined:
 *   - from start date to end date
 *   - from start date to now
 *
 *   For a certain point in time only, set start date the same as end date
 */
public class Timeframe {

    private static DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss");

    private String startDate;
    private String endDate;

    public Timeframe() {}

    public Timeframe(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}
