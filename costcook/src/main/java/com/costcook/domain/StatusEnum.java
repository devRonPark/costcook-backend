package com.costcook.domain;

public enum StatusEnum {
    INACTIVE(0), // 비활성화 상태
    ACTIVE(1); // 활성화 상태

    private final int value;

    StatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static StatusEnum fromValue(int value) {
        for (StatusEnum status : values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 값입니다: " + value);
    }
}