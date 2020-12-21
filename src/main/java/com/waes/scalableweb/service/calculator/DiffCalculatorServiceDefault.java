package com.waes.scalableweb.service.calculator;

import com.waes.scalableweb.entity.Diff;
import com.waes.scalableweb.service.iterator.BytesDiffIterator;
import com.waes.scalableweb.service.iterator.BytesDiffIteratorDefault;
import com.waes.scalableweb.service.iterator.InputStreamBufferedByteIterator;
import com.waes.scalableweb.service.iterator.PrimitiveByteIterator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

@Service
public class DiffCalculatorServiceDefault implements DiffCalculatorService {

    private final int storageReadChunkSize;

    public DiffCalculatorServiceDefault(@Value("${storage.binary.read.chunk-size:1024}") int storageReadChunkSize) {
        this.storageReadChunkSize = storageReadChunkSize;
    }

    @Override
    public Set<Diff> calculate(InputStream leftInputStream, InputStream rightInputStream) {

        Set<Diff> diffData = new HashSet<>();

        BytesDiffIterator bytesDiffIterator = getBytesDiffIterator(leftInputStream, rightInputStream);

        int currentIndex = 0;

        long currentDiffSectionOffset = 0;
        long currentDiffSectionLength = 0;
        boolean isCurrentlyInDifferentSection = false;

        while (bytesDiffIterator.hasNext()) {
            boolean bytesAreDifferent = bytesDiffIterator.areNextBytesDifferent();
            if (bytesAreDifferent) {
                if (!isCurrentlyInDifferentSection) {
                    currentDiffSectionOffset = currentIndex;
                }
                ++currentDiffSectionLength;
                isCurrentlyInDifferentSection = true;
            } else {
                if (isCurrentlyInDifferentSection) {
                    diffData.add(buildDiff(currentDiffSectionOffset, currentDiffSectionLength));
                    isCurrentlyInDifferentSection = false;
                    currentDiffSectionLength = 0;
                }
            }

            ++currentIndex;
        }

        if (isCurrentlyInDifferentSection) {
            diffData.add(buildDiff(currentDiffSectionOffset, currentDiffSectionLength));
        }

        return diffData;
    }

    private BytesDiffIterator getBytesDiffIterator(InputStream leftInputStream, InputStream rightInputStream) {
        PrimitiveByteIterator bytesIteratorLeft = new InputStreamBufferedByteIterator(leftInputStream, storageReadChunkSize);
        PrimitiveByteIterator bytesIteratorRight = new InputStreamBufferedByteIterator(rightInputStream, storageReadChunkSize);

        return new BytesDiffIteratorDefault(bytesIteratorLeft, bytesIteratorRight);
    }

    private Diff buildDiff(Long offset, long length) {
        return Diff.builder()
                .offset(offset)
                .length(length).build();
    }

}
