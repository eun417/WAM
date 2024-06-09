package com.chungjin.wam.domain.support.repository.querydsl;

import com.chungjin.wam.domain.support.entity.Support;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.chungjin.wam.domain.support.entity.QSupport.support;

@Repository
@RequiredArgsConstructor
public class SupportQuerydslRepositoryImpl implements SupportQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Support> paginationNoOffset(Long supportId, int pageSize) {
        return queryFactory
                .selectFrom(support)
                .where(ltSupportId(supportId))
                .orderBy(support.supportId.desc())
                .limit(pageSize)
                .fetch();
    }

    //주어진 supportId 보다 작은지 확인
    private BooleanExpression ltSupportId(Long supportId) {
        if (supportId == null) {
            return null;
        }
        return support.supportId.lt(supportId);
    }

}