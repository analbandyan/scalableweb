package com.waes.scalableweb.service.iterator;

public class BytesDiffIteratorDefault implements BytesDiffIterator {

    private final PrimitiveByteIterator leftIterator;
    private final PrimitiveByteIterator rightIterator;

    public BytesDiffIteratorDefault(PrimitiveByteIterator leftIterator, PrimitiveByteIterator rightIterator) {
        this.leftIterator = leftIterator;
        this.rightIterator = rightIterator;
    }

    @Override
    public boolean hasNext() {
        return leftIterator.hasNext();
    }

    @Override
    public boolean areNextBytesDifferent() {
        return leftIterator.next() != rightIterator.next();
    }
}
