package com.waes.scalableweb.service.iterators;

import com.waes.scalableweb.service.iterator.InputStreamBufferedByteIterator;
import org.apache.commons.lang3.mutable.MutableInt;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class InputStreamBufferedByteIteratorTest {

    private final InputStream inputStream = mock(InputStream.class);

    @Test
    public void testNoData() throws IOException {
        when(inputStream.read(new byte[2], 0, 2)).thenAnswer(inv -> {
            return -1;
        });

        InputStreamBufferedByteIterator inputStreamBufferedByteIterator = new InputStreamBufferedByteIterator(inputStream, 2);

        assertThat(inputStreamBufferedByteIterator.hasNext()).isFalse();
        assertThat(inputStreamBufferedByteIterator.hasNext()).isFalse();
        verify(inputStream, times(1)).read(new byte[2], 0, 2);
    }

    @Test
    public void testOneIncompleteChunk() throws IOException {
        when(inputStream.read(new byte[2], 0, 2)).thenAnswer(inv -> {
            byte[] bytes = inv.getArgument(0, byte[].class);
            bytes[0] = 1;
            return 1;
        });

        InputStreamBufferedByteIterator inputStreamBufferedByteIterator = new InputStreamBufferedByteIterator(inputStream, 2);

        assertThat(inputStreamBufferedByteIterator.hasNext()).isTrue();
        assertThat(inputStreamBufferedByteIterator.next()).isEqualTo((byte)1);
        assertThat(inputStreamBufferedByteIterator.hasNext()).isFalse();
        assertThat(inputStreamBufferedByteIterator.hasNext()).isFalse();
        assertThat(inputStreamBufferedByteIterator.hasNext()).isFalse();
        verify(inputStream, times(1)).read(new byte[]{1, 0}, 0, 2);
    }

    @Test
    public void testNextThrowsException() throws IOException {
        when(inputStream.read(new byte[2], 0, 2)).thenAnswer(inv -> {
            byte[] bytes = inv.getArgument(0, byte[].class);
            bytes[0] = 1;
            return 1;
        });

        InputStreamBufferedByteIterator inputStreamBufferedByteIterator = new InputStreamBufferedByteIterator(inputStream, 2);

        assertThatCode(inputStreamBufferedByteIterator::next).doesNotThrowAnyException();
        assertThatThrownBy(inputStreamBufferedByteIterator::next).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void testNextOnNextPageThrowsException() throws IOException {
        when(inputStream.read(new byte[2], 0, 2)).thenAnswer(inv -> {
            byte[] bytes = inv.getArgument(0, byte[].class);
            bytes[0] = 3;
            bytes[1] = 5;
            return 2;
        });

        InputStreamBufferedByteIterator inputStreamBufferedByteIterator = new InputStreamBufferedByteIterator(inputStream, 2);


        assertThatCode(inputStreamBufferedByteIterator::next).doesNotThrowAnyException();
        assertThatCode(inputStreamBufferedByteIterator::next).doesNotThrowAnyException();
        assertThatThrownBy(inputStreamBufferedByteIterator::next).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void testOneCompletePage() throws IOException {
        MutableInt callTimes = new MutableInt();
        when(inputStream.read(new byte[2], 0, 2)).thenAnswer(inv -> {
            if(callTimes.incrementAndGet() == 1) {
                byte[] bytes = inv.getArgument(0, byte[].class);
                bytes[0] = 3;
                bytes[1] = 5;
                return 2;
            } else {
                return -1;
            }

        });

        InputStreamBufferedByteIterator inputStreamBufferedByteIterator = new InputStreamBufferedByteIterator(inputStream, 2);

        assertThat(inputStreamBufferedByteIterator.hasNext()).isTrue();
        assertThat(inputStreamBufferedByteIterator.next()).isEqualTo((byte)3);

        assertThat(inputStreamBufferedByteIterator.hasNext()).isTrue();
        assertThat(inputStreamBufferedByteIterator.next()).isEqualTo((byte)5);

        assertThat(inputStreamBufferedByteIterator.hasNext()).isFalse();
        assertThat(inputStreamBufferedByteIterator.hasNext()).isFalse();

        verify(inputStream, times(1)).read(new byte[]{3, 5}, 0, 2);
        verify(inputStream, times(1)).read(new byte[2], 0, 2);
    }

    @Test
    public void testOneCompleteAndOneIncompletePages() throws IOException {
        MutableInt callTimes = new MutableInt();
        when(inputStream.read(new byte[2], 0, 2)).thenAnswer(inv -> {
            if(callTimes.incrementAndGet() == 1) {
                byte[] bytes = inv.getArgument(0, byte[].class);
                bytes[0] = 3;
                bytes[1] = 5;
                return 2;
            } else {
                byte[] bytes = inv.getArgument(0, byte[].class);
                bytes[0] = 4;
                return 1;
            }

        });

        InputStreamBufferedByteIterator inputStreamBufferedByteIterator = new InputStreamBufferedByteIterator(inputStream, 2);

        assertThat(inputStreamBufferedByteIterator.hasNext()).isTrue();
        assertThat(inputStreamBufferedByteIterator.next()).isEqualTo((byte)3);

        assertThat(inputStreamBufferedByteIterator.hasNext()).isTrue();
        assertThat(inputStreamBufferedByteIterator.next()).isEqualTo((byte)5);

        assertThat(inputStreamBufferedByteIterator.hasNext()).isTrue();
        assertThat(inputStreamBufferedByteIterator.next()).isEqualTo((byte)4);

        assertThat(inputStreamBufferedByteIterator.hasNext()).isFalse();
        assertThat(inputStreamBufferedByteIterator.hasNext()).isFalse();

        verify(inputStream, times(1)).read(new byte[]{3, 5}, 0, 2);
        verify(inputStream, times(1)).read(new byte[]{4, 0}, 0, 2);

    }

}
