package job.reader;

import DTO.GlobalSummary;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;
import util.FileUtil;
import util.SparkUtils;

import java.util.List;

public class MultipleDatasetReader implements Reader<Dataset<GlobalSummary>> {

    private final SparkSession sparkSession;
    private final List<Integer> inputPath;
    private final StructType schema;

    public MultipleDatasetReader(SparkSession sparkSession, List<Integer> inputPath, StructType schema) {
        this.sparkSession = sparkSession;
        this.inputPath = inputPath;
        this.schema = schema;
    }

    @Override
    public Dataset<GlobalSummary> read() {

        Dataset<Row> dataset = SparkUtils.readCsv(sparkSession, FileUtil.GSOD_FILES + inputPath.get(0) + "*/*.csv", schema);
        for (int i = 1; i < inputPath.size(); i++) {
            dataset = dataset.union(SparkUtils.readCsv(sparkSession, FileUtil.GSOD_FILES + inputPath.get(i) + "*/*.csv", schema));
        }

        return dataset.as(Encoders.bean(GlobalSummary.class));
    }
}
