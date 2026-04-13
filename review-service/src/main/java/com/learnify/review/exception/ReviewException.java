package com.learnify.review.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReviewException extends RuntimeException {
    public ReviewException(String message) {
        super(message);
    }
}
