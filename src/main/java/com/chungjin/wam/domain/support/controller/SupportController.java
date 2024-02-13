package com.chungjin.wam.domain.support.controller;

import com.chungjin.wam.domain.support.dto.SupportDto;
import com.chungjin.wam.domain.support.dto.response.SupportDetailDto;
import com.chungjin.wam.domain.support.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/support")
public class SupportController {

    private final SupportService supportService;

    /**
     * 후원 생성
     * */
    @PostMapping("/")
    public ResponseEntity<String> createSupport(@RequestBody SupportDto supportDto) {
        supportService.createSupport(supportDto);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /**
     * 후원 조회
     * */
    @GetMapping("/{supportId}")
    public ResponseEntity<SupportDetailDto> readSupport(@PathVariable(value = "supportId") Long supportId) {
        return ResponseEntity.ok().body(supportService.readSupport(supportId));
    }

    /**
     * 후원 List 조회 (Pagination)
     * */
    @GetMapping("/page={page}")
    public ResponseEntity<List<SupportDto>> readAllSupport(@PathVariable(value = "page") int page) {
        return ResponseEntity.ok().body(supportService.readAllSupport(page));
    }

    /**
     * 후원 수정
     * */
    @PutMapping("/{supportId}")
    public ResponseEntity<String> updateSupport(@PathVariable(value = "supportId") Long supportId,
                                            @RequestBody SupportDto updateSupportDto) {
        supportService.updateSupport(supportId, updateSupportDto);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /**
     * 후원 삭제
     * */
    @DeleteMapping("/{supportId}")
    public ResponseEntity<String> deleteSupport(@PathVariable(value = "supportId") Long supportId) {
        supportService.deleteSupport(supportId);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}
