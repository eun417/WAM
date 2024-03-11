package com.chungjin.wam.domain.payment.repository;

import com.chungjin.wam.domain.payment.entity.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentInfo, Long> {
}
