package com.waes.scalableweb.entity;

import lombok.Getter;

@Getter
public enum CalculationStatus {

    UPLOADING("Uploading"),
    CALCULATING("Calculating"),
    DONE("Done");

    private final String text;

    CalculationStatus(String text) {
        this.text = text;
    }
}
