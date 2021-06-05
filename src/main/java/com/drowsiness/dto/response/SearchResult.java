package com.drowsiness.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Optional;

@Data
public class SearchResult<T> implements Serializable {
    private Object result;

    public SearchResult() {

    }

    public SearchResult(Object result) {
        this.result = result;
    }


}
