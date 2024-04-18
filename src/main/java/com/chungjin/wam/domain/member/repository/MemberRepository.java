package com.chungjin.wam.domain.member.repository;

import com.chungjin.wam.domain.member.entity.LoginType;
import com.chungjin.wam.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //이미 가입되어 있는 사용자 확인
    boolean existsByEmail(String email);

    //이메일로 사용자 찾기
    Optional<Member> findByEmail(String email);

    //name, phoneNumber로 email 찾기
    Optional<Member> findByNameAndPhoneNumber(String name, String phoneNumber);

    //이름, 이메일로 사용자 확인
    boolean existsByNameAndEmail(String name, String email);

    /**
     * 소셜 타입과 소셜의 식별값으로 회원 찾는 메소드
     * 정보 제공을 동의한 순간 DB에 저장해야하지만, 아직 추가 정보(사는 도시, 나이 등)를 입력받지 않았으므로
     * 유저 객체는 DB에 있지만, 추가 정보가 빠진 상태이다.
     * 따라서 추가 정보를 입력받아 회원 가입을 진행할 때 소셜 타입, 식별자로 해당 회원을 찾기 위한 메소드
     */
    Optional<Member> findByLoginTypeAndOauthId(LoginType loginType, String oauthId);

}
