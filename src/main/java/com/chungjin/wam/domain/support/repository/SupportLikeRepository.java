package com.chungjin.wam.domain.support.repository;

import com.chungjin.wam.domain.support.entity.Support;
import com.chungjin.wam.domain.like.entity.SupportLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportLikeRepository extends JpaRepository<SupportLike, Long> {

    //memberId를 기준으로 해당 회원이 좋아요한 후원을 페이지별로 조회
    @Query("select s from SupportLike sl JOIN sl.support s where sl.member.memberId = :memberId")
    Page<Support> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);

}
