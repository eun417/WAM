package com.chungjin.wam.domain.payment.entity;

import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.support.entity.Support;
import com.chungjin.wam.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Entity
public class PaymentInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column(name = "payment_amount")
    private Long paymentAmount;

    @Column(name = "payment_uid")
    private String paymentUid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "support_id")
    private Support support;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
