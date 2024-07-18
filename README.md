# 🦌 WAM

WAM은 국립생태원 EcoBank에서 제공하는 야생동물 위치 정보를 조회하고, 야생동물 후원 및 QnA를 이용할 수 있는 웹 서비스입니다.

---
## 목차
1. [Tech Stack](#tech-stack)

2. [주요 기능](#주요-기능)

3. [ERD 설계](#erd-설계)

4. [Architecture](#architecture)

5. [기능별 화면 및 시연](#기능별-화면-및-시연)

---
## Tech Stack

**BackEnd**

<img src="https://img.shields.io/badge/JAVA 17-007396?style=flat&logo=Java&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat&logo=Spring Boot&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat&logo=Spring Security&logoColor=white"/> <img src="https://img.shields.io/badge/Gradle-02303A?style=flat&logo=Gradle&logoColor=white"/>
<br>
<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white"/> <img src="https://img.shields.io/badge/Redis-DC382D?style=flat&logo=Redis&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=flat&logo=Spring Data JPA&logoColor=white"/> <img src="https://img.shields.io/badge/QueryDSL-4479A1?style=flat&logo=QueryDSL&logoColor=white"/>
<br>
<img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=flat&logo=Amazon EC2&logoColor=white"/> <img src="https://img.shields.io/badge/Amazon S3-569A31?style=flat&logo=Amazon S3&logoColor=white"/> <img src="https://img.shields.io/badge/Docker-2496ED?style=flat&logo=Docker&logoColor=white">  <img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-flat&logo=GitHub Actions&logoColor=white">


**FrontEnd**

<img src="https://img.shields.io/badge/HTML5-E34F26?style=flat&logo=HTML5&logoColor=white"/> <img src="https://img.shields.io/badge/CSS3-1572B6?style=flat&logo=CSS3&logoColor=white"/> <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=flat&logo=Thymeleaf&logoColor=white"/> <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat&logo=JavaScript&logoColor=white"/> <img src="https://img.shields.io/badge/Axios-5A29E4?style=flat&logo=Axios&logoColor=white"/>  <img src="https://img.shields.io/badge/jQuery-0769AD?style=flat&logo=jQuery&logoColor=white"/>

**etc.**

<img src="https://img.shields.io/badge/Notion-000000?style=flat&logo=Notion&logoColor=white"/> <img src="https://img.shields.io/badge/Figma-F24E1E?style=flat&logo=Figma&logoColor=white"/>

---
## 주요 기능
<table>
  <tr>
    <td><b>구분</b></td>
    <td><b>기능</b></td>
  </tr>
  <tr>
    <td rowspan=8>사용자 페이지</td>
    <td>일반 회원가입 및 로그인</td>
  </tr>
  <tr>
    <td>소셜 회원가입 및 로그인</td>
  </tr>
  <tr>
    <td>야생동물 지도 조회</td>
  </tr>
  <tr>
    <td>후원 생성/수정/삭제</td>
  </tr>
  <tr>
    <td>좋아요 및 공유하기</td>
  </tr>
  <tr>
    <td>댓글 작성</td>
  </tr>
  <tr>
    <td>키워드로 후원, QnA 검색</td>
  </tr>
  <tr>
    <td>QnA를 통한 질의응답</td>
  </tr>
  <tr>
    <td rowspan=3>관리자 페이지</td>
    <td>모든 회원 조회 및 탈퇴</td>
  </tr>
  <tr>
    <td>모든 후원 조회 및 삭제</td>
  </tr>
  <tr>
    <td>모든 QnA 조회 및 삭제</td>
  </tr>
</table>

---
## ERD 설계
![wam-erd](https://github.com/eun417/wam/assets/126125547/2e0179e4-5619-437c-8175-eba4b101019d)

---
## Architecture
![architecture](https://github.com/eun417/wam/assets/126125547/b469dbfa-ac89-447c-b1c1-4e8bee7a3cd8)

---
## 기능별 화면 및 시연

### 로그인

1. **일반 로그인**

![login(local)](https://github.com/eun417/wam/assets/126125547/dd7225dd-d1c9-40d6-967d-4c2bad18d598)

3. **소셜 로그인(카카오, 네이버, 구글)**

![login(social)](https://github.com/eun417/wam/assets/126125547/5a96a33e-212f-46c1-a783-6412c2d32905)

<br/>

### 회원가입
![join](https://github.com/eun417/wam/assets/126125547/4933b11f-0c0e-493f-82cb-d8328d5eee56)
- 약관 동의
- 이메일 중복 검사 후 인증번호 메일 전송 및 검증
- 이름, 닉네임, 휴대폰 번호, 비밀번호 정보 입력

<br/>

### 이메일 찾기
![findEmail](https://github.com/eun417/wam/assets/126125547/0e3302cb-668d-46b1-9c55-e3124f56eed9)
이름, 휴대폰 번호 입력하면 마스킹된 이메일 확인

<br/>

### 비밀번호 재설정
![chagePw](https://github.com/eun417/wam/assets/126125547/f9527d00-6580-473f-929b-a5a96a87ee09)
- 비밀번호 재설정 링크 메일 전송, 해당 링크에서 비밀번호 재설정 가능
- URL에 있는 인증번호는 Redis에 저장되며, 인증 시간 만료 시 안내 메시지 반환 후 첫 화면으로 돌아감

<br/>

### 메인 페이지
![home](https://github.com/eun417/wam/assets/126125547/2664992d-0a82-4af9-a515-8d3e262d0360)
- 총 후원 금액 조회
- 종료 임박 후원 랜덤 조회
- 태그별 후원 조회
- QnA 최신순 조회

<br/>

### 야생동물 지도
![map](https://github.com/eun417/wam/assets/126125547/988ee991-6b90-4ff4-8f07-ccd492ce345e)
- 국립생태원 EcoBank의 Open API를 사용하여 “생태계 정밀조사” 데이터 조회
- 좌표를 변환하여 카카오맵 API로 위치 정보 표시
- 각각의 마커를 클릭하여 상세 정보 조회 가능

<br/>

### 후원하기
1. **후원 작성 및 상세 보기**

![support-write-detail](https://github.com/eun417/wam/assets/126125547/55eb4360-a4da-4250-b2b2-bec714921a0c)

- 모든 회원 정보를 입력한 USER 권한만 후원 작성 가능
- Summernote 텍스트 에디터를 사용하여 본문에 사진 첨부 가능
- 사진을 업로드하면 callbacks 함수가 실행되어 S3 저장소에 이미지를 저장

<br/>

2. **키워드 검색 및 태그별 후원 조회**

![support-search](https://github.com/eun417/wam/assets/126125547/7410b791-7561-4bf9-a0eb-a541ee0bcb65)
제목 및 내용을 대상으로 키워드를 입력하여 검색 결과 조회

![support-tag](https://github.com/eun417/wam/assets/126125547/2e7b237f-95b2-422d-8865-bd49d075c398)
각각의 태그별로 후원 조회

<br/>

3. **후원하기(결제)**

![support-payment](https://github.com/eun417/wam/assets/126125547/87ecc174-ee48-482c-935b-ba01c8b498cc)
PortOne API를 이용하여 결제

<br/>

4. **댓글 작성/삭제**

![support-comment](https://github.com/eun417/wam/assets/126125547/8d6fa164-8147-4ec2-a13b-22138ecc0da8)
특정 후원에 대한 댓글 작성/삭제

<br/>

5. **좋아요 및 공유하기**

![support-like-share](https://github.com/eun417/wam/assets/126125547/7435320e-a338-49c2-aa30-58a34fd283f1)
트위터, 페이스북, 카카오톡으로 해당 후원 페이지 공유

<br/>

### QnA

1. **QnA 작성 및 상세 보기**

![qna-write-detail](https://github.com/eun417/wam/assets/126125547/33c288ea-d3d4-45a8-88a8-7b14ed741be2)
상세 조회할 때마다 조회수 증가

<br/>

2. **QnA 답변 등록(관리자)**

![qna-answer](https://github.com/eun417/wam/assets/126125547/15bbe5fa-b076-4188-ac8d-84a196edb183)
관리자만 답변을 등록할 수 있고, 답변 등록 후 상태가 “답변 완료”로 변경

<br/>

### 마이페이지

1. **회원 정보 수정**

![mypage-member](https://github.com/eun417/wam/assets/126125547/51aa059a-f6bc-4310-a1a4-8bb69411facd)
이름, 닉네임, 휴대폰 번호, 비밀번호를 변경 가능

<br/>

2. **자신의 좋아요, 후원, QnA 게시글 조회**

![mypage-like](https://github.com/eun417/wam/assets/126125547/c0b92da4-10d0-4528-b93d-a9dfddfc6684)
자신이 좋아요 한 후원 목록 조회/삭제

![mypage-support-qna](https://github.com/eun417/wam/assets/126125547/7d520d0c-fca3-4638-b09c-175463df59ce)
자신이 작성한 후원 및 QnA 게시글 목록 조회, 상세 페이지로 이동하여 수정/삭제

<br/>

3. **회원 탈퇴**

![mypage-delete](https://github.com/eun417/wam/assets/126125547/b45310fb-e4ea-4400-8700-d997eeab868b)
현재 비밀번호 입력 후 회원 탈퇴

<br/>

### 관리자 페이지
![admin](https://github.com/eun417/wam/assets/126125547/82e16141-ca0a-4aef-86c9-0d90c6fd1426)
모든 회원, 후원, QnA 조회/삭제
