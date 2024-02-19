package com.chungjin.wam.domain.qna.entity;

import com.chungjin.wam.domain.member.entity.Member;
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
public class Qna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qnaId;

    private String title;

    private String content;

    @Column(name = "create_date", updatable = false)
    private String createDate;

    @Column(name = "view_count")
    private int viewCount;

    private String answer;

    @Column(name = "answer_date")
    private String answerDate;

    @Column(name = "qna_check")
    @Enumerated(EnumType.STRING)
    private QnaCheck qnaCheck;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @PrePersist
    protected void onCreate() {
        //엔터티가 영속화되기 전에 현재 날짜로 초기화
        this.createDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }

    @PreUpdate
    protected void onAnswer() {
        //answer 필드가 업데이트되면 현재 시간으로 answer_date를 설정
        if (this.answer != null) {
            this.answerDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            this.qnaCheck = QnaCheck.ANSWERED;
        }
    }

    //조회수 증가
    public void updateViewCount(int viewCount) {
        this.viewCount++;
    }

}
