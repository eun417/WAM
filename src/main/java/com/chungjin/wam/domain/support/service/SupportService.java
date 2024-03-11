package com.chungjin.wam.domain.support.service;

import com.chungjin.wam.domain.comment.service.CommentService;
import com.chungjin.wam.domain.member.entity.Authority;
import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import com.chungjin.wam.domain.support.dto.SupportMapper;
import com.chungjin.wam.domain.support.dto.request.SupportRequestDto;
import com.chungjin.wam.domain.support.dto.request.UpdateSupportRequestDto;
import com.chungjin.wam.domain.comment.dto.response.CommentDto;
import com.chungjin.wam.domain.support.dto.response.SupportDetailDto;
import com.chungjin.wam.domain.support.dto.response.SupportResponseDto;
import com.chungjin.wam.domain.support.entity.Support;
import com.chungjin.wam.domain.support.entity.SupportStatus;
import com.chungjin.wam.domain.support.repository.SupportRepository;
import com.chungjin.wam.global.common.PageResponse;
import com.chungjin.wam.global.exception.CustomException;
import com.chungjin.wam.global.exception.error.ErrorCodeType;
import com.chungjin.wam.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SupportService {

    private final SupportRepository supportRepository;
    private final MemberRepository memberRepository;
    private final CommentService commentService;
    private final SupportMapper supportMapper;

    private final S3Service s3Service;
    private final String S3_FOLDER = "support";

    /**
     * 후원 생성
     */
    public void createSupport(Long memberId, MultipartFile file, SupportRequestDto supportReq) {
        //memberId로 Member 객체 가져오기
        Member member = getMember(memberId);

        //파일 업로드
        String imgPath = uploadFileAtS3(file);

        //Dto -> Entity
        Support support = Support.builder()
                .title(supportReq.getTitle())
                .animalSubjects(supportReq.getAnimalSubjects())
                .goalAmount(supportReq.getGoalAmount())
                .startDate(supportReq.getStartDate())
                .endDate(supportReq.getEndDate())
                .firstImg(imgPath)
                .subheading(supportReq.getSubheading())
                .content(supportReq.getContent())
                .commentCheck(supportReq.getCommentCheck())
                .member(member)
                .supportStatus(SupportStatus.START)
                .build();

        //DB에 저장
        supportRepository.save(support);
    }

    /**
     * 후원 조회
     */
    public SupportDetailDto readSupport(Long supportId) {
        //supportId로 support 객체 가져오기
        Support support = getSupport(supportId);
        //supportId로 comment List 가져오기
        List<CommentDto> comments = commentService.findAllComment(supportId);

        //Entity -> Dto
        return SupportDetailDto.builder()
                .supportId(support.getSupportId())
                .animalSubjects(support.getAnimalSubjects())
                .title(support.getTitle())
                .goalAmount(support.getGoalAmount())
                .supportStatus(support.getSupportStatus())
                .startDate(support.getStartDate())
                .endDate(support.getEndDate())
                .firstImg(support.getFirstImg())
                .subheading(support.getSubheading())
                .content(support.getContent())
                .supportLike(support.getSupportLike())
                .supportAmount(support.getSupportAmount())
                .comments(comments)
                .build();
    }

    /**
     * 후원 List 조회 (Pagination)
     */
    public PageResponse readAllSupport(int pageNo) {
        //한 페이지당 10개 항목 표시
        Pageable pageable = PageRequest.of(pageNo, 10, Sort.by("supportId").ascending());
        //Support를 페이지별 조회
        Page<Support> supportPage = supportRepository.findAll(pageable);
        //현재 페이지의 Support 목록
        List<Support> supports = supportPage.getContent();
        //EntityList -> DtoList
        List<Object> supportDtos = new ArrayList<>();
        for (Support support : supports) {
            supportDtos.add(convertToDto(support)); // Support를 SupportDto로 변환하여 리스트에 추가
        }
        return PageResponse.builder()
                .content(supportDtos)	//Support 목록
                .pageNo(pageNo) //현재 페이지 번호
                .pageSize(supportPage.getSize()) //페이지당 항목 수
                .totalElements(supportPage.getTotalElements())   //전체 Support 수
                .totalPages(supportPage.getTotalPages()) //전체 페이지 수
                .last(supportPage.isLast())  //마지막 페이지 여부
                .build();
    }

    /**
     * 후원 수정
     */
    public void updateSupport(Long memberId, Long supportId, MultipartFile file, UpdateSupportRequestDto updateSupportReq) {
        //supportId로 support 객체 가져오기
        Support support = getSupport(supportId);

        //로그인한 사용자가 작성자가 아닌 경우 에러 발생
        if(!memberId.equals(support.getMember().getMemberId())) throw new CustomException(ErrorCodeType.FORBIDDEN);

        //기존 대표 이미지를 삭제한 경우
        if (updateSupportReq.getFirstImgDeleted()) {
            //S3에서 파일 삭제
            deleteFileAtS3(support.getFirstImg());
        }

        //새로운 대표 이미지 업로드, 수정
        if (file != null) {
            //파일 업로드
            String newImgPath = uploadFileAtS3(file);
            support.updateFirstImg(newImgPath);
        }

        //MapStruct로 수정
        supportMapper.updateFromDto(updateSupportReq, support);
    }

    /**
     * 후원 삭제
     */
    public void deleteSupport(Long memberId, Long supportId) {
        //supportId로 support 객체 가져오기
        Support support = getSupport(supportId);
        //memberId로 Member 객체 가져오기
        Member member = getMember(memberId);

        //로그인한 사용자가 작성자인 경우 또는 관리자인 경우 삭제 가능
        if(memberId.equals(support.getMember().getMemberId()) || member.getAuthority().equals(Authority.ADMIN)) {
            //S3에서 파일 삭제
            deleteFileAtS3(support.getFirstImg());
            //DB에서 영구 삭제
            supportRepository.delete(support);
        } else {
            //그 외 에러 발생
            throw new CustomException(ErrorCodeType.FORBIDDEN);
        }
    }

    //파일 업로드 메소드
    public String uploadFileAtS3(MultipartFile img) {
        String imgPath = null;
        if (img != null) {
            imgPath = s3Service.uploadFile(img, S3_FOLDER);
        }
        return imgPath;
    }

    //파일 삭제 메소드
    public void deleteFileAtS3(String fileName) {
        if(fileName != null) {
            s3Service.deleteImage(fileName);
        }
    }

    /**
     * memberId로 Member 객체 조회
     */
    private Member getMember (Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCodeType.MEMBER_NOT_FOUND));
    }

    /**
     * supportId로 Support 객체 조회
     */
    private Support getSupport (Long supportId) {
        return supportRepository.findById(supportId)
                .orElseThrow(() -> new CustomException(ErrorCodeType.SUPPORT_NOT_FOUND));
    }

    /**
     * 검색 - 제목+내용
     */
    public List<SupportResponseDto> searchSupport(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Support> supportPage = supportRepository.findByTitleOrContentContaining(keyword, pageable);
        List<Support> supports = supportPage.getContent();
        return convertToDtoList(supports);
    }

    /**
     * 검색 - 태그
     */
    public List<SupportResponseDto> searchSupportTag(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Support> supportPage = supportRepository.findByAnimalSubjectsContaining(keyword, pageable);
        List<Support> supports = supportPage.getContent();
        return convertToDtoList(supports);
    }

    /**
     * Entity -> Dto
     */
    public SupportResponseDto convertToDto(Support support) {
        return SupportResponseDto.builder()
                .supportId(support.getSupportId())
                .title(support.getTitle())
                .firstImg(support.getFirstImg())
                .goalAmount(support.getGoalAmount())
                .supportAmount(support.getSupportAmount())
                .createDate(support.getCreateDate())
                .nickname(support.getMember().getNickname())
                .supportStatus(support.getSupportStatus())
                .build();
    }

    /**
     * EntityList -> DtoList
     * map()으로 각 Support를 SupportResponseDto로 변환
     * collect()를 사용하여 변환된 DTO 객체들을 리스트로 수집
     */
    public List<SupportResponseDto> convertToDtoList(List<Support> supports) {
        return supports.stream()
                .map(support -> SupportResponseDto.builder()
                        .supportId(support.getSupportId())
                        .title(support.getTitle())
                        .nickname(support.getMember().getNickname())
                        .supportStatus(support.getSupportStatus())
                        .firstImg(support.getFirstImg())
                        .supportAmount(support.getSupportAmount())
                        .createDate(support.getCreateDate())
                        .build())
                .collect(Collectors.toList());
    }

}
