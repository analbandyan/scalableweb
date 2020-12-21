package com.waes.scalableweb.service.converter;

import com.waes.scalableweb.dto.DiffDto;
import com.waes.scalableweb.dto.DiffsDto;
import com.waes.scalableweb.entity.CalculationStatus;
import com.waes.scalableweb.entity.Diff;
import com.waes.scalableweb.entity.DiffStatus;
import com.waes.scalableweb.entity.Diffs;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class DiffsToDtoConverterTest {

    @Test
    public void testNull() {
        DiffsToDtoConverter diffsToDtoConverter = new DiffsToDtoConverter();
        assertThat(diffsToDtoConverter.apply(null)).isNull();
    }

    @Test
    public void testEmpty() {
        DiffsToDtoConverter diffsToDtoConverter = new DiffsToDtoConverter();

        Diffs diffs = new Diffs();
        diffs.setDiffStatus(DiffStatus.DIFFERENT);
        diffs.setCalculationStatus(CalculationStatus.DONE);
        DiffsDto diffsDto = diffsToDtoConverter.apply(diffs);

        assertThat(diffsDto).isNotNull();
        assertThat(diffsDto.getDiffStatus()).isEqualTo(DiffStatus.DIFFERENT.getText());
        assertThat(diffsDto.getCalculationStatus()).isEqualTo(CalculationStatus.DONE.getText());
        assertThat(diffsDto.getDiffs()).isNull();
    }

    @Test
    public void testDiffs() {
        DiffsToDtoConverter diffsToDtoConverter = new DiffsToDtoConverter();

        Diffs diffs = new Diffs();
        diffs.setDiffStatus(DiffStatus.DIFFERENT);
        diffs.setCalculationStatus(CalculationStatus.DONE);
        diffs.setDiffs(Set.of(
                new Diff(null, 0L, 1L, null),
                new Diff(null, 4L, 9L, null)
        ));
        DiffsDto diffsDto = diffsToDtoConverter.apply(diffs);

        assertThat(diffsDto).isNotNull();
        assertThat(diffsDto.getDiffs()).hasSize(2);
        assertThat(diffsDto.getDiffs()).contains(new DiffDto(0, 1));
        assertThat(diffsDto.getDiffs()).contains(new DiffDto(4, 9));
    }


    @ParameterizedTest
    @MethodSource("provideStatusArguments")
    public void testStatuses(DiffStatus diffStatus, CalculationStatus calculationStatus) {
        DiffsToDtoConverter diffsToDtoConverter = new DiffsToDtoConverter();

        Diffs diffs = new Diffs();
        diffs.setDiffStatus(diffStatus);
        diffs.setCalculationStatus(calculationStatus);
        DiffsDto diffsDto = diffsToDtoConverter.apply(diffs);

        assertThat(diffsDto.getDiffStatus()).isEqualTo(diffStatus.getText());
        assertThat(diffsDto.getCalculationStatus()).isEqualTo(calculationStatus.getText());
    }

    private static Stream<Arguments> provideStatusArguments() {
        return Stream.of(
                Arguments.of(DiffStatus.DIFFERENT, CalculationStatus.CALCULATING),
                Arguments.of(DiffStatus.NOT_AVAILABLE, CalculationStatus.UPLOADING),
                Arguments.of(DiffStatus.NOT_SAME_SIZE, CalculationStatus.DONE),
                Arguments.of(DiffStatus.EQUAL, CalculationStatus.DONE)

        );
    }
}
