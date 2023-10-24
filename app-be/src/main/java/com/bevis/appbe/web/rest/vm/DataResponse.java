package com.bevis.appbe.web.rest.vm;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class DataResponse<T> {

    private List<T> data;
    private Pagination pagination;
    private Object metadata;

    public static <T> DataResponse<T> of(Page<T> page, Object metadata) {
        Pagination pagination = new Pagination();
        pagination.setPage(page.getNumber());
        pagination.setDefaultPageSize(page.getSize());
        pagination.setTotalCount(page.getTotalElements());
        return new DataResponse<>(page.getContent(), pagination, metadata);
    }

    public static <T> DataResponse<T> of(List<T> data, Object metadata) {
        return new DataResponse<>(data, null, metadata);
    }

    public static <T> DataResponse<T> of(Page<T> page) {
        return of(page, null);
    }

    public static <T> DataResponse<T> of(List<T> list) {
        Pagination pagination = null;
        if (Objects.nonNull(list)) {
            pagination = new Pagination();
            pagination.setTotalCount((long) list.size());
        }
        return new DataResponse<>(list, pagination, null);
    }

    public <T2> DataResponse<T2> map(Function<T, T2> mapper) {
        return new DataResponse<>(data.stream().map(mapper).collect(Collectors.toList()),
                pagination, null);
    }
}
