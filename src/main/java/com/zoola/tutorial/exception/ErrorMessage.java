package com.zoola.tutorial.exception;

import lombok.Getter;

import java.util.Date;

@Getter
public class ErrorMessage {

    private final int statusCode;
    private final Date timestamp;
    private final String message;
    private final String description;

    public ErrorMessage(final int statusCode, final Date timestamp, final String message, final String description) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.description = description;
    }
}
