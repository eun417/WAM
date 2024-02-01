package com.chungjin.wam.domain.qna.dto;

import com.chungjin.wam.domain.qna.entity.Qna;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-01T19:41:32+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class QnaMapperImpl implements QnaMapper {

    @Override
    public QnaDto toDto(Qna e) {
        if ( e == null ) {
            return null;
        }

        QnaDto.QnaDtoBuilder qnaDto = QnaDto.builder();

        qnaDto.qnaId( e.getQnaId() );
        qnaDto.email( e.getEmail() );
        qnaDto.nickname( e.getNickname() );
        qnaDto.title( e.getTitle() );
        qnaDto.content( e.getContent() );
        qnaDto.createDate( e.getCreateDate() );
        qnaDto.viewCount( e.getViewCount() );
        qnaDto.answer( e.getAnswer() );
        qnaDto.answerDate( e.getAnswerDate() );
        qnaDto.qnaCheck( e.getQnaCheck() );

        return qnaDto.build();
    }

    @Override
    public Qna toEntity(QnaDto d) {
        if ( d == null ) {
            return null;
        }

        Qna.QnaBuilder qna = Qna.builder();

        qna.qnaId( d.getQnaId() );
        qna.email( d.getEmail() );
        qna.nickname( d.getNickname() );
        qna.title( d.getTitle() );
        qna.content( d.getContent() );
        qna.createDate( d.getCreateDate() );
        qna.viewCount( d.getViewCount() );
        qna.answer( d.getAnswer() );
        qna.answerDate( d.getAnswerDate() );
        qna.qnaCheck( d.getQnaCheck() );

        return qna.build();
    }

    @Override
    public List<QnaDto> toDtoList(List<Qna> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<QnaDto> list = new ArrayList<QnaDto>( entityList.size() );
        for ( Qna qna : entityList ) {
            list.add( toDto( qna ) );
        }

        return list;
    }

    @Override
    public List<Qna> toEntityList(List<QnaDto> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Qna> list = new ArrayList<Qna>( dtoList.size() );
        for ( QnaDto qnaDto : dtoList ) {
            list.add( toEntity( qnaDto ) );
        }

        return list;
    }

    @Override
    public void updateFromDto(QnaDto dto, Qna entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getQnaId() != null ) {
            entity.setQnaId( dto.getQnaId() );
        }
        if ( dto.getEmail() != null ) {
            entity.setEmail( dto.getEmail() );
        }
        if ( dto.getNickname() != null ) {
            entity.setNickname( dto.getNickname() );
        }
        if ( dto.getTitle() != null ) {
            entity.setTitle( dto.getTitle() );
        }
        if ( dto.getContent() != null ) {
            entity.setContent( dto.getContent() );
        }
        if ( dto.getCreateDate() != null ) {
            entity.setCreateDate( dto.getCreateDate() );
        }
        entity.setViewCount( dto.getViewCount() );
        if ( dto.getAnswer() != null ) {
            entity.setAnswer( dto.getAnswer() );
        }
        if ( dto.getAnswerDate() != null ) {
            entity.setAnswerDate( dto.getAnswerDate() );
        }
        if ( dto.getQnaCheck() != null ) {
            entity.setQnaCheck( dto.getQnaCheck() );
        }
    }
}
