package com.datastax.vehicle.webservice.validation;

import com.datastax.vehicle.webservice.resources.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class WSInputValidator {

    private static final String expectedDateFormat = "yyyy/MM/dd HH:mm:ss";

    //2018/04/25 12:32:45
    private static DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss");

    public static ValidationOutcome validateGlobalRequestInputWrapper(GlobalRequestInputWrapper iw, boolean requiresTimeframe) {

        Area area = iw.getArea();
        Timeframe timeframe = iw.getTimeframe();

        ValidationOutcome valOut = new ValidationOutcome();

        if (area == null) {
            valOut.addMessage("Area is mandatory but was not specified");
        } else {
            valOut.addMessage(WSInputValidator.validateArea(area));
        }

        if (requiresTimeframe && iw.getTimeframe() == null) {
            valOut.addMessage("Timeframe is mandatory for this call but was not specified");
        }

        if (timeframe != null) {
            valOut.addMessage(WSInputValidator.validateTimeframe(timeframe));
        }

//        if (ms != null) {
//            valOut.addMessage(WSInputValidator.validateMeasurementSubset(ms));
//        }

        return valOut;
    }

    public static ValidationOutcome validateVehicleRequestInputWrapper(VehicleRequestInputWrapper iw) {

        String vehicleId = iw.getVehicleId();
        Area area = iw.getArea();
        Timeframe timeframe = iw.getTimeframe();

        ValidationOutcome valOut = new ValidationOutcome();

        if (vehicleId == null) {
            valOut.addMessage("VehicleId is mandatory but was not specified");
        }

        if (area == null) {
            valOut.addMessage("Area is mandatory but was not specified");
        } else {
            valOut.addMessage(WSInputValidator.validateArea(area));
        }

        if (timeframe != null) {
            valOut.addMessage(WSInputValidator.validateTimeframe(timeframe));
        }

//        if (ms != null) {
//            valOut.addMessage(WSInputValidator.validateMeasurementSubset(ms));
//        }

        return valOut;
    }

    private static String validateArea(Area area) {
        Polygon polygon = area.getPolygon();
        Circle circle = area.getCircle();

        if (polygon != null && circle != null) {
            return "Area can contain only one of Polygon or Circle";
        }

        if (polygon == null && circle == null) {
            return "Area must contain either Polygon or Circle";
        }

        if ((polygon != null) &&
                (polygon.getGeoPoints() == null || polygon.getGeoPoints().size() < 2)) {
                return "Polygon must contain at least two points";
        }

        if ((circle != null) &&
                (circle.getCentre() == null || (circle.getRadius() == null || circle.getRadius().equals(0)))) {
            return "Circle must have a centre and a non-zero radius";
        }

        return null;
    }

    private static String validateTimeframe(Timeframe timeframe) {
        if (timeframe.getStartDate() == null && timeframe.getEndDate() == null) {
            return "Timeframe must define at least one date";
        }

        DateTime startDate = null;
        DateTime endDate = null;
        try {
            startDate = timeframe.getStartDate() != null ? startDate = dtf.parseDateTime(timeframe.getStartDate()) : null;
            endDate = timeframe.getEndDate() != null ? endDate = dtf.parseDateTime(timeframe.getEndDate()) : null;
        } catch (IllegalArgumentException iae) {
            return "At least one date had an invalid format. Expected format: " + expectedDateFormat +
                    ", actual start date received: " + timeframe.getStartDate() +
                    ", actual end date received: " + timeframe.getEndDate();
        }

        if ((startDate != null && endDate != null) && (startDate.isAfter(endDate))) {
            return "Start date cannot be after end date";
        }

        return null;
    }

//    private static String validateMeasurementSubset(MeasurementSubset ms) {
//        if (ms.isAllMeasurements()) {
//            if (ms.getIncludeOnly() != null && ms.getIncludeOnly().size() > 0) {
//                return "includeOnly should not be specified if all measurements have been requested";
//            }
//        } else {
//            if (ms.getIncludeOnly() == null || ms.getIncludeOnly().size() == 0) {
//                return "Specify at least one measurement to include";
//            }
//        }
//        return null;
//    }
}
