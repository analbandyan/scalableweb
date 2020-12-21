package com.waes.scalableweb.service.storage;

import com.waes.scalableweb.dao.DiffRepository;
import com.waes.scalableweb.entity.CalculationStatus;
import com.waes.scalableweb.entity.Diff;
import com.waes.scalableweb.entity.DiffStatus;
import com.waes.scalableweb.entity.Diffs;
import com.waes.scalableweb.exception.exceptions.DiffRequestAlreadyExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class DiffsStorageServiceDefault implements DiffsStorageService {

    private final DiffRepository diffRepository;

    public DiffsStorageServiceDefault(DiffRepository diffRepository) {
        this.diffRepository = diffRepository;
    }

    public void createNewDiffsObject(String naturalId, DiffStatus diffStatus, CalculationStatus calculationStatus) {
        makeSureObjectDoesNotExist(naturalId);

        Diffs diffs = Diffs.builder()
                .naturalId(naturalId)
                .diffStatus(diffStatus)
                .calculationStatus(calculationStatus)
                .build();
        diffRepository.save(diffs);
    }

    private void makeSureObjectDoesNotExist(String naturalId) {
        diffRepository.findByNaturalId(naturalId).ifPresent(d -> {
            throw new DiffRequestAlreadyExistsException(naturalId);
        });
    }

    public void updateDiffCalculationStatus(String naturalId, CalculationStatus calculationStatus) {
        Diffs diffsEntity = diffRepository.getByNaturalId(naturalId);
        diffsEntity.setCalculationStatus(calculationStatus);
    }

    public void diffCalculationCompleted(String naturalId, Set<Diff> diffData) {
        DiffStatus diffStatus = !diffData.isEmpty() ? DiffStatus.DIFFERENT : DiffStatus.EQUAL;

        Diffs diffsEntity = diffRepository.getByNaturalId(naturalId);
        diffsEntity.setDiffStatus(diffStatus);
        diffsEntity.setCalculationStatus(CalculationStatus.DONE);
        diffsEntity.setDiffs(diffData);
    }

    @Transactional(readOnly = true)
    public Optional<Diffs> findDiffsObject(String naturalId) {
        return diffRepository.findByNaturalId(naturalId);
    }

}
