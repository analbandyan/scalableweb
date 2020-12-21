package com.waes.scalableweb.exception.handling;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Builder
@JsonInclude(Include.NON_NULL)
public class ApiError {

    @Schema(example = "BAD_REQUEST", description = "HTTP Status")
    private final HttpStatus status;

    @Schema(example = "API Error Message Example", description = "Calculation request for id = 1 not found.", nullable = true)
    private final String message;

    @Schema(example = "[\"id: must not be null\", \"left: must not be null\"]",
            description = "Suberrors", nullable = true)
    private final List<String> subErrors;

    public ApiError(HttpStatus status, String message, List<String> subErrors) {
        super();
        this.status = status;
        this.message = message;
        this.subErrors = subErrors;
    }

}
