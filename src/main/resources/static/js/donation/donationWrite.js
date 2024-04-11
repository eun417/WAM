//토큰을 로컬 스토리지에서 가져오기
const token = localStorage.getItem('accessToken');


document.addEventListener('DOMContentLoaded', function() {
    /*달력 설정*/
    var today = new Date().toISOString().substring(0, 10);
    document.getElementById('startDate').value = today; //시작일을 오늘 날짜로 설정
    document.getElementById('startDate').setAttribute('min', today);    //오늘 이전 날짜 비허용
    document.getElementById('endDate').setAttribute('min', today);    //오늘 이전 날짜 비허용

    /*파일 선택 -> 파일 이름 설정*/
    var fileInput = document.getElementById('firstImg');

    //파일 변경되면 이름 변경
    fileInput.addEventListener('change', function() {
        if (fileInput.files[0]) {
            //파일 이름을 file-name 요소에 업데이트
            document.querySelector('.file-name').textContent = fileInput.files[0].name;
            console.log(fileInput.files[0].name);
        } else {
            document.querySelector('.file-name').textContent = "파일을 선택해주세요";
        }
    });
});

//수정폼인 경우 후원 데이터 조회
window.onload = function() {
    getSupportDetail();
};


/*수정폼인 경우 후원 데이터를 가져오는 함수*/
function getSupportDetail() {
    const supportId = document.getElementById('supportId').value;
    console.log('수정할 후원: ' + supportId);

    //supportId가 있는 경우에만 서버로부터 데이터 조회
    if (supportId !== '') {
        //작성폼, 수정폼에 따라 버튼 변경
        showHideElement('#createDonationBtn');
        showHideElement('#updateDonationBtn');

        axios.get('/support/' + supportId)
            .then(function (response) {
                var supportDetail = response.data;
                fillSupportDetail(supportDetail);   //supportDetail 값을 채우는 함수 실행
            })
            .catch(function (error) {
                console.error('후원 정보를 가져오는 중 오류 발생:', error);
            });
    }
}

//supportDetail 값을 채우는 함수
function fillSupportDetail(supportDetail) {
    document.getElementById('title').value = supportDetail.title;
    document.getElementById('subject').value = supportDetail.animalSubjects;
    document.getElementById('goalAmount').value = supportDetail.goalAmount;
    document.getElementById('startDate').value = dashFormatDate(supportDetail.startDate);
    document.getElementById('endDate').value = dashFormatDate(supportDetail.endDate);
    //파일 선택란에 파일 이름 표시
    if (supportDetail.firstImg) {
        var fileName = supportDetail.firstImg.substring(supportDetail.firstImg.lastIndexOf('/') + 1);
        document.querySelector('.file-name').textContent = fileName;
    }
    $('#summernote').summernote('code', supportDetail.content);
    var commentOption = document.getElementById('commentOption');
    if (supportDetail.commentCheck) {
        commentOption.classList.add('active-comment-check');
    } else {
        commentOption.classList.remove('active-comment-check');
    }
    //새로운 대표 이미지 파일 선택으로 변경(newFirstImg)
    document.querySelector('label[for="firstImg"]').setAttribute('for', 'newFirstImg');
    document.getElementById('firstImg').setAttribute('id', 'newFirstImg');
}


/*후원 생성*/
document.getElementById('createDonationBtn').addEventListener('click', function() {
    if (!token) {
        alert("로그인 후 이용해주세요.");
        window.location.href = "/support/list";
        return;
    }

    //SupportRequestDto 객체 생성
    const title = document.getElementById('title').value.trim();
    const animalSubjects = document.getElementById('subject').value;
    const goalAmount = document.getElementById('goalAmount').value.trim();
    const startDate = dotFormatDate(document.getElementById('startDate').value);
    const endDate = dotFormatDate(document.getElementById('endDate').value);
    const firstImg  = document.getElementById('firstImg').files[0];
    const content = $('#summernote').summernote('code');
    const commentCheck = checkCommentOption();

    const supportReq = {
        title: title,
        animalSubjects: animalSubjects,
        goalAmount: goalAmount,
        startDate: startDate,
        endDate: endDate,
        content: content,
        commentCheck: commentCheck
    };

    const formData = new FormData();
    const json = JSON.stringify(supportReq);    //객체 -> JSON 문자열
    const blob = new Blob([json], { type: "application/json" });    //JSON 문자열 -> Blob 객체
    formData.append('supportReq', blob);
    formData.append('firstImg', firstImg);

    //후원 생성 요청
    axios.post('/support/', formData, {
        headers: {
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'multipart/form-data'
        }
    }).then(function(response) {
        console.log(response);
        alert(response.data);
    })
    .catch(function(error) {
        console.error(error);
        if (error.response.status === 401) {
            alert('일반 회원만 작성할 수 있습니다.');
            window.location.href = "/member/profile";
        } else {
            alert(error.response.data.message);
        }
    });
});


/*후원 수정*/
document.getElementById('updateDonationBtn').addEventListener('click', function() {
    if (!token) {
        alert("로그인 후 이용해주세요.");
        window.location.href = "/support/list";
        return;
    }

    //UpdateSupportRequestDto 객체 생성
    const title = document.getElementById('title').value.trim();
    const animalSubjects = document.getElementById('subject').value;
    const goalAmount = document.getElementById('goalAmount').value.trim();
    const startDate = dotFormatDate(document.getElementById('startDate').value);
    const endDate = dotFormatDate(document.getElementById('endDate').value);
    const newFirstImg  = document.getElementById('newFirstImg').files[0];
    const content = $('#summernote').summernote('code');
    const commentCheck = checkCommentOption();

    const supportId = document.getElementById('supportId').value;
    console.log('수정할 후원: ' + supportId);

    //기존 대표 이미지 삭제 여부(기본값:false)
    var firstImgDeleted = false;

    //대표 이미지를 새로 설정한 경우
    if (newFirstImg !== undefined && newFirstImg !== null) {
        //기존 대표 이미지 삭제
        firstImgDeleted = true;
    }

    console.log('기존 대표 이미지 삭제 여부: ' + firstImgDeleted);

    const updateSupportReq = {
        title: title,
        animalSubjects: animalSubjects,
        goalAmount: goalAmount,
        startDate: startDate,
        endDate: endDate,
        firstImgDeleted: firstImgDeleted,
        content: content,
        commentCheck: commentCheck
    };

    const formData = new FormData();
    const json = JSON.stringify(updateSupportReq);    //객체 -> JSON 문자열
    const blob = new Blob([json], { type: "application/json" });    //JSON 문자열 -> Blob 객체
    formData.append('updateSupportReq', blob);
    //newFirstImg 값이 있을 경우에만 formData에 추가
    if (newFirstImg !== undefined && newFirstImg !== null) {
        formData.append('newFirstImg', newFirstImg);
    }

    //후원 수정 요청
    axios.put('/support/' + supportId, formData, {
        headers: {
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'multipart/form-data'
        }
    }).then(function(response) {
        console.log(response);
        alert(response.data);
        window.location.href = "/support/detail/" + supportId;
    })
    .catch(function(error) {
        console.error(error);
        if (error.response.status === 401) {
            alert('일반 회원만 작성할 수 있습니다.');
            window.location.href = "/member/profile";
        } else {
            alert(error.response.data.message);
        }
    });
});


/*댓글 옵션 선택*/
document.getElementById('commentOption').addEventListener('click', function() {
    const isCommentCheck = checkCommentOption();

    if (isCommentCheck) {
        this.classList.remove('active-comment-check');
    } else {
        this.classList.add('active-comment-check');
    }
});

//댓글 허용 여부 반환 함수
function checkCommentOption() {
    const commentOption = document.getElementById('commentOption');
    return commentOption.classList.contains('active-comment-check');
}



//이미지 업로드 요청 함수
function uploadImage(file) {
    var formData = new FormData();
    formData.append('file', file);

    return axios.post('/support/image-upload', formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
}

//이미지 삭제 요청 함수
function deleteImage(imgUrl) {
    const formData = new FormData();
    formData.append('fileUrl', imgUrl);

    return axios.post('/support/image-delete', formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
}


//날짜 형식 변환 함수(.)
function dotFormatDate(inputDate) {
    const selectedDate = new Date(inputDate);

    const year = selectedDate.getFullYear();
    const month = (selectedDate.getMonth() + 1).toString().padStart(2, '0'); //월은 0부터 시작하므로 +1 해줌
    const day = selectedDate.getDate().toString().padStart(2, '0');

    return `${year}.${month}.${day}`;
}

//날짜 형식 변환 함수(-)
function dashFormatDate(inputDate) {
    const selectedDate = new Date(inputDate);

    const year = selectedDate.getFullYear();
    const month = (selectedDate.getMonth() + 1).toString().padStart(2, '0'); //월은 0부터 시작하므로 +1 해줌
    const day = selectedDate.getDate().toString().padStart(2, '0');

    return `${year}-${month}-${day}`;
}

//요소를 보이게 하거나 숨기는 함수
function showHideElement(element) {
    var element = document.querySelector(element);
    if (element.style.display === 'none') {
        element.style.display = 'block';
    } else {
        element.style.display = 'none';
    }
}