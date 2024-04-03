/*후원 생성*/
document.getElementById('createDonationBtn').addEventListener('click', function() {
    //토큰을 로컬 스토리지에서 가져오기
    const token = localStorage.getItem('accessToken');
    if (!token) {
        alert("로그인 후 이용해주세요.");
        window.location.href = "/support/list";
        return;
    }

    const title = document.getElementById('title').value.trim();
    const animalSubjects = document.getElementById('subject').value;
    const goalAmount = document.getElementById('goalAmount').value.trim();
    const startDate = dotFormatDate(document.getElementById('startDate').value);
    const endDate = dotFormatDate(document.getElementById('endDate').value);
    const firstImg  = document.getElementById('firstImg').files[0];
    console.log('firstImg name:' + firstImg.name);
    const subheading = document.getElementById('subheading').value.trim();
    const content = $('#summernote').summernote('code');
    console.log('content:'+content);
    const commentCheck = checkCommentOption();

    //SupportRequestDto 객체 생성
    const supportReq = {
        title: title,
        animalSubjects: animalSubjects,
        goalAmount: goalAmount,
        startDate: startDate,
        endDate: endDate,
        subheading: subheading,
        content: content,
        commentCheck: commentCheck
    };

    const formData = new FormData();
    const json = JSON.stringify(supportReq);    //객체 -> JSON 문자열
    const blob = new Blob([json], { type: "application/json" });    //JSON 문자열 -> Blob 객체
    formData.append('supportReq', blob);
    formData.append('firstImg', firstImg);

    //후원 생성 요청을 보냄
    axios.post('/support/', formData, {
        headers: {
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'multipart/form-data'
        }
    }).then(function(response) {
        console.log(response.data);
        alert(response.data);
    })
    .catch(function(error) {
        console.error(error);
        alert(error.response.data.message);
    });
});


/*후원 수정*/
document.getElementById('updateDonationBtn').addEventListener('click', function() {
    //토큰을 로컬 스토리지에서 가져오기
    const token = localStorage.getItem('accessToken');
    if (!token) {
        alert("로그인 후 이용해주세요.");
        window.location.href = "/support/list";
        return;
    }

    const title = document.getElementById('title').value.trim();
    const animalSubjects = document.getElementById('subject').value;
    const goalAmount = document.getElementById('goalAmount').value.trim();
    const startDate = dotFormatDate(document.getElementById('startDate').value);
    const endDate = dotFormatDate(document.getElementById('endDate').value);
    const newFirstImg  = document.getElementById('newFirstImg').files[0];
    const subheading = document.getElementById('subheading').value.trim();
    const content = $('#summernote').summernote('code');
    const commentCheck = checkCommentOption();

    const supportId = document.getElementById('supportId').value;
    console.log('수정할 후원: ' + supportId);

    //기존 대표 이미지 삭제 여부(기본값:false)
    var firstImgDeleted = false;

    //대표 이미지를 새로 설정한 경우
    if (newFirstImg !== null) {
        //기존 대표 이미지 삭제
        firstImgDeleted = true;
    }

    //UpdateSupportRequestDto 객체 생성
    const updateSupportReq = {
        title: title,
        animalSubjects: animalSubjects,
        goalAmount: goalAmount,
        startDate: startDate,
        endDate: endDate,
        firstImgDeleted: firstImgDeleted,
        subheading: subheading,
        content: content,
        commentCheck: commentCheck
    };

    const formData = new FormData();
    const json = JSON.stringify(updateSupportReq);    //객체 -> JSON 문자열
    const blob = new Blob([json], { type: "application/json" });    //JSON 문자열 -> Blob 객체
    formData.append('updateSupportReq', blob);
    //newFirstImg 값이 있을 경우에만 formData에 추가
    if (newFirstImg !== null) {
        formData.append('newFirstImg', newFirstImg);
    }

    //후원 수정 요청
    axios.put('/support/' + supportId, formData, {
        headers: {
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'multipart/form-data'
        }
    }).then(function(response) {
        console.log(response.data);
        alert(response.data);
        window.location.href = "/support/detail/" + supportId;
    })
    .catch(function(error) {
        console.error(error);
        alert(error.response.data.message);
    });
});

//날짜 형식 변환 함수(.)
function dotFormatDate(inputDate) {
    const selectedDate = new Date(inputDate);

    const year = selectedDate.getFullYear();
    const month = (selectedDate.getMonth() + 1).toString().padStart(2, '0'); //월은 0부터 시작하므로 +1 해줌
    const day = selectedDate.getDate().toString().padStart(2, '0');

    return `${year}.${month}.${day}`;
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
    document.getElementById('subheading').value = supportDetail.subheading;
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