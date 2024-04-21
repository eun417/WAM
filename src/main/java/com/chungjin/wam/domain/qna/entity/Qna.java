package com.chungjin.wam.domain.qna.entity;

import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  //의미 없는 객체 생성 막음
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

    @Builder
    public Qna(String title, String content, Long viewCount, QnaCheck qnaCheck, Member member) {
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.qnaCheck = qnaCheck;
        this.member = member;
    }

    @PreUpdate
    protected void onAnswer() {
        //answer 필드가 업데이트되면 answer_date 를 현재 날짜로 설정
        if (this.answer != null) {
            this.answerDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            this.qnaCheck = QnaCheck.ANSWERED;
        }
    }

    //조회수 증가
    public void upViewCount() {
        this.viewCount++;
    }

}
