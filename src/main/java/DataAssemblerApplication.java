import DTO.GlobalSummary;
import job.processor.LeastSquares;
import job.processor.LeastSquaresProcessor;
import job.processor.MeanProcessor;
import job.processor.StandardDeviationProcessor;
import job.reader.DatasetReader;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DatasetUtils;
import util.FileUtil;
import util.IntegrityCheckConst;
import util.SparkUtils;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataAssemblerApplication {
    public static final int MAX_YEAR = 2016;
    private static final Logger LOGGER = LoggerFactory.getLogger(DataAssemblerApplication.class);

    public static void main(String[] args) {
        org.apache.log4j.Logger.getLogger("org").setLevel(Level.ERROR);
        new DataAssemblerApplication().run(args);
    }

    private static int applyAsInt(Path path) {
        String[] splittedPath = path.toString().split("/");
        return Integer.parseInt(splittedPath[splittedPath.length - 1]);
    }

    public void run(String[] args) {

        BasicConfigurator.configure();

        LOGGER.info("Checando arquivos");

        List<Integer> yearsToDownload = checkFiles();

        LOGGER.info("Iniciando o download de {} arquivos", yearsToDownload.size());

        downloadFiles(yearsToDownload);

        LOGGER.info("Unzipping files");

        unzipAndCompileFiles();


        LOGGER.info("Initializing spark");

        String yearRegex = "1999";
        Dataset<GlobalSummary> read = new DatasetReader(SparkUtils.buildSparkSession(), FileUtil.GSOD_FILES + yearRegex + "*/*.csv", DatasetUtils.schema).read();

        System.out.println("Esquema" + read.schema());
        read.show(20);
//        read.select(read.col("*")).where(read.col("DATE").like("2001-05-18")).show(false);
//        System.out.println("Teste");
//        read.select(read.col("*")).filter("NAME is not NULL").orderBy("NAME").show(20);


//        String[]  dimensions = new String[]{};
//        String[] values = new String[]{"TEMP", "DEWP"};
//        Dataset<Row> meanDataset = new MeanProcessor(dimensions, values).process(read);
//        meanDataset.show(20);
//        Dataset<Row> standardDeviantionDataset = new StandardDeviationProcessor(dimensions, values).process(read);
//        standardDeviantionDataset.show(20);

        String x = "TEMP";
        String y = "DEWP";
        LeastSquares ls = new LeastSquaresProcessor(x, y).process(read);
        System.out.println(ls.toString());
    }

    private void unzipAndCompileFiles() {
        List<Integer> years = new ArrayList<>();

        for (int i = 1929; i <= MAX_YEAR; i++) {
            years.add(i);
        }

        years.parallelStream().forEach(year -> {
            if (!containsCsvFile(year)) {
                FileUtil.unzipToStringList(year);
            }

        });
    }

    private boolean containsCsvFile(Integer year) {
        boolean result = false;
        try {
            List<Path> list = Files.list(Paths.get(FileUtil.GSOD_FILES + year)).collect(Collectors.toList());
            for (Path path : list) {
                result = result || path.toString().contains(".csv");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result) {
            LOGGER.info("Skipping file {}", year + ".csv");
        }
        return result;
    }

    private void downloadFiles(List<Integer> yearsToDownload) {
        for (Integer year : yearsToDownload) {
            new File(FileUtil.GSOD_FILES + year).mkdirs();
            LOGGER.info("Download do ano {}", year);
            boolean downloadZippedData = FileUtil.downloadZippedData(year);
            if (downloadZippedData) {
                LOGGER.info("Download do arquivo {} completo, checando integridade", year);
                boolean integrity = checkIntegrity(year);
                if (integrity) LOGGER.info("Integridade do arquivo {} verificada", year);
                else
                    LOGGER.error("Integridade do arquivo {} falhou, exclua a pasta e execute o programa novamente", year, new RuntimeException("Arquivo Corrompido"));
            } else LOGGER.error("Falha no download do arquivo {}", year);
        }
    }

    private boolean checkIntegrity(Integer year) {
        String fileName = FileUtil.GSOD_FILES + year + FileUtil.SEPARATOR + year + ".tar.gz";
        try {
            return Files.size(Paths.get(fileName)) == IntegrityCheckConst.SIZE_MAP.get(year);
        } catch (IOException e) {
            LOGGER.error("Não foi possivel verificar a integridade do arquivo {}", fileName);
        }
        return false;
    }

    private List<Integer> checkFiles() {
        List<Integer> years = new ArrayList<>();

        for (int i = 1929; i <= MAX_YEAR; i++) {
            years.add(i);
        }

        Stream<Path> list;
        List<Integer> yearsList = new ArrayList<>();
        try {
            list = Files.list(Paths.get(FileUtil.GSOD_FILES));

            List<Path> listList = list.collect(Collectors.toList());

            listList.forEach(path -> {
                yearsList.add(applyAsInt(path));
            });

        } catch (IOException e) {
            LOGGER.info("Directory does not exist, creating directory");
            new File(FileUtil.GSOD_FILES).mkdirs();
        } finally {
            LOGGER.info("{} existing directories:", yearsList.size());
            years.removeAll(yearsList);
            return years;
        }
    }
}
