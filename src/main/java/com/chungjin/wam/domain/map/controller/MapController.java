package com.chungjin.wam.domain.map.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/animal-map")
public class MapController {

    @Value("${naver.client.id}")
    String clientId;

    /**
     * 포유류 지도 조회
     */
    @GetMapping("/mml")
    public String goMmlMap(Model model) {
        model.addAttribute("clientId", clientId);
        return "map/mmlMap";
    }

    /**
     * 조류 지도 조회
     */
    @GetMapping("/birds")
    public String goBirdsMap(Model model) {
        model.addAttribute("clientId", clientId);
        return "map/birdsMap";
    }

    /**
     * 양서파충류 지도 조회
     */
    @GetMapping("/amnrp")
    public String goAmnrpMap(Model model) {
        model.addAttribute("clientId", clientId);
        return "map/AmnrpMap";
    }

    /**
     * 어류 지도 조회
     */
    @GetMapping("/fishes")
    public String goFishesMap(Model model) {
        model.addAttribute("clientId", clientId);
        return "map/fishesMap";
    }

}
