package com.datastax.vehicle.webservice.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SampleWSDataGenerator {

    public static List<VehicleData> generateListfTwoVehicleDataWithOneReadingEach() {

        List<VehicleData> vdList = new ArrayList<>();
        vdList.add(createFirstVehicleDataWithASingleReading());
        vdList.add(createSecondVehicleDataWithASingleReading());

        return vdList;
    }

    private static VehicleData createSecondVehicleDataWithASingleReading() {
        GeoPoint p2 = new GeoPoint(42.78, 23.16);
        Reading r2 = new Reading(p2, "2018/04/25 12:07:13");
        r2.addMeasurement("ghi", "789");
        r2.addMeasurement("jkl", "012");
        List<Reading> rl2 = new ArrayList<>();
        rl2.add(r2);
        return new VehicleData("vehicle2", rl2);
    }

    private static VehicleData createFirstVehicleDataWithASingleReading() {
        GeoPoint p1 = new GeoPoint(23.55, 43.71);
        Reading r1 = new Reading(p1, "2018/04/25 12:08:43");
        r1.addMeasurement("abc", "123");
        r1.addMeasurement("def", "456");
        List<Reading> rl1 = new ArrayList<>();
        rl1.add(r1);
        return new VehicleData("vehicle1", rl1);
    }

    public static List<VehicleData> generateListfTwoVehicleDataWithThreeReadingsEach() {

        List<VehicleData> vdList = new ArrayList<>();
        vdList.add(createFirstVehicleDataWithThreeReadings());
        vdList.add(createSecondVehicleDataWithThreeReadings());

        return vdList;
    }

    private static VehicleData createSecondVehicleDataWithThreeReadings() {
        GeoPoint p21 = new GeoPoint(42.78, 23.16);
        Reading r21 = new Reading(p21, "2018/04/25 12:07:13");
        r21.addMeasurement("ghi", "789");
        r21.addMeasurement("jkl", "012");

        GeoPoint p22 = new GeoPoint(13.88, 61.53);
        Reading r22 = new Reading(p22, "2018/04/25 12:11:55");
        r22.addMeasurement("ihg", "987");
        r22.addMeasurement("lkj", "210");

        GeoPoint p23 = new GeoPoint(13.88, 61.53);
        Reading r23 = new Reading(p23, "2018/04/25 12:32:45");
        r23.addMeasurement("ihg", "987");
        r23.addMeasurement("lkj", "210");

        List<Reading> rl2 = new ArrayList<>();
        rl2.add(r21);
        rl2.add(r22);
        rl2.add(r23);

        return new VehicleData("vehicle2", rl2);
    }

    private static VehicleData createFirstVehicleDataWithThreeReadings() {
        GeoPoint p11 = new GeoPoint(23.55, 43.71);
        Reading r11 = new Reading(p11, "2018/04/25 12:08:43");
        r11.addMeasurement("abc", "123");
        r11.addMeasurement("def", "456");

        GeoPoint p12 = new GeoPoint(23.55, 43.71);
        Reading r12 = new Reading(p12, "2018/04/25 12:09:22");
        r12.addMeasurement("cba", "321");
        r12.addMeasurement("fed", "654");

        GeoPoint p13 = new GeoPoint(23.55, 43.71);
        Reading r13 = new Reading(p13, "2018/04/25 12:15:55");
        r13.addMeasurement("xyz", "563");
        r13.addMeasurement("dhb", "727");


        List<Reading> rl1 = new ArrayList<>();
        rl1.add(r11);
        rl1.add(r12);
        rl1.add(r13);

        return new VehicleData("vehicle1", rl1);
    }

    public static List<VehicleData> generateListfTwoVehicleDataWithOneAndTwoReadings() {

        List<VehicleData> vdList = new ArrayList<>();
        vdList.add(createFirstVehicleDataWithASingleReading());
        vdList.add(createVehicleDataWithTwoReadings());

        return vdList;
    }

    private static VehicleData createVehicleDataWithTwoReadings() {
        GeoPoint p21 = new GeoPoint(42.78, 23.16);
        Reading r21 = new Reading(p21, "2018/04/25 12:07:13");
        r21.addMeasurement("ghi", "789");
        r21.addMeasurement("jkl", "012");

        GeoPoint p23 = new GeoPoint(13.88, 61.53);
        Reading r23 = new Reading(p23, "2018/04/25 12:32:45");
        r23.addMeasurement("ihg", "987");
        r23.addMeasurement("lkj", "210");

        List<Reading> rl2 = new ArrayList<>();
        rl2.add(r21);
        rl2.add(r23);

        return new VehicleData("vehicle2", rl2);
    }

    public static VehicleData generateVehicleDataWithSingleReading() {
        return createFirstVehicleDataWithASingleReading();
    }

    public static VehicleData generateVehicleDataWithTwoReadings() {
        return createVehicleDataWithTwoReadings();
    }

    public static VehicleData generateVehicleDataWithThreeReadings() {
        return createFirstVehicleDataWithThreeReadings();
    }

}
