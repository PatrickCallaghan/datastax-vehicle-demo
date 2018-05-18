import com.datastax.demo.utils.SearchFormatter;
import com.datastax.vehicle.webservice.resources.*;
import org.junit.Test;

public class TestSolrQuerySyntax {

    @Test
    public void testSolrStringCurrentReadingsPerArea() throws Exception {

        String areaString = SearchFormatter.formatAreaAsSearchString(createArea());
        String selectedColumns = SearchFormatter.formatMeasurementsAsSearchString(createMeasurementsforAll());
        String emptyFilterString = SearchFormatter.formatFilterAsSearchString("");

        StringBuilder cql = new StringBuilder("SELECT ").append(selectedColumns)
                .append(" FROM datastax.current_location WHERE solr_query = '{\"q\":\"lat_long:\\\"isWithin( ")
                .append(areaString)
                .append(" )\\\"").append(emptyFilterString).append("\"}' LIMIT 3");

        System.out.println(cql);
    }

    @Test
    public void testSolrStringCurrentReadingsPerAreaWithFilter() throws Exception {


        String areaString = SearchFormatter.formatAreaAsSearchString(createArea());
        String selectedColumns = SearchFormatter.formatMeasurementsAsSearchString(createMeasurementsforAll());
        String filterString = SearchFormatter.formatFilterAsSearchString("speed:[20 TO 40]");

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

    @Test
    public void testSolrStringHistoricalReadingsPerAreaAndTimeframe() {

        String areaString = SearchFormatter.formatAreaAsSearchString(createArea());
        String timeframeString = SearchFormatter.formatTimeframeAsSearchString(createTimeframe());
        String selectedColumnsString = SearchFormatter.formatMeasurementsAsSearchString(createMeasurementsforAll());
        String emptyFilterString = SearchFormatter.formatFilterAsSearchString("");
        String noOrderString = SearchFormatter.formatOrderAsSearchString(null);

        StringBuilder cql = new StringBuilder("SELECT ").append(selectedColumnsString)
                .append(" FROM datastax.vehicle WHERE solr_query = '{\"q\": \"")
                .append(timeframeString).append(" AND ").append(areaString)
                .append(emptyFilterString).append("\"").append(noOrderString).append("}' LIMIT 3");

        System.out.println(cql);

    }


    @Test
    public void testSolrStringHistoricalReadingsPerAreaAndTimeframeWithFilterAndOrder() {

        String areaString = SearchFormatter.formatAreaAsSearchString(createArea());
        String timeframeString = SearchFormatter.formatTimeframeAsSearchString(createTimeframe());
        String selectedColumnsString = SearchFormatter.formatMeasurementsAsSearchString(createMeasurementsforAll());
        String filterString = SearchFormatter.formatFilterAsSearchString("speed:[150 TO 200] AND p_Torque:[10 TO 45]");
        String orderString = SearchFormatter.formatOrderAsSearchString(createOrder());

        StringBuilder cql = new StringBuilder("SELECT ").append(selectedColumnsString)
                .append(" FROM datastax.vehicle WHERE solr_query = '{\"q\": \"")
                .append(timeframeString).append(" AND ").append(areaString)
                .append(filterString).append("\"").append(orderString).append("}' LIMIT 100");

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
        ms.includeMeasurements("meas1", "meas2", "meas3", "meas4");
        return ms;
    }

    private MeasurementSubset createMeasurementsforAll() {
        return new MeasurementSubset();
    }

    private Timeframe createTimeframe() {
        return new Timeframe("2018/05/10 01:17:52", "2018/05/10 23:22:25");
    }

    private Order createOrder() {
        return new Order(true);
    }
}
