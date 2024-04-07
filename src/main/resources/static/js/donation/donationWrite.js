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