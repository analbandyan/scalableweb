package com.waes.scalableweb.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

public class MultipartFileHelper {

    public static MockMultipartFile getMockMultipartFile(String name, String fileName) throws IOException {
        return new MockMultipartFile(name, fileName, "text/plain",
                new ClassPathResource("data/" + fileName).getInputStream());
    }

}
