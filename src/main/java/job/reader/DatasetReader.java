package job.reader;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import util.SparkUtils;

public class DatasetReader implements Reader<Dataset<Row>> {
    private final SparkSession sparkSession;
    private final String inputPath;
    private final String schema;

    public DatasetReader(SparkSession sparkSession, String inputPath, String schema) {
        this.sparkSession = sparkSession;
        this.inputPath = inputPath;
        this.schema = schema;
    }

    @Override
    public Dataset<Row> read() {
        Dataset<Row> dataset = SparkUtils.readCsv(sparkSession, inputPath, schema);

        return dataset;
    }
}
