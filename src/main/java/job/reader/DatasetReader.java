package job.reader;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import util.SparkUtils;

import java.util.List;

public class DatasetReader implements Reader<Dataset<Row>>{
    private final SparkSession sparkSession;
    private final List<String> inputPaths;
    private final String schema;

    public DatasetReader(SparkSession sparkSession, List<String> inputPaths, String schema) {
        this.sparkSession = sparkSession;
        this.inputPaths = inputPaths;
        this.schema = schema;
    }

    @Override
    public Dataset<Row> read() {
        Dataset<Row> dataset = SparkUtils.readCsv(sparkSession, inputPaths.get(0), schema);


        for (int i = 1, inputPathsSize = inputPaths.size(); i < inputPathsSize; i++) {
            String inputPath = inputPaths.get(i);
            dataset.union(SparkUtils.readCsv(sparkSession, inputPath, schema));
        }


        return dataset;
    }
}
