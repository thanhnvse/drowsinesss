package com.drowsiness.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Data
public class SearchListResult<T> implements Serializable {
    private List<T> results;

    public SearchListResult(List<T> results) {
        this.results = results;
    }
}
