package com.bevis.appbe.web.rest.vm;

import lombok.Data;

@Data
public class Pagination {
    private Integer defaultPageSize;
    private Integer page;
    private Long totalCount;
}
