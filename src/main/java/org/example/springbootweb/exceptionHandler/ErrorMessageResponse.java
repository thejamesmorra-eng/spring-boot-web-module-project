package org.example.springbootweb.exceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorMessageResponse {

    private int statusCode;
    private String error;
    private String message;
    private LocalDateTime localDateTime;
}
