package com.chungjin.wam.domain.qna.dto;

import com.chungjin.wam.domain.member.dto.response.MyQnaResponseDto;
import com.chungjin.wam.domain.qna.dto.request.QnaAnswerRequestDto;
import com.chungjin.wam.domain.qna.dto.request.UpdateQnaRequestDto;
import com.chungjin.wam.domain.qna.dto.response.QnaResponseDto;
import com.chungjin.wam.domain.qna.entity.Qna;
import com.chungjin.wam.global.util.GenericMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QnaMapper extends GenericMapper<QnaResponseDto, Qna> {

    List<MyQnaResponseDto> toMyQnaDtoList(List<Qna> entityList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromUpdateDto(UpdateQnaRequestDto dto, @MappingTarget Qna entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromAnswerDto(QnaAnswerRequestDto dto, @MappingTarget Qna entity);

}