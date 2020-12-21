package com.waes.scalableweb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Schema(example = "{\"offset\": 5, \"length\": 27 }", description = "Representing the diff section of the binaries")
public class DiffDto implements Comparable<DiffDto> {

    @EqualsAndHashCode.Include
    private final long offset;

    private final long length;

    @Override
    public int compareTo(DiffDto o) {
        return Long.valueOf(offset - o.offset).intValue();
    }
}
