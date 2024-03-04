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
import com.chungjin.wam.global.exception.CustomException;
import com.chungjin.wam.global.exception.error.ErrorCodeType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SupportService {

    private final SupportRepository supportRepository;
    private final MemberRepository memberRepository;
    private final CommentService commentService;
    private final SupportMapper supportMapper;

    @Value("${file.path}")
    private String filePath;    //파일 저장 경로

    /**
     * 후원 생성
     */
    public void createSupport(Long memberId, SupportRequestDto supportReq) throws IOException {
        //memberId로 Member 객체 가져오기
        Member member = getMember(memberId);

        //파일 업로드
        String filePath = uploadFile(supportReq.getFirstImg());

        //Dto -> Entity
        Support support = Support.builder()
                .title(supportReq.getTitle())
                .animalSubjects(supportReq.getAnimalSubjects())
                .goalAmount(supportReq.getGoalAmount())
                .startDate(supportReq.getStartDate())
                .endDate(supportReq.getEndDate())
                .firstImg(filePath)
                .subheading(supportReq.getSubheading())
                .content(supportReq.getContent())
                .commentCheck(supportReq.getCommentCheck())
                .member(member)
                .supportStatus(SupportStatus.START)
                .build();

        //DB에 저장
        supportRepository.save(support);
    }

    //파일 업로드를 위한 메서드
    public String uploadFile(MultipartFile file) throws IOException {
        //업로드할 디렉토리 생성 (없는 경우)
        Path uploadPath = Paths.get(filePath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        //파일 이름을 고유한 UUID로 생성
        String fileName = UUID.randomUUID() + ".jpg";

        //파일을 지정된 경로에 저장
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        //저장된 파일의 경로를 리턴
        return fileName;
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
    public List<SupportResponseDto> readAllSupport(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Support> supportPage = supportRepository.findAll(pageable);
        List<Support> supports = supportPage.getContent();
        return convertToDtoList(supports);
    }

    /**
     * 후원 수정
     */
    public void updateSupport(Long memberId, Long supportId, UpdateSupportRequestDto updateSupportReq) {
        //supportId로 support 객체 가져오기
        Support support = getSupport(supportId);

        //로그인한 사용자가 작성자가 아닌 경우 에러 발생
        if(!memberId.equals(support.getMember().getMemberId())) throw new CustomException(ErrorCodeType.FORBIDDEN);

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
            //DB에서 영구 삭제
            supportRepository.delete(support);
        } else {
            //그 외 에러 발생
            throw new CustomException(ErrorCodeType.FORBIDDEN);
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
    private Support getSupport (long supportId) {
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
                        .build())
                .collect(Collectors.toList());
    }

}
