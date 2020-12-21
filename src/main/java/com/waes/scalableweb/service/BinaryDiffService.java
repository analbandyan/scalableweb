package com.waes.scalableweb.service;

import com.waes.scalableweb.dto.DiffsDto;
import org.springframework.web.multipart.MultipartFile;

public interface BinaryDiffService {

    DiffsDto binaryDiff(String id, MultipartFile left, MultipartFile right);

    DiffsDto binaryDiffAsync(String id, MultipartFile left, MultipartFile right);

    DiffsDto getBinaryDiffStatus(String id);
}
