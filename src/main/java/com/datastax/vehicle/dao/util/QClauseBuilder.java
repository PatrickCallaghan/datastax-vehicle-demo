package com.datastax.vehicle.dao.util;

import com.datastax.vehicle.webservice.resources.Area;
import com.datastax.vehicle.webservice.resources.Timeframe;

public class QClauseBuilder {

    public static String generateQClause(String vehicleId, Area area, Timeframe timeframe, String filter) {
        StringBuilder cql = new StringBuilder();

        boolean vehicleIdExists = (vehicleId != null);
        boolean furtherConstraintsExist = (area != null || timeframe != null || filter != null);

        if ( !vehicleIdExists && !furtherConstraintsExist) {
            return "*:*";
        }

        if (vehicleIdExists) {
            cql.append(generateQClauseForVehicle(vehicleId));
            if (furtherConstraintsExist) {
                cql.append(" AND ");
            } else {
                return cql.toString();
            }
        }

        if (furtherConstraintsExist) {
            cql.append(generateQClauseForFurtherConstraints(area, timeframe, filter));
        }
        return cql.toString();
    }

    public static String generateQClause(Area area, Timeframe timeframe, String filter) {

        boolean furtherConstraintsExist = (area != null || timeframe != null || filter != null);
        if (!furtherConstraintsExist) {
            return "*:*";
        }
        return generateQClauseForFurtherConstraints(area, timeframe, filter);
    }

    private static String generateQClauseForVehicle(String vehicleId) {
        StringBuilder cql = new StringBuilder();
        cql.append("vehicle_id:").append(vehicleId);
        return cql.toString();
    }

    private static String generateQClauseForFurtherConstraints(Area area, Timeframe timeframe, String filter) {

        StringBuilder cql = new StringBuilder();

        boolean constraintAdded = false;
        if (area != null) {
            cql.append(SearchFormatter.formatAreaAsSearchString(area));
            constraintAdded = true;
        }

        if (timeframe != null) {
            if (constraintAdded) {
                cql.append(" AND ");
            }
            cql.append(SearchFormatter.formatTimeframeAsSearchString(timeframe));
            constraintAdded = true;
        }

        if (filter != null && !filter.isEmpty()) {
            if (constraintAdded) {
                cql.append(" AND ");
            }
            cql.append(SearchFormatter.formatFilterAsSearchString(filter));
        }

        return cql.toString();
    }

}
