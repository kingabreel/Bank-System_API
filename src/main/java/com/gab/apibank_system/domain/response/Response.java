package com.gab.apibank_system.domain.response;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
    @Setter
    @Getter
    private T response;

    @Setter
    @Getter
    private List<T> responseList;

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    @Builder.Default
    private String statusCode = "200";

    @Setter
    @Builder.Default
    private boolean error = false;

    public void setSuccess(T response, String message, String statusCode) {
        this.setResponse(response);
        this.setMessage(message);
        this.setStatusCode(statusCode);
    }

    public void setSuccess(List<T> response, String message, String statusCode) {
        this.setResponseList(response);
        this.setMessage(message);
        this.setStatusCode(statusCode);
    }

    public void setError(String message, String statusCode) {
        this.setError(true);
        this.setMessage(message);
        this.setStatusCode(statusCode);
    }
}
