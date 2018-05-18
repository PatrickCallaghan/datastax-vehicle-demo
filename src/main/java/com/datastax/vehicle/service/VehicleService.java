package com.datastax.vehicle.service;

import java.util.ArrayList;
import java.util.List;

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

	/**
	 * Retrieves latest reading of all vehicles in an area.
	 *
	 * If there is no timeframe, then the request will return the "current" position,
	 * which actually is the latest registered position of any vehicle in the area up to now
	 *
	 * If a timeframe is specified, then the request will return the latest position for each vehicle that was
	 * in that area in that timeframe.
	 *
	 * @param area
	 * @param timeframe
	 * @param measurements
	 * @param filter
	 * @return
	 */
	public List<VehicleReading> retrieveLatestReadingOfVehicles(Area area, Timeframe timeframe,
																MeasurementSubset measurements, String filter) {
		List<VehicleReading> result = new ArrayList<>();

		// if no timeframe was specified, the request is for current data
		if (timeframe == null) {
			List<VehicleReadingRow> readingRows= searchDao.getCurrentReadingsPerArea(area, measurements, filter);
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

	public List<VehicleReading> retrieveHistoricalReadingsOfVehicles(Area area, Timeframe timeframe,
																	 MeasurementSubset measurements, String filter, Order order) {

		List<VehicleReading> result = new ArrayList<>();
		List<VehicleReadingRow> readingRows= searchDao.getHistoricalReadingsPerAreaAndTimeframe(area, timeframe, measurements, filter, order);
		for (VehicleReadingRow vrr : readingRows) {
			result.add(ResourceAssembler.assembleVehicleReading(vrr));
		}

		return result;

	}

}
