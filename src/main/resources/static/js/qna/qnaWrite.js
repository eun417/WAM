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


//qnaDetail 값을 채우는 함수
function fillQnaDetail(qnaDetail) {
    document.getElementById('title').value = qnaDetail.title;
    $('#summernote').summernote('code', qnaDetail.content);
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