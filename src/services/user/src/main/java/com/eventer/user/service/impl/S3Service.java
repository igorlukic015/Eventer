package com.eventer.user.service.impl;

import com.eventer.user.service.ImageHostService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.*;

@Service
public class S3Service implements ImageHostService {
    @Value("${image.host.url}")
    private String imageHostUrl;

    @Value("${s3.bucket.name}")
    private String bucketName;

    @Value("${s3.bucket.region}")
    private String region;

    @Value("${s3.access-key}")
    private String accessKey;

    @Value("${s3.secret-access-key}")
    private String secretAccessKey;

    private S3Client client;

    public Set<String> saveAllImages(List<MultipartFile> images) {
        Set<String> savedImages = new HashSet<>();

        for (MultipartFile image : images) {
            if (!StringUtils.hasText(image.getOriginalFilename())) {
                continue;
            }

            String[] fileNameTokens =
                    StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()))
                            .split("\\.");

            if (this.checkIfExists(image.getOriginalFilename())) {
                savedImages.add(image.getOriginalFilename());
                continue;
            }

            String extension =
                    fileNameTokens.length > 0 ? fileNameTokens[fileNameTokens.length - 1] : ".jpg";

            String fileName =
                    String.format(
                            "%s_%s.%s",
                            UUID.randomUUID(), Instant.now().getEpochSecond(), extension);

            try {
                PutObjectRequest objectRequest =
                        PutObjectRequest.builder().bucket(this.bucketName).key(fileName).build();

                PutObjectResponse response =
                        this.getClient()
                                .putObject(
                                        objectRequest,
                                        RequestBody.fromFile(convertMultiPartToFile(image)));

                if (response.sdkHttpResponse().isSuccessful()) {
                    savedImages.add(fileName);
                }

            } catch (S3Exception | IOException e) {
                throw new RuntimeException(e);
            }
        }

        return savedImages;
    }

    public void deleteAll(Set<String> images) {
        List<ObjectIdentifier> keys =
                images.stream().map(name -> ObjectIdentifier.builder().key(name).build()).toList();

        DeleteObjectsRequest deleteObjectsRequest =
                DeleteObjectsRequest.builder()
                        .bucket(this.bucketName)
                        .delete(Delete.builder().objects(keys).build())
                        .build();

        try {
            this.getClient().deleteObjects(deleteObjectsRequest);
        } catch (S3Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getImageUrl(String imageName) {
        GetUrlRequest getUrlRequest =
                GetUrlRequest.builder().bucket(this.bucketName).key(imageName).build();

        try {
            return this.getClient().utilities().getUrl(getUrlRequest).toExternalForm();
        } catch (S3Exception e) {
            throw new RuntimeException(e);
        }
    }

    private S3Client getClient() {
        if (this.client != null) {
            return this.client;
        }

        AwsBasicCredentials credentials =
                AwsBasicCredentials.create(this.accessKey, this.secretAccessKey);
        StaticCredentialsProvider credentialsProvider =
                StaticCredentialsProvider.create(credentials);

        this.client =
                S3Client.builder()
                        .region(Region.of(this.region))
                        .endpointOverride(URI.create(this.imageHostUrl))
                        .credentialsProvider(credentialsProvider)
                        .build();

        CreateBucketRequest request = CreateBucketRequest.builder().bucket(this.bucketName).build();

        CreateBucketResponse response = this.client.createBucket(request);

        if (!response.sdkHttpResponse().isSuccessful()) {
            throw new RuntimeException("Failed to create bucket");
        }

        return this.client;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));

        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();

        return convFile;
    }

    private boolean checkIfExists(final String filename) {
        try {
            HeadObjectRequest headObjectRequest =
                    HeadObjectRequest.builder().bucket(this.bucketName).key(filename).build();

            HeadObjectResponse response = this.getClient().headObject(headObjectRequest);

            return response.sdkHttpResponse().isSuccessful();
        } catch (NoSuchKeyException e) {
            return false;
        }
    }
}
