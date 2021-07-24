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
    public Dataset<Row> data;

    public LeastSquares(Double a, Double b, Double xMin, Double xMax, Double y0, Double y1, Dataset<Row> data) {
        this.a = a;
        this.b = b;
        this.xMin = xMin;
        this.xMax = xMax;
        this.y0 = y0;
        this.y1 = y1;
        this.data = data;
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
                ", data=" + data.toString() +
                '}';
    }
}
