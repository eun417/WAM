package com.chungjin.wam.domain.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

    @Id
    private Long memberId;

    @Column(name = "refresh_token_value")
    private String value;

    public RefreshToken updateValue(String token) {
        this.value = token;
        return this;
    }

}
