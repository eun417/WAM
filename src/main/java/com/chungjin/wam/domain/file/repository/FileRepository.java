package com.chungjin.wam.domain.file.repository;

import com.chungjin.wam.domain.file.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i.uploadPath FROM Image i WHERE i.createDate BETWEEN :startDate AND :endDate")
    List<String> findUploadPathsByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
