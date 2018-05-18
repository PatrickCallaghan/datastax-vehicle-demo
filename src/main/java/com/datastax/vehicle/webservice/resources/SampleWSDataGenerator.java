package com.datastax.vehicle.webservice.resources;

import java.util.ArrayList;
import java.util.List;

public class SampleWSDataGenerator {

    public static VehicleReading generateSingleVehicleReading() {
        return createFirstReadingForFirstVehicle();
    }

    public static List<VehicleReading> generateListOfTwoVehiclesWithOneReadingEach() {

        List<VehicleReading> vdList = new ArrayList<>();
        vdList.add(createFirstReadingForFirstVehicle());
        vdList.add(createFirstReadingForSecondVehicle());

        return vdList;
    }


    public static List<VehicleReading> generateListOfOneVehiclesWithTwoReadings() {

        List<VehicleReading> vdList = new ArrayList<>();
        vdList.add(createFirstReadingForFirstVehicle());
        vdList.add(createFirstReadingForSecondVehicle());
        return vdList;
    }

    public static List<VehicleReading> generateListOfTwoVehiclesWithTwoReadingsEach() {

        List<VehicleReading> vdList = new ArrayList<>();
        vdList.add(createFirstReadingForFirstVehicle());
        vdList.add(createFirstReadingForSecondVehicle());
        vdList.add(createSecondReadingForFirstVehicle());
        vdList.add(createSecondReadingForSecondVehicle());

        return vdList;
    }

    private static VehicleReading createFirstReadingForFirstVehicle() {
        GeoPoint p = new GeoPoint(23.55, 43.71);
        VehicleReading r = new VehicleReading("vehicle1", p, "2018/04/25 12:08:43");
        r.addMeasurement("abc", "123");
        r.addMeasurement("def", "456");
        return r;
    }


    private static VehicleReading createSecondReadingForFirstVehicle() {
        GeoPoint p = new GeoPoint(24.22, 38.82);
        VehicleReading r = new VehicleReading("vehicle1", p, "2018/04/25 12:08:53");
        r.addMeasurement("abc", "642");
        r.addMeasurement("def", "498");
        return r;
    }

    private static VehicleReading createFirstReadingForSecondVehicle() {
        GeoPoint p = new GeoPoint(42.78, 23.16);
        VehicleReading r = new VehicleReading("vehicle2", p, "2018/04/25 12:07:13");
        r.addMeasurement("ghi", "789");
        r.addMeasurement("jkl", "012");
        return r;
    }

    private static VehicleReading createSecondReadingForSecondVehicle() {
        GeoPoint p = new GeoPoint(44.18, 24.39);
        VehicleReading r = new VehicleReading("vehicle2", p, "2018/04/25 12:07:23");
        r.addMeasurement("ghi", "245");
        r.addMeasurement("jkl", "328");
        return r;
    }

}
