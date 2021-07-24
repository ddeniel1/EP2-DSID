package job.processor;

import DTO.GlobalSummary;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import javax.xml.crypto.Data;

public class LeastSquaresProcessor implements Processor<Dataset<GlobalSummary>, LeastSquares> {

    private String x;
    private String y;

    public LeastSquaresProcessor(String x, String y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public LeastSquares process(Dataset<GlobalSummary> dataset) {
        String[] aux = new String[]{};
        String[] aux2 = new String[]{x,y};
        dataset = dataset.filter(String.format("%s != 9999.9",x));
        dataset = dataset.filter(String.format("%s != 9999.9",y));
        Dataset<Row> meanDataset = new MeanProcessor(aux, aux2).process(dataset);
        Dataset<Row> stdDataset = new StandardDeviationProcessor(aux, aux2).process(dataset);
        stdDataset = stdDataset.dropDuplicates();
        ProcessorUtils pu = new ProcessorUtils();
        Column[] values_col = pu.stringToClass(dataset, aux2);
        Dataset<Row> datasetSelect = dataset.select(values_col);
        datasetSelect = datasetSelect.join(stdDataset, pu.convertListToSeq(aux2));
        datasetSelect = datasetSelect.crossJoin(meanDataset);
        StructType structType = new StructType();
        structType = structType.add("up", DataTypes.DoubleType, false);
        structType = structType.add("down", DataTypes.DoubleType, false);
        ExpressionEncoder<Row> encoder = RowEncoder.apply(structType);
        String xVal = this.x;
        String yVal = this.y;
        Dataset<Row> temp = datasetSelect.map((MapFunction<Row, Row>) row -> pu.leastSquaresB(
                row, xVal, yVal),encoder);
        temp = temp.groupBy().sum();
        Row linha = temp.first();
        Double up = linha.getDouble(0);
        Double down = linha.getDouble(1);
        Double b = up/down;
        Row medias = meanDataset.first();
        Double xAvg = medias.getDouble(0);
        Double yAvg = medias.getDouble(1);
        Double a = yAvg - (b*xAvg);

        Dataset<Row> tempXMax = datasetSelect.groupBy().max();
        Double xMax = tempXMax.first().getDouble(0);
        Dataset<Row> tempXMin = datasetSelect.groupBy().min();
        Double xMin = tempXMin.first().getDouble(0);
        Double y0 = a + (b*xMin);
        Double y1 = a + (b*xMax);
        String[] columnsFinal = datasetSelect.columns();
        String[] columnOrder = new String[]{columnsFinal[0], columnsFinal[2], columnsFinal[4],
                columnsFinal[1], columnsFinal[3], columnsFinal[5]};
        Column[] colsOrder = pu.stringToClass(datasetSelect, columnOrder);
        LeastSquares ls = new LeastSquares(a, b, xMax, xMin, y0, y1, datasetSelect.select(colsOrder));
        return  ls;
    }
}

