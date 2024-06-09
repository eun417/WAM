package com.chungjin.wam.domain.support.service;

import com.chungjin.wam.domain.comment.service.CommentService;
import com.chungjin.wam.domain.file.service.FileService;
import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.support.dto.SupportMapper;
import com.chungjin.wam.domain.support.dto.request.SupportRequestDto;
import com.chungjin.wam.domain.support.dto.request.UpdateSupportRequestDto;
import com.chungjin.wam.domain.comment.dto.response.CommentDto;
import com.chungjin.wam.domain.support.dto.response.SupportDetailDto;
import com.chungjin.wam.domain.support.dto.response.SupportResponseDto;
import com.chungjin.wam.domain.support.entity.AnimalSubjects;
import com.chungjin.wam.domain.support.entity.Support;
import com.chungjin.wam.domain.support.entity.SupportStatus;
import com.chungjin.wam.domain.support.repository.SupportRepository;
import com.chungjin.wam.domain.support.repository.querydsl.SupportQuerydslRepositoryImpl;
import com.chungjin.wam.global.common.PageResponse;
import com.chungjin.wam.global.s3.S3Service;
import com.chungjin.wam.global.util.EntityUtils;
import com.chungjin.wam.global.util.SearchKeywordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.chungjin.wam.domain.file.entity.Board.SUPPORT;
import static com.chungjin.wam.domain.support.entity.SupportStatus.ENDING_SOON;
import static com.chungjin.wam.global.util.Constants.S3_SUPPORT_FIRST;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SupportService {

    private final SupportRepository supportRepository;
    private final SupportQuerydslRepositoryImpl supportQuerydslRepository;

    private final CommentService commentService;
    private final S3Service s3Service;
    private final FileService fileService;

    private final SupportMapper supportMapper;
    private final EntityUtils entityUtils;

    /**
     * 후원 생성
     */
    @Transactional
    public void createSupport(Long memberId, SupportRequestDto supportReq, MultipartFile firstImg) {
        //memberId로 Member 객체 가져오기
        Member member = entityUtils.getMember(memberId);

        //파일 업로드(대표 이미지)
        String imgPath = s3Service.uploadFile(firstImg, S3_SUPPORT_FIRST);

        //Dto -> Entity
        Support support = Support.builder()
                .title(supportReq.getTitle())
                .animalSubjects(supportReq.getAnimalSubjects())
                .goalAmount(supportReq.getGoalAmount())
                .startDate(supportReq.getStartDate())
                .endDate(supportReq.getEndDate())
                .firstImg(imgPath)
                .content(supportReq.getContent())
                .commentCheck(supportReq.getCommentCheck())
                .member(member)
                .supportStatus(SupportStatus.START)
                .build();

        //DB에 저장
        supportRepository.save(support);

        if (!supportReq.getFileUrls().isEmpty()) {
            //본문 첨부한 이미지들 DB에 저장
            fileService.saveImages(supportReq.getFileUrls(), SUPPORT, support.getSupportId());
        }
    }

    /**
     * 후원 조회
     */
    public SupportDetailDto readSupport(Long supportId) {
        //supportId로 support 객체 가져오기
        Support support = entityUtils.getSupport(supportId);
        //supportId로 comment List 가져오기
        List<CommentDto> comments = commentService.findAllComment(supportId);

        //Entity -> Dto
        return SupportDetailDto.builder()
                .supportId(support.getSupportId())
                .memberId(entityUtils.getMemberId(support.getMember()))
                .animalSubjects(support.getAnimalSubjects())
                .title(support.getTitle())
                .goalAmount(support.getGoalAmount())
                .supportStatus(support.getSupportStatus())
                .startDate(support.getStartDate())
                .endDate(support.getEndDate())
                .firstImg(support.getFirstImg())
                .content(support.getContent())
                .supportLike(support.getSupportLike())
                .supportAmount(support.getSupportAmount())
                .commentCheck(support.getCommentCheck())
                .comments(comments)
                .build();
    }

    /**
     * 후원 List 조회 (Pagination)
     */
    public List<SupportResponseDto> readAllSupport(Long lastId) {
        return convertToDtoList(supportQuerydslRepository.paginationNoOffset(lastId, 10));
    }

    /**
     * 종료 임박 후원 List 조회
     */
    public List<SupportResponseDto> readEndingSoonSupport() {
        return convertToDtoList(supportRepository.findBySupportStatus(ENDING_SOON));
    }

    /**
     * 후원 수정
     */
    @Transactional
    public void updateSupport(Long memberId, Long supportId, UpdateSupportRequestDto updateSupportReq, MultipartFile newFirstImg) {
        //supportId로 support 객체 가져오기
        Support support = entityUtils.getSupport(supportId);

        //로그인한 사용자가 작성자가 아닌 경우 에러 발생
        entityUtils.checkWriter(support.getMember().getMemberId(), memberId);

        //기존 대표 이미지를 삭제한 경우
        if (updateSupportReq.getFirstImgDeleted()) {
            //S3에서 파일 삭제
            s3Service.deleteImage(support.getFirstImg());
        }

        //새로운 대표 이미지 업로드, 수정
        if (newFirstImg != null) {
            //파일 업로드
            String newImgPath = s3Service.uploadFile(newFirstImg, S3_SUPPORT_FIRST);
            //새로운 대표 이미지로 수정
            support.updateFirstImg(newImgPath);
        }

        //MapStruct로 수정
        supportMapper.updateFromDto(updateSupportReq, support);
    }

    /**
     * 후원 삭제
     */
    @Transactional
    public void deleteSupport(Long memberId, Long supportId) {
        //supportId로 support 객체 가져오기
        Support support = entityUtils.getSupport(supportId);

        //로그인한 사용자가 작성자가 아닌 경우 에러 발생
        entityUtils.checkWriter(support.getMember().getMemberId(), memberId);

        //S3에서 파일 삭제
        s3Service.deleteImage(support.getFirstImg());
        //DB에서 영구 삭제
        supportRepository.delete(support);
    }

    /**
     * 검색 - 제목, 내용, 작성자
     */
    public PageResponse searchSupport(String select, String keyword, int pageNo) {
        log.info("select: {}", select);
        log.info("keyword: {}", keyword);
        if (select.equals("tc")) {
            Pageable pageable = PageRequest.of(pageNo, 10);
            return getSupportPageResponse(supportRepository.findByTitleOrContentContaining(SearchKeywordUtils.convertToBooleanModeKeyword(keyword), pageable), pageNo);
        } else {
            Pageable pageable = PageRequest.of(pageNo, 10);
            return getSupportPageResponse(supportRepository.findByNicknameContaining(keyword, pageable), pageNo);
        }
    }

    /**
     * 검색 - 태그
     */
    public PageResponse searchSupportTag(AnimalSubjects tagName, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 10, Sort.by("supportId").descending());
        return getSupportPageResponse(supportRepository.findByAnimalSubjectsContaining(tagName, pageable), pageNo);
    }

    //Pagination 결과 함수
    private PageResponse getSupportPageResponse(Page<Support> supportPage, int pageNo) {
        //현재 페이지의 Support 목록
        List<Support> supports = supportPage.getContent();
        //EntityList -> DtoList
        List<Object> supportDtos = supports.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return PageResponse.createPageResponse(supportDtos, supportPage, pageNo);
    }

    /*
      EntityList -> DtoList
      map()으로 각 Support를 SupportResponseDto로 변환
      collect()를 사용하여 변환된 DTO 객체들을 리스트로 수집
     */
    private List<SupportResponseDto> convertToDtoList(List<Support> supports) {
        return supports.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    //Entity -> Dto
    private SupportResponseDto convertToDto(Support support) {
        return SupportResponseDto.builder()
                .supportId(support.getSupportId())
                .title(support.getTitle())
                .nickname(entityUtils.getNickname(support.getMember()))
                .supportStatus(support.getSupportStatus())
                .firstImg(support.getFirstImg())
                .goalAmount(support.getGoalAmount())
                .supportAmount(support.getSupportAmount())
                .build();
    }

}
