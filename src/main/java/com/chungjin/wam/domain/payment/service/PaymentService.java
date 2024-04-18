package com.chungjin.wam.domain.payment.service;

import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import com.chungjin.wam.domain.payment.dto.PaymentRequestDto;
import com.chungjin.wam.domain.payment.entity.PaymentInfo;
import com.chungjin.wam.domain.payment.repository.PaymentRepository;
import com.chungjin.wam.domain.support.entity.Support;
import com.chungjin.wam.domain.support.entity.SupportStatus;
import com.chungjin.wam.domain.support.repository.SupportRepository;
import com.chungjin.wam.global.exception.CustomException;
import com.chungjin.wam.global.exception.error.ErrorCodeType;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.chungjin.wam.global.exception.error.ErrorCodeType.PAYMENT_AMOUNT_MISMATCH;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final SupportRepository supportRepository;

    /**
     * 후원금액 결제
     */
    public Payment createPayment(IamportClient iamportClient, Long memberId, PaymentRequestDto paymentReq) {
        //사용자가 입력한 금액과 사용자가 결제한 금액이 같은지 확인
        IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(paymentReq.getImpUid());
        long paidAmount = iamportResponse.getResponse().getAmount().longValue(); //사용자가 실제 결제한 금액
        log.info("실제로 결제한 금액: "+paidAmount);
        log.info("사용자가 입력한 금액: "+paymentReq.getInputAmount());

        //금액이 다르면 결제 취소, 에러 발생
        if (paidAmount != paymentReq.getInputAmount()) {
            CancelData cancelData = cancelPayment(iamportResponse);
            iamportClient.cancelPaymentByImpUid(cancelData);

            //에러 발생
            throw new CustomException(PAYMENT_AMOUNT_MISMATCH);
        }

        //기존 금액에서 후원 받은 금액 추가, 결제 정보 저장
        createPaymentInfo(memberId, paymentReq, paidAmount);

        return iamportResponse.getResponse();
    }

    /*결제 취소*/
    public CancelData cancelPayment(IamportResponse<Payment> response) {
        return new CancelData(response.getResponse().getImpUid(), true);
    }

    /*기존 금액에서 후원 받은 금액 추가, 결제 정보 저장*/
    public void createPaymentInfo(Long memberId, PaymentRequestDto paymentReq, Long paidAmount) {
        //memberId로 Member 객체 가져오기
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCodeType.MEMBER_NOT_FOUND));

        //supportId로 support 객체 가져오기
        Support support = supportRepository.findById(paymentReq.getSupportId())
                .orElseThrow(() -> new CustomException(ErrorCodeType.SUPPORT_NOT_FOUND));

        //해당 후원글의 후원 받은 금액 수정
        support.updateSupportAmount(paidAmount);

        //Dto -> Entity
        PaymentInfo paymentInfo = PaymentInfo.builder()
                .paymentAmount(paidAmount)
                .paymentUid(paymentReq.getImpUid())
                .support(support)
                .member(member)
                .build();

        //DB에 저장
        paymentRepository.save(paymentInfo);

        //후원 글의 첫 결제 여부 확인
        boolean isFirstPayment = support.getSupportStatus() == SupportStatus.START;

        //첫 결제인 경우에만 후원 상태를 "후원중"으로 업데이트
        if (isFirstPayment) {
            support.updateSupportStatus(SupportStatus.SUPPORTING);
        }
    }

    /**
     * 총 후원금 조회
     */
    public Long readTotalPaymentAmount() {
        return paymentRepository.findTotalPaymentAmount();
    }
}
