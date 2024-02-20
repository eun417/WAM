package com.chungjin.wam.domain.qna.repository;

import com.chungjin.wam.domain.qna.entity.Qna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Long> {

    //memberId를 기준으로 해당 회원이 작성한 QnA를 페이지별로 조회
    @Query("select q from Qna q where q.member.memberId = :memberId")
    Page<Qna> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);

}
