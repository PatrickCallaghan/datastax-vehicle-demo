package com.datastax.vehicle.dao.util;

import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.Statement;

public class PaginationBuilder {

    public static String generatePaginationClause() {
        return ",\"paging\":\"driver\"";
    }

    public static Statement addPageSize(String cql, Integer pageSize) {
        Statement stmt = new SimpleStatement(cql);
        if (pageSize != null) {
            return stmt.setFetchSize(pageSize);
        }
        return stmt;
    }

}
