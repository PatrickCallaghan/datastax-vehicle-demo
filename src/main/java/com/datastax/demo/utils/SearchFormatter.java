package com.datastax.demo.utils;

import com.datastax.vehicle.webservice.resources.*;

import org.apache.commons.codec.binary.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Iterator;

public class SearchFormatter {

    //"yyyy/MM/dd HH:mm:ss"
    private static DateTimeFormatter inputDateFormatter = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss");
    private static DateTimeFormatter searchDateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:sss'Z'");


    public static String formatAreaAsSearchString(Area area) {
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

    //select * from datastax.vehicle where solr_query = '{"q": "speed:[150 TO 200]", "fq": "date:[2018-03-30T12:32:00.000Z TO 2018-03-30T14:45:00.000Z] AND lat_long:\"IsWithin(BUFFER(POINT(47.310179325765915 11.25321866241), .3))\""}' ;

    public static String formatTimeframeAsSearchString(Timeframe timeframe) {
        StringBuilder sb = new StringBuilder(" date:[");
        sb.append(formatDateAsSearchString(timeframe.getStartDate()))
                                                    .append(" TO ")
                                                    .append(formatDateAsSearchString(timeframe.getEndDate()))
                                                    .append("] ");
        return sb.toString();
    }

    public static String formatMeasurementsAsSearchString(boolean measurementsRequired) {
        if (measurementsRequired) {
            return " * ";
        } else {
            return " vehicle_id, date, lat_long ";
        }
    }

    public static String formatOrderAsSearchString(Order order) {
        StringBuilder sb = new StringBuilder(", \"sort\":\"");
        if (order != null) {
            if (order.isAscending()) {
                sb.append("date asc");
            } else {
                sb.append("date desc");
            }
        }
        sb.append("\" ");
        return sb.toString();
    }

    public static String formatFilterAsSearchString(String filter) {
        StringBuilder sb = new StringBuilder();
        sb.append(filter).append(" ");
        return sb.toString();
    }


    private static String formatPointAsSearchString(GeoPoint p) {
        return p.getLat() + " " + p.getLng();
    }


    private static String formatDateAsSearchString(String dateString) {
        DateTime dt = new DateTime(inputDateFormatter.parseDateTime(dateString));
        return dt != null ? searchDateFormatter.print(dt) : " * ";
    }
}
