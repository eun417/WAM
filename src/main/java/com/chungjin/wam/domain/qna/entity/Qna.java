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
    @Column(name = "member_email")
    private String memberEmail;

    @Column(name = "member_nickname")
    private String memberNickname;

    @Column(name = "qna_title")
    private String qnaTitle;

    @Column(name = "qna_content")
    private String qnaContent;

    @Column(name = "qna_img")
    private String qnaImg;

    @Column(name = "qna_date")
    private String qnaDate;

    @Column(name = "qna_view")
    private int qnaView;

    @Column(name = "qna_check")
    @Enumerated(EnumType.STRING)
    private QnaCheck qnaCheck;

}
