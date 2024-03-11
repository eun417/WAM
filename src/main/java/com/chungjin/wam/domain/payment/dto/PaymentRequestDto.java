package com.chungjin.wam.domain.payment.dto;

import lombok.Getter;

@Getter
public class PaymentRequestDto {

    private Long supportId;
    private Long inputAmount;
    private String impUid;

}
