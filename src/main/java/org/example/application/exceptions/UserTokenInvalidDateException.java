package org.example.application.exceptions;

import java.time.Instant;
import java.util.Date;

public class UserTokenInvalidDateException extends RuntimeException {
    public UserTokenInvalidDateException(Instant time,String message) {
        super(String.format("%s, Date: %s", message, Date.from(time)));
    }
}