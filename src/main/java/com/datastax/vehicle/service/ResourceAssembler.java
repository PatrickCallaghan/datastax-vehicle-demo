package com.datastax.vehicle.service;

import com.datastax.vehicle.dao.VehicleReadingRow;
import com.datastax.vehicle.webservice.resources.GeoPoint;
import com.datastax.vehicle.webservice.resources.VehicleReading;
import com.datastax.vehicle.webservice.resources.VehicleData;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Map;

public class ResourceAssembler {

    private static DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss");

    public static VehicleReading assembleVehicleReading(VehicleReadingRow readingRow) {

        if (readingRow == null) {
            return null;
        }

        GeoPoint position = new GeoPoint(readingRow.getLat(), readingRow.getLng());
        String createdAt = dtf.print(readingRow.getCreatedAt());

        VehicleReading reading = new VehicleReading(readingRow.getVehicleId(), position, createdAt);

        Map<String, Double> measurements = readingRow.getMeasurements();
        for (String measName: measurements.keySet()) {
            reading.addMeasurement(measName, String.valueOf(measurements.get(measName)));
        }

        return reading;
    }

}
