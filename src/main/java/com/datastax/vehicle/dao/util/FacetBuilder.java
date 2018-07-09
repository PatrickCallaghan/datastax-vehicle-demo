package com.datastax.vehicle.dao.util;

public class FacetBuilder {

    public static String buildFacetingClauseByGeoHashOnly(Integer geoHashLevel) {
        //.append("\"facet\":{\"field\":\"geohash\",\"prefix\":").append(geoHashLevel)
        StringBuilder sb = new StringBuilder();
        sb.append("\"facet\":{\"field\":\"geohash\",\"prefix\":\"").append(geoHashLevel).append("\", \"mincount\":1, \"limit\":-1} ");
        return sb.toString();
    }

    public static String buildFacetingClauseByGeoHashAndVehicle(Integer geoHashLevel) {
        StringBuilder sb = new StringBuilder();
        sb.append("\"facet\":{\"field\":[\"geohash\",\"vehicle_id\"], \"f.geohash.prefix\":\"")
                .append(geoHashLevel).append("\", \"mincount\":1, \"limit\":-1} ");
        return sb.toString();
    }
}
