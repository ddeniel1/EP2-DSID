package job.processor;

import DTO.GlobalSummary;
import org.apache.commons.lang.ArrayUtils;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import java.io.Serializable;
import java.util.Arrays;

public class StandardDeviationProcessor implements Processor<Dataset<GlobalSummary>, Dataset<Row>> {
    private String[] dimensions;
    private String[] values;

    public StandardDeviationProcessor(String[] dimensions, String[] values) {
        this.dimensions = dimensions;
        this.values = values;
    }

    @Override
    public Dataset<Row> process(Dataset<GlobalSummary> dataset) {
        Dataset<Row> meanDataset = new MeanProcessor(this.dimensions, this.values).process(dataset).alias("mean");
//        System.out.println("meanDataset");
//        meanDataset.show(20);

        Dataset<Row> countDataset = new CountProcessor(this.dimensions).process(dataset).alias("count");
//        System.out.println("countDataset");
//        countDataset.show(20);

        ProcessorUtils pu = new ProcessorUtils();
        Column[] values_col = pu.stringToClass(dataset, values);
        Column[] dimensions_col = pu.stringToClass(dataset, dimensions);
        Column[] columns = (Column[]) ArrayUtils.addAll(dimensions_col, values_col);
        Dataset<Row> datasetSelect = dataset.select(columns).alias("sel");
        datasetSelect = datasetSelect.withColumn("id", functions.monotonicallyIncreasingId());
//        System.out.println("datasetSelect");
//        datasetSelect.show(20);

        Dataset<Row> datasetAll = datasetSelect.join(meanDataset, pu.convertListToSeq(dimensions)).join(countDataset, pu.convertListToSeq(dimensions));
//        System.out.println("datasetAll");
//        datasetAll.show(20);

        String[] calculatedColumns = new String[values.length*2];
        int i = 0;
        for(String val: values){
            calculatedColumns[i] = val;
            i++;
            calculatedColumns[i] = String.format("std(%s)",val);
            i++;
            StructType structType = new StructType();
            structType = structType.add("id", DataTypes.LongType, false);
            structType = structType.add(val, pu.dataType(datasetSelect, val), false);
            structType = structType.add(String.format("std(%s)",val), DataTypes.DoubleType, false);
            ExpressionEncoder<Row> encoder = RowEncoder.apply(structType);
            Dataset<Row> newDs = datasetAll.map((MapFunction<Row, Row>) row -> pu.standardDeviation(row, val),encoder);
            String[] aux = new String[]{"id", val};
            datasetSelect = datasetSelect.join(newDs, pu.convertListToSeq(aux));
        }

        Column[] calculatedColumnsCol = pu.stringToClass(datasetSelect, calculatedColumns);
        Column[] finalColumns = (Column[])  ArrayUtils.addAll(dimensions_col, calculatedColumnsCol);
        datasetSelect = datasetSelect.select(finalColumns);


        return datasetSelect;

    }
}
