package com.datastax.vehicle.webservice.resources;

import java.util.ArrayList;
import java.util.List;

/*
    Probably obsolete but leaving it here for now
 */

public class VehicleData {

    String vehicleId;
    List<VehicleReading> readings = new ArrayList<>();

    public VehicleData() {
    }

    public VehicleData(String vehicleId, List<VehicleReading> readings) {
        this.vehicleId = vehicleId;
        this.readings = readings;
    }

    public VehicleData(String vehicleId, VehicleReading reading) {
        this.vehicleId = vehicleId;
        addReading(reading);
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public List<VehicleReading> getReadings() {
        return readings;
    }

    public void setReadings(List<VehicleReading> readings) {
        this.readings = readings;
    }

    public void addReading(VehicleReading reading) {
        readings.add(reading);
    }
}
