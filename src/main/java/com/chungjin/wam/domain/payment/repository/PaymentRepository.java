package com.chungjin.wam.domain.payment.repository;

import com.chungjin.wam.domain.payment.entity.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentInfo, Long> {

    //총 후원금 조회 (기본값: 0)
    @Query("SELECT COALESCE(SUM(p.paymentAmount), 0) FROM PaymentInfo p")
    Long findTotalPaymentAmount();

}
