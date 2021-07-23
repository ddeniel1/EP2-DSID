package job.processor;

import DTO.GlobalSummary;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class CountProcessor implements Processor<Dataset<GlobalSummary>, Dataset<Row>> {
    private final String[] dimensions;

    public CountProcessor(String[] dimensions){
        this.dimensions = dimensions;
    }
    @Override
    public Dataset<Row> process(Dataset<GlobalSummary> dataset) {
        Column[] dimensions_col = new ProcessorUtils().stringToClass(dataset, dimensions);

        for (int i=0; i < this.dimensions.length; i++){
            dimensions_col[i] = dataset.col(this.dimensions[i]);
        }
        return dataset.
                groupBy(dimensions_col).
                count();
    }
}
