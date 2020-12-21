package com.waes.scalableweb.service;

import com.waes.scalableweb.dto.DiffsDto;
import com.waes.scalableweb.entity.CalculationStatus;
import com.waes.scalableweb.entity.Diff;
import com.waes.scalableweb.entity.DiffStatus;
import com.waes.scalableweb.entity.Diffs;
import com.waes.scalableweb.exception.exceptions.CalculationRequestNotFoundException;
import com.waes.scalableweb.service.calculator.DiffCalculatorService;
import com.waes.scalableweb.service.converter.DiffsToDtoConverter;
import com.waes.scalableweb.service.storage.DiffsStorageService;
import com.waes.scalableweb.service.storage.binary.BinaryStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Service
public class BinaryDiffServiceDefault implements BinaryDiffService {

    private final BinaryStorageService binaryStorageService;
    private final DiffCalculatorService diffCalculatorService;
    private final DiffsStorageService diffsStorageService;
    private final DiffsToDtoConverter diffsToDtoConverter;


    public BinaryDiffServiceDefault(
            BinaryStorageService binaryStorageService,
            DiffCalculatorService diffCalculatorService,
            DiffsStorageService diffsStorageService, DiffsToDtoConverter diffsToDtoConverter) {
        this.binaryStorageService = binaryStorageService;
        this.diffCalculatorService = diffCalculatorService;
        this.diffsStorageService = diffsStorageService;
        this.diffsToDtoConverter = diffsToDtoConverter;
    }

    @Override
    public DiffsDto binaryDiff(String id, MultipartFile left, MultipartFile right) {
        return binaryDiffInternal(id, left, right)
                .thenApply(buildDiffsDto())
                .join();

    }

    @Override
    public DiffsDto binaryDiffAsync(String id, MultipartFile left, MultipartFile right) {
        binaryDiffInternal(id, left, right);
        return buildDiffsDto().apply(id);
    }

    @Override
    public DiffsDto getBinaryDiffStatus(String id) {
        return buildDiffsDto().apply(id);
    }


    public CompletableFuture<String> binaryDiffInternal(String id, MultipartFile left, MultipartFile right) {
        if (left.getSize() == right.getSize()) {
            diffsStorageService.createNewDiffsObject(id, DiffStatus.NOT_AVAILABLE, CalculationStatus.UPLOADING);
            return binaryStorageService.uploadBoth(id, left, right)
                    .thenApply(calculateDiffs(id))
                    .thenApply(storeDiffs(id));
        } else {
            diffsStorageService.createNewDiffsObject(id, DiffStatus.NOT_SAME_SIZE, CalculationStatus.DONE);
            return CompletableFuture.completedFuture(id);
        }

    }

    private Function<Set<Diff>, String> storeDiffs(String naturalId) {
        return diffs -> {
            diffsStorageService.diffCalculationCompleted(naturalId, diffs);
            return naturalId;
        };
    }

    private Function<String, DiffsDto> buildDiffsDto() {
        return naturalId -> {
            Optional<Diffs> diffs = diffsStorageService.findDiffsObject(naturalId);
            return diffs.map(diffsToDtoConverter)
                    .orElseThrow(() -> new CalculationRequestNotFoundException(naturalId));
        };
    }

    private Function<Void, Set<Diff>> calculateDiffs(String id) {
        return ignored -> {
            diffsStorageService.updateDiffCalculationStatus(id, CalculationStatus.CALCULATING);
            return diffCalculatorService.calculate(
                    binaryStorageService.getDataInputStream(StorageBucket.LEFT, id),
                    binaryStorageService.getDataInputStream(StorageBucket.RIGHT, id)
            );
        };
    }

}
