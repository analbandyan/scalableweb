package com.waes.scalableweb.service.iterators;

import com.waes.scalableweb.service.iterator.BytesDiffIteratorDefault;
import com.waes.scalableweb.service.iterator.PrimitiveByteArrayIterator;
import com.waes.scalableweb.service.iterator.PrimitiveByteIterator;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

public class BytesDiffIteratorDefaultTest {

    @Test
    public void testEmpty() {
        PrimitiveByteIterator primitiveByteIterator1 = new PrimitiveByteArrayIterator(new byte[0]);
        PrimitiveByteIterator primitiveByteIterator2 = new PrimitiveByteArrayIterator(new byte[0]);

        BytesDiffIteratorDefault bytesDiffIteratorDefault = new BytesDiffIteratorDefault(primitiveByteIterator1, primitiveByteIterator2);
        assertThat(bytesDiffIteratorDefault.hasNext()).isFalse();
    }

    @Test
    public void testNotEmptyEmpty() {
        PrimitiveByteIterator primitiveByteIterator1 = new PrimitiveByteArrayIterator(new byte[]{0, 2});
        PrimitiveByteIterator primitiveByteIterator2 = new PrimitiveByteArrayIterator(new byte[]{1, 2});

        BytesDiffIteratorDefault bytesDiffIteratorDefault = new BytesDiffIteratorDefault(primitiveByteIterator1, primitiveByteIterator2);

        assertThat(bytesDiffIteratorDefault.hasNext()).isTrue();
        assertThat(bytesDiffIteratorDefault.areNextBytesDifferent()).isTrue();

        assertThat(bytesDiffIteratorDefault.hasNext()).isTrue();
        assertThat(bytesDiffIteratorDefault.areNextBytesDifferent()).isFalse();

        assertThat(bytesDiffIteratorDefault.hasNext()).isFalse();
    }

    @Test
    public void testNextThrowsException() {
        PrimitiveByteIterator primitiveByteIterator1 = new PrimitiveByteArrayIterator(new byte[]{1});
        PrimitiveByteIterator primitiveByteIterator2 = new PrimitiveByteArrayIterator(new byte[]{1});

        BytesDiffIteratorDefault bytesDiffIteratorDefault = new BytesDiffIteratorDefault(primitiveByteIterator1, primitiveByteIterator2);

        assertThatCode(bytesDiffIteratorDefault::areNextBytesDifferent).doesNotThrowAnyException();
        assertThatThrownBy(bytesDiffIteratorDefault::areNextBytesDifferent).isInstanceOf(NoSuchElementException.class);
    }

}
