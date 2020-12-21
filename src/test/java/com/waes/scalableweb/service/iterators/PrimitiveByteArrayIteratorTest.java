package com.waes.scalableweb.service.iterators;

import com.waes.scalableweb.service.iterator.PrimitiveByteArrayIterator;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PrimitiveByteArrayIteratorTest {

    @Test
    public void testEmpty() {
        PrimitiveByteArrayIterator primitiveByteArrayIterator = new PrimitiveByteArrayIterator(new byte[0]);
        assertThat(primitiveByteArrayIterator.hasNext()).isFalse();
    }

    @Test
    public void testNotEmpty() {
        PrimitiveByteArrayIterator primitiveByteArrayIterator = new PrimitiveByteArrayIterator(new byte[]{1, 2});
        assertThat(primitiveByteArrayIterator.hasNext()).isTrue();
        assertThat(primitiveByteArrayIterator.next()).isEqualTo((byte)1);

        assertThat(primitiveByteArrayIterator.hasNext()).isTrue();
        assertThat(primitiveByteArrayIterator.next()).isEqualTo((byte)2);

        assertThat(primitiveByteArrayIterator.hasNext()).isFalse();
    }

    @Test
    public void testThrowsException() {
        PrimitiveByteArrayIterator primitiveByteArrayIterator = new PrimitiveByteArrayIterator(new byte[0]);
        assertThatThrownBy(primitiveByteArrayIterator::next).isInstanceOf(NoSuchElementException.class);
    }

}
