package com.datastax.vehicle.dao;

import com.datastax.demo.utils.SolrFormatter;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.dse.DseCluster;
import com.datastax.driver.dse.DseSession;
import com.datastax.driver.dse.geometry.Point;
import com.datastax.vehicle.webservice.resources.*;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class VehicleSearchDao {

    private DseSession session;

    public VehicleSearchDao(String[] contactPoints) {

        DseCluster cluster = DseCluster.builder().addContactPoints(contactPoints).build();
        session = cluster.connect();

    }

    public List<VehicleReadingRow> getCurrentReadingsPerArea(Area area, MeasurementSubset measurements, String filter) {

        String areaString = SolrFormatter.formatAreaAsSolrString(area);
        String selectedColumns = SolrFormatter.formatMeasurementsAsSolrString(measurements) ;
        String filterString = filter != null ? SolrFormatter.formatFilterAsSolrString(filter) : "";

        StringBuilder cql = new StringBuilder("SELECT ").append(selectedColumns)
                .append(" FROM datastax.current_location WHERE solr_query = '{\"q\":\"lat_long:\\\"isWithin( ")
                .append(areaString)
                .append(" )\\\"").append(filterString).append("\"}' LIMIT 3");

        ResultSet resultSet = session.execute(cql.toString());

        List<Row> allRows = resultSet.all();
        List<VehicleReadingRow> results = new ArrayList<>();
        // each row contains the vehicleId and one single reading (the current / latest)
        for (Row row : allRows) {
            String vehicleId = row.getString("vehicle");
            DateTime readingTimestamp = new DateTime(row.getTimestamp("date"));
            Double tempValue = row.getDouble("temperature");
            Double speedValue = row.getDouble("speed");

            Point lat_long = (Point) row.getObject("lat_long");
            Double lat = lat_long.X();
            Double lng = lat_long.Y();

            VehicleReadingRow readingRow = new VehicleReadingRow(vehicleId, readingTimestamp, lat, lng);
            readingRow.addMeasurement("speed", speedValue);
            readingRow.addMeasurement("temperature", tempValue);
            results.add(readingRow);
        }

        return results;
    }


}
