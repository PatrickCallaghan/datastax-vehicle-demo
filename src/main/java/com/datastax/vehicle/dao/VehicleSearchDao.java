package com.datastax.vehicle.dao;

import com.datastax.demo.codecs.JacksonNodeTypeCodec;
import com.datastax.driver.core.*;
import com.datastax.driver.dse.DseCluster;
import com.datastax.driver.dse.DseSession;
import com.datastax.driver.dse.geometry.Point;
import com.datastax.vehicle.dao.util.*;
import com.datastax.vehicle.webservice.resources.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;

import java.util.*;

public class VehicleSearchDao {

    private static int PORT = 9042;
    private static int DEFAULT_PAGE_SIZE = 1000;

    private DseSession session;

    public VehicleSearchDao(String[] contactPoints) {

        TypeCodec<JsonNode> jsonCodec = new JacksonNodeTypeCodec();

        DseCluster cluster = DseCluster.builder()
                .addContactPoints(contactPoints).withPort(PORT)
                .withQueryOptions(new QueryOptions().setFetchSize(DEFAULT_PAGE_SIZE))
                .withCodecRegistry(new CodecRegistry()
                        .register(jsonCodec))
                .build();

        session = cluster.connect();

    }

    public List<VehicleReadingRow> getCurrentReadingsPerArea(Area area, String filter,
                                                             boolean measurementsRequired, Integer pageSize) {

        String selectedColumnsSearchString = SearchFormatter.formatMeasurementsAsSearchString(measurementsRequired) ;

        StringBuilder cql = new StringBuilder("SELECT ").append(selectedColumnsSearchString)
                .append(" FROM datastax.vehicle_current_reading WHERE solr_query = '{\"q\": \"");

        cql.append(QClauseBuilder.generateQClause(area, null, filter));
        cql.append("\"").append(PaginationBuilder.generatePaginationClause()).append("}' ");

        System.out.println("getCurrentReadingsPerArea. Executing query: " + cql.toString());

        Statement stmt = PaginationBuilder.addPageSize(cql.toString(), pageSize);

        ResultSet resultSet = session.execute(stmt);

        List<Row> allRows = resultSet.all();
        List<VehicleReadingRow> readingRows = new ArrayList<>();
        for (Row row : allRows) {
            readingRows.add(buildVehicleReadingRow(row, measurementsRequired));
        }

        return readingRows;
    }

    public List<VehicleReadingRow> getHistoricalReadingsPerAreaAndTimeframe(Area area, Timeframe timeframe,
                                                                             String filter, Order order,
                                                                            boolean measurementsRequired,
                                                                            Integer pageSize) {

        String selectedColumnsSearchString = SearchFormatter.formatMeasurementsAsSearchString(measurementsRequired) ;

        StringBuilder cql = new StringBuilder("SELECT ").append(selectedColumnsSearchString)
                .append(" FROM datastax.vehicle_historical_readings WHERE solr_query = '{\"q\": \"");

        cql.append(QClauseBuilder.generateQClause(area, timeframe, filter)).append("\" ");

        if (order != null) {
            cql.append(SortClauseBuilder.generateSortClause(order));
        }

        cql.append(PaginationBuilder.generatePaginationClause()).append("}' ");

        System.out.println("getHistoricalReadingsPerAreaAndTimeframe. Executing query: " + cql.toString());

        Statement stmt = PaginationBuilder.addPageSize(cql.toString(), pageSize);

        ResultSet resultSet = session.execute(stmt);

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
                            .append(" FROM datastax.vehicle_historical_readings WHERE solr_query = '{\"q\": \"");

        cql.append(QClauseBuilder.generateQClause(vehicleId, area, timeframe, filter)).append("\" ");

        // order any results by time descending, as only the most recent one must be returned
        cql.append(SortClauseBuilder.generateSortClause(new Order())).append("}' LIMIT 1");

        System.out.println("getLatestVehicleReading. Executing query: " + cql.toString());
        Statement stmt = new SimpleStatement(cql.toString());
        ResultSet resultSet = session.execute(stmt);
        Row row = resultSet.one();
        return buildVehicleReadingRow(row, measurementsRequired);
    }

    public List<VehicleReadingRow> getHistoricalVehicleReadings(String vehicleId, Area area, Timeframe timeframe,
                                                                String filter, Order order,
                                                                boolean measurementsRequired, Integer pageSize) {

        String selectedColumnsSearchString = SearchFormatter.formatMeasurementsAsSearchString(measurementsRequired);
        StringBuilder cql = new StringBuilder("SELECT ").append(selectedColumnsSearchString)
                .append(" FROM datastax.vehicle_historical_readings WHERE solr_query = '{\"q\": \" ");

        cql.append(QClauseBuilder.generateQClause(vehicleId, area, timeframe, filter)).append("\" ");

        if (order != null) {
            cql.append(SortClauseBuilder.generateSortClause(order));
        }

        cql.append(PaginationBuilder.generatePaginationClause()).append("}' ");

        System.out.println("getHistoricalVehicleReadings. Executing query: " + cql.toString());

        Statement stmt = PaginationBuilder.addPageSize(cql.toString(), pageSize);

        ResultSet resultSet = session.execute(stmt);
        List<Row> allRows = resultSet.all();
        List<VehicleReadingRow> readingRows = new ArrayList<>();
        for (Row row : allRows) {
            readingRows.add(buildVehicleReadingRow(row, measurementsRequired));
        }

        return readingRows;
    }


    public AggregatedResultWrapper getLatestAggregatesByGeoHash(Integer geoHashLevel, Area area, String filter) {

        StringBuilder cql = new StringBuilder("SELECT * FROM datastax.vehicle_current_reading WHERE solr_query = '{\"q\": \"");

        cql.append(QClauseBuilder.generateQClause(area, null, filter)).append("\" ");
        cql.append(", ").append(FacetBuilder.buildFacetingClauseByGeoHashOnly(geoHashLevel)).append("}' ");

        System.out.println("getLatestAggregatesByGeoHash. Executing query: " + cql.toString());

        Statement stmt = new SimpleStatement(cql.toString());
        ResultSet resultSet = session.execute(stmt);

        Row row = resultSet.one();
        JsonNode geoHashNode = row.get("facet_fields", JsonNode.class).path("geohash");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Integer> geoHashResult = mapper.convertValue(geoHashNode, Map.class);

        return new AggregatedResultWrapper(geoHashResult, null);
    }


    public AggregatedResultWrapper getHistoricalAggregatesByVehicleAndGeoHash(Integer geoHashLevel, Area area, Timeframe timeframe, String filter) {

        StringBuilder cql = new StringBuilder("SELECT * FROM datastax.vehicle_historical_readings WHERE solr_query = '{\"q\": \"");

        cql.append(QClauseBuilder.generateQClause(area, timeframe, filter)).append("\" ");
        cql.append(", ").append(FacetBuilder.buildFacetingClauseByGeoHashAndVehicle(geoHashLevel)).append("}' ");

        System.out.println("getHistoricalAggregatesByVehicleAndGeoHash. Executing query: " + cql.toString());

        ResultSet resultSet = session.execute(cql.toString());
        Row row = resultSet.one();

        ObjectMapper mapper = new ObjectMapper();

        JsonNode geoHashNode = row.get("facet_fields", JsonNode.class).path("geohash");
        Map<String, Integer> geoHashResult = mapper.convertValue(geoHashNode, Map.class);

        JsonNode vehicleNode = row.get("facet_fields", JsonNode.class).path("vehicle_id");
        Map<String, Integer> vehicleResult = mapper.convertValue(vehicleNode, Map.class);

        return new AggregatedResultWrapper(geoHashResult, vehicleResult);
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
        Double lat = lat_long.Y();
        Double lng = lat_long.X();

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
