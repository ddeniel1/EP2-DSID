package job.writer;

import job.processor.LeastSquares;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;

public class PlotGraphWriter implements Writer<LeastSquares> {

    private final String xAxis;
    private final String yAxis;

    public PlotGraphWriter(String xAxis, String yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    @Override
    public void write(LeastSquares input) {

        XYSeries xy = new XYSeries("");
        List<Double[]> rows = input.getData().dropDuplicates().collectAsList().stream().parallel().map(value -> {
            Double[] result = new Double[2];
            result[0] = value.getDouble(0);
            result[1] = value.getDouble(1);
            return result;
        }).collect(Collectors.toList());
        rows.parallelStream().forEachOrdered(row -> xy.add(row[0], row[1]));
        XYSeries xySeries = new XYSeries("");
        Double xMin = input.getxMin();
        Double xMax = input.getxMax();
        double range = xMax - xMin;
        range = range / 100;
        for (int i = 0; i <= 100; i++) {
            Double x = xMin + (range * i);
            Double y = input.getA() + (input.getB() * x);
            xySeries.add(x, y);
        }

        XYSeriesCollection data = new XYSeriesCollection(xy);
        XYSeriesCollection reta = new XYSeriesCollection(xySeries);
        JFreeChart chart = ChartFactory.createScatterPlot(xAxis + " x " + yAxis, xAxis, yAxis, data, PlotOrientation.VERTICAL, false, true, false);
        JFreeChart chart2 = ChartFactory.createXYLineChart(xAxis + " x " + yAxis, xAxis, yAxis, reta, PlotOrientation.VERTICAL, false, true, false);
        XYPlot xyPlot = chart.getXYPlot();
        XYPlot xyPlot2 = chart2.getXYPlot();


        xyPlot.setDataset(1, xyPlot.getDataset());
        xyPlot.setRenderer(1, xyPlot.getRenderer());
        xyPlot.setDomainAxis(1, xyPlot.getDomainAxis());
        xyPlot.setRangeAxis(1, xyPlot.getRangeAxis());

        xyPlot.setDataset(0, xyPlot2.getDataset());
        xyPlot.setRenderer(0, xyPlot2.getRenderer());
        xyPlot.setDomainAxis(0, xyPlot2.getDomainAxis());
        xyPlot.setRangeAxis(0, xyPlot2.getRangeAxis());
        configurePlot(xyPlot);

        show(chart, input);

    }

    private void show(JFreeChart chart, LeastSquares input) {
        JFrame frame = new JFrame("Resultado");
        frame.setLayout(new BorderLayout());
        ChartFrame chartFrame = new ChartFrame("plot", chart);
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Variavel");
        tableModel.addColumn("Valor");
        tableModel.addRow(new Object[]{"A", input.getA()});
        tableModel.addRow(new Object[]{"B", input.getB()});
        tableModel.addRow(new Object[]{"Y0", input.getY0()});
        tableModel.addRow(new Object[]{"Y1", input.getY1()});
        tableModel.addRow(new Object[]{"Xmin", input.getxMin()});
        tableModel.addRow(new Object[]{"Xmax", input.getxMax()});
        tableModel.addRow(new Object[]{"Desvio Padrão X", input.getxStddev()});
        tableModel.addRow(new Object[]{"Desvio Padrão Y", input.getyStddev()});

        frame.add(new JScrollPane(new JTable(tableModel)), BorderLayout.SOUTH);
        ChartPanel chartPanel = chartFrame.getChartPanel();
        chartPanel.setVisible(true);
        frame.add(chartPanel, BorderLayout.NORTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void configurePlot(XYPlot plot) {
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.BLACK);
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setOutlineVisible(false);
    }
}
