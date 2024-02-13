package com.chungjin.wam.domain.support.repository;

import com.chungjin.wam.domain.support.entity.Support;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportRepository extends JpaRepository<Support, Long> {
}
