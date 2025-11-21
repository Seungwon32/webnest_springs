package com.app.webnest.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WordAiException extends RuntimeException {
    public WordAiException(String message) {
        super(message);
    }
}
