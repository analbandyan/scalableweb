package com.waes.scalableweb.service.iterator;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Slf4j
public class InputStreamBufferedByteIterator implements PrimitiveByteIterator {

    private final InputStream inputStream;
    private final int chunkSize;

    private PrimitiveByteIterator currentChunkIterator;

    private boolean shouldLoadMoreData = true;

    public InputStreamBufferedByteIterator(InputStream inputStream, int chunkSize) {
        this.inputStream = inputStream;
        this.chunkSize = chunkSize;
    }


    @Override
    public boolean hasNext() {
        loadInitialChunkDataIfNotLoaded();
        if (!doesCurrentChunkHaveMoreElements() && checkShouldLoadMoreData()) {
            loadNewChunkData();
        }

        return doesCurrentChunkHaveMoreElements();
    }

    @Override
    public byte next() {
        return getNext();
    }

    private byte getNext() {
        loadInitialChunkDataIfNotLoaded();
        return currentChunkIterator.next();
    }

    private void loadInitialChunkDataIfNotLoaded() {
        if (currentChunkIterator == null) {
            log.debug("Initializing current chunk");
            loadNewChunkData();
        }
    }

    private void loadNewChunkData() {
        log.debug("Resetting current chunk data");
        currentChunkIterator = fetchChunk();
    }

    private boolean doesCurrentChunkHaveMoreElements() {
        boolean currentChunkHasMoreElements = currentChunkIterator.hasNext();
        if (!currentChunkHasMoreElements) {
            log.debug("Current chunk does not have more elements");
        }
        return currentChunkHasMoreElements;
    }

    private boolean checkShouldLoadMoreData() {
        log.debug("shouldLoadMoreData: " + shouldLoadMoreData);
        return shouldLoadMoreData;
    }

    private PrimitiveByteArrayIterator fetchChunk() {
        try {
            return doFetchChunk();
        } catch (IOException e) {
            log.error("Failed to load the next chunk data", e);
            throw new RuntimeException(e);
        }
    }

    private PrimitiveByteArrayIterator doFetchChunk() throws IOException {
        log.debug("Fetching a new chunk.");
        byte[] currentChunk = new byte[chunkSize];

        int readReadBytesCount = inputStream.read(currentChunk, 0, chunkSize);
        if (readReadBytesCount < chunkSize) {
            log.debug("Fetched chunk data size = {}, retrieval chunk size = {}", readReadBytesCount, chunkSize);
            currentChunk = readReadBytesCount != -1 ? Arrays.copyOfRange(currentChunk, 0, readReadBytesCount) : new byte[0];
            doNotLoadMoreData();
        }

        return new PrimitiveByteArrayIterator(currentChunk);
    }

    private void doNotLoadMoreData() {
        log.debug("Mark to not load more data");
        shouldLoadMoreData = false;
    }
}
