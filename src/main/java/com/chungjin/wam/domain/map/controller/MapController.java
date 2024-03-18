package com.chungjin.wam.domain.map.controller;

import com.chungjin.wam.domain.map.dto.MapDataDto;
import com.chungjin.wam.domain.map.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/animal-map")
public class MapController {

    private final MapService mapService;

    /**
     * 포유류 정보 조회
     */
    @GetMapping("/mml")
    public ResponseEntity<List<MapDataDto>> callmmlMapApi() {
        return ResponseEntity.ok(mapService.getMmlMap());
    }

    /**
     * 조류 정보 조회
     */
    @GetMapping("/birds")
    public ResponseEntity<List<MapDataDto>> callBirdsMapApi() {
        return ResponseEntity.ok(mapService.getBirdsMap());
    }

    /**
     * 양서파충류 정보 조회
     */
    @GetMapping("/amnrp")
    public ResponseEntity<List<MapDataDto>> callAmnrpMapApi() {
        return ResponseEntity.ok(mapService.getAmnrpMap());
    }

    /**
     * 어류 정보 조회
     */
    @GetMapping("/fishes")
    public ResponseEntity<List<MapDataDto>> callFishesMapApi() {
        return ResponseEntity.ok(mapService.getFishesMap());
    }

}
