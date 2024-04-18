package com.chungjin.wam.domain.comment.entity;

import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.support.entity.Support;
import com.chungjin.wam.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "support_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Support support;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Comment(String content, Support support, Member member) {
        this.content = content;
        this.support = support;
        this.member = member;
    }

}