package com.bevis.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DataPage<T> {
    private List<T> data;

    public static <T> DataPage<T> of(List<T> data) {
        return new DataPage<>(data);
    }
}
