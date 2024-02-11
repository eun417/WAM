package com.chungjin.wam.domain.qna.dto;

import com.chungjin.wam.domain.qna.dto.QnaDto;
import com.chungjin.wam.domain.qna.entity.Qna;
import com.chungjin.wam.global.util.GenericMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface QnaMapper extends GenericMapper<QnaDto, Qna> {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(QnaAnswerRequestDto dto, @MappingTarget Qna entity);

}