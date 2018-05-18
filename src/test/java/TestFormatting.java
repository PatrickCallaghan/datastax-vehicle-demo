import com.datastax.demo.utils.SearchFormatter;
import com.datastax.vehicle.webservice.resources.Area;
import com.datastax.vehicle.webservice.resources.MeasurementSubset;
import com.datastax.vehicle.webservice.resources.GeoPoint;
import com.datastax.vehicle.webservice.resources.Polygon;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.util.HashSet;


public class TestFormatting {

    @Test
    public void testGetIncludedMeasurementsAsStringWithSelectedMeasurements() {
        MeasurementSubset ms = new MeasurementSubset();
        ms.includeMeasurements("meas1", "meas2", "meas3");

        System.out.println(SearchFormatter.formatMeasurementsAsSearchString(ms));
    }

    @Test
    public void testGetIncludedMeasurementsAsStringWithNullOrEmptyMeasurements() {
        System.out.println("Null: " + SearchFormatter.formatMeasurementsAsSearchString(null));

        MeasurementSubset ms = new MeasurementSubset();
        ms.setIncludeOnly(new HashSet<>());
        System.out.println("Empty: " + SearchFormatter.formatMeasurementsAsSearchString(ms));
    }

    /*
        //2018/04/25 12:32:45
    private static DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss");
     */
    @Test
    public void testFormatDateforSolr() throws Exception{
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss");
        DateTime startDate = dtf.parseDateTime("2018/04/25 12:32:45");

        DateTimeFormatter solrDateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:sss'Z'");
        System.out.println(solrDateFormatter.print(startDate));
    }

    @Test
    public void testFormatPolygonSolr() throws Exception {

        GeoPoint pt1 = new GeoPoint(42.78, 23.16);
        GeoPoint pt2 = new GeoPoint(11.29, 26.89);
        GeoPoint pt3 = new GeoPoint(31.44, 27.55);
        Polygon p = new Polygon();
        p.addGeoPoints(pt1, pt2, pt3);

        Area a = new Area(p);
        System.out.println(SearchFormatter.formatAreaAsSearchString(a));
    }
}
