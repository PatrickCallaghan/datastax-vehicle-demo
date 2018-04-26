package com.datastax.vehicle.webservice.resources;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Reading {

    private Point position;
    private String createdAt;
    private Map<String, String> measurements = new HashMap<>();

    public Reading() {
    }

    public Reading(Point position, String createdAt, Map<String, String> measurements) {
        this.position = position;
        this.createdAt = createdAt;
        this.measurements = measurements;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
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
}
