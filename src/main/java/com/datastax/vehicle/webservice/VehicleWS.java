package com.datastax.vehicle.webservice;

import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.datastax.vehicle.service.VehicleService;
import com.datastax.vehicle.webservice.resources.*;
import com.datastax.vehicle.webservice.validation.ValidationOutcome;
import com.datastax.vehicle.webservice.validation.WSInputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebService
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleWS {

	private static Logger logger = LoggerFactory.getLogger(VehicleWS.class);

	//Service Layer.
	private VehicleService service = new VehicleService();

	//Dates - 20160801-000000
//	@GET
//	@Path("/getmovements/{vehicle}/{date}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response getMovementsForVehicle(@PathParam("vehicle") String vehicle, @PathParam("date") String dateString) {
//
//		List<Vehicle> result = service.getVehicleMovements(vehicle, dateString);
//
//		return Response.status(201).entity(result).build();
//	}
//
//	@GET
//	@Path("/getvehicles/{tile}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response getVehiclesByTile(@PathParam("tile") String tile) {
//
//		List<Vehicle> result = service.getVehiclesByTile(tile);
//
//		return Response.status(201).entity(result).build();
//	}
//
//	@GET
//	@Path("/search/{lat}/{lon}/{distance}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response searchForVehicles(@PathParam("lat") double lat, @PathParam("lon") double lon, @PathParam("distance") int distance) {
//
//		List<Vehicle> result = service.searchVehiclesByLonLatAndDistance(distance, new LatLong(lat, lon));
//
//		return Response.status(201).entity(result).build();
//	}
//
//	@GET
//	@Path("/getlastmovements/{fromdate}/{todate}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response getLastMovements(@PathParam("fromdate") String fromDate, @PathParam("todate") String toDate) {
//
//		logger.info("GetLastMovements");
//		DateTime to = null;
//		DateTime from = null;
//		try {
//			to = DateUtils.parseDate(toDate);
//			from = DateUtils.parseDate(fromDate);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return Response.status(400).entity("error in date format").build();
//		}
//
//		logger.info("Calling");
//		List<Vehicle> result = service.searchAreaTimeLastPosition(from, to);
//
//		return Response.status(201).entity(result).build();
//	}

	/*****************
	 * New API calls
	 ****************/

	/**
	 * TODO add validation of input parameters later
	 */

	//Two temporary test calls to check that everything is set up properly TODO remove these
	@POST
	@Path("/hello/world")
	public Response helloWorldPost() {
		String result = "Hello world ";
		System.out.println(result);
		return Response.status(201).entity(result).build();
	}

	@POST
	@Path("/hello/world/geoPoint")
	public Response helloWorldPoint(GeoPoint geoPoint) {
		String result = "Hello world. This is the geoPoint. Lat " + geoPoint.getLat() + " long " + geoPoint.getLng();
		System.out.println(result);
		return Response.status(201).entity(result).build();
	}

	/**** By area ****/

	/**
	 * Get the latest reading of each vehicle in the selected area in the specified point in time or timeframe
	 *
	 * Input is a GlobalRequestInputWrapper containing:
	 *   - Area: mandatory. It can be a polygon or a circle.
	 *   - Timeframe: optional. If not specified, the request is considered for current data.
	 *        For specific point in time, set start date the same as end date
	 *   - MeasurementSubset: optional. If specified, it defines what measurements should be returned for each reading
	 *
 	 * @return a list of VehicleReading objects, representing the latest for each vehicle in the area and timeframe.
	 * It will always contain position and timestamp and may optionally contain measurements (if requested) as key-value pairs
	 */
	@POST
	@Path("/area/vehicles/readings/latest")
	@Produces(MediaType.APPLICATION_JSON)
	public Response retrieveLatestReadingOfVehicles(GlobalRequestInputWrapper inputWrapper) {

		ValidationOutcome valOut = WSInputValidator.validateGlobalRequestInputWrapper(inputWrapper, false);
		if ( !valOut.isValid() ) {
			return Response.status(400).entity(valOut.getValidationMessagesAsSingleString()).build();
		}

		System.out.println("Web Service: Retrieving current vehicles in area...");
		//TODO implement latest on historical timeframe
		List<VehicleReading> result = service.retrieveLatestReadingOfVehicles(
									inputWrapper.getArea(), inputWrapper.getTimeframe(),
									inputWrapper.getMeasurementSubset(), inputWrapper.getFilter());
		System.out.println("Web Service: Current vehicles in area successfully retrieved. Size " + result.size());

		return Response.status(201).entity(result).build();
	}

	/**
	 * Get all readings of each vehicle that was in the selected area within the timeframe.
	 * This is basically the portion of the route of each vehicle within the area and timeframe, with optional measurements
	 *
	 * Input is a GlobalRequestInputWrapper containing:
	 *   - Area: mandatory. It can be a polygon or a circle.
	 *   - Timeframe: mandatory. Cannot be a single point in time (i.e. start time must be < end time, or end time can be null)
	 *   - MeasurementSubset: optional. If specified, it defines what measurements should be returned for each reading
	 *   - Order: optional. Passed only if the readings should be time-ordered and specifies how.
	 *   - Filter: optional. Just a Search snippet defining the custom filtering logic. It contains conditions on one or more properties
	 *
	 * @return a list of VehicleReading objects
	 * All readings matching the filter that were recorded for each vehicle in the area and timeframe,
	 * ordered by time ascending or descending or not ordered at all (according to the flag).
	 * Each reading will always contain position and timestamp and may optionally contain measurements (if requested) as key-value pairs.
	 *
	 */
	@POST
	@Path("/area/vehicles/readings/history")
	public Response retrieveHistoricalReadingsOfVehicles(GlobalRequestInputWrapper inputWrapper) {

		ValidationOutcome valOut = WSInputValidator.validateGlobalRequestInputWrapper(inputWrapper, true);

		if ( !valOut.isValid() ) {
			return Response.status(400).entity(valOut.getValidationMessagesAsSingleString()).build();
		}

		System.out.println("Web Service: Retrieving all readings of all vehicles in area...");
		List<VehicleReading> result = service.retrieveHistoricalReadingsOfVehicles(
									inputWrapper.getArea(), inputWrapper.getTimeframe(),
									inputWrapper.getMeasurementSubset(),
									inputWrapper.getFilter(), inputWrapper.getOrder());
		System.out.println("Web Service: Historical readings of all vehicles in area successfully retrieved. Size " + result.size());

		return Response.status(201).entity(result).build();
	}


	/**** By vehicle ****/

	/**
	 * Get the latest reading of the requested vehicle in the selected area in the specified point in time or timeframe
	 *
	 * Input is a VehicleRequestInputWrapper containing:
	 *   - VehicleID: mandatory.
	 *   - Area: optional. It can be a polygon or a circle.
	 *   - Timeframe: optional. If not specified, the request is considered for current data.
	 *        For specific point in time, set start date the same as end date
	 *   - MeasurementSubset: optional. If specified, it defines what measurements should be returned for the reading
	 *
	 * @return a VehicleData object with a single reading
	 * The latest reading of the requested vehicle. Area and timeframe are optional. It will always contain position and
	 * timestamp and may optionally contain measurements (if requested) as key-value pairs
	 */
	@POST
	@Path("/vehicle/readings/latest")
	public Response retrieveLatestReadingOfVehicle(VehicleRequestInputWrapper inputWrapper) {

		ValidationOutcome valOut = WSInputValidator.validateVehicleRequestInputWrapper(inputWrapper);

		if ( !valOut.isValid() ) {
			return Response.status(400).entity(valOut.getValidationMessagesAsSingleString()).build();
		}

		// Generate some stubbed data to get going for now
		//VehicleReading result = SampleWSDataGenerator.generateSingleVehicleReading();
		System.out.println("Web Service: Retrieving latest reading of vehicle " + inputWrapper.getVehicleId());
		VehicleReading result = service.retrieveLatestReadingOfSingleVehicle(inputWrapper.getVehicleId(),
														inputWrapper.getArea(), inputWrapper.getTimeframe(),
														inputWrapper.getMeasurementSubset(), inputWrapper.getFilter() );
		System.out.println("Web Service: Retrieved latest reading of vehicle " + inputWrapper.getVehicleId());
		return Response.status(201).entity(result).build();
	}

	/**
	 * Get all readings of the requested vehicle in the selected area within the timeframe.
	 * This is basically the portion of the route of the vehicle within the area and timeframe, with optional measurements
	 *
	 * Input is a VehicleRequestInputWrapper containing:
	 *   - VehicleID: mandatory.
	 *   - Area: mandatory. It can be a polygon or a circle.
	 *   - Timeframe: mandatory and cannot be a single point in time (i.e. start time must be < end time, or end time can be null)
	 *   - MeasurementSubset: optional. If specified, it defines what measurements should be returned for each reading
	 *   - Order: optional. Passed only if the readings should be time-ordered and specifies how.
	 *   - Filter: optional. Just a Search snippet defining the custom filtering logic. It contains conditions on one or more properties
	 *
	 * @return a VehicleData object
	 * All readings matching the filter that were recorded for the vehicle in the area and timeframe,
	 * ordered by time ascending or descending or not ordered at all (according to the flag).
	 * Each reading will always contain position and timestamp and may optionally contain measurements (if requested) as key-value pairs.
	 *
	 */
	@POST
	@Path("/vehicle/readings/history")
	public Response retrieveHistoricalReadingsOfVehicle(VehicleRequestInputWrapper inputWrapper) {

		ValidationOutcome valOut = WSInputValidator.validateVehicleRequestInputWrapper(inputWrapper);

		if ( !valOut.isValid() ) {
			return Response.status(400).entity(valOut.getValidationMessagesAsSingleString()).build();
		}

		// Generate some stubbed data to get going for now
		//List<VehicleReading> result = SampleWSDataGenerator.generateListOfOneVehiclesWithTwoReadings();
		System.out.println("Web Service: Retrieving historical readings of vehicle " + inputWrapper.getVehicleId());
		List<VehicleReading> result = service.retrieveHistoricalReadingsOfSingleVehicle(inputWrapper.getVehicleId(),
																	inputWrapper.getArea(), inputWrapper.getTimeframe(),
																	inputWrapper.getMeasurementSubset(),
																	inputWrapper.getFilter(), inputWrapper.getOrder() );
		System.out.println("Web Service: Retrieved historical readings of vehicle " + inputWrapper.getVehicleId());

		return Response.status(201).entity(result).build();
	}

	// TODO add API for heatmap
}