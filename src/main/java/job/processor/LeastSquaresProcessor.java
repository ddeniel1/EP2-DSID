package job.processor;

import DTO.GlobalSummary;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

public class LeastSquaresProcessor implements Processor<Dataset<GlobalSummary>, LeastSquares> {
    /*
    Esse Processos realiza a regressão por quadrados mínimos requerida no enunciado do EP.
    Como qualquer regressão, esse Processor pretende conseguir achar coeficientes a e b que dada a função
    y = a + b*x, consiga chegar próxima ao resultado real.
    1 - é realizado o filtro de valores iguais 9999.9, pois estes valores representam valores nulos;
    2 - é relaizado o calculo da média das duas colunas
    3 - é calculado o desvio padrão das duas colunas, e as duplicatas desse dataset são jogadas fora
    4 - é realizada a união entre o dataset original, o dataset com médias e o dataset com o desvio padrão
    5 - é feito o calculo do coeficientes a e b
    6 - é calculado o y0 e y1
    7 - é retornado um objeto LeastSquares

    x: Nome da coluna que vai ser o x da questão String
    y: Nome da coluna alvo  String
     */

    private final String x;
    private final String y;

    public LeastSquaresProcessor(String x, String y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public LeastSquares process(Dataset<GlobalSummary> dataset) {
        dataset = dataset.filter(String.format("%s != 9999.9", x));
        dataset = dataset.filter(String.format("%s != 9999.9", y));
        ProcessorUtils pu = new ProcessorUtils();
        String[] aux = new String[]{};
        String[] aux2 = new String[]{x, y};
        Dataset<Row> meanDataset = new MeanProcessor(aux, aux2).process(dataset);
        Double xAvg = meanDataset.first().getDouble(0);
        Double yAvg = meanDataset.first().getDouble(1);

        Dataset<Row> stdDataset = new StandardDeviationProcessor(aux, aux2).process(dataset);
        Double xStd = stdDataset.first().getDouble(0);
        Double yStd = stdDataset.first().getDouble(1);

        Column[] values_col = pu.stringToClass(dataset, aux2);
        Dataset<Row> datasetSelect = dataset.select(values_col);

        StructType structType = new StructType();
        structType = structType.add("up", DataTypes.DoubleType, false);
        structType = structType.add("down", DataTypes.DoubleType, false);
        ExpressionEncoder<Row> encoder = RowEncoder.apply(structType);

        String xVal = this.x;
        String yVal = this.y;
        Dataset<Row> temp = datasetSelect.map((MapFunction<Row, Row>) row -> pu.leastSquaresB(
                row, xVal, yVal, xAvg, yAvg), encoder);

        temp = temp.groupBy().sum();
        Double up = temp.first().getDouble(0);
        Double down = temp.first().getDouble(1);
        Double b = up / down;
        Double a = yAvg - (b * xAvg);

        datasetSelect = datasetSelect.withColumn(y + "_predicted", functions.lit((functions.col(x).multiply(b)).plus(a)));

        Dataset<Row> tempXMax = datasetSelect.groupBy().max();
        Double xMax = tempXMax.first().getDouble(0);
        Dataset<Row> tempXMin = datasetSelect.groupBy().min();
        Double xMin = tempXMin.first().getDouble(0);
        Double y0 = a + (b * xMin);
        Double y1 = a + (b * xMax);
        LeastSquares ls = new LeastSquares(a, b, xMin, xMax, y0, y1, xAvg, yAvg, xStd, yStd, datasetSelect, datasetSelect.describe());

        return ls;
    }
}

