package com.waes.scalableweb.service.calculator;

import com.waes.scalableweb.entity.Diff;

import java.io.InputStream;
import java.util.Set;

public interface DiffCalculatorService {

    /**
     * Calculating the differences between left and right streams assuming the source data of both streams have the same size
     *
     * @param left  - the left side data input stream
     * @param right - the right side data input stream
     * @return Set<Diff></> - the differences between left and right sites data
     */
    Set<Diff> calculate(InputStream left, InputStream right);

}
