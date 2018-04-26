import com.datastax.vehicle.webservice.resources.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestToAndFromJSON {

    @Test
    public void generateJSONListOfVehicleData1() throws Exception {
        Point p11 = new Point(23.55, 43.71);
        Map m11 = new HashMap();
        m11.put("abc", "123");
        m11.put("def", "456");
        Reading r11 = new Reading(p11, "2018/04/25 12:08:43", m11);

        Point p12 = new Point(23.55, 43.71);
        Map m12 = new HashMap();
        m12.put("cba", "321");
        m12.put("fed", "654");
        Reading r12 = new Reading(p12, "2018/04/25 12:09:22", m12);

        List<Reading> rl1 = new ArrayList<>();
        rl1.add(r11);
        rl1.add(r12);

        VehicleData vd1 = new VehicleData("vehicle1", rl1);

        Point p21 = new Point(42.78, 23.16);
        Map m21 = new HashMap();
        m21.put("ghi", "789");
        m21.put("jkl", "012");
        Reading r21 = new Reading(p21, "2018/04/25 12:07:13", m21);

        Point p22 = new Point(13.88, 61.53);
        Map m22 = new HashMap();
        m22.put("ihg", "987");
        m22.put("lkj", "210");
        Reading r22 = new Reading(p12, "2018/04/25 12:07:55", m22);

        List<Reading> rl2 = new ArrayList<>();
        rl2.add(r21);
        rl2.add(r22);

        VehicleData vd2 = new VehicleData("vehicle2", rl2);

        List<VehicleData> vdList = new ArrayList<>();
        vdList.add(vd1);
        vdList.add(vd2);

        ObjectMapper mapper = new ObjectMapper();

        String resultAsString = mapper.writeValueAsString( vdList);

        System.out.println(resultAsString);

    }

    @Test
    public void generatePoint() throws Exception{
        Point pt1 = new Point(42.78, 23.16);
        ObjectMapper mapper = new ObjectMapper();

        String resultAsString = mapper.writeValueAsString(pt1);

        System.out.println(resultAsString);
    }

    /**
     * Area with two points
     * Timeframe
     **/
    @Test
    public void generateGlobalRequestInputWrapper1() throws Exception{

        GlobalRequestInputWrapper iw = new GlobalRequestInputWrapper();

        Point pt1 = new Point(42.78, 23.16);
        Point pt2 = new Point(11.29, 26.89);

        Polygon p = new Polygon();
        p.addPoints(pt1, pt2);

        Area a = new Area();
        a.setPolygon(p);

        Timeframe t = new Timeframe("2018/04/23 12:07:55", "2018/04/25 13:19:21");

        iw.setArea(a);
        iw.setTimeframe(t);

        ObjectMapper mapper = new ObjectMapper();

        String resultAsString = mapper.writeValueAsString( iw);

        System.out.println(resultAsString);

    }


    /**
     * Area with three points
     * Timeframe with start and end
     * Measurement subset with four measurements
     **/
    @Test
    public void generateGlobalRequestInputWrapper2() throws Exception{

        GlobalRequestInputWrapper iw = new GlobalRequestInputWrapper();

        Point pt1 = new Point(42.78, 23.16);
        Point pt2 = new Point(11.29, 26.89);
        Point pt3 = new Point(31.44, 27.55);

        Polygon p = new Polygon();
        p.addPoints(pt1, pt2, pt3);

        Area a = new Area();
        a.setPolygon(p);

        Timeframe t = new Timeframe("2018/04/23 11:17:52", "2018/04/25 09:22:25");

        iw.setArea(a);
        iw.setTimeframe(t);

        MeasurementSubset ms = new MeasurementSubset();
        ms.setAllMeasurements(false);
        ms.includeMeasurements("meas1", "meas2", "meas3", "meas4");

        iw.setMeasurementSubset(ms);

        ObjectMapper mapper = new ObjectMapper();

        String resultAsString = mapper.writeValueAsString( iw);

        System.out.println(resultAsString);

    }


    /**
     * Area with three points
     * Timeframe with start and end
     * Measurement subset with four measurements
     * Order descending
     * Filter with something
     **/
    @Test
    public void generateGlobalRequestInputWrapper3() throws Exception{

        GlobalRequestInputWrapper iw = new GlobalRequestInputWrapper();

        Point pt1 = new Point(42.78, 23.16);
        Point pt2 = new Point(11.29, 26.89);
        Point pt3 = new Point(31.44, 27.55);
        Polygon p = new Polygon();
        p.addPoints(pt1, pt2, pt3);
        Area a = new Area();
        a.setPolygon(p);
        iw.setArea(a);

        Timeframe t = new Timeframe("2018/04/23 11:17:52", "2018/04/25 09:22:25");
        iw.setTimeframe(t);

        MeasurementSubset ms = new MeasurementSubset();
        ms.setAllMeasurements(false);
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

        Point pt1 = new Point(42.78, 23.16);
        Point pt2 = new Point(11.29, 26.89);
        Point pt3 = new Point(31.44, 27.55);
        Polygon p = new Polygon();
        p.addPoints(pt1, pt2, pt3);
        Area a = new Area();
        a.setPolygon(p);
        iw.setArea(a);

        Timeframe t = new Timeframe("2018/04/23 11:17:52", "2018/04/25 09:22:25");
        iw.setTimeframe(t);

        MeasurementSubset ms = new MeasurementSubset();
        ms.setAllMeasurements(false);
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
}
