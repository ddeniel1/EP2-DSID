package util;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;

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

    public static Dataset<Row> readCsv(SparkSession sparkSession, String path, StructType schema) {

        String bucket = null;
        try {
            bucket = new File(".").getCanonicalPath() + "/" + path;
            System.out.println(bucket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sparkSession.read()
                .option("header", true)
                .option("sep", ",")
                .option("quote","\"")
                .schema(schema)
//                .option("inferSchema","true")
                .csv(bucket);
    }
}
