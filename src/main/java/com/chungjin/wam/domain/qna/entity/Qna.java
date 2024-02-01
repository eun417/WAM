package com.chungjin.wam.domain.qna.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Qna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qnaId;

    //member 테이블에서 가져옴
    private String email;

    private String nickname;

    private String title;

    private String content;

    @Column(name = "create_date")
    private String createDate;

    @Column(name = "view_count")
    private int viewCount;

    private String answer;

    @Column(name = "answer_date")
    private String answerDate;

    @Column(name = "qna_check")
    @Enumerated(EnumType.STRING)
    private QnaCheck qnaCheck;

}
