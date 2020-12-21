package com.waes.scalableweb.controller;

import com.waes.scalableweb.dto.DiffsDto;
import com.waes.scalableweb.exception.handling.ApiError;
import com.waes.scalableweb.service.BinaryDiffService;
import com.waes.scalableweb.timetracking.TrackExecutionTime;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/diff")
@Slf4j
public class BinaryDiffController {

    private final BinaryDiffService binaryDiffService;

    public BinaryDiffController(BinaryDiffService binaryDiffService) {
        this.binaryDiffService = binaryDiffService;
    }

    @Operation(summary = "Upload binaries and get calculated differences")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(schema = @Schema(implementation = DiffsDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(schema = @Schema(implementation = ApiError.class))})
    })
    @TrackExecutionTime
    @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DiffsDto binaryDiff(@PathVariable("id") String id,
                               @RequestParam("left") MultipartFile left,
                               @RequestParam("right") MultipartFile right) {

        return binaryDiffService.binaryDiff(id, left, right);
    }

    @Operation(summary = "Requests to upload binaries and get calculated differences, does not wait for completion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(schema = @Schema(implementation = DiffsDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(schema = @Schema(implementation = ApiError.class))})
    })
    @TrackExecutionTime
    @PostMapping(value = "/{id}/async", produces = MediaType.APPLICATION_JSON_VALUE)
    public DiffsDto binaryDiffAsync(@PathVariable("id") String id,
                                    @RequestParam("left") MultipartFile left,
                                    @RequestParam("right") MultipartFile right) {
        return binaryDiffService.binaryDiffAsync(id, left, right);
    }

    @Operation(summary = "Requests a diff status for previously uploaded files")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(schema = @Schema(implementation = DiffsDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(schema = @Schema(implementation = ApiError.class))})
    })
    @TrackExecutionTime
    @GetMapping(value = "/{id}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public DiffsDto binaryDiffStatus(@PathVariable("id") String id) {
        return binaryDiffService.getBinaryDiffStatus(id);
    }

}
