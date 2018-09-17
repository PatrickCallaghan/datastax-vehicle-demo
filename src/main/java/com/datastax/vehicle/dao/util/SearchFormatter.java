package com.datastax.vehicle.dao.util;

import com.datastax.vehicle.webservice.resources.*;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Iterator;

public class SearchFormatter {

    //"yyyy/MM/dd HH:mm:ss"
    private static DateTimeFormatter inputDateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:sss'Z'");
    private static DateTimeFormatter searchDateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:sss'Z'");

    public static String formatMeasurementsAsSearchString(boolean measurementsRequired) {
        if (measurementsRequired) {
            return " * ";
        } else {
            return " vehicle_id, date, lat_long ";
        }
    }

    protected static String formatTimeframeAsSearchString(Timeframe timeframe) {
        StringBuilder sb = new StringBuilder(" date:[");
        sb.append(formatDateAsSearchString(timeframe.getStartDate()))
                .append(" TO ")
                .append(formatDateAsSearchString(timeframe.getEndDate()))
                .append("] ");
        return sb.toString();
    }

    protected static String formatAreaAsSearchString(Area area) {
        StringBuilder sb = new StringBuilder(" lat_long:\\\"IsWithin(");

        if (area.isPolygon()) {
            sb.append("POLYGON((");
            Iterator<GeoPoint> iter = area.getPolygon().getGeoPoints().iterator();
            while (iter.hasNext()) {
                GeoPoint p = iter.next();
                sb.append(formatPointAsSearchString(p));
                sb.append(iter.hasNext() ? ", " : ")) ");
            }
        } else {
            Circle circle = area.getCircle();
            sb.append("BUFFER(POINT(")
                    .append(formatPointAsSearchString(circle.getCentre()))
                    .append("), ")
                    .append(circle.getRadius()).append(") ");
        }
        sb.append( ")\\\" " );

        return sb.toString();
    }

    protected static String formatFilterAsSearchString(String filter) {
        StringBuilder sb = new StringBuilder();
        sb.append(filter).append(" ");
        return sb.toString();
    }

    protected static String formatPointAsSearchString(GeoPoint p) {
        //return p.getLng() + " " + p.getLat();
        return p.getLng() + " " + p.getLat();
    }

    protected static String formatDateAsSearchString(String dateString) {
        DateTime dt = new DateTime(inputDateFormatter.parseDateTime(dateString));
        return dt != null ? searchDateFormatter.print(dt) : " * ";
    }
}
