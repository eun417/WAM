package com.chungjin.wam.domain.support.repository;

import com.chungjin.wam.domain.support.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select c from Comment c where c.support.supportId = :supportId")
    List<Comment> findAllBySupportId(@Param("supportId") Long supportId);

}
