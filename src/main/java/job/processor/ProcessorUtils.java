package job.processor;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import scala.collection.JavaConverters;
import scala.collection.Seq;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.spark.sql.types.DataTypes.*;

public class ProcessorUtils implements Serializable {
    public Column[] stringToClass(Dataset<?> dataset, String[] list){
        Column[] cols = new Column[list.length];

        for (int i=0; i < list.length; i++){
            cols[i] = dataset.col(list[i]);
        }
        return cols;
    }

    public Seq<String> convertListToSeq(String[] inputList) {
        List<String> targetList = Arrays.asList(inputList);

        return JavaConverters.asScalaIteratorConverter(targetList.iterator()).asScala().toSeq();
    }

//    public Double standardDeviation(Row row) {
//
//        Double val = row.<Double>getAs("TEMP");
//        Double media = row.<Double>getAs("avg(TEMP)");
//        Long n = row.<Long>getAs("count");
//
//        Double resul = Math.pow(val - media, 2) / n;
//
//        return resul;
//    }

    public Row standardDeviation(Row row, String val_str) {
        Double val = row.<Double>getAs(val_str);
        Double media = row.<Double>getAs(String.format("avg(%s)",val_str));
        Long n = row.<Long>getAs("count");
        Long id = row.getAs("id");
        Double std = Math.pow(val - media, 2) / n;
        Row resul = RowFactory.create(id, val, std);

        return resul;
    }

    public static DataType dataType(Dataset<Row> dataset, String colName) {
        StructField[] fields = dataset.schema().fields();
        DataType dataType = null;
        for(StructField field: fields) {
            if(field.name().equals(colName)) {
                dataType =  field.dataType();
                break;
            }
        }
        return dataType;
    }
}
