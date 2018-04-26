package com.datastax.vehicle.webservice.resources;

public class Order {

    private boolean ascending;

    public Order() {
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }
}
