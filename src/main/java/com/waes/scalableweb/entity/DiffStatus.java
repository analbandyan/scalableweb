package com.waes.scalableweb.entity;

import lombok.Getter;

@Getter
public enum DiffStatus {
    NOT_AVAILABLE("Not available"),
    EQUAL("Equal"),
    DIFFERENT("Different"),
    NOT_SAME_SIZE("Not same size");

    private final String text;

    DiffStatus(String text) {
        this.text = text;
    }
}
