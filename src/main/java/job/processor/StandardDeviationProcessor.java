package job.processor;

import DTO.GlobalSummary;
import org.apache.commons.lang.ArrayUtils;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

public class StandardDeviationProcessor implements Processor<Dataset<GlobalSummary>, Dataset<Row>> {
    /*
    dimensions: Colunas será agrupado por essas colunas, recebe um Array de Columns.
                Caso não seja agrupado por nenhuma coluna, passar um Array vazio.
    values: Colunas a agrupadas, recebe um Array de Strings, sempre é necessário ter pelo menos uma coluna.
     */

    private String[] dimensions;
    private String[] values;

    public StandardDeviationProcessor(String[] dimensions, String[] values) {
        this.dimensions = dimensions;
        this.values = values;
    }

    @Override
    public Dataset<Row> process(Dataset<GlobalSummary> dataset) {

        ProcessorUtils pu = new ProcessorUtils();
        Column[] dimensions_col = pu.stringToClass(dataset,dimensions);

        Dataset<Row> datasetResult = null;

        for (String val: values){
            Dataset<Row> temp;
            if (this.dimensions.length != 0) {
                temp = dataset.groupBy(dimensions_col).agg(functions.stddev(val));
                if(datasetResult == null){
                    datasetResult = temp;
                }
                else {
                    datasetResult = datasetResult.join(temp, pu.convertListToSeq(dimensions));
                }
            }
            else {
                temp = dataset.groupBy().agg(functions.stddev(val));
                if(datasetResult == null){
                    datasetResult = temp;
                }
                else {
                    datasetResult = datasetResult.crossJoin(temp);
                }
            }
        }
        return datasetResult;

    }
}
