package assembler;

import buttons.MainFrame;
import job.Job;
import job.JobExecutor;
import job.processor.CountProcessor;
import job.processor.DateProcessor;
import job.processor.LeastSquaresProcessor;
import job.processor.MeanProcessor;
import job.processor.Processor;
import job.processor.StandardDeviationProcessor;
import job.reader.MultipleDatasetReader;
import job.writer.PlotGraphWriter;
import job.writer.PrintWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DatasetUtils;
import util.DateUtils;
import util.FileUtil;
import util.IntegrityCheckConst;
import util.SparkUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataAssembler extends Thread {
    public static final int MAX_YEAR = 2016;
    private static final Logger LOGGER = LoggerFactory.getLogger(DataAssembler.class);

    private static int applyAsInt(Path path) {
        String[] splittedPath = path.toString().split("/");
        return Integer.parseInt(splittedPath[splittedPath.length - 1]);
    }

    public void run() {
        LOGGER.info("Thread running");
    }

    public void countProcessData(List<Integer> years, String[] dimensions) {
        Job job = new JobExecutor<>(new MultipleDatasetReader(SparkUtils.buildSparkSession(), years, DatasetUtils.schema),
                Processor.chainProcess(new DateProcessor(DateUtils.getDate()), new CountProcessor(dimensions)),
                new PrintWriter());
        job.execute();
    }

    public void leastSquaresProcess(List<Integer> years, String x, String y) {
        Job job = new JobExecutor<>(new MultipleDatasetReader(SparkUtils.buildSparkSession(), years, DatasetUtils.schema),
                new LeastSquaresProcessor(x, y),
                new PlotGraphWriter(x, y));
        job.execute();
    }

    public void meanProcess(List<Integer> years, String[] dimensions, String[] values) {
        Job job = new JobExecutor<>(new MultipleDatasetReader(SparkUtils.buildSparkSession(), years, DatasetUtils.schema),
                Processor.chainProcess(new DateProcessor(DateUtils.getDate()), new MeanProcessor(dimensions, values)),
                new PrintWriter());
        job.execute();
    }

    public void standardDeviationProcess(List<Integer> years, String[] dimensions, String[] values) {
        Job job = new JobExecutor<>(new MultipleDatasetReader(SparkUtils.buildSparkSession(), years, DatasetUtils.schema),
                Processor.chainProcess(new DateProcessor(DateUtils.getDate()), new StandardDeviationProcessor(dimensions, values)),
                new PrintWriter());
        job.execute();
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

    public void unzipAndCompileFiles(List<Integer> years) {
        years.forEach(year -> {
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

    public void downloadFiles(List<Integer> yearsToDownload) {
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
        MainFrame.createConfirmationDialog("Download finalizado com sucesso!");

    }

    private boolean checkIntegrity(Integer year) {
        String fileName = FileUtil.GSOD_FILES + year + FileUtil.SEPARATOR + year + ".tar.gz";
        try {
            return Files.size(Paths.get(fileName)) == IntegrityCheckConst.SIZE_MAP.get(year);
        } catch (IOException e) {
            LOGGER.error("Não foi possivel verificar a integridade do arquivo {}, talvez precise fazer o download do ano {}", fileName, year);
        }
        return false;
    }

    public List<Integer> checkFiles() {
        List<Integer> allYears = new ArrayList<>();

        for (int i = 1929; i <= MAX_YEAR; i++) {
            allYears.add(i);
        }


        List<Integer> fileFolderYears = new ArrayList<>();
        try {

            List<Path> pathList = Files.list(Paths.get(FileUtil.GSOD_FILES)).collect(Collectors.toList());

            pathList.forEach(path -> fileFolderYears.add(applyAsInt(path)));

        } catch (IOException e) {
            LOGGER.info("Main directory does not exist, creating directory");
            new File(FileUtil.GSOD_FILES).mkdirs();
        } finally {
            LOGGER.info("{} existing directories:", fileFolderYears.size());
            allYears.removeAll(fileFolderYears);
            allYears.forEach(this::checkIntegrity);
            return allYears;
        }
    }
}
