package com.chungjin.wam.domain.support.repository;

import com.chungjin.wam.domain.support.entity.Support;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportRepository extends JpaRepository<Support, Long> {

    //memberId를 기준으로 해당 회원이 작성한 후원을 페이지별로 조회
    @Query("select s from Support s where s.member.memberId = :memberId")
    Page<Support> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);

}
