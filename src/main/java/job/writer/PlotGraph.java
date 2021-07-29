package job.writer;

import job.processor.LeastSquares;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.PlotEntity;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.util.List;

public class PlotGraph implements Writer<LeastSquares> {
    @Override
    public void write(LeastSquares input) throws Exception {
        XYSeries xy = new XYSeries("");
        List<Row> rows = input.data.collectAsList();
        rows.parallelStream().forEachOrdered(row -> xy.add(row.getDouble(0), row.getDouble(1)));
        XYSeriesCollection data = new XYSeriesCollection(xy);
        JFreeChart chart =  ChartFactory.createScatterPlot("Teste", "Temp", "DEWP", data, PlotOrientation.VERTICAL, false,true,false );
        XYPlot xyPlot = chart.getXYPlot();
        configurePlot(xyPlot);
        show(chart);
//        JFreeChart chart = ChartFactory.createScatterPlot("Teste", "Temp", "DEWP", data);

    }

    private void show(JFreeChart chart) {
        ChartFrame frame = new ChartFrame("plot", chart);
        frame.pack();
        frame.setVisible(true);
    }

    private void configurePlot(XYPlot plot) {
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.BLACK);
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setOutlineVisible(false);
    }
}
