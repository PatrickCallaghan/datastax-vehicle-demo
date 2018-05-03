import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import com.datastax.vehicle.model.Vehicle;
import com.datastax.vehicle.service.VehicleService;

public class TestFacet {
	
	@Test
	public void testFacet(){
		
		DateTime from = DateTime.now().minusDays(6);
		DateTime to = DateTime.now().minusDays(6).plusHours(2);
		
		
		VehicleService service = new VehicleService();

		//List<Vehicle> vehicleMovements = service.searchAreaTimeLastPosition(from, to);
		
		//System.out.println(vehicleMovements);
	}
	

}
