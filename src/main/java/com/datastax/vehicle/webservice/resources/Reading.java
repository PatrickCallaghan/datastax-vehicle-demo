package com.datastax.vehicle.webservice.resources;

import java.util.HashMap;
import java.util.Map;

public class Reading {

    private GeoPoint position;
    private String createdAt;
    private Map<String, String> measurements = new HashMap<>();

    public Reading() {
    }
    public Reading(GeoPoint position, String createdAt) {
        this.position = position;
        this.createdAt = createdAt;
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
