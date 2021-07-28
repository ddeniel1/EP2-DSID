package job.writer;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class PrintWriter implements Writer<Dataset<Row>>{
    @Override
    public void write(Dataset<Row> input) throws Exception {
        input.show(false);
    }
}
