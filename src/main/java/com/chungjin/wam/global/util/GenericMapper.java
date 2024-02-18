package com.chungjin.wam.global.util;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

public interface GenericMapper<D, E> {

    D toDto(E e);
    E toEntity(D d);

    List<D> toDtoList(List<E> entityList);
    List<E> toEntityList(List<D> dtoList);

}
