package job.processor;

import DTO.GlobalSummary;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;


public class MeanProcessor implements Processor<Dataset<GlobalSummary>, Dataset<Row>> {

    /*
    dimensions: Colunas será agrupado por essas colunas, recebe um Array de Columns.
                Caso não seja agrupado por nenhuma coluna, passar um Array vazio.
    values: Colunas a agrupadas, recebe um Array de Strings, sempre é necessário ter pelo menos uma coluna.
     */
    private final String[] dimensions;
    private final String[] values;

    public MeanProcessor(String[] dimensions, String[] values) {
        this.dimensions = dimensions;
        this.values = values;
    }

    @Override
    public Dataset<Row> process(Dataset<GlobalSummary> dataset) {
        Column[] dimensions_col = new ProcessorUtils().stringToClass(dataset, dimensions);

        if (this.dimensions.length == 0) {
            return dataset.
                    groupBy().
                    avg(this.values);
        } else {
            return dataset.
                    groupBy(dimensions_col).
                    avg(this.values);
        }
    }

//    private List<Column> ListToColmn
}
