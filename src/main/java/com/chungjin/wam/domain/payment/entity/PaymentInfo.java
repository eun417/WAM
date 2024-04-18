package com.chungjin.wam.domain.payment.entity;

import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.support.entity.Support;
import com.chungjin.wam.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public PaymentInfo(Long paymentAmount, String paymentUid, Support support, Member member) {
        this.paymentAmount = paymentAmount;
        this.paymentUid = paymentUid;
        this.support = support;
        this.member = member;
    }

}
