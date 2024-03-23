package com.chungjin.wam.domain.map.service;

import com.chungjin.wam.domain.map.dto.MapDataDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.chungjin.wam.global.util.Constants.*;

@Slf4j
@Service
@Transactional
public class MapService {

    private final String serviceKey;

    public MapService(@Value("${ecobank.servicekey}") String serviceKey) {
        this.serviceKey = serviceKey;
    }

    /**
     * 포유류 정보 조회
     */
    public List<MapDataDto> getMmlMap() {
        try {
            return extractData(getAnimalData(MML_URL_DETAIL, MML_TYPE_NAME), MML_TYPE_NAME);
        } catch (Exception e) {
            e.getStackTrace();  //예외 추적 정보 출력
            return Collections.emptyList(); //빈 리스트 반환
        }
    }

    /**
     * 조류 정보 조회
     */
    public List<MapDataDto> getBirdsMap() {
        try {
            return extractData(getAnimalData(BIRDS_URL_DETAIL, BIRDS_TYPE_NAME), BIRDS_TYPE_NAME);
        } catch (Exception e) {
            e.getStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * 양서파충류 정보 조회
     */
    public List<MapDataDto> getAmnrpMap() {
        try {
            return extractData(getAnimalData(AMNRP_URL_DETAIL, AMNRP_TYPE_NAME), AMNRP_TYPE_NAME);
        } catch (Exception e) {
            e.getStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * 어류 정보 조회
     */
    public List<MapDataDto> getFishesMap() {
        try {
            return extractData(getAnimalData(FISHES_URL_DETAIL, FISHES_TYPE_NAME), FISHES_TYPE_NAME);
        } catch (Exception e) {
            e.getStackTrace();
            return Collections.emptyList();
        }
    }

    //조회한 데이터(XML)를 파싱하여 Dto에 저장하는 함수
    public List<MapDataDto> extractData(String xmlData, String typeName) {
        List<MapDataDto> mapDataDtoList = new ArrayList<>();

        try {
            //XML 파싱을 위한 DocumentBuilderFactory 생성
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            DocumentBuilder builder = factory.newDocumentBuilder();

            //XML 데이터를 InputStream으로 변환하여 Document로 파싱
            ByteArrayInputStream input = new ByteArrayInputStream(xmlData.getBytes());
            Document document = builder.parse(input);

            //필요한 데이터를 추출하기 위해 XML 트리를 탐색
            NodeList featureList = document.getElementsByTagName("gml:featureMember");

            log.info("야생동물 데이터 조회: {}", featureList.getLength());

            //featureMember 태그 내의 데이터 추출
            for (int i = 0; i < featureList.getLength(); i++) {
                Node featureNode = featureList.item(i);
                if (featureNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element featureElement = (Element) featureNode;
                    NodeList ecoBankList = featureElement.getElementsByTagName("EcoBank:" + typeName);

                    if (ecoBankList.getLength() > 0) {
                        Element ecoBankElement = (Element) ecoBankList.item(0);

                        String speciesName = getElementTextContent(ecoBankElement, "EcoBank:spcs_korean_nm");
                        if (speciesName.equals("알수없음")) {
                            continue;
                        }
                        String coordinates = getElementTextContent(ecoBankElement, "EcoBank:geom");
                        String year = getElementTextContent(ecoBankElement, "EcoBank:examin_year");
                        String areaName = getElementTextContent(ecoBankElement, "EcoBank:examin_area_nm");
                        String beginDate = getElementTextContent(ecoBankElement, "EcoBank:examin_begin_de");
                        String endDate = getElementTextContent(ecoBankElement, "EcoBank:examin_end_de");

                        //좌표 데이터 분리
                        String[] coordinatePairs = coordinates.split(",");

                        //Double로 형 변환하여 List 저장
                        List<Double> coordinateList = new ArrayList<>();
                        coordinateList.add(Double.parseDouble(coordinatePairs[0])); // 위도
                        coordinateList.add(Double.parseDouble(coordinatePairs[1])); // 경도

                        //DTO에 저장
                        MapDataDto mapDataDto = MapDataDto.builder()
                                .coordinates(coordinateList)
                                .year(year)
                                .speciesName(speciesName)
                                .areaName(areaName)
                                .beginDate(beginDate)
                                .endDate(endDate)
                                .build();

                        mapDataDtoList.add(mapDataDto);
                    }
                }
            }
        } catch (Exception e) {
            e.getStackTrace();  //예외 추적 정보 출력
        }

        return mapDataDtoList;
    }

    //null 처리하는 함수
    private String getElementTextContent(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        return nodeList.getLength() > 0 ? nodeList.item(0).getTextContent() : "알수없음";
    }

    //자연환경조사 데이터 가져오는 함수
    public String getAnimalData(String urlDetail, String typeName) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append(urlDetail);
        StringBuilder parameter = new StringBuilder();
        parameter.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
        parameter.append("&" + URLEncoder.encode("srs", "UTF-8") + "=" + URLEncoder.encode("EPSG:5186", "UTF-8"));
        parameter.append("&" + URLEncoder.encode("bbox", "UTF-8") + "=" + URLEncoder.encode(BBOX, "UTF-8"));
        parameter.append("&" + URLEncoder.encode("typeName", "UTF-8") + "=" + URLEncoder.encode(typeName, "UTF-8"));
        parameter.append("&" + URLEncoder.encode("maxFeatures", "UTF-8") + "=" + URLEncoder.encode("500", "UTF-8"));

        URL url = new URL(urlBuilder.toString() + parameter.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        StringBuilder sb = new StringBuilder();
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        return sb.toString();
    }

}