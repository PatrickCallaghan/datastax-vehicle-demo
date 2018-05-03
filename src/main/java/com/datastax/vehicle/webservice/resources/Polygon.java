package com.datastax.vehicle.webservice.resources;

import java.util.ArrayList;
import java.util.List;

/**
 * If defined by two geoPoints only, these are the bottom left and top right corners of a rectangle
 * Otherwise it is an arbitrary polygon
 */
public class Polygon {

    private List<GeoPoint> geoPoints = new ArrayList<>();

    public Polygon() {

    }

    public List<GeoPoint> getGeoPoints() {
        return geoPoints;
    }

    public void setGeoPoints(List<GeoPoint> geoPoints) {
        this.geoPoints = geoPoints;
    }

    public void addGeoPoints(GeoPoint... pts) {
        for (GeoPoint p: pts) {
            geoPoints.add(p);
        }
    }

}
