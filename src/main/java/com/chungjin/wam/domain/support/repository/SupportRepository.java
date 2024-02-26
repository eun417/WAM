package com.chungjin.wam.domain.support.repository;

import com.chungjin.wam.domain.support.entity.AnimalSubjects;
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

    //제목+내용을 기준으로 후원을 페이지별로 조회
    @Query("SELECT s FROM Support s WHERE s.title LIKE %:keyword% OR s.content LIKE %:keyword%")
    Page<Support> findByTitleOrContentContaining(@Param("keyword") String keyword, Pageable pageable);

    //동물 분류(태그)를 기준으로 후원을 페이지별로 조회
    @Query("SELECT s FROM Support s WHERE CAST(s.animalSubjects AS string) LIKE %:keyword%")
    Page<Support> findByAnimalSubjectsContaining(@Param("keyword") String keyword, Pageable pageable);

}
