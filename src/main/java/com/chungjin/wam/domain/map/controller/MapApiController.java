package com.chungjin.wam.domain.map.controller;

import com.chungjin.wam.domain.map.dto.MapDataDto;
import com.chungjin.wam.domain.map.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

import static com.chungjin.wam.global.util.Constants.MML_TYPE_NAME;
import static com.chungjin.wam.global.util.Constants.MML_URL_DETAIL;


@RestController
@RequiredArgsConstructor
@RequestMapping("/animal-map")
public class MapApiController {

    private final MapService mapService;

    /**
     * 포유류 정보 조회
     */
    @GetMapping("/mml-all")
    public ResponseEntity<String> callAllmmlMapApi() throws IOException {
        return ResponseEntity.ok(mapService.getAnimalData(MML_URL_DETAIL, MML_TYPE_NAME));
    }


    /**
     * 포유류 정보 조회
     */
    @GetMapping("/mml-detail")
    public ResponseEntity<List<MapDataDto>> callMmlMapApi() {
        return ResponseEntity.ok(mapService.getMmlMap());
    }

    /**
     * 조류 정보 조회
     */
    @GetMapping("/birds-detail")
    public ResponseEntity<List<MapDataDto>> callBirdsMapApi() {
        return ResponseEntity.ok(mapService.getBirdsMap());
    }

    /**
     * 양서파충류 정보 조회
     */
    @GetMapping("/amnrp-detail")
    public ResponseEntity<List<MapDataDto>> callAmnrpMapApi() {
        return ResponseEntity.ok(mapService.getAmnrpMap());
    }

    /**
     * 어류 정보 조회
     */
    @GetMapping("/fishes-detail")
    public ResponseEntity<List<MapDataDto>> callFishesMapApi() {
        return ResponseEntity.ok(mapService.getFishesMap());
    }

}
