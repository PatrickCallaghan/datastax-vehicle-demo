import com.datastax.vehicle.webservice.resources.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestToAndFromJSON {

    @Test
    public void generatePoint() throws Exception{
        GeoPoint pt1 = new GeoPoint(42.78, 23.16);
        ObjectMapper mapper = new ObjectMapper();

        String resultAsString = mapper.writeValueAsString(pt1);

        System.out.println(resultAsString);
    }

    /**
     * Area
     * Timeframe
     **/
    @Test
    public void generateGlobalRequestInputWrapper1() throws Exception{

        GlobalRequestInputWrapper iw = new GlobalRequestInputWrapper();

        Timeframe t = new Timeframe("2018/04/23 12:07:55", "2018/04/25 13:19:21");

        iw.setArea(createArea());
        iw.setTimeframe(t);

        ObjectMapper mapper = new ObjectMapper();

        String resultAsString = mapper.writeValueAsString( iw);

        System.out.println(resultAsString);

    }


    /**
     * Area
     * Timeframe with start and end
     * Measurement subset with two measurements
     **/
    @Test
    public void generateGlobalRequestInputWrapper2() throws Exception{

        GlobalRequestInputWrapper iw = new GlobalRequestInputWrapper();

        Timeframe t = new Timeframe("2018/05/18 01:17:52", "2018/05/18 23:22:25");

        iw.setArea(createArea());
        iw.setTimeframe(t);

        MeasurementSubset ms = new MeasurementSubset();
        ms.includeMeasurements("speed", "temperature");

        iw.setMeasurementSubset(ms);

        ObjectMapper mapper = new ObjectMapper();

        String resultAsString = mapper.writeValueAsString( iw);

        System.out.println(resultAsString);

    }


    /**
     * Area
     * Timeframe with start and end
     * Measurement subset with four measurements
     * Order descending
     * Filter with something
     **/
    @Test
    public void generateGlobalRequestInputWrapper3() throws Exception{

        GlobalRequestInputWrapper iw = new GlobalRequestInputWrapper();

        iw.setArea(createArea());

        Timeframe t = new Timeframe("2018/04/23 11:17:52", "2018/04/25 09:22:25");
        iw.setTimeframe(t);

        MeasurementSubset ms = new MeasurementSubset();
        ms.includeMeasurements("meas1", "meas2", "meas3", "meas4");
        iw.setMeasurementSubset(ms);

        Order o = new Order();
        o.setAscending(false);
        iw.setOrder(o);

        String f = "some valid search filter";
        iw.setFilter(f);

        ObjectMapper mapper = new ObjectMapper();

        String resultAsString = mapper.writeValueAsString(iw);

        System.out.println(resultAsString);

    }

    /**
     * Area
     * No timeframe
     * Measurement subset with all measurements
     * Filter with something
     **/
    @Test
    public void generateGlobalRequestInputWrapper4() throws Exception{

        GlobalRequestInputWrapper iw = new GlobalRequestInputWrapper();

        iw.setArea(createArea());

        MeasurementSubset ms = new MeasurementSubset();
        iw.setMeasurementSubset(ms);

        String f = "speed:[20 TO 40]";
        iw.setFilter(f);

        ObjectMapper mapper = new ObjectMapper();

        String resultAsString = mapper.writeValueAsString(iw);

        System.out.println(resultAsString);

    }

    /**
     * Vehicle Id
     * Area with three points
     * Timeframe with start and end
     * Measurement subset with four measurements
     * Order descending
     * Filter with something
     **/
    @Test
    public void generateVehicleRequestInputWrapper1() throws Exception{

        VehicleRequestInputWrapper iw = new VehicleRequestInputWrapper();

        iw.setVehicleId("vehicle12345");

        GeoPoint pt1 = new GeoPoint(42.78, 23.16);
        GeoPoint pt2 = new GeoPoint(11.29, 26.89);
        GeoPoint pt3 = new GeoPoint(31.44, 27.55);
        Polygon p = new Polygon();
        p.addGeoPoints(pt1, pt2, pt3);
        Area a = new Area();
        a.setPolygon(p);
        iw.setArea(a);

        Timeframe t = new Timeframe("2018/04/23 11:17:52", "2018/04/25 09:22:25");
        iw.setTimeframe(t);

        MeasurementSubset ms = new MeasurementSubset();
        ms.includeMeasurements("meas1", "meas2", "meas3", "meas4");
        iw.setMeasurementSubset(ms);

        Order o = new Order();
        o.setAscending(false);
        iw.setOrder(o);

        String f = "some valid search filter";
        iw.setFilter(f);

        ObjectMapper mapper = new ObjectMapper();

        String resultAsString = mapper.writeValueAsString(iw);

        System.out.println(resultAsString);

    }

    private Area createArea() {
        GeoPoint pt1 = new GeoPoint(48.736989, 10.271339);
        GeoPoint pt2 = new GeoPoint(48.067576, 11.609030);
        GeoPoint pt3 = new GeoPoint(48.774243, 12.913120);
        GeoPoint pt4 = new GeoPoint(49.595759, 11.123788);
        GeoPoint pt5 = new GeoPoint(48.736989, 10.271339);
        Polygon p = new Polygon();
        p.addGeoPoints(pt1, pt2, pt3, pt4, pt5);
        Area a = new Area();
        a.setPolygon(p);

        return a;
    }
}
