package com.datastax.vehicle.webservice.resources;

import java.util.*;

public class MeasurementSubset {

    private Set<String> includeOnly = new HashSet<>();

    public MeasurementSubset() {
    }

    public Set<String> getIncludeOnly() {
        return includeOnly;
    }

    public void setIncludeOnly(Set<String> includeOnly) {
        this.includeOnly = includeOnly;
    }


    public boolean retrieveAllMeasurements() {
        return includeOnly == null || includeOnly.isEmpty();
    }

    public void includeMeasurements(String... measurements) {
        for (String m : measurements) {
            includeOnly.add(m);
        }

    }

}
