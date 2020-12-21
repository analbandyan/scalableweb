package com.waes.scalableweb.service.storage.binary;

import com.waes.scalableweb.service.StorageBucket;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

public interface BinaryStorageService {

    default CompletableFuture<Void> uploadBoth(String id, MultipartFile left, MultipartFile right) {

        CompletableFuture<Void> uploadFutureLeft = uploadOne(StorageBucket.LEFT, id, left);
        CompletableFuture<Void> uploadFutureRight = uploadOne(StorageBucket.RIGHT, id, right);

        return CompletableFuture.allOf(uploadFutureLeft, uploadFutureRight);
    }

    CompletableFuture<Void> uploadOne(StorageBucket bucket, String id, MultipartFile binary);

    InputStream getDataInputStream(StorageBucket bucket, String id);

}
