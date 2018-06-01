package com.datastax.vehicle.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.datastax.vehicle.dao.VehicleReadingRow;
import com.datastax.vehicle.dao.VehicleSearchDao;
import com.datastax.vehicle.webservice.resources.*;

import com.datastax.demo.utils.PropertyHelper;
import com.datastax.vehicle.dao.VehicleDao;

public class VehicleService {

	public static VehicleDao dao;
	public static VehicleSearchDao searchDao;
	
	public VehicleService(){
		String contactPointsStr = PropertyHelper.getProperty("contactPoints", "localhost");
		if (dao == null){
			dao = new VehicleDao(contactPointsStr.split(","));		
		}
		if (searchDao == null) {
			searchDao = new VehicleSearchDao(contactPointsStr.split(","));
		}
	}

//	public List<Vehicle> getVehicleMovements(String vehicle, String dateString) {
//
//		return dao.getVehicleMovements(vehicle, dateString);
//	}
//
//	public List<Vehicle> getVehiclesByTile(String tile){
//
//		return dao.getVehiclesByTile(tile);
//
//	}
//
//	public List<Vehicle> searchVehiclesByLonLatAndDistance(int distance, LatLong latLong){
//		return dao.searchVehiclesByLonLatAndDistance(distance, latLong);
//	}
//
//	public List<Vehicle> searchAreaTimeLastPosition(DateTime from, DateTime to){
//
//		return dao.getVehiclesByAreaTimeLastPosition(from, to);
//	}

	/** New methods **/

	public List<VehicleReading> retrieveLatestReadingOfVehicles(Area area, Timeframe timeframe, String filter,
																boolean measurementsRequired) {
		List<VehicleReading> result = new ArrayList<>();

		// if no timeframe was specified, the request is for current data
		if (timeframe == null) {
			List<VehicleReadingRow> readingRows= searchDao.getCurrentReadingsPerArea(area, filter, measurementsRequired);
			for (VehicleReadingRow vrr : readingRows) {
				result.add(ResourceAssembler.assembleVehicleReading(vrr));
			}
		} else {
			//todo - retrieve latest in timeframe. this will read from the historical table
			// this requires an http query as we have to group and facet etc etc
			//TODO implement this case (also with optional filtering)
		}
		return result;
	}

	public List<VehicleReading> retrieveHistoricalReadingsOfVehicles(Area area, Timeframe timeframe, String filter,
																	 Order order, boolean measurementsRequired) {

		List<VehicleReading> result = new ArrayList<>();
		List<VehicleReadingRow> readingRows= searchDao.getHistoricalReadingsPerAreaAndTimeframe(area, timeframe, filter,
																							order, measurementsRequired);
		for (VehicleReadingRow vrr : readingRows) {
			result.add(ResourceAssembler.assembleVehicleReading(vrr));
		}

		return result;
	}

	public VehicleReading retrieveLatestReadingOfSingleVehicle(String vehicleId, Area area, Timeframe timeframe,
															   String filter, boolean measurementsRequired) {
		VehicleReadingRow readingRow = searchDao.getLatestVehicleReading(vehicleId, area, timeframe,
																			filter, measurementsRequired);
		return ResourceAssembler.assembleVehicleReading(readingRow);
	}

	public List<VehicleReading> retrieveHistoricalReadingsOfSingleVehicle(String vehicleId, Area area, Timeframe timeframe,
														String filter, Order order, boolean measurementsRequired) {
		List<VehicleReadingRow> readingRows = searchDao.getHistoricalVehicleReadings(vehicleId, area, timeframe, filter,
				order, measurementsRequired);
		List<VehicleReading> result = new ArrayList<>();
		for (VehicleReadingRow vrr : readingRows) {
			result.add(ResourceAssembler.assembleVehicleReading(vrr));
		}
		return result;
	}

	public AggregatedResultWrapper retrieveLatestAggregatesByGeoHash(Integer geoHashLevel, Area area, String filter) {
		return searchDao.getLatestAggregatesByGeoHash(geoHashLevel, area, filter);
	}

	public AggregatedResultWrapper retrieveHistoricalAggregatesByVehicleAndGeoHash(Integer geoHashLevel, Area area,
																				   Timeframe timeframe, String filter) {

		// SELECT * FROM datastax.vehicle_historical_readings
		// //WHERE solr_query = '{"q": " lat_long:\"IsWithin(POLYGON((48.736989 10.271339, 48.067576 11.60903, 48.774243 12.91312,
		// 49.595759 11.123788, 48.736989 10.271339)) )\"  AND  date:[2018-05-10T01:10:055Z TO 2018-05-25T23:14:012Z] " ,
		// "facet":{ "field":["geohash","vehicle_id"], "f.geohash.prefix":5 } }';

		return searchDao.getHistoricalAggregatesByVehicleAndGeoHash(geoHashLevel, area, timeframe, filter) ;
	}
}
