package com.chungjin.wam.domain.support.dto;

import com.chungjin.wam.domain.support.entity.Support;
import com.chungjin.wam.global.util.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupportMapper extends GenericMapper<SupportDto, Support> {
}