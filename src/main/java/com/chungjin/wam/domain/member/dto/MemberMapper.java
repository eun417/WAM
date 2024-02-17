package com.chungjin.wam.domain.member.dto;

import com.chungjin.wam.domain.member.dto.request.UpdateMemberRequestDto;
import com.chungjin.wam.domain.member.dto.response.MemberDto;
import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.global.util.GenericMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper extends GenericMapper<MemberDto, Member> {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateMemberRequestDto dto, @MappingTarget Member entity);

}
