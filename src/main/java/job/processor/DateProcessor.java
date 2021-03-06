package job.processor;

import DTO.GlobalSummary;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.functions;

public class DateProcessor implements Processor<Dataset<GlobalSummary>, Dataset<GlobalSummary>> {
    /*
    Esse processor trunca a data:
    trunc: String - year trunca por ano, month trunca por mês, week trunca por semana, quarter trunca por trimestre
     */
    private final String trunc;

    public DateProcessor(String trunc) {
        this.trunc = trunc;
    }

    @Override
    public Dataset<GlobalSummary> process(Dataset<GlobalSummary> dataset) {

        Column truncate = functions.date_trunc(trunc, functions.col("DATE"));
        return dataset.withColumn("DATE", truncate).as(Encoders.bean(GlobalSummary.class));
    }
}
