package com.wato.watobackend.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class PageDto {

    private Object list;

    private int page;

    private int size;

    private int totalPage;

    private int totalSize;

    @Builder
    public PageDto(Object list, int page, int size, int totalPage, int totalSize) {
        this.list = list;
        this.page = page;
        this.size = size;
        this.totalPage = totalPage;
        this.totalSize = totalSize;
    }
}
