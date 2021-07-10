package util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import config.AwsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class AmazonS3Util {
    private static final Logger LOGGER = LoggerFactory.getLogger(AmazonS3Util.class);

    private final AmazonS3 amazonS3Client;

    public AmazonS3Util() {
        this.amazonS3Client = new AwsConfig().getS3Client();
    }

    public boolean downloadToTempFolder(String path) {

        InputStream is = null;
        FileOutputStream fos = null;


        String filePath = path.split("-")[2] + "/" + path.split("-")[0] + path.split("-")[1] + ".csv";

        try {

            URL url = new URL("https://www.ncei.noaa.gov/data/global-summary-of-the-day/access/" + filePath);

            URLConnection urlConn = url.openConnection();


            System.out.print(".");
            is = urlConn.getInputStream();


            String tempDir = "./gsod-files/";
            String outputPath = tempDir + "/" + filePath;
            fos = new FileOutputStream(outputPath);

            byte[] buffer = new byte[8388608];
            int length;
            while ((length = is.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        } catch (IOException e) {
            System.out.print("x");
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

    public String getIndexFile(String bucketName, String prefix) {
        LOGGER.info("Getting Directory Content from bucket {}", bucketName);

        List<String> filesPath = new ArrayList<>();

        List<S3ObjectSummary> s3ObjectSummaries = amazonS3Client.listObjects(bucketName, prefix).getObjectSummaries();

        s3ObjectSummaries.forEach(s3ObjectSummary -> addToList(s3ObjectSummary, filesPath, prefix + "/"));

        return filesPath.get(0);

    }

    private void addToList(S3ObjectSummary s3ObjectSummary, List<String> filesPath, String prefix) {
        if (s3ObjectSummary.getKey().equals(prefix)) return;
        String path = s3ObjectSummary.getBucketName() + "/" + s3ObjectSummary.getKey();
        filesPath.add(path);
    }

    public List<String> getPaths(String pathFile) throws IOException {
        S3Object s3Object = amazonS3Client.getObject(pathFile.split("/")[0], pathFile.split("/")[1]);

        List<String> paths = new ArrayList<>();
        InputStreamReader inputStreamReader = new InputStreamReader(s3Object.getObjectContent().getDelegateStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader, 8388608);
        bufferedReader.readLine();
        while (bufferedReader.ready() && paths.size() < 40) {
            String line = bufferedReader.readLine();
            if (line == null || line.trim().isEmpty()) break;
            paths.add(line.split(",")[0]);
            if (paths.size() % 5000 == 0) {
                System.out.print("Indexando arquivos: ");
                System.out.println(paths.size() + "/491571 arquivos indexados");
            }
        }
        System.out.println("Indexação Completa, iniciando download");
        return paths;
    }
}
