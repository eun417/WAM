package com.chungjin.wam.domain.support.repository;

import com.chungjin.wam.domain.support.entity.SupportLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportLikeRepository extends JpaRepository<SupportLike, Long> {
}
