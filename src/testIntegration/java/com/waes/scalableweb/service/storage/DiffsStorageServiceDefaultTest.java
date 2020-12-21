package com.waes.scalableweb.service.storage;

import com.waes.scalableweb.dao.DiffRepository;
import com.waes.scalableweb.entity.CalculationStatus;
import com.waes.scalableweb.entity.Diff;
import com.waes.scalableweb.entity.DiffStatus;
import com.waes.scalableweb.entity.Diffs;
import com.waes.scalableweb.exception.exceptions.DiffRequestAlreadyExistsException;
import com.waes.scalableweb.service.AbstractServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
public class DiffsStorageServiceDefaultTest extends AbstractServiceTest {

    @Autowired
    private DiffRepository diffRepository;

    @Autowired
    private DiffsStorageServiceDefault diffsStorageServiceDefault;

    @Test
    public void testCreateNewDiffsObject() {
        diffsStorageServiceDefault.createNewDiffsObject("id", DiffStatus.DIFFERENT, CalculationStatus.DONE);
        Diffs actual = diffRepository.getByNaturalId("id");
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getDiffStatus()).isEqualTo(DiffStatus.DIFFERENT);
        assertThat(actual.getCalculationStatus()).isEqualTo(CalculationStatus.DONE);
        assertThat(actual.getDiffs()).isNull();
    }

    @Test
    public void testCreateDiffsObjectWithExistingNaturalId() {
        diffsStorageServiceDefault.createNewDiffsObject("id", DiffStatus.DIFFERENT, CalculationStatus.DONE);

        assertThatThrownBy(() -> diffsStorageServiceDefault
                .createNewDiffsObject("id", DiffStatus.NOT_SAME_SIZE, CalculationStatus.UPLOADING))
                .isInstanceOf(DiffRequestAlreadyExistsException.class);
    }

    @Test
    public void testUpdateDiffCalculationStatus() {
        diffsStorageServiceDefault.createNewDiffsObject("id", DiffStatus.DIFFERENT, CalculationStatus.UPLOADING);

        diffsStorageServiceDefault.updateDiffCalculationStatus("id", CalculationStatus.CALCULATING);

        Diffs actual = diffRepository.getByNaturalId("id");
        assertThat(actual.getCalculationStatus()).isEqualTo(CalculationStatus.CALCULATING);
    }

    @Test
    public void testDiffCalculationCompleted() {
        diffsStorageServiceDefault.createNewDiffsObject("id", DiffStatus.DIFFERENT, CalculationStatus.UPLOADING);

        diffsStorageServiceDefault.diffCalculationCompleted("id", Set.of(
                new Diff(null, 1L, 2L, null),
                new Diff(null, 5L, 10L, null)
        ));

        Diffs actual = diffRepository.getByNaturalId("id");
        assertThat(actual.getCalculationStatus()).isEqualTo(CalculationStatus.DONE);
    }

}
