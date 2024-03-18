package com.chungjin.wam.domain.map.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MapDataDto {

    private List<String> coordinates;
    private String realmCode;
    private String year;
    private String speciesName;
    private String speciesEngName;
    private String pointLnPynSeCode;
    private String tme;
    private String beginDate;
    private String endDate;

}
