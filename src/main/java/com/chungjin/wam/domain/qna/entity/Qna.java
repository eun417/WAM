package com.chungjin.wam.domain.qna.entity;

import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Qna extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qnaId;

    private String title;

    private String content;

    @Column(name = "view_count")
    private Long viewCount;

    private String answer;

    @Column(name = "answer_date")
    private String answerDate;

    @Column(name = "qna_check")
    @Enumerated(EnumType.STRING)
    private QnaCheck qnaCheck;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @PreUpdate
    protected void onAnswer() {
        //answer 필드가 업데이트되면 answer_date를 현재 날짜로 설정
        if (this.answer != null) {
            this.answerDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            this.qnaCheck = QnaCheck.ANSWERED;
        }
    }

    //조회수 증가
    public void updateViewCount(Long viewCount) {
        this.viewCount++;
    }

}
