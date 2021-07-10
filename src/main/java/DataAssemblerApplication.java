import job.reader.DatasetReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.AmazonS3Util;
import util.DatasetUtils;
import util.SparkUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class DataAssemblerApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataAssemblerApplication.class);

    public static void main(String[] args) throws IOException {
        new DataAssemblerApplication().run(args);
    }

    public void run(String[] args) throws IOException {
        LOGGER.info("Começando Download");
        AmazonS3Util amazonS3Util = new AmazonS3Util();

        String indexFile = amazonS3Util.getIndexFile("aws-gsod", "isd-inventory.csv");

        List<String> paths = amazonS3Util.getPaths(indexFile);
        Stream<Path> list = Files.list(Paths.get("./gsod-files"));
        int size = paths.size();
        System.out.println(size + " Arquivos antes");
        list.forEach(path -> {
            try {
                Files.list(path).forEach(path1 -> {
//                    System.out.println(path1.toString());
                    String[] s = path1.toString().split("/");
                    String toRemove = s[s.length - 1].substring(0, 6) + "-" + s[s.length - 1].substring(6, 11) + "-" + s[s.length - 2];
                    paths.remove(toRemove);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        System.out.println(paths.size() + " Arquivos depois");


        AtomicInteger i = new AtomicInteger(size - paths.size());
        List<String> pathsToRemove = new ArrayList<>();
        paths.parallelStream().forEach(path -> {
            new File("./gsod-files/" + path.split("-")[2] + "/").mkdirs();
            boolean download = amazonS3Util.downloadToTempFolder(path);
            int j = i.get();
            if (download) j = i.getAndIncrement();
            else pathsToRemove.add(path);
            if (j % 100 == 0) {
                System.out.println(j + " arquivos baixados");
            }
        });

        paths.removeAll(pathsToRemove);

        Stream<Path> pathStream = Files.list(Paths.get("./gsod-files"));

        List<String> pathStringList = new ArrayList<>();

        AtomicInteger j = new AtomicInteger();

        pathStream.forEach(path -> {
            try {
                Files.list(path).forEach(path1 -> {
                    if (j.get()>400) return;
                    pathStringList.add(path1.toString());
                    j.getAndIncrement();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        });


        Dataset<Row> read = new DatasetReader(SparkUtils.buildSparkSession(), pathStringList, DatasetUtils.INPUT_SCHEMA).read();

        read.show(100);
    }
}
