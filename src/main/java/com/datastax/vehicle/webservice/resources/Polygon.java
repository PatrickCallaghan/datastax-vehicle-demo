package com.datastax.vehicle.webservice.resources;

import java.util.ArrayList;
import java.util.List;

/**
 * If defined by two points only, these are the bottom left and top right corners of a rectangle
 * Otherwise it is an arbitrary polygon
 */
public class Polygon {

    private List<Point> points = new ArrayList<>();

    public Polygon() {

    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public void addPoints(Point... pts) {
        for (Point p: pts) {
            points.add(p);
        }
    }
}
