package com.chungjin.wam.domain.support.repository.querydsl;

import com.chungjin.wam.domain.support.entity.Support;

import java.util.List;

public interface SupportQuerydslRepository {

    //모든 후원을 페이지별로 조회
    List<Support> paginationNoOffset(Long supportId, int pageSize);

}
