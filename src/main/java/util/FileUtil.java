package util;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileUtil {
    public static final String ARCHIVE = "https://www.ncei.noaa.gov/data/global-summary-of-the-day/archive/";
    public static final String GSOD_FILES = "../gsod-files/";
    public static final String FILE_HEADER = "STATION,DATE,LATITUDE,LONGITUDE,ELEVATION,NAME,TEMP,TEMP_ATTRIBUTES,DEWP,DEWP_ATTRIBUTES,SLP,SLP_ATTRIBUTES,STP,STP_ATTRIBUTES,VISIB,VISIB_ATTRIBUTES,WDSP,WDSP_ATTRIBUTES,MXSPD,GUST,MAX,MAX_ATTRIBUTES,MIN,MIN_ATTRIBUTES,PRCP,PRCP_ATTRIBUTES,SNDP,FRSHTT";
    public static final String SEPARATOR = "/";
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    public static boolean downloadZippedData(int year) {

        InputStream is = null;
        FileOutputStream fos = null;


        String filePath = year + ".tar.gz";

        try {

            URL url = new URL(ARCHIVE + filePath);

            URLConnection urlConn = url.openConnection();


            is = urlConn.getInputStream();


            String outputPath = GSOD_FILES + year + SEPARATOR + filePath;
            LOGGER.info(outputPath);
            fos = new FileOutputStream(outputPath);

            byte[] buffer = new byte[8388608];
            int length;
            while ((length = is.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }


    public static void unzipToStringList(Integer year) {

        LOGGER.info("Unzipping file from year {}", year);

        try (InputStream fileInputStream = Files.newInputStream(Paths.get(GSOD_FILES + year + SEPARATOR + year + ".tar.gz"));
             InputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
             InputStream gzipCompressorInputStream = new GzipCompressorInputStream(bufferedInputStream);
             TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(gzipCompressorInputStream)) {

            TarArchiveEntry entry;
            PrintWriter pw = null;

            while ((entry = tarArchiveInputStream.getNextTarEntry()) != null) {

                InputStreamReader inputStreamReader = new InputStreamReader(tarArchiveInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();

                while (bufferedReader.ready()) {
                    String readLine = bufferedReader.readLine();
                    if (!readLine.equals(""))
                        stringBuilder.append(readLine).append("\n");
                }

                pw = new PrintWriter(GSOD_FILES + year + SEPARATOR + entry.getName());
                stringBuilder = new StringBuilder(stringBuilder.toString().replaceAll("\",", ";").replaceAll("\"", "").replaceAll("\n\n", "\n").trim());

                pw.println(stringBuilder);
                pw.flush();
                pw.close();

            }


        } catch (IOException e) {
            LOGGER.error("File not found, Causa provável: Arquivo corrompido, exclua a rode o programa novamente. Ano problemático: {}", year, e);
            throw new RuntimeException(e);
        }
    }

    public static void addFileByYear(Integer year, List<String> unzip) {

        LOGGER.info("Compiling file {}", year + ".csv");


        File csvOutputFile = new File(GSOD_FILES + year + SEPARATOR + year + ".csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pw.print(FILE_HEADER);
            unzip.forEach(pw::println);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
