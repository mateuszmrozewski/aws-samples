package test.aws;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Example of storing a file in S3 using AWS SDK v2 and
 * customer provided encryption keys.
 */
public class SSEC {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // Generate a random 256 bit AES key and encode it in Base64
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256, new SecureRandom());
        SecretKey secretKey = keyGenerator.generateKey();
        String secretKeyString = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        // Generate md5 digest of the key and encode it in Base64
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(secretKey.getEncoded());
        byte[] digest = md5.digest();
        String md5String = Base64.getEncoder().encodeToString(digest);

        S3Client client = S3Client.builder().region(Region.AP_SOUTHEAST_2).build();


        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket("my-encrypted-bucket-ssec")
                .key("my-file.png")
                .sseCustomerAlgorithm("AES256") // only AES256 supported
                .sseCustomerKey(secretKeyString) // pass the key
                .sseCustomerKeyMD5(md5String) // and md5 of the key
                .build();
        client.putObject(putRequest, Paths.get("my-file.png"));

        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket("my-encrypted-bucket-ssec")
                .key("my-file.png")
                .sseCustomerAlgorithm("AES256")
                .sseCustomerKey(secretKeyString)
                .sseCustomerKeyMD5(md5String)
                .build();
        client.getObject(getRequest, Paths.get("my-file-copy.png"));
    }
}
