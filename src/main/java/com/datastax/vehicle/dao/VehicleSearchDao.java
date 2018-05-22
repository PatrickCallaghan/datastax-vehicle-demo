package com.datastax.vehicle.dao;

import com.datastax.demo.utils.SearchFormatter;
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

        String areaSearchString = SearchFormatter.formatAreaAsSearchString(area);
        String selectedColumnsSearchString = SearchFormatter.formatMeasurementsAsSearchString(measurements) ;

        StringBuilder cql = new StringBuilder("SELECT ").append(selectedColumnsSearchString)
                .append(" FROM datastax.current_location WHERE solr_query = '{\"q\":\"").append(areaSearchString);

        if (filter != null && !filter.isEmpty()) {
            cql.append(" AND ").append(SearchFormatter.formatFilterAsSearchString(filter));
        }
        cql.append("\"}' LIMIT 100");

        System.out.println("getCurrentReadingsPerArea. Executing query: " + cql.toString());

        ResultSet resultSet = session.execute(cql.toString());

        List<Row> allRows = resultSet.all();
        List<VehicleReadingRow> readingRows = new ArrayList<>();
        for (Row row : allRows) {
            readingRows.add(buildVehicleReadingRow(row));
        }

        return readingRows;
    }

    //select * from datastax.vehicle where solr_query = '{"q": "speed:[150 TO 200]", "fq": "date:[2018-03-30T12:32:00.000Z TO 2018-03-30T14:45:00.000Z] AND lat_long:\"IsWithin(BUFFER(POINT(47.310179325765915 11.25321866241), .3))\""}' ;

    public List<VehicleReadingRow> getHistoricalReadingsPerAreaAndTimeframe(Area area, Timeframe timeframe,
                                                                             MeasurementSubset measurements,
                                                                             String filter, Order order) {

        String areaSearchString = SearchFormatter.formatAreaAsSearchString(area);
        String selectedColumnsSearchString = SearchFormatter.formatMeasurementsAsSearchString(measurements) ;
        String timeframeSearchString = SearchFormatter.formatTimeframeAsSearchString(timeframe);

        // Area and timeframe are mandatory here
        StringBuilder cql = new StringBuilder("SELECT ").append(selectedColumnsSearchString)
                .append(" FROM datastax.vehicle WHERE solr_query = '{\"q\": \"")
                .append(areaSearchString).append(" AND ").append(timeframeSearchString);

        if (filter != null && !filter.isEmpty()) {
            cql.append(" AND ").append(SearchFormatter.formatFilterAsSearchString(filter));
        }

        if (order != null) {
            cql.append("\"").append(SearchFormatter.formatOrderAsSearchString(order));
        }

        cql.append("}' LIMIT 100");

        System.out.println("getHistoricalReadingsPerAreaAndTimeframe. Executing query: " + cql.toString());

        ResultSet resultSet = session.execute(cql.toString());

        List<Row> allRows = resultSet.all();
        List<VehicleReadingRow> readingRows = new ArrayList<>();
        for (Row row : allRows) {
            readingRows.add(buildVehicleReadingRow(row));
        }

        return readingRows;
    }

    public VehicleReadingRow getLatestVehicleReading(String vehicleId, Area area, Timeframe timeframe,
                                                     MeasurementSubset measurements, String filter) {

        StringBuilder cql = new StringBuilder("SELECT ")
                            .append(SearchFormatter.formatMeasurementsAsSearchString(measurements))
                            .append(" FROM datastax.vehicle WHERE solr_query = '{\"q\": \" vehicle:").append(vehicleId);

        if (area != null) {
            cql.append(" AND ").append(SearchFormatter.formatAreaAsSearchString(area));
        }
        if (timeframe != null) {
            cql.append(" AND ").append(SearchFormatter.formatTimeframeAsSearchString(timeframe));
        }

        if (filter != null && !filter.isEmpty()) {
            cql.append(" AND ").append(SearchFormatter.formatFilterAsSearchString(filter));
        }
        cql.append("\" ");
        // order any results by time descending, as only the most recent one must be returned
        cql.append(SearchFormatter.formatOrderAsSearchString(new Order()));

        cql.append("}' LIMIT 1");

        System.out.println("getLatestVehicleReading. Executing query: " + cql.toString());

        ResultSet resultSet = session.execute(cql.toString());
        Row row = resultSet.one();
        return buildVehicleReadingRow(row);
    }

    public List<VehicleReadingRow> getHistoricalVehicleReadings(String vehicleId, Area area, Timeframe timeframe,
                                                     MeasurementSubset measurements, String filter, Order order) {

        StringBuilder cql = new StringBuilder("SELECT ")
                .append(SearchFormatter.formatMeasurementsAsSearchString(measurements))
                .append(" FROM datastax.vehicle WHERE solr_query = '{\"q\": \" vehicle:").append(vehicleId);

        if (area != null) {
            cql.append(" AND ").append(SearchFormatter.formatAreaAsSearchString(area));
        }
        if (timeframe != null) {
            cql.append(" AND ").append(SearchFormatter.formatTimeframeAsSearchString(timeframe));
        }

        if (filter != null && !filter.isEmpty()) {
            cql.append(" AND ").append(SearchFormatter.formatFilterAsSearchString(filter));
        }
        cql.append("\" ");

        if (order != null) {
            cql.append(SearchFormatter.formatOrderAsSearchString(new Order(false)));
        }
        cql.append("}' LIMIT 100");

        System.out.println("getHistoricalVehicleReadings. Executing query: " + cql.toString());

        ResultSet resultSet = session.execute(cql.toString());
        List<Row> allRows = resultSet.all();
        List<VehicleReadingRow> readingRows = new ArrayList<>();
        for (Row row : allRows) {
            readingRows.add(buildVehicleReadingRow(row));
        }

        return readingRows;
    }

    private VehicleReadingRow buildVehicleReadingRow(Row row) {
        if (row == null) {
            return null;
        }

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

        return readingRow;
    }

}
