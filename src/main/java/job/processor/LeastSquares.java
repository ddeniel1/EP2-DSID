package job.processor;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class LeastSquares {
    private Double a;
    private Double b;
    private Double xMin;
    private Double xMax;
    private Double y0;
    private Double y1;
    private Double xAvg;
    private Double yAVg;
    private Double xStddev;
    private Double yStddev;
    private Dataset<Row> data;
    private Dataset<Row> describe;

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

    public Double getA() {
        return a;
    }

    public void setA(Double a) {
        this.a = a;
    }

    public Double getB() {
        return b;
    }

    public void setB(Double b) {
        this.b = b;
    }

    public Double getxMin() {
        return xMin;
    }

    public void setxMin(Double xMin) {
        this.xMin = xMin;
    }

    public Double getxMax() {
        return xMax;
    }

    public void setxMax(Double xMax) {
        this.xMax = xMax;
    }

    public Double getY0() {
        return y0;
    }

    public void setY0(Double y0) {
        this.y0 = y0;
    }

    public Double getY1() {
        return y1;
    }

    public void setY1(Double y1) {
        this.y1 = y1;
    }

    public Double getxAvg() {
        return xAvg;
    }

    public void setxAvg(Double xAvg) {
        this.xAvg = xAvg;
    }

    public Double getyAVg() {
        return yAVg;
    }

    public void setyAVg(Double yAVg) {
        this.yAVg = yAVg;
    }

    public Double getxStddev() {
        return xStddev;
    }

    public void setxStddev(Double xStddev) {
        this.xStddev = xStddev;
    }

    public Double getyStddev() {
        return yStddev;
    }

    public void setyStddev(Double yStddev) {
        this.yStddev = yStddev;
    }

    public Dataset<Row> getData() {
        return data;
    }

    public void setData(Dataset<Row> data) {
        this.data = data;
    }

    public Dataset<Row> getDescribe() {
        return describe;
    }

    public void setDescribe(Dataset<Row> describe) {
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
