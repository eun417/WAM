package com.chungjin.wam.domain.map.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MapDataDto {

    private List<Double> coordinates;
    private String year;
    private String speciesName;
    private String areaName;
    private String beginDate;
    private String endDate;

}
