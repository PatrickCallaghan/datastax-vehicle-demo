package com.datastax.vehicle.dao;

import com.datastax.demo.codecs.JacksonNodeTypeCodec;
import com.datastax.demo.utils.SearchFormatter;
import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.TypeCodec;
import com.datastax.driver.dse.DseCluster;
import com.datastax.driver.dse.DseSession;
import com.datastax.driver.dse.geometry.Point;
import com.datastax.vehicle.webservice.resources.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.joda.time.DateTime;

import java.util.*;

public class VehicleSearchDao {

    private static int PORT = 9042;

    private DseSession session;

    public VehicleSearchDao(String[] contactPoints) {

        TypeCodec<JsonNode> jsonCodec = new JacksonNodeTypeCodec();

        DseCluster cluster = DseCluster.builder()
                .addContactPoints(contactPoints).withPort(PORT)
                .withCodecRegistry(new CodecRegistry()
                        .register(jsonCodec))
                .build();

        session = cluster.connect();

    }

    public List<VehicleReadingRow> getCurrentReadingsPerArea(Area area, String filter, boolean measurementsRequired) {

        String areaSearchString = SearchFormatter.formatAreaAsSearchString(area);
        String selectedColumnsSearchString = SearchFormatter.formatMeasurementsAsSearchString(measurementsRequired) ;

        StringBuilder cql = new StringBuilder("SELECT ").append(selectedColumnsSearchString)
                .append(" FROM datastax.vehicle_current_reading WHERE solr_query = '{\"q\":\"").append(areaSearchString);

        if (filter != null && !filter.isEmpty()) {
            cql.append(" AND ").append(SearchFormatter.formatFilterAsSearchString(filter));
        }
        cql.append("\"}' LIMIT 100");

        System.out.println("getCurrentReadingsPerArea. Executing query: " + cql.toString());

        ResultSet resultSet = session.execute(cql.toString());

        List<Row> allRows = resultSet.all();
        List<VehicleReadingRow> readingRows = new ArrayList<>();
        for (Row row : allRows) {
            readingRows.add(buildVehicleReadingRow(row, measurementsRequired));
        }

        return readingRows;
    }

    public List<VehicleReadingRow> getHistoricalReadingsPerAreaAndTimeframe(Area area, Timeframe timeframe,
                                                                             String filter, Order order,
                                                                            boolean measurementsRequired) {

        String areaSearchString = SearchFormatter.formatAreaAsSearchString(area);
        String selectedColumnsSearchString = SearchFormatter.formatMeasurementsAsSearchString(measurementsRequired) ;
        String timeframeSearchString = SearchFormatter.formatTimeframeAsSearchString(timeframe);

        // Area and timeframe are mandatory here
        StringBuilder cql = new StringBuilder("SELECT ").append(selectedColumnsSearchString)
                .append(" FROM datastax.vehicle_historical_readings WHERE solr_query = '{\"q\": \"")
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
            readingRows.add(buildVehicleReadingRow(row, measurementsRequired));
        }

        return readingRows;
    }

    public VehicleReadingRow getLatestVehicleReading(String vehicleId, Area area, Timeframe timeframe,
                                                        String filter, boolean measurementsRequired) {

        String selectedColumnsSearchString = SearchFormatter.formatMeasurementsAsSearchString(measurementsRequired);
        StringBuilder cql = new StringBuilder("SELECT ")
                            .append(selectedColumnsSearchString)
                            .append(" FROM datastax.vehicle_historical_readings WHERE solr_query = '{\"q\": \" vehicle_id:").append(vehicleId);

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
        return buildVehicleReadingRow(row, measurementsRequired);
    }

    public List<VehicleReadingRow> getHistoricalVehicleReadings(String vehicleId, Area area, Timeframe timeframe,
                                                     String filter, Order order, boolean measurementsRequired) {

        String selectedColumnsSearchString = SearchFormatter.formatMeasurementsAsSearchString(measurementsRequired);
        StringBuilder cql = new StringBuilder("SELECT ").append(selectedColumnsSearchString)
                .append(" FROM datastax.vehicle_historical_readings WHERE solr_query = '{\"q\": \" vehicle_id:").append(vehicleId);

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
            readingRows.add(buildVehicleReadingRow(row, measurementsRequired));
        }

        return readingRows;
    }


    public Map<String,Integer> retrieveCurrentVehicleCountByGeoTile(Integer geoHashLevel, Area area, String filter) {

        StringBuilder cql = new StringBuilder("SELECT * FROM datastax.vehicle_current_reading WHERE solr_query = '{\"q\": \"");

        boolean optionalConstraintSpecified = false;

        if (area != null) {
            cql.append(SearchFormatter.formatAreaAsSearchString(area));
            optionalConstraintSpecified = true;
        }

        if (filter != null) {
            if (optionalConstraintSpecified) {
                cql.append(" AND ");
            }
            cql.append(SearchFormatter.formatFilterAsSearchString(filter));
            optionalConstraintSpecified = true;
        }

        if (!optionalConstraintSpecified) {
            cql.append("*:*");
        }
        cql.append("\", ");

        cql.append("\"facet\":{\"field\":\"geohash\",\"prefix\":").append(geoHashLevel).append("} }'");

        System.out.println("retrieveCurrentVehicleCountByGeoTile. Executing query: " + cql.toString());

        ResultSet resultSet = session.execute(cql.toString());
        //String facetMapJSON = resultSet.all().get(0).getString(0);
        Row row = resultSet.one();
        JsonNode geoHashNode = row.get("facet_fields", JsonNode.class).path("geohash");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Integer> result = mapper.convertValue(geoHashNode, Map.class);

        return result;
    }

    private VehicleReadingRow buildVehicleReadingRow(Row row, boolean measurementsRequired) {
        if (row == null) {
            System.out.println("Null row, returning...");
            return null;
        }

        // this is always going to be there
        String vehicleId = row.getString("vehicle_id");
        DateTime readingTimestamp = new DateTime(row.getTimestamp("date"));
        Point lat_long = (Point) row.getObject("lat_long");
        Double lat = lat_long.X();
        Double lng = lat_long.Y();

        VehicleReadingRow readingRow = new VehicleReadingRow(vehicleId, readingTimestamp, lat, lng);

        // if select * then read all measurements - the named ones + the map
        if (measurementsRequired) {
            Double speedValue = row.getDouble("speed");
            Double tempValue = row.getDouble("temperature");
            Map<String, Double> properties = row.getMap("p_", String.class, Double.class);

            readingRow.addMeasurement("speed", speedValue);
            readingRow.addMeasurement("temperature", tempValue);
            for (String propName : properties.keySet()) {
                readingRow.addMeasurement(propName, properties.get(propName));
            }
        }

        return readingRow;
    }

}
