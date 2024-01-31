package com.chungjin.wam.domain.qna.dto;

import com.chungjin.wam.domain.qna.entity.Qna;
import com.chungjin.wam.domain.qna.entity.QnaCheck;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QnaDto {

    private Long qnaId;
    private String memberEmail;
    private String memberNickname;
    private String qnaTitle;
    private String qnaContent;
    private String qnaImg;
    private String qnaDate;
    private int qnaView;
    @Enumerated(EnumType.STRING)
    private QnaCheck qnaCheck;

    /**
     * Qna 엔티티를 QnaDto로 변환
     * */
    public static QnaDto of(Qna qnA) {
        return QnaDto.builder()
                .qnaId(qnA.getQnaId())
                .memberEmail(qnA.getMemberEmail())
                .memberNickname(qnA.getMemberNickname())
                .qnaTitle(qnA.getQnaTitle())
                .qnaContent(qnA.getQnaContent())
                .qnaImg(qnA.getQnaImg())
                .qnaDate(qnA.getQnaDate())
                .qnaView(qnA.getQnaView())
                .qnaCheck(qnA.getQnaCheck())
//                .userDto(UsersDto.of(qnA.getUser()))
                .build();
    }

    /**
     * QnaDto를 Qna 엔티티로 변환
     * */
    public static Qna toEntity(QnaDto qnADto) {
        return Qna.builder()
                .qnaId(qnADto.getQnaId())
                .memberEmail(qnADto.getMemberEmail())
                .memberNickname(qnADto.getMemberNickname())
                .qnaTitle(qnADto.getQnaTitle())
                .qnaContent(qnADto.getQnaContent())
                .qnaImg(qnADto.getQnaImg())
                .qnaDate(qnADto.getQnaDate())
                .qnaView(qnADto.getQnaView())
                .qnaCheck(qnADto.getQnaCheck())
//                .user(UsersDto.toEntity(qnADto.getUserDto()))
                .build();
    }

}
