package com.datastax.vehicle.webservice.resources;

/**
 * Can only contain either a polygon or a circle.
 * Validation will be performed in the API.
 *
 */
public class Area {

    private Polygon polygon;
    private Circle circle;

    public Area() { }

    public Area(Polygon p) {
        polygon = p;
    }

    public Area(Circle c) {
        circle = c;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public boolean isPolygon() {
        return polygon != null;
    }


}
