package com.datastax.vehicle;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.demo.SessionLimiter;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Row;
import com.datastax.driver.dse.DseCluster;
import com.datastax.driver.dse.DseSession;
import com.datastax.driver.dse.geometry.Point;
import com.datastax.vehicle.model.Vehicle;
import com.github.davidmoten.geo.LatLong;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

public class VehicleDao {

	private static Logger logger = LoggerFactory.getLogger(VehicleDao.class);

	private DseSession session;
	private static String keyspaceName = "datastax";
	private static String vehicleTable = keyspaceName + ".vehicle";
	private static String currentLocationTable = keyspaceName + ".current_location";
	private static String vehicleStatusTable = keyspaceName + ".vehicle_status";

	private static final String INSERT_INTO_VEHICLE = "Insert into " + vehicleTable
			+ " (vehicle, day, date, lat_long, tile, speed, temperature, p_) values (?,?,?,?,?,?,?,?);";
	private static final String INSERT_INTO_CURRENTLOCATION = "Insert into " + currentLocationTable
			+ "(vehicle, tile1, tile2, lat_long, date, speed, temperature, p_) values (?,?,?,?,?,?,?,?)";
	private static final String INSERT_INTO_VEHICLESTATUS = "Insert into " + vehicleStatusTable
			+ "(vehicle, day, state_change_time, vehicle_state) values (?,?,?,?)";

	private static final String QUERY_BY_VEHICLE = "select * from " + vehicleTable + " where vehicle = ? and day = ?";
	private static final String QUERY_BY_VEHICLE_DATE = "select * from " + vehicleTable
			+ " where vehicle = ? and day = ? and date < ? limit 1";

	private static final String SOLR_QUERY_CURRENT_LOCATION = "select * from " + currentLocationTable
			+ " where solr_query = ?  limit 1000";

	private static final String SOLR_QUERY_VEHICLE = "SELECT * FROM " + vehicleTable + " where solr_query = ?";

	private PreparedStatement insertVehicle;
	private PreparedStatement insertCurrentLocation;
	private PreparedStatement insertVehicleState;
	private PreparedStatement queryVehicle;
	private PreparedStatement queryVehicleDate;
	private PreparedStatement queryCurrentLocation;
	private PreparedStatement queryVehicleSolr;

	private DateFormat solrDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'");
	private DateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");

	private SessionLimiter limiter;

	public VehicleDao(String[] contactPoints) {

		DseCluster cluster = DseCluster.builder().addContactPoints(contactPoints).build();

		this.session = cluster.connect();
		this.limiter = new SessionLimiter(session);

		this.insertVehicle = session.prepare(INSERT_INTO_VEHICLE);
		this.insertCurrentLocation = session.prepare(INSERT_INTO_CURRENTLOCATION);
		this.insertVehicleState = session.prepare(INSERT_INTO_VEHICLESTATUS);

		this.queryVehicle = session.prepare(QUERY_BY_VEHICLE);
		this.queryVehicleDate = session.prepare(QUERY_BY_VEHICLE_DATE);
		this.queryCurrentLocation = session.prepare(SOLR_QUERY_CURRENT_LOCATION);
		this.queryVehicleSolr = session.prepare(SOLR_QUERY_VEHICLE);
	}

	public void insertVehicleData(Vehicle vehicle) {
		try {
			limiter.executeAsync(insertVehicle.bind(vehicle.getVehicle(), dateFormatter.format(vehicle.getDate()),
					vehicle.getDate(), new Point(vehicle.getLatLong().getLat(), vehicle.getLatLong().getLon()),
					vehicle.getTile2(), vehicle.getSpeed(), vehicle.getTemperature(), vehicle.getProperties()));

			limiter.executeAsync(insertCurrentLocation.bind(vehicle.getVehicle(), vehicle.getTile(), vehicle.getTile2(),
					new Point(vehicle.getLatLong().getLat(), vehicle.getLatLong().getLon()), vehicle.getDate(),
					vehicle.getSpeed(), vehicle.getTemperature(), vehicle.getProperties()));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void insertVehicleStatus(String vehicleId, DateTime statusDate, String status) {
		try {
			limiter.executeAsync(insertVehicleState.bind(vehicleId, dateFormatter.format(statusDate.toDate()),
					statusDate.toDate(), status));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public List<Vehicle> getVehicleMovements(String vehicleId, String dateString) {
		ResultSet resultSet = session.execute(this.queryVehicle.bind(vehicleId, dateString));
		List<Vehicle> vehicleMovements = new ArrayList<Vehicle>();

		for (Row row : resultSet) {
			Date date = row.getTimestamp("date");
			Point lat_long = (Point) row.getObject("lat_long");
			String tile = row.getString("tile");
			Double temperature = row.getDouble("temperature");
			Double speed = row.getDouble("speed");

			Double lat = lat_long.X();
			Double lng = lat_long.Y();

			Vehicle vehicle = new Vehicle(vehicleId, date, new LatLong(lat, lng), tile, "", temperature, speed);
			vehicleMovements.add(vehicle);
		}

		return vehicleMovements;
	}

	public List<Vehicle> searchVehiclesByLonLatAndDistance(int distance, LatLong latLong) {
		String solr_query = "{\"q\": \"*:*\", \"fq\": \"{!geofilt sfield=lat_long pt=\"" + latLong.getLat() + " "
				+ latLong.getLon() + "\" d=" + distance + "}\"}";
		ResultSet resultSet = session.execute(queryCurrentLocation.bind(solr_query));

		List<Vehicle> vehicleMovements = new ArrayList<Vehicle>();

		for (Row row : resultSet) {
			Date date = row.getTimestamp("date");
			String vehicleId = row.getString("vehicle");
			Point lat_long = (Point) row.getObject("lat_long");
			String tile = row.getString("tile");
			Double temperature = row.getDouble("temperature");
			Double speed = row.getDouble("speed");

			Double lat = lat_long.X();
			Double lng = lat_long.Y();

			Vehicle vehicle = new Vehicle(vehicleId, date, new LatLong(lat, lng), tile, "", temperature, speed);
			vehicleMovements.add(vehicle);
		}

		return vehicleMovements;
	}

	public List<Vehicle> getVehiclesByTile(String tile) {
		String solr_query = "{\"q\": \"tile1: " + tile + "\"}";
		ResultSet resultSet = session.execute(queryCurrentLocation.bind(solr_query));

		List<Vehicle> vehicleMovements = new ArrayList<Vehicle>();

		for (Row row : resultSet) {
			Date date = row.getTimestamp("date");
			String vehicleId = row.getString("vehicle");
			Point lat_long = (Point) row.getObject("lat_long");
			String tile1 = row.getString("tile");
			String tile2 = row.getString("tile2");
			Double temperature = row.getDouble("temperature");
			Double speed = row.getDouble("speed");

			Double lat = lat_long.X();
			Double lng = lat_long.Y();

			Vehicle vehicle = new Vehicle(vehicleId, date, new LatLong(lat, lng), tile1, tile2, temperature, speed);
			vehicleMovements.add(vehicle);
		}

		return vehicleMovements;
	}

	public List<Vehicle> getVehiclesByAreaTimeLastPosition(DateTime from, DateTime to) {

		String solr_query = "'{\"q\":\"*:*\"," + "\"fq\":\"date:[" + solrDateFormatter.format(from.toDate()) + " TO "
				+ solrDateFormatter.format(to.toDate()) + "] "
				+ "AND lat_long:\\\"isWithin(POLYGON((48.736989 10.271339, 48.067576 11.609030, 48.774243 12.913120, 49.595759 11.123788, 48.736989 10.271339)))\\\"\",\"facet\":{\"field\":\"vehicle\", \"limit\":\"5000000\"}}";

		ResultSet resultSet = session.execute(queryVehicleSolr.bind(solr_query));

		String result = resultSet.one().getString(0);
		ObjectMapper mapper = new ObjectMapper();

		List<String> vehicles = new ArrayList<String>();

		try {
			Map<String, Object> map = mapper.readValue(result, new TypeReference<Map<String, Object>>() {
			});

			Map<String, Integer> facets = (Map<String, Integer>) map.get("vehicle");

			for (Map.Entry<String, Integer> entry : facets.entrySet()) {

				if (entry.getValue() > 0) {
					vehicles.add(entry.getKey());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<ResultSetFuture> futures = new ArrayList<ResultSetFuture>();

		for (String vehicle : vehicles) {
			Date date = to.toDate();
			BoundStatement boundStatement = this.queryVehicleDate.bind(vehicle, dateFormatter.format(date), date);
			futures.add(session.executeAsync(boundStatement));
		}

		List<Vehicle> vehicleMovements = new ArrayList<Vehicle>();

		ImmutableList<ListenableFuture<ResultSet>> inCompletionOrder = Futures.inCompletionOrder(futures);

		for (ListenableFuture<ResultSet> future : futures) {
			ResultSet rs = null;
			try {
				rs = future.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			if (rs == null) {
				continue;
			}

			for (Row row : rs) {
				Date date = row.getTimestamp("date");
				String vehicleId = row.getString("vehicle");
				Point lat_long = (Point) row.getObject("lat_long");
				String tile = row.getString("tile");

				Double temperature = row.getDouble("temperature");
				Double speed = row.getDouble("speed");
				Map<String, Double> map = row.getMap("p_", String.class, Double.class);

				Double lat = lat_long.X();
				Double lng = lat_long.Y();

				Vehicle vehicle = new Vehicle(vehicleId, date, new LatLong(lat, lng), tile, null, temperature, speed);
				vehicle.setProperties(map);
				vehicleMovements.add(vehicle);
			}
		}

		return vehicleMovements;
	}
}
