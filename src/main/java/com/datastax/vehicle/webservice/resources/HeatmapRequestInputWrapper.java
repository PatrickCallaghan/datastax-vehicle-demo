package com.datastax.vehicle.webservice.resources;

public class HeatmapRequestInputWrapper {

    // is it possible to pass a bounding box and find all the geohashes within it?
    // if you wanted to limit the heatmap to an area of the map (e.g. a bounding box around southern Germany if you have
    // a map covering the whole of Europe)

    private Integer geoHashLevel;
    private Area area;
    private Timeframe timeframe;
    private String filter;

    public HeatmapRequestInputWrapper() {}

    public HeatmapRequestInputWrapper(Integer geoHashLevel) {
        this.geoHashLevel = geoHashLevel;
    }

    public Integer getGeoHashLevel() {
        return geoHashLevel;
    }

    public void setGeoHashLevel(Integer geoHashLevel) {
        this.geoHashLevel = geoHashLevel;
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

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
