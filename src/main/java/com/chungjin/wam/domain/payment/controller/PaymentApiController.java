package com.chungjin.wam.domain.payment.controller;

import com.chungjin.wam.domain.auth.dto.CustomUserDetails;
import com.chungjin.wam.domain.payment.dto.PaymentRequestDto;
import com.chungjin.wam.domain.payment.service.PaymentService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentApiController {

    private final PaymentService paymentService;

    private IamportClient iamportClient;

    @Value("${imp.api.key}")
    private String apiKey;

    @Value("${imp.api.secretkey}")
    private String secretKey;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, secretKey);
    }

    /**
     * 후원금액 결제
     */
    @PostMapping("/validate")
    public Payment paymentComplete(@AuthenticationPrincipal CustomUserDetails userDetails,
                                   @RequestBody PaymentRequestDto paymentReq) {
        return paymentService.createPayment(iamportClient, userDetails.getMember().getMemberId(), paymentReq);
    }

    /**
     * 총 후원금 조회
     */
    @GetMapping("/total-amount")
    public Long getTotalPaymentAmount() {
        return paymentService.readTotalPaymentAmount();
    }

}
