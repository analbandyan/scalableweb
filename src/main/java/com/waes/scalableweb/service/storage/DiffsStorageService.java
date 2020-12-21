package com.waes.scalableweb.service.storage;

import com.waes.scalableweb.entity.CalculationStatus;
import com.waes.scalableweb.entity.Diff;
import com.waes.scalableweb.entity.DiffStatus;
import com.waes.scalableweb.entity.Diffs;

import java.util.Optional;
import java.util.Set;

public interface DiffsStorageService {

    void createNewDiffsObject(String naturalId, DiffStatus diffStatus, CalculationStatus calculationStatus);

    void updateDiffCalculationStatus(String naturalId, CalculationStatus calculationStatus);

    void diffCalculationCompleted(String naturalId, Set<Diff> diffData);

    Optional<Diffs> findDiffsObject(String naturalId);

}
