package com.datastax.vehicle.dao.util;

import com.datastax.vehicle.webservice.resources.Order;

public class SortClauseBuilder {

    public static String generateSortClause(Order order) {
        StringBuilder sb = new StringBuilder(", \"sort\":\"");
        if (order != null) {
            if (order.isAscending()) {
                sb.append("date asc");
            } else {
                sb.append("date desc");
            }
        }
        sb.append("\" ");
        return sb.toString();
    }
}
