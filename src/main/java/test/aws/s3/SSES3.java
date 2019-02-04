package test.aws.s3;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.ServerSideEncryption;

import java.nio.file.Paths;

/**
 * Example of storing file in a bucket with SSE-S3.
 * If it is enabled by default on the bucket no extra actions are needed.
 */
public class SSES3 {
    public static void main(String[] args) {
        S3Client client = S3Client.builder().region(Region.AP_SOUTHEAST_2).build();

        PutObjectRequest putRequestEncrypted = PutObjectRequest.builder()
                .bucket("myencrypted-bucket-sse-s3")
                .key("my-file.png")
                .build();
        client.putObject(putRequestEncrypted, Paths.get("my-file.png"));

        PutObjectRequest putRequestUnencrytped = PutObjectRequest.builder()
                .bucket("myunencrypted-bucket")
                .key("my-file.png")
                .serverSideEncryption(ServerSideEncryption.AES256)
                .build();
        client.putObject(putRequestUnencrytped, Paths.get("my-file.png"));
    }
}
