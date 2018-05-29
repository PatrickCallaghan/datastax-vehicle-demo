package com.datastax.vehicle.dao;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehicleReadingRow {

    private String vehicleId;
    private DateTime createdAt;
    private Double lat;
    private Double lng;
    private Map<String, Double> measurements = new HashMap<>();

    public VehicleReadingRow(String vehicleId, DateTime createdAt, Double lat, Double lng) {
        this.vehicleId = vehicleId;
        this.createdAt = createdAt;
        this.lat = lat;
        this.lng = lng;

    }

    public String getVehicleId() {
        return vehicleId;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public Map<String, Double> getMeasurements() {
        return measurements;
    }

    public void addMeasurement(String name, Double value) {
        measurements.put(name, value);
    }
}
