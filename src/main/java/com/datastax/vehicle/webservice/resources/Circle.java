package com.datastax.vehicle.webservice.resources;

public class Circle {

    private GeoPoint centre;
    private Double radius;

    public Circle() { }

    public GeoPoint getCentre() {
        return centre;
    }

    public void setCentre(GeoPoint centre) {
        this.centre = centre;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

}
