package com.datastax.vehicle.webservice.resources;

import java.util.HashMap;
import java.util.Map;

public class AggregatedResultWrapper {

    private Map<String, Integer> facetPerGeoHashTile = new HashMap<>();
    private Map<String, Integer> facetPerVehicle = new HashMap<>();

    public AggregatedResultWrapper() {
    }

    public AggregatedResultWrapper(Map<String, Integer> facetPerGeoHashTile, Map<String, Integer> facetPerVehicle) {
        this.facetPerGeoHashTile = facetPerGeoHashTile;
        this.facetPerVehicle = facetPerVehicle;
    }

    public Map<String, Integer> getFacetPerGeoHashTile() {
        return facetPerGeoHashTile;
    }

    public void setFacetPerGeoHashTile(Map<String, Integer> facetPerGeoHashTile) {
        this.facetPerGeoHashTile = facetPerGeoHashTile;
    }

    public Map<String, Integer> getFacetPerVehicle() {
        return facetPerVehicle;
    }

    public void setFacetPerVehicle(Map<String, Integer> facetPerVehicle) {
        this.facetPerVehicle = facetPerVehicle;
    }
}
