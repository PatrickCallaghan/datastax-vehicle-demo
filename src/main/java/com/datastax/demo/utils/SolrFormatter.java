package com.datastax.demo.utils;

import com.datastax.vehicle.webservice.resources.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Iterator;

public class SolrFormatter {

    private static DateTimeFormatter solrDateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:sss'Z'");


    public static String formatAreaAsSolrString(Area area) {
        StringBuilder sb = new StringBuilder();

        if (area.isPolygon()) {
            sb.append("POLYGON((");
            Iterator<GeoPoint> iter = area.getPolygon().getGeoPoints().iterator();
            while (iter.hasNext()) {
                GeoPoint p = iter.next();
                sb.append(getPointAsSolrString(p));
                sb.append(iter.hasNext() ? ", " : ")) ");
            }
        } else {
            Circle circle = area.getCircle();
            sb.append("BUFFER(POINT(")
                    .append(getPointAsSolrString(circle.getCentre()))
                    .append("), ")
                    .append(circle.getRadius()).append(") ");
        }

        return sb.toString();
    }

    public static String formatDateAsSolrString(DateTime dt) {
        return dt != null ? solrDateFormatter.print(dt) : " * ";
    }

    public static String formatMeasurementsAsSolrString(MeasurementSubset ms) {
        if (ms.isAllMeasurements()) {
            return " * ";
        }

        StringBuilder sb = new StringBuilder( "vehicle, date, lat_long, ");
        Iterator<String> iter = ms.getIncludeOnly().iterator();
        while (iter.hasNext()) {
            sb.append(iter.next());
            if (iter.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public static String formatFilterAsSolrString(String filter) {
        StringBuilder sb = new StringBuilder();

        sb.append(" AND ").append(filter).append(" ");
        return sb.toString();
    }


    private static String getPointAsSolrString(GeoPoint p) {
        return p.getLatitude() + " " + p.getLongitude();
    }


}
