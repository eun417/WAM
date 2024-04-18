package com.chungjin.wam.domain.support.repository;

import com.chungjin.wam.domain.support.entity.Support;
import com.chungjin.wam.domain.support.entity.SupportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportRepository extends JpaRepository<Support, Long> {

    //memberId를 기준으로 해당 회원이 작성한 후원을 페이지별로 조회
    @Query("select s from Support s where s.member.memberId = :memberId")
    Page<Support> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    //제목+내용을 기준으로 후원을 페이지별로 조회
    @Query("SELECT s FROM Support s WHERE s.title LIKE %:keyword% OR s.content LIKE %:keyword% OR s.member.nickname LIKE %:keyword%")
    Page<Support> findByTitleOrContentOrNicknameContaining(@Param("keyword") String keyword, Pageable pageable);

    //동물 분류(태그)를 기준으로 후원을 페이지별로 조회
    @Query("SELECT s FROM Support s WHERE CAST(s.animalSubjects AS string) LIKE %:keyword%")
    Page<Support> findByAnimalSubjectsContaining(@Param("keyword") String keyword, Pageable pageable);

    //현재 날짜 기준으로 마감일이 24시간 이내인 후원 조회
    @Query("SELECT s FROM Support s WHERE s.endDate <= :endDate AND s.supportStatus != :status")
    List<Support> findByEndDateAndSupportStatusNot(@Param("endDate")String endDate, @Param("status") SupportStatus status);    //endDate 보다 이전, status 가 특정 값이 아닌 엔티티 검색

    //후원 상태를 기준으로 모든 후원 조회
    List<Support> findBySupportStatus(SupportStatus supportStatus);

}
