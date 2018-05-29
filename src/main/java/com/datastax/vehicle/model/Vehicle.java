package com.datastax.vehicle.model;

import java.util.*;

import com.github.davidmoten.geo.GeoHash;
import com.github.davidmoten.geo.LatLong;

public class Vehicle {

	private final static int LARGEST_GEOHASH_TILE = 5;
	private final static int SMALLEST_GEOHASH_TILE = 12;
	private String vehicleId;
	private Date date;
	private LatLong latLong;
	private List<String> geoHashList;
	private double temperature;
	private double speed;
	private Map<String, Double> p_ = new HashMap<String, Double>();


	public Vehicle(String vehicleId, Date date, LatLong latLong, double temperature,
			double speed, Map<String, Double> properties) {
		this(vehicleId, date, latLong, temperature, speed, null, properties);
		this.geoHashList = calculateGeoHashList(latLong);
	}


	public Vehicle(String vehicleId, Date date, LatLong latLong, double temperature,
				   double speed, List<String> geoHashList, Map<String, Double> properties) {

		this.vehicleId = vehicleId;
		this.date = date;
		this.latLong = latLong;
		this.geoHashList = geoHashList;
		this.temperature = temperature;
		this.speed = speed;
		this.p_ = properties;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public Date getDate() {
		return date;
	}

	public LatLong getLatLong() {
		return latLong;
	}

	public double getTemperature() {
		return temperature;
	}

	public double getSpeed() {
		return speed;
	}

	public Map<String, Double> getProperties() {
		return p_;
	}

	public List<String> getGeoHashList() {
		return geoHashList;
	}

	private static List<String> calculateGeoHashList(LatLong latLong) {
		List<String> hashList = new ArrayList<>();

		String geoHash =  GeoHash.encodeHash(latLong.getLat(),latLong.getLon());
		for (int i = LARGEST_GEOHASH_TILE ; i < SMALLEST_GEOHASH_TILE ; i++){
			String thisHash = geoHash.substring(0,i);
			hashList.add( i + thisHash );
		}
		return hashList;
	}

	@Override
	public String toString() {
		return "Vehicle [vehicleId=" + vehicleId + ", date=" + date + ", latLong=" + latLong +
				", temperature=" + temperature + ", speed=" + speed + ", properties=" + p_ + "]";
	}
}
