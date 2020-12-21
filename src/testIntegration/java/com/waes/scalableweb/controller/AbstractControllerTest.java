package com.waes.scalableweb.controller;

import com.waes.scalableweb.service.storage.binary.BinaryStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractControllerTest {

    static final String URL_PREFIX = "/v1/diff";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BinaryStorageService binaryStorageService;

}
