package config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class AwsConfig {
    private AmazonS3 s3;

    public AmazonS3 getS3Client() {
        if (s3 != null) return s3;
        s3 = amazonS3Client();
        return s3;
    }

    private AmazonS3 amazonS3Client() {
        return AmazonS3ClientBuilder
                .standard()
                .withRegion("us-east-1")
                .build();
    }
}
