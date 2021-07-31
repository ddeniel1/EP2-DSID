package util;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public final class SparkUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SparkUtils.class);
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

        String filePath = null;
        try {
            filePath = new File(".").getCanonicalPath() + "/" + path;
            LOGGER.info("m=readCsv msg=Reading file {}", filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sparkSession.read()
                .option("header", true)
                .option("sep", ",")
                .option("quote", "\"")
                .schema(schema)
//                .option("inferSchema","true")
                .csv(filePath);
    }
}
