package job.writer;

import job.processor.LeastSquares;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;

public class PlotGraph implements Writer<LeastSquares> {

    private final String xAxis;
    private final String yAxis;

    public PlotGraph(String xAxis, String yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    @Override
    public void write(LeastSquares input) throws Exception {
        XYSeries xy = new XYSeries("");
        List<Double[]> rows = input.data.collectAsList().stream().parallel().map(value -> {
            Double[] result = new Double[2];
            result[0] = value.getDouble(0);
            result[1] = value.getDouble(1);
            return result;
        }).collect(Collectors.toList());
        rows.parallelStream().forEachOrdered(row -> xy.add(row[0], row[1]));
        XYSeriesCollection data = new XYSeriesCollection(xy);
        JFreeChart chart = ChartFactory.createScatterPlot(xAxis + " x " + yAxis, xAxis, yAxis, data, PlotOrientation.VERTICAL, false, true, false);
        XYPlot xyPlot = chart.getXYPlot();
        configurePlot(xyPlot);
        show(chart);

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
