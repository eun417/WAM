package com.chungjin.wam.global.common;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class PageResponse {

    private List<Object> content;
    private int pageNo;
    private int pageSize;
    private Long totalElements;
    private int totalPages;
    private boolean last;

    public static PageResponse createPageResponse(List<Object> content, Page<?> page, int pageNo) {
        return PageResponse.builder()
                .content(content)   //목록
                .pageNo(pageNo) //현재 페이지 번호
                .pageSize(page.getSize())   //페이지당 항목 수
                .totalElements(page.getTotalElements()) //전체 요소 수
                .totalPages(page.getTotalPages())   //전체 페이지 수
                .last(page.isLast())    //마지막 페이지 여부
                .build();
    }

}
