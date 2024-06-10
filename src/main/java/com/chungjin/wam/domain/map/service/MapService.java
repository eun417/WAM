package com.chungjin.wam.domain.map.service;

import com.chungjin.wam.domain.map.dto.MapDataDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.URI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.chungjin.wam.global.util.Constants.*;

@Slf4j
@Service
public class MapService {

    private final String serviceKey;

    public MapService(@Value("${ecobank.servicekey}") String serviceKey) {
        this.serviceKey = serviceKey;
    }

    /**
     * 포유류 정보 조회
     */
    public List<MapDataDto> getMmlMap() {
        return getAnimalData(MML_URL_DETAIL, MML_TYPE_NAME);
    }

    /**
     * 조류 정보 조회
     */
    public List<MapDataDto> getBirdsMap() {
        return getAnimalData(BIRDS_URL_DETAIL, BIRDS_TYPE_NAME);
    }

    /**
     * 양서파충류 정보 조회
     */
    public List<MapDataDto> getAmnrpMap() {
        return getAnimalData(AMNRP_URL_DETAIL, AMNRP_TYPE_NAME);
    }

    /**
     * 어류 정보 조회
     */
    public List<MapDataDto> getFishesMap() {
        return getAnimalData(FISHES_URL_DETAIL, FISHES_TYPE_NAME);
    }

    //자연환경조사 데이터 가져오는 함수
    private List<MapDataDto> getAnimalData(String urlDetail, String typeName) {
        try {
            URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL + urlDetail)
                    .queryParam("serviceKey", serviceKey)
                    .queryParam("srs", SRC)
                    .query("bbox=" + BBOX)
                    .queryParam("typeName", typeName)
                    .queryParam("maxFeatures", 500)
                    .build()
                    .toUri();

            //XML 데이터 읽기
            try (InputStream inputStream = uri.toURL().openStream()) {
                return extractData(inputStream, typeName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    //조회한 데이터(XML)를 파싱하여 Dto 에 저장하는 함수
    private List<MapDataDto> extractData(InputStream xmlInputStream, String typeName) {
        try {
            //XML 데이터를 JsonNode 객체로 변환
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode rootNode = xmlMapper.readTree(xmlInputStream);
            JsonNode featureMembersNode = rootNode.path("featureMember");

            List<MapDataDto> mapDataDtoList = new ArrayList<>();
            for (JsonNode featureMemberNode : featureMembersNode) {
                //야생동물 종류(typeName)에 따라 설정
                JsonNode mvMapEcpeMmlPointNode = featureMemberNode.path(typeName);

                //위도, 경도
                List<Double> coordinateList = extractCoordinates(mvMapEcpeMmlPointNode
                        .path("geom")
                        .path("Point")
                        .path("coordinates")
                        .path("").asText());

                //Json 문자열을 Dto 에 저장
                MapDataDto mapDataDto = MapDataDto.builder()
                        .coordinates(coordinateList)
                        .year(mvMapEcpeMmlPointNode.path("examin_year").asText())
                        .speciesName(mvMapEcpeMmlPointNode.path("spcs_korean_nm").asText())
                        .areaName(mvMapEcpeMmlPointNode.path("examin_area_nm").asText())
                        .beginDate(mvMapEcpeMmlPointNode.path("examin_begin_de").asText())
                        .endDate(mvMapEcpeMmlPointNode.path("examin_end_de").asText())
                        .build();

                mapDataDtoList.add(mapDataDto);
            }
            return mapDataDtoList;

        } catch (Exception e) {
            e.getStackTrace();
            return Collections.emptyList();
        }
    }

    //위도, 경도 추출하여 List 에 저장하는 함수
    private List<Double> extractCoordinates(String coordinates) {
        String[] coordinatePairs = coordinates.split(",");
        return Arrays.asList(Double.parseDouble(coordinatePairs[0]), Double.parseDouble(coordinatePairs[1]));
    }

}