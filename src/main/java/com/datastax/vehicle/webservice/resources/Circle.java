package com.datastax.vehicle.webservice.resources;

public class Circle {

    private Point centre;
    private Double radius;

    public Circle() { }

    public Point getCentre() {
        return centre;
    }

    public void setCentre(Point centre) {
        this.centre = centre;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }
}
