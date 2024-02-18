package com.chungjin.wam.domain.support.entity;

import com.chungjin.wam.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SupportLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supportLikeId;

    @ManyToOne
    @JoinColumn(name = "support_id")
    private Support support;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
