package com.chungjin.wam.domain.file.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  //의미 없는 객체 생성 막음
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ImageId;

    @Column(name = "upload_path")
    private String uploadPath;

    @Column(name = "create_date", updatable = false)
    private LocalDate createDate;

    @Enumerated(EnumType.STRING)
    private Board board;

    @Column(name = "board_id")
    private Long boardId;

    @Builder
    public Image(String uploadPath, Board board, Long boardId) {
        this.uploadPath = uploadPath;
        this.board = board;
        this.boardId = boardId;
    }

    @PrePersist
    protected void onCreate() {
        //엔터티가 영속화되기 전에 현재 날짜로 초기화
        this.createDate = LocalDate.now();
    }

}
