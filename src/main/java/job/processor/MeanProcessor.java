package job.processor;

import DTO.GlobalSummary;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;

import static org.apache.spark.sql.expressions.javalang.typed.avg;


public class MeanProcessor implements Processor<Dataset<GlobalSummary>, Dataset<GlobalSummary>>{
    public MeanProcessor(){

    }

    @Override
    public Dataset<GlobalSummary> process(Dataset<GlobalSummary> dataset) {
        dataset.show(100);
        System.out.println("Esquema" + dataset.schema());
        Dataset<GlobalSummary> teste =   dataset.groupBy("TEMP").mean("TEMP_ATTRIBUTES").as(Encoders.bean(GlobalSummary.class));
        System.out.println("*******************");
        teste.show(100);


        return teste;
    }
}
