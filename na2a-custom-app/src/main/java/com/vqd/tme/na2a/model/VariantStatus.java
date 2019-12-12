package com.vqd.tme.na2a.model;

import java.util.Arrays;
import java.util.Optional;

public enum VariantStatus {

    CREATED("100", "created"),
    ALL_MODEL_CONFIRMATION("200", "all brand confirmation"),
    WITHDRAWN("300", "withdrawn"),
    PUBLISHED("400", "published"),
    MODIFIED("500", "modified"),
    PHASED_OUT("600", "phased out");

    private final String code;
    private final String value;

    VariantStatus(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public static Optional<VariantStatus> findByCode(String code) {
        return Arrays.asList(VariantStatus.values())
                .stream()
                .filter(status -> status.code.equalsIgnoreCase(code))
                .findFirst();
    }

    public static Optional<VariantStatus> findByValue(String value) {
        return Arrays.asList(VariantStatus.values())
                .stream()
                .filter(status -> status.value.equalsIgnoreCase(value))
                .findFirst();
    }

    public boolean isCancelled() {
        return this.code.equalsIgnoreCase("300");
    }
}
