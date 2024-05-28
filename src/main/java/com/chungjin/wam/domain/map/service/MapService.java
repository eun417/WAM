package com.chungjin.wam.domain.map.service;

import com.chungjin.wam.domain.map.dto.MapDataDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import java.io.*;
import java.util.ArrayList;
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

            try (BufferedReader rd = new BufferedReader(new InputStreamReader(uri.toURL().openStream()))) {
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }

                return extractData(convertXmlToJson(sb.toString()), typeName);
            } catch (Exception e) {
                e.printStackTrace();
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    //조회한 데이터(XML)를 파싱하여 Dto 에 저장하는 함수
    private List<MapDataDto> extractData(String jsonData, String typeName) {


        try {
            //JSON 문자열을 JsonNode 객체로 변환
            ObjectMapper jsonMapper = new ObjectMapper();
            JsonNode rootNode = jsonMapper.readTree(jsonData);
            JsonNode featureMembersNode = rootNode.path("featureMember");

            List<MapDataDto> mapDataDtoList = new ArrayList<>();
            for (JsonNode featureMemberNode : featureMembersNode) {
                //야생동물 종류(typeName)에 따라 설정
                JsonNode mvMapEcpeMmlPointNode = featureMemberNode.path(typeName);

                //위도, 경도
                List<Double> coordinateList = extractCoordinates(mvMapEcpeMmlPointNode.path("geom").path("Point").path("coordinates").path("").asText());

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

    //XML -> Json 변환 함수
    private String convertXmlToJson(String xml) throws IOException {
        ObjectMapper xmlMapper = new XmlMapper();
        JsonNode node = xmlMapper.readTree(xml.getBytes());
        ObjectMapper jsonMapper = new ObjectMapper();
        return jsonMapper.writeValueAsString(node);
    }

    //위도, 경도 추출하여 List 에 저장하는 함수
    private List<Double> extractCoordinates(String coordinates) {
        String[] coordinatePairs = coordinates.split(",");  //좌표 데이터 분리
        List<Double> coordinateList = new ArrayList<>();    //Double로 형 변환하여 List 저장
        coordinateList.add(Double.parseDouble(coordinatePairs[0])); // 위도
        coordinateList.add(Double.parseDouble(coordinatePairs[1])); // 경도
        return coordinateList;
    }

}