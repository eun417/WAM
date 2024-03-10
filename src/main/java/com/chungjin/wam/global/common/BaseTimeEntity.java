package com.chungjin.wam.global.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass   //해당 클래스가 매핑 정보를 상속하기 위한 슈퍼클래스임을 나타냄
@EntityListeners(AuditingEntityListener.class)  //엔티티의 변경사항 추적
public abstract class BaseTimeEntity {

    @Column(name = "create_date", updatable = false)
    private String createDate;

    @PrePersist
    protected void onCreate() {
        //엔터티가 영속화되기 전에 현재 날짜로 초기화
        this.createDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }

}
