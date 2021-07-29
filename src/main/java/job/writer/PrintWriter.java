package job.writer;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import scala.Option;
import scala.Serializable;
import scala.collection.JavaConverters;
import scala.collection.Seq;
import scala.collection.immutable.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.Arrays;

public class PrintWriter implements Writer<Dataset<Row>>, Serializable {
    @Override
    public void write(Dataset<Row> input) throws Exception {

        input = input.cache();

        DefaultTableModel tableModel = new DefaultTableModel();

        String[] columns = input.columns();
        Seq<String> seq = JavaConverters.asScalaBufferConverter(Arrays.asList(columns)).asScala().toSeq();

        for (String column : columns) {
            tableModel.addColumn(column);
        }

        input.collectAsList().forEach(row -> {
            Object[] objects = addToVector(row.getValuesMap(seq), columns);
            tableModel.addRow(objects);
        });

        JFrame frame = new JFrame();
        frame.setSize(700,300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(new JScrollPane(new JTable(tableModel)));
        frame.setVisible(true);
    }

    private Object[] addToVector(Map<String, Object> valuesMap, String[] columns) {
        Object[] objects = new Object[columns.length];
        for (int i = 0; i < columns.length; i++) {
            String column = columns[i];
            Option<Object> option = valuesMap.get(column);
            objects[i]= option.get();
        }
        return objects;
    }
}
