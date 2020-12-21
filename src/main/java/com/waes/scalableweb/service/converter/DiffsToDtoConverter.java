package com.waes.scalableweb.service.converter;

import com.waes.scalableweb.dto.DiffDto;
import com.waes.scalableweb.dto.DiffsDto;
import com.waes.scalableweb.entity.Diff;
import com.waes.scalableweb.entity.Diffs;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DiffsToDtoConverter implements Function<Diffs, DiffsDto> {

    @Override
    public DiffsDto apply(Diffs diffs) {
        if(diffs == null) {
            return null;
        }
        return DiffsDto.builder()
                .diffStatus(diffs.getDiffStatus().getText())
                .calculationStatus(diffs.getCalculationStatus().getText())
                .diffs(toDto(diffs.getDiffs()))
                .build();
    }


    private static TreeSet<DiffDto> toDto(Set<Diff> diffData) {
        if (CollectionUtils.isEmpty(diffData)) {
            return null;
        }

        return diffData.stream()
                .map(DiffsToDtoConverter::toDto)
                .collect(Collectors.toCollection(
                        () -> new TreeSet<>(Comparator.comparing(DiffDto::getOffset))
                ));
    }

    private static DiffDto toDto(Diff diff) {
        return new DiffDto(diff.getOffset(), diff.getLength());
    }

}
