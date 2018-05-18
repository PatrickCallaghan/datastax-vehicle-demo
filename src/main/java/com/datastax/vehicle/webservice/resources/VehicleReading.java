package com.datastax.vehicle.webservice.resources;

import java.util.HashMap;
import java.util.Map;

public class VehicleReading {

    private String vehicleId;
    private GeoPoint position;
    private String createdAt;
    private Map<String, String> measurements = new HashMap<>();

    public VehicleReading() {
    }
    public VehicleReading(String vehicleId, GeoPoint position, String createdAt) {
        this.vehicleId = vehicleId;
        this.position = position;
        this.createdAt = createdAt;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public GeoPoint getPosition() {
        return position;
    }

    public void setPosition(GeoPoint position) {
        this.position = position;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Map<String, String> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(Map<String, String> measurements) {
        this.measurements = measurements;
    }

    public void addMeasurement(String measName, String measValue) {
        measurements.put(measName, measValue);
    }
}
