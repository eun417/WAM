package com.chungjin.wam.domain.support.dto;

import com.chungjin.wam.domain.member.dto.response.MySupportResponseDto;
import com.chungjin.wam.domain.support.dto.request.UpdateSupportRequestDto;
import com.chungjin.wam.domain.support.dto.response.SupportResponseDto;
import com.chungjin.wam.domain.support.entity.Support;
import com.chungjin.wam.global.util.GenericMapper;
import org.mapstruct.*;

import java.util.List;

//ReportingPolicy.IGNORE: 매핑되지 않은 속성에 대해 무시
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SupportMapper extends GenericMapper<SupportResponseDto, Support> {

    List<MySupportResponseDto> toMySupportDtoList(List<Support> entityList);

    //NullValuePropertyMappingStrategy.IGNORE: 소스 객체의 속성이 null인 경우 매핑을 무시, 대상 객체의 해당 속성은 변경되지 않음
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateSupportRequestDto dto, @MappingTarget Support entity);

}