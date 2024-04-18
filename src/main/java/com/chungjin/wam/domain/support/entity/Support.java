package com.chungjin.wam.domain.support.entity;

import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Support extends BaseTimeEntity {

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

    private String content;

    @Column(name = "comment_check")
    private Boolean commentCheck;

    @Column(name = "support_like")
    private Long supportLike;

    @Column(name = "support_amount")
    private Long supportAmount;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Support(String title, AnimalSubjects animalSubjects, Long goalAmount, String startDate, String endDate, String firstImg, String content, Boolean commentCheck, Member member, SupportStatus supportStatus) {
        this.title = title;
        this.animalSubjects = animalSubjects;
        this.goalAmount = goalAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.firstImg = firstImg;
        this.content = content;
        this.commentCheck = commentCheck;
        this.member = member;
        this.supportStatus = supportStatus;
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

    //후원 받은 금액 수정
    public void updateSupportAmount(Long paymentAmount) {
        this.supportAmount += paymentAmount;
    }

    //상태 수정
    public void updateSupportStatus(SupportStatus supportStatus) {
        this.supportStatus = supportStatus;
    }

}
