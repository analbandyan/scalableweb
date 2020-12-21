package com.waes.scalableweb.dao;

import com.waes.scalableweb.entity.CalculationStatus;
import com.waes.scalableweb.entity.Diffs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DiffRepository extends JpaRepository<Diffs, Long> {

    Diffs getByNaturalId(String naturalId);

    Optional<Diffs> findByNaturalId(String naturalId);
}
