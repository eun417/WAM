package com.chungjin.wam.domain.comment.entity;

import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.support.entity.Support;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Column(name = "create_date")
    private String createDate;

    @ManyToOne
    @JoinColumn(name = "support_id")
    private Support support;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}