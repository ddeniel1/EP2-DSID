package job.processor;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class LeastSquares {
    public Double a;
    public Double b;
    public Double xMin;
    public Double xMax;
    public Double y0;
    public Double y1;
    public Double xAvg;
    public Double yAVg;
    public Double xStddev;
    public Double yStddev;
    public Dataset<Row> data;
    public Dataset<Row> describe;

    public LeastSquares(Double a, Double b, Double xMin, Double xMax, Double y0, Double y1,
                        Double xAvg, Double yAVg, Double xStddev, Double yStddev,
                        Dataset<Row> data, Dataset<Row> describe) {
        this.a = a;
        this.b = b;
        this.xMin = xMin;
        this.xMax = xMax;
        this.y0 = y0;
        this.y1 = y1;
        this.xAvg = xAvg;
        this.yAVg = yAVg;
        this.xStddev = xStddev;
        this.yStddev = yStddev;
        this.data = data;
        this.describe = describe;
    }

    @Override
    public String toString() {
        return "LeastSquares{" +
                "a=" + a +
                ", b=" + b +
                ", xMin=" + xMin +
                ", xMax=" + xMax +
                ", y0=" + y0 +
                ", y1=" + y1 +
                ", xAvg=" + xAvg +
                ", yAVg=" + yAVg +
                ", xStddev=" + xStddev +
                ", yStddev=" + yStddev +
                ", data=" + data +
                ", describe=" + describe +
                '}';
    }
}
