package com.waes.scalableweb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waes.scalableweb.dao.DiffRepository;
import com.waes.scalableweb.dto.DiffDto;
import com.waes.scalableweb.dto.DiffsDto;
import com.waes.scalableweb.entity.CalculationStatus;
import com.waes.scalableweb.entity.DiffStatus;
import com.waes.scalableweb.service.StorageBucket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BinaryDiffControllerTest extends AbstractControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    DiffRepository diffRepository;

    @AfterEach
    public void cleanUp() {
        diffRepository.deleteAll();
    }

    @Test
    public void testBinaryDiffSameData() throws Exception {
        InputStream leftInputStream = MultipartFileHelper.getMockMultipartFile("left", "1.txt").getInputStream();
        InputStream rightInputStream = MultipartFileHelper.getMockMultipartFile("right", "1_same.txt").getInputStream();

        given(binaryStorageService.uploadBoth(any(), any(), any())).willReturn(CompletableFuture.runAsync(() -> {
        }));
        given(binaryStorageService.getDataInputStream(StorageBucket.LEFT, "1")).willReturn(leftInputStream);
        given(binaryStorageService.getDataInputStream(StorageBucket.RIGHT, "1")).willReturn(rightInputStream);

        MockMultipartFile mockMultipartLeft = MultipartFileHelper.getMockMultipartFile("left", "1.txt");
        MockMultipartFile mockMultipartRight = MultipartFileHelper.getMockMultipartFile("right", "1_same.txt");

        String resultStr = mockMvc.perform(multipart(URL_PREFIX + "/{id}", 1)
                .file(mockMultipartLeft)
                .file(mockMultipartRight)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        DiffsDto diffsDto = objectMapper.readValue(resultStr, DiffsDto.class);

        assertThat(diffsDto.getCalculationStatus()).isEqualTo(CalculationStatus.DONE.getText());
        assertThat(diffsDto.getDiffStatus()).isEqualTo(DiffStatus.EQUAL.getText());
        assertThat(diffsDto.getDiffs()).isNull();
    }


    @Test
    public void testBinaryDiffDifferentData() throws Exception {
        InputStream leftInputStream = MultipartFileHelper.getMockMultipartFile("left", "1.txt").getInputStream();
        InputStream rightInputStream = MultipartFileHelper.getMockMultipartFile("right", "1_different.txt").getInputStream();

        given(binaryStorageService.uploadBoth(any(), any(), any())).willReturn(CompletableFuture.runAsync(() -> {
        }));
        given(binaryStorageService.getDataInputStream(StorageBucket.LEFT, "1")).willReturn(leftInputStream);
        given(binaryStorageService.getDataInputStream(StorageBucket.RIGHT, "1")).willReturn(rightInputStream);

        MockMultipartFile mockMultipartLeft = MultipartFileHelper.getMockMultipartFile("left", "1.txt");
        MockMultipartFile mockMultipartRight = MultipartFileHelper.getMockMultipartFile("right", "1_different.txt");

        String resultStr = mockMvc.perform(multipart(URL_PREFIX + "/{id}", 1)
                .file(mockMultipartLeft)
                .file(mockMultipartRight)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        DiffsDto diffsDto = objectMapper.readValue(resultStr, DiffsDto.class);

        assertThat(diffsDto.getCalculationStatus()).isEqualTo(CalculationStatus.DONE.getText());
        assertThat(diffsDto.getDiffStatus()).isEqualTo(DiffStatus.DIFFERENT.getText());
        assertThat(diffsDto.getDiffs()).isNotNull();
        assertThat(diffsDto.getDiffs()).hasSize(3);
        assertThat(diffsDto.getDiffs()).contains(new DiffDto(0, 1));
        assertThat(diffsDto.getDiffs()).contains(new DiffDto(5, 2));
        assertThat(diffsDto.getDiffs()).contains(new DiffDto(17, 2));
    }

    @Test
    public void testBinaryDiffDifferentSize() throws Exception {
        given(binaryStorageService.uploadBoth(any(), any(), any())).willReturn(CompletableFuture.runAsync(() -> {
        }));

        MockMultipartFile mockMultipartLeft = MultipartFileHelper.getMockMultipartFile("left", "1.txt");
        MockMultipartFile mockMultipartRight = MultipartFileHelper.getMockMultipartFile("right", "1_different_size.txt");

        String resultStr = mockMvc.perform(multipart(URL_PREFIX + "/{id}", 1)
                .file(mockMultipartLeft)
                .file(mockMultipartRight)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        DiffsDto diffsDto = objectMapper.readValue(resultStr, DiffsDto.class);

        assertThat(diffsDto.getCalculationStatus()).isEqualTo(CalculationStatus.DONE.getText());
        assertThat(diffsDto.getDiffStatus()).isEqualTo(DiffStatus.NOT_SAME_SIZE.getText());
        assertThat(diffsDto.getDiffs()).isNull();
    }

    @Test
    public void testBinaryDiffAsyncSameData() throws Exception {
        InputStream leftInputStream = MultipartFileHelper.getMockMultipartFile("left", "1.txt").getInputStream();
        InputStream rightInputStream = MultipartFileHelper.getMockMultipartFile("right", "1_same.txt").getInputStream();

        given(binaryStorageService.uploadBoth(any(), any(), any())).willReturn(CompletableFuture.runAsync(() -> {
            sleep(400);
        }));
        given(binaryStorageService.getDataInputStream(StorageBucket.LEFT, "1")).willReturn(leftInputStream);
        given(binaryStorageService.getDataInputStream(StorageBucket.RIGHT, "1")).willReturn(rightInputStream);

        MockMultipartFile mockMultipartLeft = MultipartFileHelper.getMockMultipartFile("left", "1.txt");
        MockMultipartFile mockMultipartRight = MultipartFileHelper.getMockMultipartFile("right", "1_same.txt");

        String resultStr = mockMvc.perform(multipart(URL_PREFIX + "/{id}/async", 1)
                .file(mockMultipartLeft)
                .file(mockMultipartRight)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        DiffsDto diffsDto = objectMapper.readValue(resultStr, DiffsDto.class);

        assertThat(diffsDto.getCalculationStatus()).isNotEqualTo(CalculationStatus.DONE.getText());
        assertThat(diffsDto.getDiffStatus()).isEqualTo(DiffStatus.NOT_AVAILABLE.getText());
        assertThat(diffsDto.getDiffs()).isNull();

        sleep(500);

        String resultStrLater = mockMvc.perform(get(URL_PREFIX + "/{id}/status", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        DiffsDto diffsDtoLater = objectMapper.readValue(resultStrLater, DiffsDto.class);

        assertThat(diffsDtoLater.getCalculationStatus()).isEqualTo(CalculationStatus.DONE.getText());
        assertThat(diffsDtoLater.getDiffStatus()).isEqualTo(DiffStatus.EQUAL.getText());
        assertThat(diffsDtoLater.getDiffs()).isNull();
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }


}
