//수정폼인 경우 QnA 데이터 조회
window.onload = function() {
    getQnaDetail();
};

/*수정폼인 경우 QnA 데이터를 가져오는 함수*/
function getQnaDetail() {
    const qnaId = document.getElementById('qnaId').value;
    console.log('수정할 QnA: ' + qnaId);

    document.getElementById('qnaId').value = qnaId;

    //qnaId가 있는 경우에만 서버로부터 데이터 조회
    if (qnaId !== '') {
        //작성폼, 수정폼에 따라 버튼 변경
        showHideElement('#createQnaBtn');
        showHideElement('#updateQnaBtn');

        axios.get('/qna/' + qnaId)
            .then(function (response) {
                var qnaDetail = response.data;
                fillQnaDetail(qnaDetail);
            })
            .catch(function (error) {
                console.error('QnA 정보를 가져오는 중 오류 발생:', error);
            });
    }
}

//qnaDetail 값을 채우는 함수
function fillQnaDetail(qnaDetail) {
    document.getElementById('title').value = qnaDetail.title;
    $('#summernote').summernote('code', qnaDetail.content);
}


/*QnA 생성*/
document.getElementById('createQnaBtn').addEventListener('click', function() {
    //QnaRequestDto 객체 생성
    const title = document.getElementById('title').value.trim();
    const content = $('#summernote').summernote('code');

    const qnaReq = {
        title: title,
        content: content,
    };

    //QnA 생성 요청
    api.post('/qna/', qnaReq)
        .then(function(response) {
            alert(response.data);
            window.location.href = "/qna/list";  //목록 페이지로 이동
        })
        .catch(function(error) {
            console.error(error);

            if (error.response && error.response.data && error.response.data.errors) {
                alert(error.response.data.errors[0].message);
            } else {
                alert('서버 요청 중 에러가 발생했습니다.');
            }
        });
});

/*QnA 수정*/
document.getElementById('updateQnaBtn').addEventListener('click', function() {
    if (!token) {
        alert("로그인 후 이용해주세요.");
        window.location.href = "/qna/list";
        return;
    }

    //UpdateQnaRequestDto 객체 생성
    const title = document.getElementById('title').value.trim();
    const content = $('#summernote').summernote('code');

    const updateQnaReq = {
        title: title,
        content: content,
    };

    const qnaId = document.getElementById('qnaId').value;
    console.log('수정할 QnA: ' + qnaId);

    //QnA 수정 요청
    api.put('/qna/' + qnaId, updateQnaReq)
        .then(function(response) {
            console.log(response.data);
            alert(response.data);
            window.location.href = "/qna/detail/" + qnaId;
        })
        .catch(function(error) {
            console.error(error);

            if (error.response && error.response.data && error.response.data.errors) {
                alert(error.response.data.errors[0].message);
            } else {
                alert('서버 요청 중 에러가 발생했습니다.');
            }
        });
});


//이미지 업로드 요청 함수
function uploadImage(file) {
    var formData = new FormData();
    formData.append('file', file);

    return axios.post('/qna/image-upload', formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
}

//이미지 삭제 요청 함수
function deleteImage(imgUrl) {
    const formData = new FormData();
    formData.append('fileUrl', imgUrl);

    return axios.post('/qna/image-delete', formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
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