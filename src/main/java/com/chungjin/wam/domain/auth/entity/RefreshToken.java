package com.chungjin.wam.domain.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken {

    @Id
    private String email;

    @Column(name = "refresh_token_value")
    private String value;

    @Builder
    public RefreshToken(String email, String value) {
        this.email = email;
        this.value = value;
    }

    public RefreshToken updateValue(String token) {
        this.value = token;
        return this;
    }

}
