package com.waes.scalableweb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.TreeSet;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
@Schema(example = "{ " +
        "\"diffStatus\": \"Different\",\n" +
        "\"calculationStatus\": \"Done\",\n" +
        "\"diffs\": [\n" +
        "  {\n" +
        "\"offset\": 1,\n" +
        "\"length\": 2\n" +
        "},\n" +
        "  {\n" +
        "\"offset\": 5,\n" +
        "\"length\": 3\n" +
        "}\n" +
        "] }", description = "Representing the binaries comparison status")
public class DiffsDto {

    private String diffStatus;

    private String calculationStatus;

    private final TreeSet<DiffDto> diffs;

}
