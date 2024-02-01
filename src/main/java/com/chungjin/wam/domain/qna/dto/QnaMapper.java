package com.chungjin.wam.domain.qna.dto;

import com.chungjin.wam.domain.qna.dto.QnaDto;
import com.chungjin.wam.domain.qna.entity.Qna;
import com.chungjin.wam.global.util.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QnaMapper extends GenericMapper<QnaDto, Qna> {

}
