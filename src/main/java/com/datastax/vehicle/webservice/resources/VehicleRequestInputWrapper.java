package com.datastax.vehicle.webservice.resources;

public class VehicleRequestInputWrapper {

    private String vehicleId;
    private Area area;
    private Timeframe timeframe;
    private MeasurementSubset measurementSubset;
    private Order order;
    private String filter;

    public VehicleRequestInputWrapper() {
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Timeframe getTimeframe() {
        return timeframe;
    }

    public void setTimeframe(Timeframe timeframe) {
        this.timeframe = timeframe;
    }

    public MeasurementSubset getMeasurementSubset() {
        return measurementSubset;
    }

    public void setMeasurementSubset(MeasurementSubset measurementSubset) {
        this.measurementSubset = measurementSubset;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
