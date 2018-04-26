package com.datastax.vehicle.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.datastax.vehicle.webservice.resources.*;
import com.datastax.vehicle.webservice.validation.ValidationOutcome;
import com.datastax.vehicle.webservice.validation.WSInputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebService
@Path("/")
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
	@Produces(MediaType.APPLICATION_JSON)
	public Response helloWorldPost() {
		String result = "Hello world ";
		System.out.println(result);
		return Response.status(201).entity(result).build();
	}

	@POST
	@Path("/hello/world/point")
	@Produces(MediaType.APPLICATION_JSON)
	public Response helloWorldPoint(Point point) {
		String result = "Hello world. This is the point. Lat " + point.getLatitude() + " long " + point.getLongitude();
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
 	 * @return a list of VehicleData objects, each with a single reading
	 * The latest reading of each vehicle in the area and timeframe. It will always contain position and timestamp
	 * and may optionally contain measurements (if requested) as key-value pairs
	 */
	@POST
	@Path("/area/vehicles/readings/latest")
	@Produces(MediaType.APPLICATION_JSON)
	public Response retrieveLatestReadingOfVehicles(GlobalRequestInputWrapper inputWrapper) {

		ValidationOutcome valOut = WSInputValidator.validateGlobalRequestInputWrapper(inputWrapper);

		if ( !valOut.isValid() ) {
			return Response.status(400).entity(valOut.getValidationMessagesAsSingleString()).build();
		}

		// Generate some stubbed data to get going for now - TODO this will change!
		List<VehicleData> result = SampleWSDataGenerator.generateListfTwoVehicleDataWithOneReadingEach();

		//List<VehicleData> result = service.retrieveLatestReadingOfVehicles(ar);
		return Response.status(200).entity(result).build();
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
	 *
	 * @return a list of VehicleData objects
	 * The route of each vehicle in the area and timeframe. This means all the readings that were recorded for each vehicle,
	 * grouped by vehicle and ordered by time ascending or descending or not ordered at all (according to the flag).
	 * Each reading will always contain position and timestamp and may optionally contain measurements (if requested) as key-value pairs
	 *
	 */
	@POST
	@Path("/area/vehicles/readings/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response retrieveAllReadingsOfVehicles(GlobalRequestInputWrapper inputWrapper) {

		ValidationOutcome valOut = WSInputValidator.validateGlobalRequestInputWrapper(inputWrapper);

		if ( !valOut.isValid() ) {
			return Response.status(400).entity(valOut.getValidationMessagesAsSingleString()).build();
		}

		// Generate some stubbed data to get going for now - TODO this will change!
		List<VehicleData> result = SampleWSDataGenerator.generateListfTwoVehicleDataWithThreeReadingsEach();

		return Response.status(201).entity(result).build();
	}


	/**
	 * Get the readings of each vehicle that was in the selected area within the timefram.
	 * This is basically the portion of the route of each vehicle within the area and timeframe, with optional measurements
	 *
	 * Input is a GlobalRequestInputWrapper containing:
	 *   - Area: mandatory. It can be a polygon or a circle.
	 *   - Timeframe: mandatory. Cannot be a single point in time (i.e. start time must be < end time, or end time can be null)
	 *   - MeasurementSubset: optional. If specified, it defines what measurements should be returned for each reading
	 *   - Order: optional. Passed only if the readings should be time-ordered and specifies how.
	 *   - Filter is just a Search snippet defining the custom filtering logic. It contains conditions on one or more properties
	 *
	 * @return* @return a list of VehicleData objects
	 * All readings matching the filter that were recorded for each vehicle in the area and timeframe,
	 * grouped by vehicle and ordered by time ascending or descending or not ordered at all (according to the flag).
	 * Each reading will always contain position and timestamp and may optionally contain measurements (if requested) as key-value pairs.
	 *
	 */
	@POST
	@Path("/area/vehicles/readings/filtered")
	@Produces(MediaType.APPLICATION_JSON)
	public Response retrieveFilteredReadingsOfVehicles(GlobalRequestInputWrapper inputWrapper) {

		ValidationOutcome valOut = WSInputValidator.validateGlobalRequestInputWrapper(inputWrapper);

		if ( !valOut.isValid() ) {
			return Response.status(400).entity(valOut.getValidationMessagesAsSingleString()).build();
		}

		// Generate some stubbed data to get going for now - TODO this will change!
		List<VehicleData> result = SampleWSDataGenerator.generateListfTwoVehicleDataWithOneAndTwoReadings();

		return Response.status(201).entity(result).build();
	}

	/**** By vehicle ****/

	/**
	 * Get the latest reading of the requested vehicle in the selected area in the specified point in time or timeframe
	 *
	 * Input is a VehicleRequestInputWrapper containing:
	 *   - VehicleID: mandatory.
	 *   - Area: mandatory. It can be a polygon or a circle.
	 *   - Timeframe: optional. If not specified, the request is considered for current data.
	 *        For specific point in time, set start date the same as end date
	 *   - MeasurementSubset: optional. If specified, it defines what measurements should be returned for the reading
	 *
	 * @return a VehicleData object with a single reading
	 * The latest reading of the requested vehicle in the area and timeframe. It will always contain position and timestamp
	 * and may optionally contain measurements (if requested) as key-value pairs
	 */
	@POST
	@Path("/vehicle/readings/latest")
	@Produces(MediaType.APPLICATION_JSON)
	public Response retrieveLatestReadingOfVehicle(VehicleRequestInputWrapper inputWrapper) {

		ValidationOutcome valOut = WSInputValidator.validateVehicleRequestInputWrapper(inputWrapper);

		if ( !valOut.isValid() ) {
			return Response.status(400).entity(valOut.getValidationMessagesAsSingleString()).build();
		}

		// Generate some stubbed data to get going for now - TODO this will change!
		VehicleData result = SampleWSDataGenerator.generateVehicleDataWithSingleReading();

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
	 *
	 * @return a VehicleData object
	 * The route of the vehicle in the area and timeframe. This means all the readings that were recorded for that vehicle,
	 * ordered by time ascending or descending or not ordered at all (according to the flag).
	 * Each reading will always contain position and timestamp and may optionally contain measurements (if requested) as key-value pairs
	 *
	 */
	@POST
	@Path("/vehicle/readings/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response retrieveAllReadingsOfVehicle(VehicleRequestInputWrapper inputWrapper) {

		ValidationOutcome valOut = WSInputValidator.validateVehicleRequestInputWrapper(inputWrapper);

		if ( !valOut.isValid() ) {
			return Response.status(400).entity(valOut.getValidationMessagesAsSingleString()).build();
		}


		// Generate some stubbed data to get going for now - TODO this will change!
		VehicleData result = SampleWSDataGenerator.generateVehicleDataWithThreeReadings();

		return Response.status(201).entity(result).build();
	}

	/**
	 * Get the readings of the requested vehicle in the selected area within the timeframe.
	 * This is basically the portion of the route of the vehicle within the area and timeframe, with optional measurements
	 *
	 * Input is a VehicleRequestInputWrapper containing:
	 *   - VehicleID: mandatory.
	 *   - Area: mandatory. It can be a polygon or a circle.
	 *   - Timeframe: mandatory and cannot be a single point in time (i.e. start time must be < end time, or end time can be null)
	 *   - MeasurementSubset is optional. If specified, it defines what measurements should be returned for each reading
	 *   - Order: optional. Passed only if the readings should be time-ordered and specifies how.
	 *   - Filter is just a Search snippet defining the custom filtering logic. It contains conditions on one or more properties
	 *
	 * @return a VehicleData object
	 * All readings matching the filter that were recorded for the vehicle in the area and timeframe,
	 * ordered by time ascending or descending or not ordered at all (according to the flag).
	 * Each reading will always contain position and timestamp and may optionally contain measurements (if requested) as key-value pairs.
	 *
	 */
	@POST
	@Path("/vehicle/readings/filtered")
	@Produces(MediaType.APPLICATION_JSON)
	public Response retrieveFilteredReadingsOfVehicle(VehicleRequestInputWrapper inputWrapper) {

		ValidationOutcome valOut = WSInputValidator.validateVehicleRequestInputWrapper(inputWrapper);

		if ( !valOut.isValid() ) {
			return Response.status(400).entity(valOut.getValidationMessagesAsSingleString()).build();
		}


		// Generate some stubbed data to get going for now - TODO this will change!
		VehicleData result = SampleWSDataGenerator.generateVehicleDataWithTwoReadings();

		return Response.status(201).entity(result).build();
	}


}