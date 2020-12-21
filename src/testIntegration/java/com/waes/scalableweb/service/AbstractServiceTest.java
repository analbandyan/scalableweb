package com.waes.scalableweb.service;

import com.waes.scalableweb.service.storage.binary.BinaryStorageService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class AbstractServiceTest {

    @MockBean
    BinaryStorageService binaryStorageService;

}
