package com.drowsiness.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApiResult<T> implements Serializable {
    private Status status = Status.SUCCESS;
    private String message;
    private Object data;
    public enum Status {
        SUCCESS, FAILED;
    }

    public ApiResult(Object data, String message) {
        this.message = message;
        this.data = data;
    }
}
