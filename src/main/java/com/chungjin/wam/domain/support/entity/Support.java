package com.chungjin.wam.domain.support.entity;

import com.chungjin.wam.domain.comment.entity.CommentCheck;
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
public class Support {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supportId;

    @Enumerated(EnumType.STRING)
    private AnimalSubjects animalSubjects;

    private String title;

    @Column(name = "goal_amount")
    private Long goalAmount;

    @Column(name = "support_status")
    @Enumerated(EnumType.STRING)
    private SupportStatus supportStatus;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "first_img")
    private String firstImg;

    private String subheading;

    private String content;

    @Column(name = "comment_check")
    @Enumerated(EnumType.STRING)
    private CommentCheck commentCheck;

    @Column(name = "support_like")
    private Long supportLike;

    @Column(name = "support_amount")
    private Long supportAmount;

    @Column(name = "create_date", updatable = false)
    private String createDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @PrePersist
    protected void onCreate() {
        //엔터티가 영속화되기 전에 현재 날짜로 초기화
        this.createDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }

    //좋아요 증가
    public void upLike(Long supportLike) {
        this.supportLike++;
    }

    //좋아요 감소
    public void downLike(Long supportLike) {
        this.supportLike--;
    }

    //대표 이미지 수정
    public void updateFirstImg(String imgPath) {
        this.firstImg = imgPath;
    }

}
