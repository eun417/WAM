package com.chungjin.wam.domain.member.dto;

import com.chungjin.wam.domain.member.dto.request.UpdateMemberRequestDto;
import com.chungjin.wam.domain.member.dto.response.MemberResponseDto;
import com.chungjin.wam.domain.member.dto.response.MyQnaResponseDto;
import com.chungjin.wam.domain.member.dto.response.MySupportResponseDto;
import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.qna.entity.Qna;
import com.chungjin.wam.domain.support.entity.Support;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {

    MemberResponseDto toDto(Member e);
    MyQnaResponseDto toDto(Qna e);
    MySupportResponseDto toDto(Support e);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateMemberRequestDto dto, @MappingTarget Member entity);

}
