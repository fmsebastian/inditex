package com.paradigma.poc.inditex.productpricebydate.domain.exceptions;

public class SamePriorityOverlapException extends RuntimeException {

    public SamePriorityOverlapException(String message) {
        super(message);
    }
}
