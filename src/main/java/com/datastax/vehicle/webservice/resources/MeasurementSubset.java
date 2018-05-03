package com.datastax.vehicle.webservice.resources;

import java.util.*;

public class MeasurementSubset {

    private boolean allMeasurements;
    private Set<String> includeOnly = new HashSet<>();

    public MeasurementSubset() {
    }

    public boolean isAllMeasurements() {
        return allMeasurements;
    }

    public void setAllMeasurements(boolean allMeasurements) {
        this.allMeasurements = allMeasurements;
    }

    public Set<String> getIncludeOnly() {
        return includeOnly;
    }

    public void setIncludeOnly(Set<String> includeOnly) {
        this.includeOnly = includeOnly;
    }

    public void includeMeasurements(String... measurements) {
        for (String m : measurements) {
            includeOnly.add(m);
        }

    }

}
