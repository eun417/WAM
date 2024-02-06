package com.chungjin.wam.domain.member.dto;

import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.global.util.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper extends GenericMapper<MemberDto, Member> {

}
