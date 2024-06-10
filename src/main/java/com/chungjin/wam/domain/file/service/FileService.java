package com.chungjin.wam.domain.file.service;

import com.chungjin.wam.domain.file.entity.Board;
import com.chungjin.wam.domain.file.entity.Image;
import com.chungjin.wam.domain.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    //본문 첨부한 이미지들 DB에 저장
    @Transactional
    public void saveImages(List<String> fileUrls, Board board, Long boardId) {
        for (String fileUrl : fileUrls) {
            Image image = Image.builder()
                    .uploadPath(fileUrl)
                    .board(board)
                    .boardId(boardId)
                    .build();

            fileRepository.save(image);
        }
    }

}
