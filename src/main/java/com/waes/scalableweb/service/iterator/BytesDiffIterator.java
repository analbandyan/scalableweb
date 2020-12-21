package com.waes.scalableweb.service.iterator;

/**
 * Bytes diff iterator for equal size of data
 */
public interface BytesDiffIterator {

    boolean hasNext();

    boolean areNextBytesDifferent();

}
