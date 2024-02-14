package com.chungjin.wam.domain.member.repository;

import com.chungjin.wam.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 이미 가입되어 있는 사용자 확인
    boolean existsByEmail(String email);

    //이메일, 비밀번호로 사용자 찾기
    Optional<Member> findByEmailAndPassword(String email, String password);

    //이메일로 사용자 찾기
    Optional<Member> findByEmail(String email);

    //name, phoneNumber로 email 찾기
    Member findByNameAndPhoneNumber(String name, String phoneNumber);

}
