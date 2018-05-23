package com.datastax.vehicle.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.github.davidmoten.geo.LatLong;

public class Vehicle {
	private String vehicleId;
	private Date date;
	private LatLong latLong;
	private double temperature;
	private double speed;
	private Map<String, Double> p_ = new HashMap<String, Double>();

	public Vehicle(String vehicleId, Date date, LatLong latLong, double temperature,
			double speed) {
		super();
		this.vehicleId = vehicleId;
		this.date = date;
		this.latLong = latLong;
		this.setTemperature(temperature);
		this.setSpeed(speed);
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

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public Map<String, Double> getProperties() {
		return p_;
	}

	public void setProperties(Map<String, Double> p_) {
		this.p_ = p_;
	}

	@Override
	public String toString() {
		return "Vehicle [vehicleId=" + vehicleId + ", date=" + date + ", latLong=" + latLong +
				", temperature=" + temperature + ", speed=" + speed + ", properties=" + p_ + "]";
	}
}
