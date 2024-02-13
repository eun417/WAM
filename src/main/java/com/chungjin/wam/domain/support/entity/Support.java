package com.chungjin.wam.domain.support.entity;

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

    //member FK
    private String email;

    //animal FK
    @Column(name = "animal_id")
    private Long animalId;

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

    @Column(name = "comment_check")
    @Enumerated(EnumType.STRING)
    private CommentCheck commentCheck;

    private String subheading;

    private String content;

    @Column(name = "support_like")
    private Long supportLike;

    @Column(name = "support_amount")
    private Long supportAmount;

}
