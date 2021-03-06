package com.datastax.vehicle.webservice.resources;

public class GlobalRequestInputWrapper {

    private Area area;
    private Timeframe timeframe;
    private Order order;
    private String filter;
    private boolean measurementsRequired;
    private Integer pageSize;

    public GlobalRequestInputWrapper() {
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

    public boolean isMeasurementsRequired() {
        return measurementsRequired;
    }

    public void setMeasurementsRequired(boolean retrieveMeasurements) {
        this.measurementsRequired = retrieveMeasurements;
    }

    public Integer getPageSize() { return pageSize; }

    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
