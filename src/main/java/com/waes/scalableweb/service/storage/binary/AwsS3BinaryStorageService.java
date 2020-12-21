package com.waes.scalableweb.service.storage.binary;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.waes.scalableweb.service.StorageBucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class AwsS3BinaryStorageService implements BinaryStorageService {

    private final String appId;
    private final AmazonS3 amazonS3;
    private final TransferManager transferManager;

    public AwsS3BinaryStorageService(@Value("${app.id}") String appId, AmazonS3 amazonS3, TransferManager transferManager) {
        this.appId = appId;
        this.amazonS3 = amazonS3;
        this.transferManager = transferManager;
    }

    @Override
    public CompletableFuture<Void> uploadOne(StorageBucket bucket, String id, MultipartFile binary) {
        Upload asyncUploadResult = uploadAsync(bucket, id, binary);

        return CompletableFuture.supplyAsync(() -> waitForCompletion(asyncUploadResult));
    }

    private Upload uploadAsync(StorageBucket bucket, String id, MultipartFile binary) {
        makeSureBucketExists(bucket);
        return doUploadAsync(bucket, id, binary);
    }

    private Upload doUploadAsync(StorageBucket bucket, String id, MultipartFile binary) {
        try {
            long binarySize = binary.getSize();
            InputStream binaryInputStream = binary.getInputStream();

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(binarySize);

            return transferManager.upload(getBucketUniqueName(bucket), id, binaryInputStream, objectMetadata);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private synchronized void makeSureBucketExists(StorageBucket bucket) {
        final String bucketName = getBucketUniqueName(bucket);
        log.debug("Check if bucket exists: {}", bucketName);
        boolean doesBucketExist = amazonS3.doesBucketExistV2(bucketName);
        if (!doesBucketExist) {
            log.debug("Bucket does not exists, create one: {}", bucketName);
            amazonS3.createBucket(bucketName);
        }
    }

    private static Void waitForCompletion(Upload upload) {
        try {
            upload.waitForCompletion();
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public InputStream getDataInputStream(StorageBucket bucket, String id) {
        return getData(bucket, id).getObjectContent();
    }

    private S3Object getData(StorageBucket bucket, String id) {
        String bucketUniqueName = getBucketUniqueName(bucket);
        return amazonS3.getObject(new GetObjectRequest(bucketUniqueName, id));
    }

    private String getBucketUniqueName(StorageBucket bucket) {
        return bucket.name().toLowerCase() + "-" + appId;
    }
}
