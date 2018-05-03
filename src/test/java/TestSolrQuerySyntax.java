import com.datastax.demo.utils.SolrFormatter;
import com.datastax.vehicle.webservice.resources.Area;
import com.datastax.vehicle.webservice.resources.GeoPoint;
import com.datastax.vehicle.webservice.resources.MeasurementSubset;
import com.datastax.vehicle.webservice.resources.Polygon;
import org.junit.Test;

public class TestSolrQuerySyntax {

    @Test
    public void testSolrStringCurrentReadingsPerArea() throws Exception {


        String areaString = SolrFormatter.formatAreaAsSolrString(createArea());
        String selectedColumns = SolrFormatter.formatMeasurementsAsSolrString(createMeasurementsforAll());

        StringBuilder cql = new StringBuilder("SELECT ").append(selectedColumns)
                .append(" FROM datastax.current_location WHERE solr_query = '{\"q\":\"lat_long:\\\"isWithin( ")
                .append(areaString)
                .append(" )\\\"\"}'");

        System.out.println(cql);
    }

    @Test
    public void testSolrStringCurrentReadingsPerAreaWithFilter() throws Exception {


        String areaString = SolrFormatter.formatAreaAsSolrString(createArea());
        String selectedColumns = SolrFormatter.formatMeasurementsAsSolrString(createMeasurementsforAll());
        String filterString = SolrFormatter.formatFilterAsSolrString("speed:[20 TO 40]");

//        StringBuilder cql = new StringBuilder("SELECT ").append(selectedColumns)
//                .append(" FROM datastax.current_location WHERE solr_query = '{\"q\":\"lat_long:\\\"isWithin( ")
//                .append(areaString)
//                .append(" )\\\"").append(" ").append(filter).append(" ").append("\"}'");

        StringBuilder cql = new StringBuilder("SELECT ").append(selectedColumns)
                .append(" FROM datastax.current_location WHERE solr_query = '{\"q\":\"lat_long:\\\"isWithin( ")
                .append(areaString)
                .append(" )\\\"").append(filterString).append("\"}' LIMIT 3");

        System.out.println(cql);
    }

    private Area createArea() {
        Polygon p = new Polygon();
        GeoPoint gpt1 = new GeoPoint(48.736989, 10.271339);
        GeoPoint gpt2 = new GeoPoint(48.067576, 11.609030);
        GeoPoint gpt3 = new GeoPoint(48.774243, 12.913120);
        GeoPoint gpt4 = new GeoPoint(49.595759, 11.123788);
        GeoPoint gpt5 = new GeoPoint(48.736989, 10.271339);

        p.addGeoPoints(gpt1, gpt2, gpt3, gpt4, gpt5);

        Area a = new Area();
        a.setPolygon(p);

        return a;
    }

    private MeasurementSubset createMeasurementsForSelected() {
        MeasurementSubset ms = new MeasurementSubset();
        ms.setAllMeasurements(false);
        ms.includeMeasurements("meas1", "meas2", "meas3", "meas4");
        return ms;
    }

    private MeasurementSubset createMeasurementsforAll() {
        MeasurementSubset ms = new MeasurementSubset();
        ms.setAllMeasurements(true);
        return ms;
    }


}
