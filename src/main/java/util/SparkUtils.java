package util;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.File;
import java.io.IOException;

public final class SparkUtils {
    private SparkUtils() {
    }

    public static SparkSession buildSparkSession() {
        SparkConf conf = new SparkConf()
                .setMaster("local[*]")
                .setAppName("dsid-ep2-spark-application");

        return SparkSession.builder()
                .config(conf)
                .getOrCreate();
    }

    public static Dataset<Row> readCsv(SparkSession sparkSession, String path, String schema) {


        String bucket = null;
        try {
            bucket = new File(".").getCanonicalPath() + "/" + path;
            System.out.println(bucket);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        String pathFile = bucket + "/" + path.split("-")[2] + "/" + path.split("-")[0] + path.split("-")[1] + ".csv";
        return sparkSession.read()
                .option("header", true)
                .option("sep", ",")
                .option("quote", "\"")
                .option("charToEscapeQuoteEscaping", "")
                .option("inferSchema", true)
                .csv("file:///" + bucket);
    }
}
