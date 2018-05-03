package com.datastax.vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.datastax.vehicle.dao.VehicleDao;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.demo.utils.KillableRunner;
import com.datastax.demo.utils.PropertyHelper;
import com.datastax.demo.utils.Timer;
import com.datastax.vehicle.model.Vehicle;

public class Main {
	private static Logger logger = LoggerFactory.getLogger(Main.class);
	private static int TOTAL_VEHICLES = 1000;

	private BlockingQueue<Vehicle> queue = new ArrayBlockingQueue<Vehicle>(1000);
	private List<VehicleRunner> vehicles = new ArrayList<VehicleRunner>();

	private VehicleDao dao;
	private static DateTime date;

	public Main() {


		String contactPointsStr = PropertyHelper.getProperty("contactPoints", "localhost");
		this.dao = new VehicleDao(contactPointsStr.split(","));
		
		int noOfDays= Integer.parseInt(PropertyHelper.getProperty("noOfDays", "8"));		
		int noOfThreads = Integer.parseInt(PropertyHelper.getProperty("noOfThreads", "5"));

		ExecutorService executor = Executors.newFixedThreadPool(noOfThreads);
		List<KillableRunner> tasks = new ArrayList<>();

		date = DateTime.now().minusDays(noOfDays);
		
		//Start Executors
		for (int i = 0; i < noOfThreads; i++) {

			KillableRunner task = new VehicleWriter(dao, queue);
			executor.execute(task);
			tasks.add(task);
		}

		Timer timer = new Timer();
		timer.start();

		logger.info("Creating Locations");
		VehicleUpdater vehicleUpdater = new VehicleUpdater(dao);
		vehicleUpdater.createStartValues(TOTAL_VEHICLES);

		//Create all the vehicle threads and start them up.
		for (int i=0; i < TOTAL_VEHICLES; i++){
			VehicleRunner runner = new VehicleRunner("" + i, vehicleUpdater, queue, date);
			vehicles.add(runner);
		}

		//Start the clock
		while (date.isBefore(DateTime.now())) {
			date = date.plusSeconds(1);

			for (VehicleRunner runner : vehicles){
				try {
					runner.nextStep(date);
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		}	

		while (true) {
			date = DateTime.now();
			for (VehicleRunner runner : vehicles){
				try {
					runner.nextStep(date);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			sleep(1);
		}
	}

	private void sleep(int sec) {
		try {
			Thread.sleep(sec * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main();
	}
}