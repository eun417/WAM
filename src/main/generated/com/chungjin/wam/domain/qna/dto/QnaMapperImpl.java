package com.chungjin.wam.domain.qna.dto;

import com.chungjin.wam.domain.qna.entity.Qna;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-01T14:21:49+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class QnaMapperImpl implements QnaMapper {

    @Override
    public QnaDto toDto(Qna arg0) {
        if ( arg0 == null ) {
            return null;
        }

        QnaDto.QnaDtoBuilder qnaDto = QnaDto.builder();

        qnaDto.qnaId( arg0.getQnaId() );
        qnaDto.email( arg0.getEmail() );
        qnaDto.nickname( arg0.getNickname() );
        qnaDto.title( arg0.getTitle() );
        qnaDto.content( arg0.getContent() );
        qnaDto.createDate( arg0.getCreateDate() );
        qnaDto.viewCount( arg0.getViewCount() );
        qnaDto.answer( arg0.getAnswer() );
        qnaDto.answerDate( arg0.getAnswerDate() );
        qnaDto.qnaCheck( arg0.getQnaCheck() );

        return qnaDto.build();
    }

    @Override
    public Qna toEntity(QnaDto arg0) {
        if ( arg0 == null ) {
            return null;
        }

        Qna.QnaBuilder qna = Qna.builder();

        qna.qnaId( arg0.getQnaId() );
        qna.email( arg0.getEmail() );
        qna.nickname( arg0.getNickname() );
        qna.title( arg0.getTitle() );
        qna.content( arg0.getContent() );
        qna.createDate( arg0.getCreateDate() );
        qna.viewCount( arg0.getViewCount() );
        qna.answer( arg0.getAnswer() );
        qna.answerDate( arg0.getAnswerDate() );
        qna.qnaCheck( arg0.getQnaCheck() );

        return qna.build();
    }

    @Override
    public void updateFromDto(QnaDto arg0, Qna arg1) {
        if ( arg0 == null ) {
            return;
        }

        if ( arg0.getQnaId() != null ) {
            arg1.setQnaId( arg0.getQnaId() );
        }
        if ( arg0.getEmail() != null ) {
            arg1.setEmail( arg0.getEmail() );
        }
        if ( arg0.getNickname() != null ) {
            arg1.setNickname( arg0.getNickname() );
        }
        if ( arg0.getTitle() != null ) {
            arg1.setTitle( arg0.getTitle() );
        }
        if ( arg0.getContent() != null ) {
            arg1.setContent( arg0.getContent() );
        }
        if ( arg0.getCreateDate() != null ) {
            arg1.setCreateDate( arg0.getCreateDate() );
        }
        arg1.setViewCount( arg0.getViewCount() );
        if ( arg0.getAnswer() != null ) {
            arg1.setAnswer( arg0.getAnswer() );
        }
        if ( arg0.getAnswerDate() != null ) {
            arg1.setAnswerDate( arg0.getAnswerDate() );
        }
        if ( arg0.getQnaCheck() != null ) {
            arg1.setQnaCheck( arg0.getQnaCheck() );
        }
    }
}
