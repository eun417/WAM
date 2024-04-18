package com.chungjin.wam.domain.like.entity;

import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.support.entity.Support;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SupportLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supportLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "support_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Support support;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public SupportLike(Support support, Member member) {
        this.support = support;
        this.member = member;
    }

}
