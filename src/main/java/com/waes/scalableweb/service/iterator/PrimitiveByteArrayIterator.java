package com.waes.scalableweb.service.iterator;

import java.util.NoSuchElementException;

public class PrimitiveByteArrayIterator implements PrimitiveByteIterator {

    private final byte[] bytes;
    private int currentIndex = 0;

    public PrimitiveByteArrayIterator(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < bytes.length;
    }

    @Override
    public byte next() {
        if(currentIndex >= bytes.length) {
            throw new NoSuchElementException("No more elements");
        }
        return bytes[currentIndex++];
    }
}
