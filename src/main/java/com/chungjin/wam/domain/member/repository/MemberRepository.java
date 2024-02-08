package com.chungjin.wam.domain.member.repository;

import com.chungjin.wam.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 이미 가입되어 있는 사용자 확인
    boolean existsByEmail(String email);

    //name, phoneNumber로 email 찾기
    Member findByNameAndPhoneNumber(String name, String phoneNumber);

}
