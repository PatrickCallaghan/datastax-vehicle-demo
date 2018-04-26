package com.datastax.vehicle.webservice.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SampleWSDataGenerator {

    public static List<VehicleData> generateListfTwoVehicleDataWithOneReadingEach() {

        Point p1 = new Point(23.55, 43.71);
        Map m1 = new HashMap();
        m1.put("abc", "123");
        m1.put("def", "456");
        Reading r1 = new Reading(p1, "2018/04/25 12:08:43", m1);
        List<Reading> rl1 = new ArrayList<>();
        rl1.add(r1);
        VehicleData vd1 = new VehicleData("vehicle1", rl1);

        Point p2 = new Point(42.78, 23.16);
        Map m2 = new HashMap();
        m2.put("ghi", "789");
        m2.put("jkl", "012");
        Reading r2 = new Reading(p2, "2018/04/25 12:07:13", m2);
        List<Reading> rl2 = new ArrayList<>();
        rl2.add(r2);
        VehicleData vd2 = new VehicleData("vehicle2", rl2);

        List<VehicleData> vdList = new ArrayList<>();
        vdList.add(vd1);
        vdList.add(vd2);

        return vdList;
    }

    public static List<VehicleData> generateListfTwoVehicleDataWithThreeReadingsEach() {
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

        Point p13 = new Point(23.55, 43.71);
        Map m13 = new HashMap();
        m13.put("xyz", "563");
        m13.put("dhb", "727");
        Reading r13 = new Reading(p13, "2018/04/25 12:15:55", m13);


        List<Reading> rl1 = new ArrayList<>();
        rl1.add(r11);
        rl1.add(r12);
        rl1.add(r13);

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
        Reading r22 = new Reading(p22, "2018/04/25 12:11:55", m22);

        Point p23 = new Point(13.88, 61.53);
        Map m23 = new HashMap();
        m23.put("ihg", "987");
        m23.put("lkj", "210");
        Reading r23 = new Reading(p23, "2018/04/25 12:32:45", m23);

        List<Reading> rl2 = new ArrayList<>();
        rl2.add(r21);
        rl2.add(r22);
        rl2.add(r23);

        VehicleData vd2 = new VehicleData("vehicle2", rl2);

        List<VehicleData> vdList = new ArrayList<>();
        vdList.add(vd1);
        vdList.add(vd2);

        return vdList;
    }

    public static List<VehicleData> generateListfTwoVehicleDataWithOneAndTwoReadings() {
        Point p11 = new Point(23.55, 43.71);
        Map m11 = new HashMap();
        m11.put("abc", "123");
        m11.put("def", "456");
        Reading r11 = new Reading(p11, "2018/04/25 12:08:43", m11);


        List<Reading> rl1 = new ArrayList<>();
        rl1.add(r11);

        VehicleData vd1 = new VehicleData("vehicle1", rl1);

        Point p21 = new Point(42.78, 23.16);
        Map m21 = new HashMap();
        m21.put("ghi", "789");
        m21.put("jkl", "012");
        Reading r21 = new Reading(p21, "2018/04/25 12:07:13", m21);

        Point p23 = new Point(13.88, 61.53);
        Map m23 = new HashMap();
        m23.put("ihg", "987");
        m23.put("lkj", "210");
        Reading r23 = new Reading(p23, "2018/04/25 12:32:45", m23);

        List<Reading> rl2 = new ArrayList<>();
        rl2.add(r21);
        rl2.add(r23);

        VehicleData vd2 = new VehicleData("vehicle2", rl2);

        List<VehicleData> vdList = new ArrayList<>();
        vdList.add(vd1);
        vdList.add(vd2);

        return vdList;
    }

    public static VehicleData generateVehicleDataWithSingleReading() {
        Point p11 = new Point(23.55, 43.71);
        Map m11 = new HashMap();
        m11.put("abc", "123");
        m11.put("def", "456");
        Reading r11 = new Reading(p11, "2018/04/25 12:08:43", m11);

        List<Reading> rl1 = new ArrayList<>();
        rl1.add(r11);

        VehicleData vd1 = new VehicleData("vehicle12345", rl1);

        return vd1;
    }

    public static VehicleData generateVehicleDataWithTwoReadings() {
        Point p21 = new Point(42.78, 23.16);
        Map m21 = new HashMap();
        m21.put("ghi", "789");
        m21.put("jkl", "012");
        Reading r21 = new Reading(p21, "2018/04/25 12:07:13", m21);

        Point p23 = new Point(13.88, 61.53);
        Map m23 = new HashMap();
        m23.put("ihg", "987");
        m23.put("lkj", "210");
        Reading r23 = new Reading(p23, "2018/04/25 12:32:45", m23);

        List<Reading> rl2 = new ArrayList<>();
        rl2.add(r21);
        rl2.add(r23);

        VehicleData vd2 = new VehicleData("vehicle12345", rl2);

        return vd2;
    }

    public static VehicleData generateVehicleDataWithThreeReadings() {
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

        Point p13 = new Point(23.55, 43.71);
        Map m13 = new HashMap();
        m13.put("xyz", "563");
        m13.put("dhb", "727");
        Reading r13 = new Reading(p13, "2018/04/25 12:15:55", m13);


        List<Reading> rl1 = new ArrayList<>();
        rl1.add(r11);
        rl1.add(r12);
        rl1.add(r13);

        VehicleData vd1 = new VehicleData("vehicle12345", rl1);

        return vd1;
    }

}
