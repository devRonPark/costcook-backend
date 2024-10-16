package com.costcook.exceptions;

public class MissingFieldException extends RuntimeException {
    public MissingFieldException() {
        super("필수 정보가 누락되었습니다.");
    }
}