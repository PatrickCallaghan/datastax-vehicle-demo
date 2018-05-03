package com.datastax.vehicle;

import java.util.concurrent.BlockingQueue;

import com.datastax.vehicle.dao.VehicleDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.demo.utils.KillableRunner;
import com.datastax.vehicle.model.Vehicle;

public class VehicleWriter implements KillableRunner {

	private static Logger logger = LoggerFactory.getLogger(VehicleWriter.class);
	private volatile boolean shutdown = false;
	private VehicleDao dao;
	private BlockingQueue<Vehicle> queue;

	public VehicleWriter(VehicleDao dao, BlockingQueue<Vehicle> queue) {
		this.dao = dao;
		this.queue = queue;
	}

	@Override
	public void run() {
		Vehicle vehicle;
		while(!shutdown){				
			vehicle = queue.poll();
			
			if (vehicle!=null){
				try {
					this.dao.insertVehicleData(vehicle);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}				
		}				
	}
	
	@Override
    public void shutdown() {
		while(!queue.isEmpty())
			
		shutdown = true;
    }
}
