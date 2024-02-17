package com.chungjin.wam.domain.qna.dto;

import com.chungjin.wam.domain.qna.dto.request.QnaAnswerRequestDto;
import com.chungjin.wam.domain.qna.dto.request.UpdateQnaRequestDto;
import com.chungjin.wam.domain.qna.entity.Qna;
import com.chungjin.wam.global.util.GenericMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QnaMapper extends GenericMapper<QnaDto, Qna> {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromUpdateDto(UpdateQnaRequestDto dto, @MappingTarget Qna entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromAnswerDto(QnaAnswerRequestDto dto, @MappingTarget Qna entity);

}