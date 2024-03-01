package com.chungjin.wam.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoginType {

    GOOGLE, KAKAO, LOCAL, NAVER

}
