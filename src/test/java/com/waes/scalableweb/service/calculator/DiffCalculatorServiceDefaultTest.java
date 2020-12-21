package com.waes.scalableweb.service.calculator;

import com.waes.scalableweb.entity.Diff;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class DiffCalculatorServiceDefaultTest {

    @Test
    public void testNoDiffEmpty() {
        DiffCalculatorServiceDefault diffCalculatorServiceDefault = new DiffCalculatorServiceDefault(10);
        String data1 = "";
        String data2 = data1;
        Set<Diff> diffs = diffCalculatorServiceDefault.calculate(
                new ByteArrayInputStream(data1.getBytes()),
                new ByteArrayInputStream(data2.getBytes())
        );
        assertThat(diffs).isEmpty();
    }

    @Test
    public void testNoDiff() {
        DiffCalculatorServiceDefault diffCalculatorServiceDefault = new DiffCalculatorServiceDefault(10);
        String data1 = "123456789";
        String data2 = data1;
        Set<Diff> diffs = diffCalculatorServiceDefault.calculate(
                new ByteArrayInputStream(data1.getBytes()),
                new ByteArrayInputStream(data2.getBytes())
        );
        assertThat(diffs).isEmpty();
    }

    @Test
    public void testDiff() {
        DiffCalculatorServiceDefault diffCalculatorServiceDefault = new DiffCalculatorServiceDefault(10);
        String data1 = "123456789";
        String data2 = "003406700";
        Set<Diff> diffs = diffCalculatorServiceDefault.calculate(
                new ByteArrayInputStream(data1.getBytes()),
                new ByteArrayInputStream(data2.getBytes())
        );
        assertThat(diffs).isNotEmpty();
        assertThat(diffs).contains(new Diff(null, 0L, 2L, null));
        assertThat(diffs).contains(new Diff(null, 4L, 1L, null));
        assertThat(diffs).contains(new Diff(null, 7L, 2L, null));
    }

}
