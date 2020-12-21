package com.waes.scalableweb.controller;

import com.waes.scalableweb.exception.exceptions.CalculationRequestNotFoundException;
import com.waes.scalableweb.exception.exceptions.DiffRequestAlreadyExistsException;
import com.waes.scalableweb.service.BinaryDiffService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ExceptionHandlingTest extends AbstractControllerTest {

    @MockBean
    BinaryDiffService binaryDiffService;

    @Test
    public void testBinaryDiffParametersOk() throws Exception {
        given(binaryDiffService.binaryDiff(any(), any(), any())).willReturn(null);

        MockMultipartFile mockMultipartLeft = MultipartFileHelper.getMockMultipartFile("left", "1.txt");
        MockMultipartFile mockMultipartRight = MultipartFileHelper.getMockMultipartFile("right", "1_same.txt");
        mockMvc.perform(multipart(URL_PREFIX + "/{id}", 1).file(mockMultipartLeft).file(mockMultipartRight)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testBinaryDiffLeftMultipartMissing() throws Exception {
        given(binaryDiffService.binaryDiff(any(), any(), any())).willReturn(null);

        MockMultipartFile mockMultipartRight = MultipartFileHelper.getMockMultipartFile("right", "1_same.txt");
        mockMvc.perform(multipart(URL_PREFIX + "/{id}", 1).file(mockMultipartRight)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testBinaryDiffRightMultipartMissing() throws Exception {
        given(binaryDiffService.binaryDiff(any(), any(), any())).willReturn(null);

        MockMultipartFile mockMultipartLeft = MultipartFileHelper.getMockMultipartFile("left", "1.txt");
        mockMvc.perform(multipart(URL_PREFIX + "/{id}", 1).file(mockMultipartLeft)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testBinaryDiffForExistingId() throws Exception {
        given(binaryDiffService.binaryDiff(any(), any(), any())).willThrow(new DiffRequestAlreadyExistsException("1"));

        MockMultipartFile mockMultipartLeft = MultipartFileHelper.getMockMultipartFile("left", "1.txt");
        mockMvc.perform(multipart(URL_PREFIX + "/{id}", 1).file(mockMultipartLeft)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testBinaryDiffStatusOk() throws Exception {
        given(binaryDiffService.getBinaryDiffStatus(any())).willReturn(null);

        MockMultipartFile mockMultipartLeft = MultipartFileHelper.getMockMultipartFile("left", "1.txt");
        mockMvc.perform(get(URL_PREFIX + "/{id}/status", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testBinaryDiffStatusForNotExistingRequest() throws Exception {
        given(binaryDiffService.getBinaryDiffStatus(any())).willThrow(new CalculationRequestNotFoundException("1"));

        MockMultipartFile mockMultipartLeft = MultipartFileHelper.getMockMultipartFile("left", "1.txt");
        mockMvc.perform(get(URL_PREFIX + "/{id}/status", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
