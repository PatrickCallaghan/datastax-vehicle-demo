package com.datastax.vehicle.webservice.resources;

import java.util.ArrayList;
import java.util.List;

public class VehicleData {

    String vehicleId;
    List<Reading> readings = new ArrayList<>();

    public VehicleData() {
    }

    public VehicleData(String vehicleId, List<Reading> readings) {
        this.vehicleId = vehicleId;
        this.readings = readings;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public List<Reading> getReadings() {
        return readings;
    }

    public void setReadings(List<Reading> readings) {
        this.readings = readings;
    }
}
