package com.jojos.challenge.sudoku.domain;

import org.springframework.http.HttpStatus;

/**
 * General purpose class encapsulating error information
 * to be returned as part of the response body in case of application exceptions
 *
 * Created by karanikasg@gmail.com.
 */
public class ErrorInfo {
    // making these fields public is necessary in order to be readable in the response body
    public final int status;
    public final String reason;
    public final String url;
    public final String message;

    public ErrorInfo(HttpStatus httpStatus, String url, Exception exception) {
        this.status = httpStatus.value();
        this.reason = httpStatus.getReasonPhrase();
        this.url = url;
        this.message = exception.getMessage();
    }

    @Override
    public String toString() {
        return "ErrorInfo{" +
                "status=" + status +
                ", reason='" + reason + '\'' +
                ", url='" + url + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}