package com.datastax.vehicle.service;

import com.datastax.vehicle.dao.VehicleReadingRow;
import com.datastax.vehicle.webservice.resources.GeoPoint;
import com.datastax.vehicle.webservice.resources.Reading;
import com.datastax.vehicle.webservice.resources.VehicleData;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Map;

public class ResourceAssembler {

    private static DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss");

    public static VehicleData assembleVehicleDataWithSingleReading(VehicleReadingRow readingRow) {

        GeoPoint position = new GeoPoint(readingRow.getLat(), readingRow.getLng());
        String createdAt = dtf.print(readingRow.getCreatedAt());

        Reading reading = new Reading(position, createdAt);

        Map<String, Double> measurements = readingRow.getMeasurements();
        for (String measName: measurements.keySet()) {
            reading.addMeasurement(measName, String.valueOf(measurements.get(measName)));
        }

        return new VehicleData(readingRow.getVehicleId(), reading);
    }
}
