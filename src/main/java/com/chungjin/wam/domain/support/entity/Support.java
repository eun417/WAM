package com.chungjin.wam.domain.support.entity;

import com.chungjin.wam.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

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
    private int goalAmount;

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
    private int supportLike;

    @Column(name = "support_amount")
    private int supportAmount;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
